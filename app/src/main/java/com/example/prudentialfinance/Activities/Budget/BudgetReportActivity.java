package com.example.prudentialfinance.Activities.Budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.example.prudentialfinance.Container.budgets.budgetGET.Datum;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.budget.AmountViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class BudgetReportActivity extends AppCompatActivity {

    ImageButton btnBack;
    List<Datum> datum;
    ArrayList<PieEntry> entries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_report);
        setControl();
        setEvent();
        loadData();
        drawChart();
    }
    private void drawChart()
    {
        entries = new ArrayList<>();
        PieChart pieChart = findViewById(R.id.pie_chart_report_budget);
        PieDataSet pieDataSet = new PieDataSet(entries, getResources().getString(R.string.report_lable));
        for (Datum d: datum
             ) {
            entries.add(new PieEntry(d.getAmount(), d.getCategory().getDescription()));
        }

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
            datum = (ArrayList<Datum>) intent.getSerializableExtra("budgets");
        }
        catch (Exception ex)
        {
            datum = new ArrayList<>();
        }
        for (Datum d: datum
             ) {
            Log.i("Reporter Datum", d.toString());
        }
    }

    private void setEvent() {
        btnBack.setOnClickListener(view -> finish());
    }

    private void setControl() {
        btnBack = findViewById(R.id.budget_back);
    }
}
