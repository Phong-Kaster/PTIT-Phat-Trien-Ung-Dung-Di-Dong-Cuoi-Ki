package com.example.prudentialfinance.ViewModel.budget;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.budgets.budgetGET.Root;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BudgetGetModelView extends ViewModel {
    private MutableLiveData<Root> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;
    private int start = 0;
    private int length = 50;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<Root> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void getData(Map<String, String> headers,  String query){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<Root> container = api.budget(headers, query, start, length, "id", "decs");
        container.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
                isLoading.setValue(false);
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    Root resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
    public void remove(Map<String, String> headers,  int id){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);
        Call<Root> container = api.removeBudget(headers, id);
        container.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
                isLoading.setValue(false);
                Log.i("Remove budget", String.valueOf(response));
                if (response.isSuccessful()) {
                    Root resource = response.body();
                    assert resource != null;
                    Log.i("Remove budget", "Success");
                    Log.i("Remove budget", resource.toString());
//                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

}