package com.example.prudentialfinance.ViewModel.Report;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Report.CategoryReportResponse;
import com.example.prudentialfinance.Container.Report.IncomeVsExpenseResponse;
import com.example.prudentialfinance.Container.Transactions.TransactionGetTotal;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportViewModel extends ViewModel {

    private MutableLiveData<CategoryReportResponse> object;
    private MutableLiveData<IncomeVsExpenseResponse> objectChart;
    private MutableLiveData<TransactionGetTotal> objectReport;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<CategoryReportResponse> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public LiveData<IncomeVsExpenseResponse> getObjectChart()
    {
        if (objectChart == null) {
            objectChart = new MutableLiveData<>();
        }
        return objectChart;
    }

    public LiveData<TransactionGetTotal> getObjectReport()
    {
        if (objectReport == null) {
            objectReport = new MutableLiveData<>();
        }
        return objectReport;
    }

    public void getData(Map<String, String> headers, String type, String date){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<CategoryReportResponse> container;
        if(type.equals("income")){
            container = api.incomeByDate(headers, date);
        }else {
            container = api.expenseByDate(headers, date);
        }
        container.enqueue(new Callback<CategoryReportResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryReportResponse> call, @NonNull Response<CategoryReportResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    CategoryReportResponse resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<CategoryReportResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void getDataChart(Map<String, String> headers, String type, String date){
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<IncomeVsExpenseResponse> container = api.getReportGroupByDate(headers, type, date);
        container.enqueue(new Callback<IncomeVsExpenseResponse>() {
            @Override
            public void onResponse(@NonNull Call<IncomeVsExpenseResponse> call, @NonNull Response<IncomeVsExpenseResponse> response) {
                if (response.isSuccessful()) {
                    IncomeVsExpenseResponse resource = response.body();
                    assert resource != null;
                    objectChart.setValue(resource);
                    return;
                }
                objectChart.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<IncomeVsExpenseResponse> call, @NonNull Throwable t) {
                objectChart.setValue(null);
            }
        });
    }

    public void getTotal(Map<String, String> headers, String typeCategory){
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<TransactionGetTotal> container;

        if(typeCategory.equals("income")){
            container = api.transactionIncomeTotal(headers);
        }else {
            container = api.transactionExpenseTotal(headers);
        }
        container.enqueue(new Callback<TransactionGetTotal>() {
            @Override
            public void onResponse(@NonNull Call<TransactionGetTotal> call, @NonNull Response<TransactionGetTotal> response) {
                if (response.isSuccessful()) {
                    TransactionGetTotal resource = response.body();
                    assert resource != null;
                    objectReport.setValue(resource);
                    return;
                }
                objectReport.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<TransactionGetTotal> call, @NonNull Throwable t) {
                objectReport.setValue(null);
            }
        });
    }
}