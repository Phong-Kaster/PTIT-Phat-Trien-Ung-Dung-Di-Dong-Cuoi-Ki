package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.CategoryMonthlyResponse;
import com.example.prudentialfinance.Container.NotificationGetAll;
import com.example.prudentialfinance.Container.NotificationResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationViewModel extends ViewModel {
    private MutableLiveData<NotificationGetAll> object;
    private MutableLiveData<NotificationResponse> objectResponse;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;
    private Map<String, String> headers;

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<NotificationGetAll> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public LiveData<NotificationResponse> getObjectResponse()
    {
        if (objectResponse == null) {
            objectResponse = new MutableLiveData<>();
        }
        return objectResponse;
    }

    public void getData(){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<NotificationGetAll> container = api.getNotification(headers);
        container.enqueue(new Callback<NotificationGetAll>() {
            @Override
            public void onResponse(@NonNull Call<NotificationGetAll> call, @NonNull Response<NotificationGetAll> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    NotificationGetAll resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<NotificationGetAll> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void maskedAsRead(){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<NotificationResponse> container = api.maskedAsRead(headers);
        container.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotificationResponse> call, @NonNull Response<NotificationResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    NotificationResponse resource = response.body();
                    assert resource != null;
                    objectResponse.setValue(resource);
                    return;
                }
                objectResponse.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<NotificationResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                objectResponse.setValue(null);
            }
        });
    }

    public void maskedAsReadOne(int id){
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<NotificationResponse> container = api.maskedAsReadOne(headers, id);
        container.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotificationResponse> call, @NonNull Response<NotificationResponse> response) {
                if (response.isSuccessful()) {
                    NotificationResponse resource = response.body();
                    assert resource != null;
                    objectResponse.setValue(resource);
                    return;
                }
                objectResponse.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<NotificationResponse> call, @NonNull Throwable t) {
                objectResponse.setValue(null);
            }
        });
    }

}
