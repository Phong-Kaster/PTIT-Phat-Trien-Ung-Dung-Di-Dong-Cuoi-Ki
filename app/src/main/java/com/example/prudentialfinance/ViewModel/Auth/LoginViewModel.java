package com.example.prudentialfinance.ViewModel.Auth;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<Login> object = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private Retrofit service;
    /**
     * https://codingwithmitch.com/blog/getting-started-with-mvvm-android/#view-models
     *
     * A LiveData object is an observable data-holder class.
     * It's responsible for holding the data that's displayed in the view.
     * But it's not just any old data-holder, the data is observable.
     * Meaning that the data is actively being watched for changes.
     * If a change occurs, the view is updated automatically.
     *
     * MutableLiveData is a subclass of LiveData.
     * You need to use a MutableLiveData object if you want to allow the LiveData object to be changed.
     * LiveData can not be changed by itself.
     * That is, the setValue method is not public for LiveData.
     * But it is public for MutableLiveData.
     * */
    public LiveData<Login> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return this.object;
    }

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public void login(String username, String password)
    {
        isLoading.setValue(true);
        /*Step 1*/
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 2*/
        Call<Login> container = api.login(username, password);
        container.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                isLoading.setValue(false);
                if(response.isSuccessful())
                {
                    Login resource = response.body();
                    assert resource != null;
                    if(resource.getResult() == 1){
                        object.setValue(resource);
                    }else{
                        object.setValue(null);
                    }
                }else{
                    object.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });

    }

    public void loginGoogle(String idToken) {
        isLoading.setValue(true);
        /*Step 1*/
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 2*/
        Call<Login> container = api.loginGoogle(idToken);
        container.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                isLoading.setValue(false);
                if(response.isSuccessful())
                {
                    Login resource = response.body();
                    assert resource != null;
                    if(resource.getResult() == 1){
                        object.setValue(resource);
                    }else{
                        object.setValue(null);
                    }
                }else{
                    object.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void loginFacebook(String access_token) {
        isLoading.setValue(true);
        /*Step 1*/
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 2*/
        Call<Login> container = api.loginFacebook(access_token);
        container.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                isLoading.setValue(false);
                if(response.isSuccessful())
                {
                    Login resource = response.body();
                    assert resource != null;
                    if(resource.getResult() == 1){
                        object.setValue(resource);
                    }else{
                        object.setValue(null);
                    }
                }else{
                    object.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
}
