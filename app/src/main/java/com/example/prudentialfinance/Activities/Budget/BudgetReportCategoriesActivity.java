package com.example.prudentialfinance.Activities.Budget;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.prudentialfinance.Container.budgets.budgetGET.AmountGet;
import com.example.prudentialfinance.Container.budgets.budgetGET.Datum;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.budget.AmountViewModel;
import com.example.prudentialfinance.ViewModel.budget.BudgetGetModelView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BudgetReportCategoriesActivity extends AppCompatActivity {

    AmountViewModel amountViewModel;
    TextView remainAmount;
    ImageButton btnBack;
    String id, date;
    Alert alert;
    String currency;
    ArrayList<PieEntry> entries;
    ArrayList<AmountGet> amountData;
    int amount, amountBudget;
    private LoadingDialog loadingDialog;
    private Map<String, String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_report);
        setControl();
        loadComponent();
        setEvent();
        loadData();
        drawChart();
    }

    private void loadComponent() {
        headers = ((GlobalVariable)getApplication()).getHeaders();
        currency = ((GlobalVariable)getApplication()).getAppInfo().getCurrency();
        loadingDialog = new LoadingDialog(BudgetReportCategoriesActivity.this);
        alert = new Alert(this, 1);
        amountViewModel = new ViewModelProvider(this).get(AmountViewModel.class);
    }

    private void drawChart()
    {
        int amountRemain = amountBudget - amount;
        NumberFormat n = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String s = n.format(amountRemain);
        String a = String.valueOf(amountRemain) + " " + currency;
        remainAmount.setText(a);
        entries = new ArrayList<>();
        PieChart pieChart = findViewById(R.id.pie_chart_report_budget);
        PieDataSet pieDataSet = new PieDataSet(entries, getResources().getString(R.string.report_lable));
        entries.add(new PieEntry(amount, getResources().getString(R.string.report_lable_real)));
        entries.add(new PieEntry(amountBudget, getResources().getString(R.string.report_lable_plan)));
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(getResources().getString(R.string.chart_center_lable));
        pieChart.animate();
    }
    private void loadData() {
        Intent intent = getIntent();
        try {
            id = (String) intent.getSerializableExtra("id");
            date = (String) intent.getSerializableExtra("date");
            amountBudget = (int) intent.getSerializableExtra("amount");
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Lỗi lấy extra", Toast.LENGTH_LONG).show();
        }
        try {
            amountViewModel.getData(headers, id, date);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Lỗi lấy dữ liệu", Toast.LENGTH_LONG).show();
        }
    }

    private void setEvent() {
        btnBack.setOnClickListener(view -> finish());
        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        amountViewModel.isLoading().observe((LifecycleOwner) this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        amountViewModel.getObject().observe((LifecycleOwner) this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                amount = Integer.parseInt(object.getTotalamount());
                Log.i("Amount", String.valueOf(amount));
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMethod(), R.drawable.ic_close);
            }
        });

    }

    private void setControl() {
        btnBack = findViewById(R.id.budget_back);
        remainAmount = findViewById(R.id.budget_amount_value);
    }
}
