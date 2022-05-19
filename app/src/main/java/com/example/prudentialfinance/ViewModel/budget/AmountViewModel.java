package com.example.prudentialfinance.ViewModel.budget;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Activities.Budget.Helper;
import com.example.prudentialfinance.Container.budgets.budgetGET.AmountGet;
import com.example.prudentialfinance.Container.budgets.budgetGET.BudgetAdd;
import com.example.prudentialfinance.Container.budgets.budgetGET.DatumAdd;
import com.example.prudentialfinance.Container.budgets.budgetGET.Root;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AmountViewModel extends ViewModel {
    private MutableLiveData<AmountGet> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<AmountGet> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void getData(Map<String, String> headers,  String id, String date){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);
        Call<AmountGet> container = api.getAmount(headers, id, date);
        container.enqueue(new Callback<AmountGet>() {
            @Override
            public void onResponse(@NonNull Call<AmountGet> call, @NonNull Response<AmountGet> response) {
                isLoading.setValue(false);
                System.out.println(response);
                if (response.isSuccessful()) {
                    AmountGet resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<AmountGet> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
}