package com.example.prudentialfinance.ViewModel.Users;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Users.UserAdd;
import com.example.prudentialfinance.Model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddUserViewModel extends ViewModel {
    private MutableLiveData<UserAdd> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<UserAdd> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public void saveData(Map<String, String> headers, User data){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<UserAdd> container = api.addUser(headers, data.getEmail(), data.getFirstname(), data.getLastname(), data.getAccount_type(), data.getIs_active());

        container.enqueue(new Callback<UserAdd>() {
            @Override
            public void onResponse(@NonNull Call<UserAdd> call, @NonNull Response<UserAdd> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    UserAdd resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UserAdd> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void updateData(Map<String, String> headers, User data){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<UserAdd> container = api.updateUser(headers, data.getId(), data.getFirstname(), data.getLastname(), data.getAccount_type(), data.getIs_active());

        container.enqueue(new Callback<UserAdd>() {
            @Override
            public void onResponse(@NonNull Call<UserAdd> call, @NonNull Response<UserAdd> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    UserAdd resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UserAdd> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
}