package com.example.prudentialfinance.Activities.Transaction;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Helpers.ModalBottomSheet;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.TransactionRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;
import com.example.prudentialfinance.ViewModel.TransactionViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TransactionActivity extends AppCompatActivity implements ModalBottomSheet.ModalBottomSheetListener {

    private RecyclerView recycleView;
    private HomeFragmentViewModel homeFragmentviewModel;

    private ImageButton buttonGoBack, buttonCreate;
    private TransactionViewModel transactionViewModel;
    private LoadingDialog loadingDialog;

    private TextView notice;

    private static Map<String, String > headers = null;
    private static LiveData<Integer> transactionCreation = null;
    private static LiveData<Integer> transactionRemoval = null;

    private SearchView searchView;
    private final List<TransactionDetail> objects = new ArrayList<>();
    private TransactionRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        /*this command belows prevent keyboard from popping up automatically*/
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /*get header from global variable*/
        headers = ((GlobalVariable)getApplication()).getHeaders();

        setControl();
        setViewModel(headers);

        transactionCreation = transactionViewModel.getTransactionCreation();
        transactionRemoval = transactionViewModel.getTransactionRemoval();

        setEvent();
        setRecycleView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void setControl() {
        recycleView = findViewById(R.id.transactionRecycleView);
        buttonGoBack = findViewById(R.id.transactionButtonGoBack);
        buttonCreate = findViewById(R.id.transactionButtonCreate);
        searchView = findViewById(R.id.searchView);
        loadingDialog = new LoadingDialog(TransactionActivity.this);
        notice = findViewById(R.id.transactionNotice);
    }

    /**
     * @author Phong-Kaster
     *
     * @param headers is used to attach to HTTP Request headers include Access-Token and Content-Type
     *
     * Step 1: declare viewModel which will be used in this fragment
     * Step 2: retrieve data from API
     * Step 3: observe data if some data changes on server then
     * the data in this fragment is also updated automatically
     * */
    @SuppressLint({"NotifyDataSetChanged", "FragmentLiveDataObserve"})
    private void setViewModel(Map<String, String> headers) {
        /*Step 1*/
        homeFragmentviewModel = new ViewModelProvider( this).get(HomeFragmentViewModel.class);
        homeFragmentviewModel.instanciate(headers);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        /*Step 2*/
        homeFragmentviewModel.getTransactions().observe(this, transactionDetails -> {
            if( transactionDetails.size() > 0 )
            {
                objects.clear();
                objects.addAll(transactionDetails);
                adapter.notifyDataSetChanged();

                notice.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                recycleView.setVisibility(View.VISIBLE);

            }
            else
            {
                notice.setVisibility(View.VISIBLE);
                recycleView.setVisibility(View.GONE);
            }
        });

        /*Step 3*/
        homeFragmentviewModel.getAnimation().observe(this, aBoolean -> {
            if( aBoolean )
            {
                loadingDialog.startLoadingDialog();
            }
            else
            {
                loadingDialog.dismissDialog();
            }
        });
    }

    /**
     * @author Phong-Kaster
     * */
    private void setRecycleView() {
        /*Step 1*/
        adapter = new TransactionRecycleViewAdapter(this, objects);
        recycleView.setAdapter(adapter);


        /*Step 2*/
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(manager);

        /*Step 3*/
        swipeToDelete(recycleView, adapter, objects);
    }

    /**
     * @author Phong-Kaster
     * set event for each component
     * */
    @SuppressLint({"RestrictedApi", "NonConstantResourceId"})
    private void setEvent()
    {
        buttonGoBack.setOnClickListener(view-> finish());


        buttonCreate.setOnClickListener(view -> {
            ModalBottomSheet sheet = new ModalBottomSheet();
            sheet.show(getSupportFragmentManager(), "TransactionMenu");
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                objects.clear();
                homeFragmentviewModel.retrieveWithQuery(headers,query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if( newText.equals(""))
                {
                    objects.clear();
                    homeFragmentviewModel.retrieveWithQuery(headers,"");
                    searchView.clearFocus();
                }

                return false;
            }
        });
    }

    /**
     * @author Phong-Kaster
     * Swipe from right to left to eradicate a transaction
     * */
    private void swipeToDelete(RecyclerView recycleView, TransactionRecycleViewAdapter adapter, List<TransactionDetail> transactions)
    {
        /*Step 1*/
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                Toast.makeText(TransactionActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView

                /*declare local variables*/
                int position = viewHolder.getLayoutPosition();
                TransactionDetail eradicatedTransaction = transactions.get(position);

                transactions.remove(position);

                /*notify to adapter & remove the transaction on SERVER*/
                adapter.notifyItemRemoved(position);
                removeTransaction(eradicatedTransaction);
                homeFragmentviewModel.setTransactions(transactions);


                Snackbar.make(recycleView, eradicatedTransaction.getName(), Snackbar.LENGTH_SHORT)
                        .setAction("Khôi phục", view ->{
                            /*restore the transaction in recycle view*/
                            transactions.add(position, eradicatedTransaction);
                            adapter.notifyItemInserted(position);

                            homeFragmentviewModel.setTransactions(transactions);
                            /*restore the transaction on Server*/
                            createTransaction(eradicatedTransaction);

                        }).show();


            }

            public void onChildDraw (@NonNull Canvas c,
                                     @NonNull RecyclerView recyclerView,
                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                     float dX, float dY, int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(TransactionActivity.this, R.color.colorRed))
                        .addActionIcon(R.drawable.ic_baseline_delete_forever_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        /*Step 2*/
        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        touchHelper.attachToRecyclerView(recycleView);
    }

    /**
     * @author Phong-Kaster
     * remove a transaction
     * */
    private void removeTransaction(TransactionDetail transaction)
    {
        String id = transaction.getId().toString();

        transactionViewModel.eradicateTransaction(headers, id);

        transactionRemoval.observe(this, integer -> System.out.println("transaction activity remove: " +integer));
    }

    /**
     * @author Phong-Kaster
     * create | retore a transaction
     */
    private void createTransaction(TransactionDetail transaction)
    {
        String categoryId = transaction.getCategory().getId().toString();
        String accountId = String.valueOf(transaction.getAccount().getId());
        String name = transaction.getName();
        String amount = transaction.getAmount().toString();
        String reference = transaction.getReference();
        String transactionDate = transaction.getTransactiondate();
        String type = transaction.getType().toString();
        String description = transaction.getDescription();


        transactionViewModel.createTransaction(headers,
                categoryId,
                accountId,
                name,
                amount,
                reference,
                transactionDate,
                type,
                description);

        transactionCreation.observe(this, integer -> System.out.println("transaction activity create | restore: " +integer));
    }
}