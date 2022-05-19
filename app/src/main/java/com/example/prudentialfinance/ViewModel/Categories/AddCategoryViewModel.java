package com.example.prudentialfinance.ViewModel.Categories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.CategoryAdd;
import com.example.prudentialfinance.Model.Category;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddCategoryViewModel extends ViewModel {
    private MutableLiveData<CategoryAdd> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<CategoryAdd> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void saveData(Map<String, String> headers, Category data){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<CategoryAdd> container;
        System.out.println(data.toString());
        if(data.getType() == 1){
            container = api.addNewIncomeCategory(headers, data.getName(), data.getDescription(), data.getColor());
        }else{
            container = api.addNewExpenseCategory(headers, data.getName(), data.getDescription(), data.getColor());
        }
        container.enqueue(new Callback<CategoryAdd>() {
            @Override
            public void onResponse(@NonNull Call<CategoryAdd> call, @NonNull Response<CategoryAdd> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    CategoryAdd resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<CategoryAdd> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void updateData(Map<String, String> headers, Category data){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<CategoryAdd> container;
        if(data.getType() == 1){
            container = api.editIncomeCategory(headers, data.getId(), data.getName(), data.getDescription(), data.getColor());
        }else{
            container = api.editExpenseCategory(headers, data.getId(), data.getName(), data.getDescription(), data.getColor());
        }
        container.enqueue(new Callback<CategoryAdd>() {
            @Override
            public void onResponse(@NonNull Call<CategoryAdd> call, @NonNull Response<CategoryAdd> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    CategoryAdd resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<CategoryAdd> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
}