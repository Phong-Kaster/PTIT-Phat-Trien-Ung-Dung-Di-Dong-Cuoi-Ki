package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.CategoryMonthlyResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryExportViewModel extends ViewModel {
    private MutableLiveData<CategoryMonthlyResponse> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<CategoryMonthlyResponse> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void getData(Map<String, String> headers, String type, String nature, int length, String column){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<CategoryMonthlyResponse> container;
        if(type.equals("income")){
            container = api.getCategoryMonthly(headers, "", 0, length, column, nature, 1);
        }else {
            container = api.getCategoryMonthly(headers, "", 0, length, column, nature, 2);
        }
        container.enqueue(new Callback<CategoryMonthlyResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryMonthlyResponse> call, @NonNull Response<CategoryMonthlyResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    CategoryMonthlyResponse resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<CategoryMonthlyResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

}
