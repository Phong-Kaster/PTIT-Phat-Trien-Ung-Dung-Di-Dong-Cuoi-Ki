package com.example.prudentialfinance.ViewModel.budget;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Users.UserAdd;
import com.example.prudentialfinance.Container.Users.UserGetAll;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BudgetsViewModel extends ViewModel {
    private MutableLiveData<UserGetAll> object;
    private MutableLiveData<UserAdd> oneUser;
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

    public LiveData<UserGetAll> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public LiveData<UserAdd> getOneUser()
    {
        if (oneUser == null) {
            oneUser = new MutableLiveData<>();
        }
        return oneUser;
    }

    public void getData(Map<String, String> headers,  String query){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<UserGetAll> container = api.searchUsers(headers, query, start, length, "id", "decs");
        container.enqueue(new Callback<UserGetAll>() {
            @Override
            public void onResponse(@NonNull Call<UserGetAll> call, @NonNull Response<UserGetAll> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    UserGetAll resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UserGetAll> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void deteteItem(Map<String, String> headers, Integer id){
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<UserAdd> container = api.removeUser(headers, id);
        container.enqueue(new Callback<UserAdd>() {
            @Override
            public void onResponse(@NonNull Call<UserAdd> call, @NonNull Response<UserAdd> response) {
                if (response.isSuccessful()) {
                    UserAdd resource = response.body();
                    assert resource != null;
                    oneUser.setValue(resource);
                    return;
                }
                oneUser.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UserAdd> call, @NonNull Throwable t) {
                oneUser.setValue(null);
            }
        });
    }

    public void restoreUser(Map<String, String> headers, Integer id){
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<UserAdd> container = api.restoreUser(headers, id);
        container.enqueue(new Callback<UserAdd>() {
            @Override
            public void onResponse(@NonNull Call<UserAdd> call, @NonNull Response<UserAdd> response) {
                if (response.isSuccessful()) {
                    UserAdd resource = response.body();
                    assert resource != null;
                    oneUser.setValue(resource);
                    return;
                }
                oneUser.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<UserAdd> call, @NonNull Throwable t) {
                oneUser.setValue(null);
            }
        });
    }
}