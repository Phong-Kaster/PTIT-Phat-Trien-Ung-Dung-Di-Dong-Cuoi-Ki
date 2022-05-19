package com.example.prudentialfinance.ViewModel.Settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PasswordViewModel extends ViewModel {

    private MutableLiveData<Login> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<Login> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void updateData(Map<String, String> headers, String newPass, String confirmPass, String oldPass){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        Call<Login> container = api.changePassword(headers, newPass, confirmPass, oldPass);
        container.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                isLoading.setValue(false);
                if(response.isSuccessful())
                {
                    if (response.isSuccessful()) {
                        Login resource = response.body();
                        assert resource != null;
                        object.setValue(resource);
                        return;
                    }
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
}