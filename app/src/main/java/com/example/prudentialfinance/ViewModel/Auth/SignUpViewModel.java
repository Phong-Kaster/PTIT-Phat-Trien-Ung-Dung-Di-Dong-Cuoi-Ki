package com.example.prudentialfinance.ViewModel.Auth;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpViewModel extends ViewModel {

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

    public void signUp(String firstName, String lastName, String email, String pass, String passConfirm) {
        isLoading.setValue(true);

        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<Login> container = api.signup(firstName, lastName, email, pass, passConfirm);
        container.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    Login resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
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
