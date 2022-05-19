package com.example.prudentialfinance.Activities.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prudentialfinance.Container.Report.CategoryReport;
import com.example.prudentialfinance.Container.Report.DateRange;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.TransactionsByCategoryRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.Report.TransactionsByCategoryViewModel;

import java.util.ArrayList;
import java.util.Map;

public class TransactionsByCategoryActivity extends AppCompatActivity {

    ImageButton backBtn, cat_color;
    TextView category_name, category_desc, topTitle;
    DateRange dateRange;
    CategoryReport categoryReport;

    TransactionsByCategoryViewModel viewModel;
    GlobalVariable global;
    LoadingDialog loadingDialog;
    Alert alert;
    User authUser;
    SiteSettings appInfo;
    Map<String, String> headers;

    RecyclerView lvTransactions;
    TransactionsByCategoryRecycleViewAdapter adapter;
    LinearLayoutManager manager;

    ArrayList<TransactionDetail> data;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_by_category);

        Intent intent = getIntent();
        dateRange = intent.getParcelableExtra("dateRange");
        categoryReport = intent.getParcelableExtra("categoryReport");

        setComponent();

        setControl();
        setEvent();

        setData();

        loadData();

    }

    private void loadData() {
        data = new ArrayList<>();
        viewModel.getData(headers, dateRange, categoryReport);

        manager = new LinearLayoutManager(this);
        lvTransactions.setLayoutManager(manager);

        adapter = new TransactionsByCategoryRecycleViewAdapter(this, data, appInfo);
        lvTransactions.setAdapter(adapter);
    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(this);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(TransactionsByCategoryViewModel.class);
        authUser = global.getAuthUser();
        appInfo = global.getAppInfo();
    }

    private void setData() {
        topTitle.setText(Helper.truncate_string(categoryReport.getName(), 50, "...", true));

    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> finish());

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                data.clear();
                data.addAll(object.getData());
                adapter.notifyDataSetChanged();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            data.clear();
            viewModel.getData(headers, dateRange, categoryReport);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);

        category_desc = findViewById(R.id.category_desc);
        cat_color = findViewById(R.id.cat_color);
        category_name = findViewById(R.id.category_name);

        topTitle = findViewById(R.id.topTitle);

        lvTransactions = findViewById(R.id.lvTransactions);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);


    }
}