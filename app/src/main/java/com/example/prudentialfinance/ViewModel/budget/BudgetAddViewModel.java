package com.example.prudentialfinance.ViewModel.budget;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Activities.Budget.Helper;
import com.example.prudentialfinance.Container.budgets.budgetGET.BudgetAdd;
import com.example.prudentialfinance.Container.budgets.budgetGET.DatumAdd;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BudgetAddViewModel extends ViewModel {
    private MutableLiveData<BudgetAdd> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<BudgetAdd> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void saveData(Map<String, String> headers, DatumAdd data){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);
        Call<BudgetAdd> container = api.addBudget(headers,
                data.getAmount(),
                data.getDescription(),
                data.getCategoryString(),
                Helper.getMonth(data.getTodate()),
                Helper.getYear(data.getTodate()));
        container.enqueue(new Callback<BudgetAdd>() {
            @Override
            public void onResponse(@NonNull Call<BudgetAdd> call, @NonNull Response<BudgetAdd> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    BudgetAdd resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<BudgetAdd> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void updateData(Map<String, String> headers, DatumAdd data){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<BudgetAdd> container = api.updateBudget(headers,
                data.getIdString(),
                data.getAmount(),
                data.getDescription(),
                data.getCategoryString(),
                Helper.getMonth(data.getTodate()),
                Helper.getYear(data.getTodate()));

        container.enqueue(new Callback<BudgetAdd>() {
            @Override
            public void onResponse(@NonNull Call<BudgetAdd> call, @NonNull Response<BudgetAdd> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    BudgetAdd resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<BudgetAdd> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
}