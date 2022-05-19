package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.CategoryGetAll;
import com.example.prudentialfinance.Model.Category;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Category>> categories = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Category>> getCategories() {
        return categories;
    }

    /***
     * @author Phong-Kaster
     * headers is the header of HTTP Request
     * type is string number, it tells api which income or expense categories will be retrieved
     * */
    public void instanciate(Map<String, String> headers, String type)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 2*/
        retrieveCategories(headers, api, type);
    }

    private void retrieveCategories(Map<String, String> headers, HTTPRequest api, String type) {
        if( categories == null)
        {
            categories = new MutableLiveData<>();
        }

        if( type.length() < 0)
        {
            type = "1";
        }

        Map<String, String> options = new HashMap<>();
        options.put("search","");
        options.put("start", "0");
        options.put("length", "10");
        options.put("order[column]","");
        options.put("order[dir]","desc");

        Call<CategoryGetAll> container = type.equals("1") ?
                api.retrieveInflowCategories(headers, options) :
                api.retrieveOutflowCategories(headers,options);



        container.enqueue(new Callback<CategoryGetAll>() {
            @Override
            public void onResponse(@NonNull Call<CategoryGetAll> call, @NonNull Response<CategoryGetAll> response) {
                if(response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    } catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                }
                if(response.isSuccessful())
                {
                    CategoryGetAll resource = response.body();

                    assert resource != null;
                    ArrayList<Category> array = resource.getData();

                    categories.postValue(array);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryGetAll> call, @NonNull Throwable t) {

            }
        });
    }
}
