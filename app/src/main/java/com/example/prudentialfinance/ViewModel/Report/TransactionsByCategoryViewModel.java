package com.example.prudentialfinance.ViewModel.Report;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Report.CategoryReport;
import com.example.prudentialfinance.Container.Report.DateRange;
import com.example.prudentialfinance.Container.Report.TransactionByCategoryResponse;
import com.example.prudentialfinance.Model.Category;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionsByCategoryViewModel extends ViewModel {

    private MutableLiveData<TransactionByCategoryResponse> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<TransactionByCategoryResponse> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void getData(Map<String, String> headers, DateRange dateRange, CategoryReport category){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<TransactionByCategoryResponse> container = api.getTransactionByCategory(
                headers, "", 0, 100, "id", "desc",
                dateRange.getFrom(), dateRange.getTo(), category.getId());
        container.enqueue(new Callback<TransactionByCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<TransactionByCategoryResponse> call, @NonNull Response<TransactionByCategoryResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    TransactionByCategoryResponse resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<TransactionByCategoryResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

}
