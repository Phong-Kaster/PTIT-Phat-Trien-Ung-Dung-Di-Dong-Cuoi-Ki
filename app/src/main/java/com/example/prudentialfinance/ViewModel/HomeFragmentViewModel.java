package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.HomeLatestTransactions;
import com.example.prudentialfinance.Container.ReportTotalBalance;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragmentViewModel extends ViewModel {
    private  MutableLiveData<List<TransactionDetail>> transactions = new MutableLiveData<>();
    private final MutableLiveData<Double> totalBalance = new MutableLiveData<>();
    private final MutableLiveData<Boolean> animation = new MutableLiveData<>();

    public void setTransactions(List<TransactionDetail> transactions) {
        MutableLiveData<List<TransactionDetail>> dataSet = new MutableLiveData<>();
        dataSet.setValue(transactions);
        this.transactions = dataSet;
    }

    public void instanciate(Map<String, String> headers)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 2*/
        retrieveTransactions(headers, api);
        retrieveTotalBalance(headers, api);
    }

    public void retrieveWithQuery(Map<String, String> headers, String query)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);

        /*Step 2*/
        retrieveTransactionsWithQuery(headers, api, query);
    }

    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }

    public MutableLiveData<List<TransactionDetail>> getTransactions() {
        return transactions;
    }

    public MutableLiveData<Double> getTotalBalance() {
        return totalBalance;
    }


    /**
     * @author Phong-Kaster
     *
     * get all latest transactions in the last 7 days
     *
     * @param headers headers is used to attach to HTTP Request headers
     * */
    public void retrieveTransactions(Map<String, String> headers, HTTPRequest api)
    {
        animation.setValue(true);
        /*Step 2*/

        Map<String, String> options = new HashMap<>();
        options.put("start", "0");
        options.put("length", "10");
        options.put("draw", "1");
        options.put("order[column]","");
        options.put("order[dir]","desc");
        options.put("search","");

        Call<HomeLatestTransactions> container = api.homeLatestTransactions(headers, options);


        /*Step 3*/
        container.enqueue(new Callback<HomeLatestTransactions>() {
            @Override
            public void onResponse(@NonNull Call<HomeLatestTransactions> call, @NonNull Response<HomeLatestTransactions> response) {
                if(response.isSuccessful())
                {
                    animation.setValue(false);
                    HomeLatestTransactions resource = response.body();

                    assert resource != null;
                    List<TransactionDetail> array = resource.getData();
                    transactions.postValue(array);
                }
                if(response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    } catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeLatestTransactions> call, @NonNull Throwable t) {

            }
        });

        animation.setValue(false);
    }


    public void retrieveTransactionsWithQuery(Map<String, String> headers, HTTPRequest api, String search)
    {
        animation.setValue(true);
        /*Step 2*/

        Map<String, String> options = new HashMap<>();
        options.put("start", "0");
        options.put("length", "10");
        options.put("draw", "1");
        options.put("order[column]","");
        options.put("order[dir]","desc");
        options.put("search",search);

        Call<HomeLatestTransactions> container = api.homeLatestTransactions(headers, options);

        /*Step 3*/
        container.enqueue(new Callback<HomeLatestTransactions>() {
            @Override
            public void onResponse(@NonNull Call<HomeLatestTransactions> call, @NonNull Response<HomeLatestTransactions> response) {
                if(response.isSuccessful())
                {
                    animation.setValue(false);
                    HomeLatestTransactions resource = response.body();

                    assert resource != null;
                    List<TransactionDetail> array = resource.getData();
                    transactions.postValue(array);
                    return;
                }
                if(response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        System.out.println( jObjError );
                    } catch (Exception e) {
                        System.out.println( e.getMessage() );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeLatestTransactions> call, @NonNull Throwable t) {

            }
        });
    }

    /**
     * @author Phong-Kaster
     *
     * get account's remaining in the 7 days
     *
     * @param headers headers is used to attach to HTTP Request headers
     * */
    public void retrieveTotalBalance(Map<String, String> headers, HTTPRequest api)
    {
        animation.setValue(true);
        /*Step 2*/
        Call<ReportTotalBalance> container = api.reportTotalBalace(headers, "month");

        /*Step 3*/
        container.enqueue(new Callback<ReportTotalBalance>() {
            @Override
            public void onResponse(@NonNull Call<ReportTotalBalance> call, @NonNull Response<ReportTotalBalance> response) {
                animation.setValue(false);
                if(response.isSuccessful())
                {
                    ReportTotalBalance resource = response.body();
                    assert resource != null;
                    totalBalance.setValue(resource.getMonth());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReportTotalBalance> call,@NonNull Throwable t) {
                animation.setValue(false);
                totalBalance.setValue(0.0);
            }
        });
    }
}
