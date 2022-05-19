package com.example.prudentialfinance.ViewModel.Accounts;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Accounts.AccountMonthlyResponse;
import com.example.prudentialfinance.Model.Account;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountChartViewModel extends ViewModel {
    private MutableLiveData<AccountMonthlyResponse> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<AccountMonthlyResponse> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void getData(Map<String, String> headers, Account account){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<AccountMonthlyResponse> container = api.accountBalanceMonthly(headers, account.getId());
        container.enqueue(new Callback<AccountMonthlyResponse>() {
            @Override
            public void onResponse(@NonNull Call<AccountMonthlyResponse> call, @NonNull Response<AccountMonthlyResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    AccountMonthlyResponse resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<AccountMonthlyResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

}