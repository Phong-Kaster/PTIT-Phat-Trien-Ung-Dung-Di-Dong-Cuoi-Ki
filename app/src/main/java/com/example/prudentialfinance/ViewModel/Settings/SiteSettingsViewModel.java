package com.example.prudentialfinance.ViewModel.Settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Settings.SiteSettingsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SiteSettingsViewModel extends ViewModel {

    private MutableLiveData<SiteSettingsResponse> object;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<SiteSettingsResponse> getObject()
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

        Call<SiteSettingsResponse> container = api.getSiteSettings(headers);
        container.enqueue(new Callback<SiteSettingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SiteSettingsResponse> call, @NonNull Response<SiteSettingsResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    SiteSettingsResponse resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<SiteSettingsResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void updateData(Map<String, String> headers, String action,
                           String site_name, String site_slogan, String site_description, String site_keyword, String logo_type,
                           String logo_mark, String language, String currency){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        Call<SiteSettingsResponse> container = api.saveSiteSettings(headers,action,
                site_name, site_slogan, site_description, site_keyword,
                logo_type, logo_mark,
                language, currency);
        container.enqueue(new Callback<SiteSettingsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SiteSettingsResponse> call, @NonNull Response<SiteSettingsResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    SiteSettingsResponse resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<SiteSettingsResponse> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
}