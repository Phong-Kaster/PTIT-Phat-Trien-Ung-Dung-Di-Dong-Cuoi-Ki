package com.example.prudentialfinance.Activities.Budget;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prudentialfinance.Container.budgets.budgetGET.Datum;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.Budget.BudgetRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.budget.BudgetGetModelView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class BudgetsActivity extends AppCompatActivity {

    BudgetGetModelView budgetViewModel;
    ImageButton btnBack, btnAdd, btnPDF;
    LoadingDialog loadingDialog;
    Alert alert;
    Map<String, String > headers;
    ArrayList<Datum> budgetData;
    ArrayList<Datum> budgetDataS;
    BudgetRecycleViewAdapter budgetRecycleViewAdapter;
    RecyclerView rViewGoal;
    SearchView budgetSearch;
    LinearLayoutManager manager;
    SwipeRefreshLayout swipeRefreshLayout;

    int pageHeight = 1120;
    int pagewidth = 792;

    Datum entry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets);
        setControl();
        loadComponent();
        setEvent();
        loadData();
        setSwipe();
    }

    private void setControl(){
        btnBack = findViewById(R.id.budget_back);
        btnAdd = findViewById(R.id.budget_add);
        btnPDF = findViewById(R.id.budget_pdf);
        budgetSearch = findViewById(R.id.budget_search);
        rViewGoal = findViewById(R.id.rv_budgets);
        swipeRefreshLayout = findViewById(R.id.refresh_layout_budget);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setEvent(){


        btnBack.setOnClickListener(view -> finish());

        btnAdd.setOnClickListener(view ->{
            Intent intent = new Intent (this, AddBudgetActivity.class);
            intent.putExtra("budget", new Datum(0));
            addBudgetActivity.launch(intent);
        });
        btnPDF.setOnClickListener(
                view->{
                    generatePDF();
                    Log.i("Export PDF", "Clicked");
                }
        );
        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Datum> temp = budgetDataS.stream()
                        .filter(datum -> datum.getCategory().getName().toLowerCase().contains(newText.toLowerCase(Locale.ROOT)))
                        .collect(Collectors.toList());
                budgetData.clear();
                budgetData.addAll(temp);
                budgetRecycleViewAdapter.notifyDataSetChanged();
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Datum> temp = budgetDataS.stream()
                        .filter(datum -> datum.getCategory().getName().toLowerCase().contains(query.toLowerCase(Locale.ROOT)))
                        .collect(Collectors.toList());
                budgetData.clear();
                budgetData.addAll(temp);
                budgetRecycleViewAdapter.notifyDataSetChanged();
                return true;
            }
        };

        budgetSearch.setOnQueryTextListener(queryTextListener);
        budgetViewModel.isLoading().observe((LifecycleOwner) this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        budgetViewModel.getObject().observe((LifecycleOwner) this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                budgetData.clear();
                budgetData.addAll(object.getData() == null ? new ArrayList<>() : object.getData());
                budgetDataS.clear();
                budgetDataS.addAll(budgetData);
                budgetRecycleViewAdapter.notifyDataSetChanged();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            budgetViewModel.getData(headers, "");
            swipeRefreshLayout.setRefreshing(false);
        });
    }


    private void loadData() {
        Log.i("Datum data", "Load data");
        budgetData = new ArrayList<>();
        budgetDataS = new ArrayList<>();
        budgetViewModel.getData(headers, "");
        budgetViewModel.getObject().observe(this, models -> {
            if (models != null) {
                for (Datum r:models.getData()
                     ) {
                    Log.i("Datum data", r.toString());
                }
            }
            else Log.i("Datum data", "Models null ");
        });
        manager = new LinearLayoutManager(this.getApplicationContext());
        rViewGoal.setLayoutManager(manager);

        budgetRecycleViewAdapter = new BudgetRecycleViewAdapter(this.getApplicationContext(), budgetData, addBudgetActivity);
        rViewGoal.setAdapter(budgetRecycleViewAdapter);
        Log.i("Datum data", "Load data success");
        for(Datum d: budgetData)
            Log.i("Datum data", d.toString());

    }

    private void loadComponent() {
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(BudgetsActivity.this);
        alert = new Alert(this, 1);
        budgetViewModel = new ViewModelProvider(this).get(BudgetGetModelView.class);
    }
    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(17);
        title.setColor(ContextCompat.getColor(this, R.color.black));
        canvas.drawText("", 100, 50, title);

        title.setTextSize(15);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(15);
        title.setTextAlign(Paint.Align.CENTER);
        int y = 100;
        String nameTitle = getResources().getString(R.string.name_title);
        String descriptionTitle =  getResources().getString(R.string.description_title);
        String amountTitle = getResources().getString(R.string.amount_title);
        String todateTitle = getResources().getString(R.string.todate_title);
       canvas.drawText(nameTitle, 100, y , title);
        canvas.drawText(descriptionTitle, 400, y , title);
        canvas.drawText(amountTitle, 600, y, title);
        canvas.drawText(todateTitle, 700, y, title);
        y += 30;
        for(Datum d: budgetData)
        {
            if (y >= 1000)
            {
                y = 130;
            }
            y += 30;
            canvas.drawText(String.valueOf(d.getCategory().getName()), 100, y , title);
            canvas.drawText(String.valueOf(d.getDescription()), 400, y , title);
            canvas.drawText(String.valueOf(d.getAmount()), 600, y, title);
            canvas.drawText(String.valueOf(d.getTodate()), 700, y, title);
        }

        pdfDocument.finishPage(myPage);

        File file = new File(getApplicationContext().getFilesDir(), "budget.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(BudgetsActivity.this, "PDF file generated successfully."
                    + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("GeneratePDF", "PDF file generated successfully" + e.toString());
            Toast.makeText(BudgetsActivity.this, "PDF file generated Failed.",
                    Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }
    private void setSwipe() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                entry = budgetData.get(position);
                budgetData.remove(position);
                budgetDataS.clear();
                budgetDataS.addAll(budgetData);
                budgetRecycleViewAdapter.notifyItemRemoved(position);
                Snackbar.make(rViewGoal, "Đã xóa " + entry.getDescription(), 3000)
                        .addCallback(new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                    budgetViewModel.remove(headers, entry.getId());
                                    budgetDataS.clear();
                                    budgetDataS.addAll(budgetData);
                                }
                            }
                        })
                        .setAction("Khôi phục", view -> {
                            budgetData.add(position, entry);
                            budgetDataS.clear();
                            budgetDataS.addAll(budgetData);
                            budgetRecycleViewAdapter.notifyItemInserted(position);
                        }).show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(BudgetsActivity.this, R.color.colorRed))
                        .addActionIcon(R.drawable.ic_close)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rViewGoal);
    }
    ActivityResultLauncher<Intent> report = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 78) {
                    Intent data = result.getData();
                    assert data != null;
                    budgetViewModel.getData(headers, "");
                }
            });
    ActivityResultLauncher<Intent> addBudgetActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 78) {
                    Intent data = result.getData();
                    assert data != null;
                    budgetViewModel.getData(headers, "");
                }
            });
}