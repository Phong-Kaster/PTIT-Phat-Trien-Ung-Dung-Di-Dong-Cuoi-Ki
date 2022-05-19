package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Login;
import com.example.prudentialfinance.Container.Settings.SiteSettingsResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Login> objectLogin;
    private MutableLiveData<SiteSettingsResponse> objectAppInfo;
    private Retrofit service;

    public LiveData<Login> getObjectLogin() {
        if (objectLogin == null) {
            objectLogin = new MutableLiveData<>();
        }
        return objectLogin;
    }

    public LiveData<SiteSettingsResponse> getObjectAppInfo() {
        if (objectAppInfo == null) {
            objectAppInfo = new MutableLiveData<>();
        }
        return objectAppInfo;
    }

    public void getInfoUser(String token)
    {
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<Login> container = api.profile(token);
        container.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                if(response.isSuccessful())
                {
                    Login resource = response.body();
                    assert resource != null;
                    if(resource.getResult() == 1){
                        objectLogin.setValue(resource);
                    }else{
                        objectLogin.setValue(null);
                    }
                }else{
                    objectLogin.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                objectLogin.setValue(null);
            }
        });
    }

    public void getInfoSettings()
    {
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Map<String, String> headers = new HashMap<>();
        Call<SiteSettingsResponse> container = api.getSiteSettings(headers);
        container.enqueue(new Callback<SiteSettingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SiteSettingsResponse> call, @NonNull Response<SiteSettingsResponse> response) {
                if(response.isSuccessful())
                {
                    SiteSettingsResponse resource = response.body();
                    assert resource != null;
                    if(resource.getResult() == 1){
                        objectAppInfo.setValue(resource);
                    }else{
                        objectAppInfo.setValue(null);
                    }
                }else{
                    objectAppInfo.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SiteSettingsResponse> call, Throwable t) {
                objectAppInfo.setValue(null);
            }
        });
    }

}