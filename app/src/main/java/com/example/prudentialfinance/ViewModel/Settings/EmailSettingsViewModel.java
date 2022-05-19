package com.example.prudentialfinance.ViewModel.Settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Settings.EmailSettingsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmailSettingsViewModel extends ViewModel {

    private MutableLiveData<EmailSettingsResponse> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<EmailSettingsResponse> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void getData(Map<String, String> headers){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<EmailSettingsResponse> container = api.getEmailSettings(headers);
        container.enqueue(new Callback<EmailSettingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmailSettingsResponse> call, @NonNull Response<EmailSettingsResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    EmailSettingsResponse resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<EmailSettingsResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void updateData(Map<String, String> headers, String action,
                           String host, String port, String encryption, Boolean auth, String username, String password, String  from){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        Call<EmailSettingsResponse> container = api.saveEmailSettings(headers, action,
                host, port, encryption, auth, username, password, from);
        container.enqueue(new Callback<EmailSettingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmailSettingsResponse> call, @NonNull Response<EmailSettingsResponse> response) {
                isLoading.setValue(false);

                if (response.isSuccessful()) {
                    EmailSettingsResponse resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<EmailSettingsResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
}