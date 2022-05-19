package com.example.prudentialfinance.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.prudentialfinance.Activities.Card.AccountChartActivity;
import com.example.prudentialfinance.Activities.Card.CardUpdateActivity;
import com.example.prudentialfinance.Activities.Report.CategoryExportActivity;
import com.example.prudentialfinance.Container.Report.CategoryReport;
import com.example.prudentialfinance.Container.Report.DateRange;
import com.example.prudentialfinance.Container.Report.DateReport;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.CategoryReportRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.Report.ReportViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFragment extends Fragment{

    ImageButton btnMenu, exportBtn;
    private MenuPopupHelper menuHelper;
    private MenuBuilder menuBuilder;
    View view;
    ViewGroup container;
    TextView topTitle, total_money, title_total;
    RadioGroup rGroup;
    RadioButton checkedRadioButton;

    ReportViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;
    Map<String, String> headers;
    User authUser;
    SiteSettings appInfo;

    RecyclerView lvCategory;
    CategoryReportRecycleViewAdapter adapter;
    LinearLayoutManager manager;

    ArrayList<CategoryReport> data;
    SwipeRefreshLayout swipeRefreshLayout;
    String typeCategory;
    String typeDate;
    DateRange dateRange;


    //chart
    AnyChartView anyChartView;
    Cartesian cartesian;
    Column column;

    public ReportFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_report, container, false);
        this.container = container;

        setComponent();
        setControl();
        setEvent();

        loadData();
        return view;
    }

    private void setComponent() {
        assert this.getArguments() != null;
        authUser = this.getArguments().getParcelable("authUser");
        appInfo = this.getArguments().getParcelable("appInfo");
        String accessToken = this.getArguments().getString("accessToken");
        String contentType = this.getArguments().getString("contentType");


        // initialize headers to attach HTTP Request
        headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        headers.put("Content-Type", contentType);


        loadingDialog = new LoadingDialog(this.getActivity());
        alert = new Alert(this.getContext(), 1);
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

    @SuppressLint("RestrictedApi")
    private void setEvent() {
        btnMenu.setOnClickListener(view -> {
            menuHelper.show();
        });

        exportBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CategoryExportActivity.class);
            startActivity(intent);
        });

        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.incomeMenu:
                        typeCategory = "income";
                        topTitle.setText(container.getContext().getString(R.string.reportIncome));
                        break;
                    case R.id.expenseMenu:
                        typeCategory = "expense";
                        topTitle.setText(container.getContext().getString(R.string.reportExpense));
                        break;
                }

                switch (typeDate){
                    case "week":
                        title_total.setText(container.getContext().getString(R.string.total_money_income_week));
                        break;
                    case "month":
                        title_total.setText(container.getContext().getString(R.string.total_money_income_month));
                        break;
                    case "year":
                        title_total.setText(container.getContext().getString(R.string.total_money_income_year));
                        break;
                }

                viewModel.getData(headers, typeCategory, typeDate);
                viewModel.getDataChart(headers, typeCategory, typeDate);
                viewModel.getTotal(headers, typeCategory);
                return false;
            }

            @Override
            public void onMenuModeChange(@NonNull MenuBuilder menu) {

            }
        });


        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(getViewLifecycleOwner(), object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                dateRange.setFrom(object.getDate().getFrom());
                dateRange.setTo(object.getDate().getTo());
                data.clear();
                data.addAll(object.getData());
                adapter.notifyDataSetChanged();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        rGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton checked = radioGroup.findViewById(i);
            boolean isChecked = checked.isChecked();
            if(isChecked){
                switch (checked.getId()){
                    case R.id.btnWeek:
                        typeDate = "week";
                        title_total.setText(getString(R.string.total_money_income_week));
                        break;
                    case R.id.btnMonth:
                        typeDate = "month";
                        title_total.setText(getString(R.string.total_money_income_month));
                        break;
                    case R.id.btnYear:
                        typeDate = "year";
                        title_total.setText(getString(R.string.total_money_income_year));
                        break;
                }
                viewModel.getData(headers, typeCategory, typeDate);
                viewModel.getDataChart(headers, typeCategory, typeDate);
                viewModel.getTotal(headers, typeCategory);
            }
        });



        swipeRefreshLayout.setOnRefreshListener(() -> {
            data.clear();
            viewModel.getData(headers, typeCategory, typeDate);
            viewModel.getDataChart(headers, typeCategory, typeDate);
            viewModel.getTotal(headers, typeCategory);
            swipeRefreshLayout.setRefreshing(false);
        });


        viewModel.getObjectReport().observe(getViewLifecycleOwner(), object -> {
            if(object == null || object.getResult() != 1){
                return;
            }
            switch (typeDate){
                case "week":
                    total_money.setText(Helper.formatNumber(object.getData().getWeek()) + " " + appInfo.getCurrency());
                    break;
                case "month":
                    total_money.setText(Helper.formatNumber(object.getData().getMonth()) + " " + appInfo.getCurrency());
                    break;
                case "year":
                    total_money.setText(Helper.formatNumber(object.getData().getYear()) + " " + appInfo.getCurrency());
                    break;
            }

        });
    }

    @SuppressLint("RestrictedApi")
    private void setControl() {
        btnMenu = view.findViewById(R.id.btnMenu);
        exportBtn = view.findViewById(R.id.exportBtn);
        topTitle = view.findViewById(R.id.topTitle);
        title_total = view.findViewById(R.id.title_total);
        total_money = view.findViewById(R.id.total_money);
        rGroup = view.findViewById(R.id.rGroup);
        checkedRadioButton = rGroup.findViewById(rGroup.getCheckedRadioButtonId());

        menuBuilder = new MenuBuilder(getContext());
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.category_menu, menuBuilder);

        menuHelper = new MenuPopupHelper(getContext(), menuBuilder, btnMenu);
        menuHelper.setForceShowIcon(true);

        lvCategory = view.findViewById(R.id.lvCategory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));
    }

    private void loadData() {
        dateRange = new DateRange("", "");
        typeCategory = "income";
        typeDate = "week";
        data = new ArrayList<>();
        viewModel.getData(headers, typeCategory, typeDate);
        viewModel.getDataChart(headers, typeCategory, typeDate);
        viewModel.getTotal(headers, typeCategory);

        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        lvCategory.setLayoutManager(manager);

        adapter = new CategoryReportRecycleViewAdapter(getActivity().getApplicationContext(), data, dateRange, appInfo);
        lvCategory.setAdapter(adapter);

        loadDataChart();
    }

    private void loadDataChart(){
        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CategoryValueDataEntry("", "", 0));

        cartesian = AnyChart.column();
        column = cartesian.column(seriesData);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format(getString(R.string.money_string) + ": {%Value} " + appInfo.getCurrency());

        cartesian.animation(true);
        cartesian.yScale().minimum(0d);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        anyChartView.setChart(cartesian);

        cartesian.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value", "category"}) {
            @Override
            public void onClick(Event event) {
                System.out.println(event.getData().get("x") + "-" + event.getData().get("value") + "-" + event.getData().get("category"));
            }
        });

        viewModel.getObjectChart().observe(getViewLifecycleOwner(), object -> {
            if(object == null || object.getResult() != 1) {
                return;
            }

            List<DataEntry> list = new ArrayList<>();
            if(object.getIncome() != null){
                for (DateReport item : object.getIncome()) {
                    list.add(new CategoryValueDataEntry(item.getName(), item.getDate(), item.getValue()));
                }
                column.data(list);
            }

            if(object.getExpense() != null){
                for (DateReport item : object.getExpense()) {
                    list.add(new CategoryValueDataEntry(item.getName(), item.getDate(), item.getValue()));
                }
                column.data(list);
            }
        });

    }
}