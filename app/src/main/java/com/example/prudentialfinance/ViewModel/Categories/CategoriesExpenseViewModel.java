package com.example.prudentialfinance.ViewModel.Categories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.CategoryAdd;
import com.example.prudentialfinance.Container.CategoryGetAll;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoriesExpenseViewModel extends ViewModel {

    private MutableLiveData<CategoryGetAll> object;
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

    public LiveData<CategoryGetAll> getObject()
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

        Call<CategoryGetAll> container = api.searchExpenseCategories(headers, query, start, length, "id", "decs");
        container.enqueue(new Callback<CategoryGetAll>() {
            @Override
            public void onResponse(@NonNull Call<CategoryGetAll> call, @NonNull Response<CategoryGetAll> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    CategoryGetAll resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<CategoryGetAll> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void deteteItem(Map<String, String> headers, Integer id){
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<CategoryAdd> container = api.removeExpenseCategories(headers, id);
        container.enqueue(new Callback<CategoryAdd>() {
            @Override
            public void onResponse(@NonNull Call<CategoryAdd> call, @NonNull Response<CategoryAdd> response) {
            }

            @Override
            public void onFailure(@NonNull Call<CategoryAdd> call, @NonNull Throwable t) {

            }
        });
    }
}