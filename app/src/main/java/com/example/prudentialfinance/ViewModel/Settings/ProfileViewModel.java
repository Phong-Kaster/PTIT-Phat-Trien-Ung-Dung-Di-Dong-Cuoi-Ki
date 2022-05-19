package com.example.prudentialfinance.ViewModel.Settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Settings.AvatarUpload;
import com.example.prudentialfinance.Container.Login;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<AvatarUpload> object;
    private MutableLiveData<Login> objectProfile;
    private Retrofit service;
    private MutableLiveData<Boolean> isLoading;

    public LiveData<Boolean> isLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public LiveData<AvatarUpload> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }

    public LiveData<Login> getObjectProfile()
    {
        if (objectProfile == null) {
            objectProfile = new MutableLiveData<>();
        }
        return objectProfile;
    }

    public void updateData(Map<String, String > headers, String action, String firstName, String lastName){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        Call<Login> container = api.updateProfile(headers, action, firstName, lastName);
        container.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    Login resource = response.body();
                    assert resource != null;
                    objectProfile.setValue(resource);
                    return;
                }
                objectProfile.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                objectProfile.setValue(null);
            }
        });
    }

    public void uploadAvatar(String token, String picturePath){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        File file = new File(picturePath);

        RequestBody action = RequestBody.create(MediaType.parse("multipart/form-data"), "avatar");
        RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileData = MultipartBody.Part.createFormData("file", file.getName(), requestBodyFile);

        Call<AvatarUpload> container = api.uploadAvatar(token, action, fileData);
        container.enqueue(new Callback<AvatarUpload>() {
            @Override
            public void onResponse(@NonNull Call<AvatarUpload> call, @NonNull Response<AvatarUpload> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    AvatarUpload resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<AvatarUpload> call, @NonNull Throwable t) {
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }
}