package com.example.prudentialfinance.ViewModel.Goal;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.GoalAdd;
import com.example.prudentialfinance.Container.GoalGetAll;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoalViewModel extends ViewModel {
    private MutableLiveData<GoalGetAll> object;
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

    public LiveData<GoalGetAll> getObject()
    {
        if (object == null) {
            object = new MutableLiveData<>();
        }
        return object;
    }


    public void getData(Map<String, String> headers, String query,int status){
        isLoading.setValue(true);
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        Call<GoalGetAll> container = api.getGoals(headers, query, start, length, "id", "desc",status,"","");
        container.enqueue(new Callback<GoalGetAll>() {
            @Override
            public void onResponse(@NonNull Call<GoalGetAll> call, @NonNull Response<GoalGetAll> response) {
                isLoading.setValue(false);
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    GoalGetAll resource = response.body();
                    assert resource != null;
                    object.setValue(resource);
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<GoalGetAll> call, @NonNull Throwable t) {
                t.printStackTrace();
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

    public void deteteItem(Map<String, String> headers, Integer id){
        this.service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);
        Call<GoalAdd> container = api.removeGoal(headers, id);
        container.enqueue(new Callback<GoalAdd>() {
            @Override
            public void onResponse(@NonNull Call<GoalAdd> call, @NonNull Response<GoalAdd> response) {
                isLoading.setValue(false);
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    GoalAdd resource = response.body();
                    assert resource != null;
                    System.out.println(resource.toString());
                    return;
                }
                object.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<GoalAdd> call, @NonNull Throwable t) {
                t.printStackTrace();
                isLoading.setValue(false);
                object.setValue(null);
            }
        });
    }

}
