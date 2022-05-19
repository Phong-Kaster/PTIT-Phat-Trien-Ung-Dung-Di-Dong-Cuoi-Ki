package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.HomeLatestTransactions;
import com.example.prudentialfinance.Container.Transactions.TransactionCreate;
import com.example.prudentialfinance.Container.Transactions.TransactionRemove;
import com.example.prudentialfinance.Container.Transactions.TransactionUpdate;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionViewModel extends ViewModel {
    private final MutableLiveData<Integer> transactionCreation = new MutableLiveData<>();
    private final MutableLiveData<Integer> transactionUpdate = new MutableLiveData<>();
    private final MutableLiveData<Integer> transactionRemoval = new MutableLiveData<>();
    private final MutableLiveData<String> transactionMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> animation = new MutableLiveData<>();
    private final MutableLiveData<HomeLatestTransactions> transactionCreationStatement = new MutableLiveData<>();

    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }

    public MutableLiveData<String> getTransactionMessage() {
        return transactionMessage;
    }

    public MutableLiveData<Integer> getTransactionCreation() {
        return transactionCreation;
    }

    public MutableLiveData<Integer> getTransactionUpdate() {
        return transactionUpdate;
    }

    public MutableLiveData<Integer> getTransactionRemoval() {
        return transactionRemoval;
    }

    public MutableLiveData<HomeLatestTransactions> getTransactionCreationStatement() {
        return transactionCreationStatement;
    }

    /**
     * @author Phong-Kaster
     * create a whole new transaction
     * */
    public void createTransaction(Map<String ,String> headers,
                                  String categoryId,
                                  String accountId,
                                  String name,
                                  String amount,
                                  String reference,
                                  String transactionDate,
                                  String type,
                                  String description)
    {
        animation.setValue(true);
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<TransactionCreate> container = api.transactionCreate(headers,
                categoryId,
                accountId,
                name,
                amount,
                reference,
                transactionDate,
                type,
                description  );

        /*Step 3*/
        container.enqueue(new Callback<TransactionCreate>() {
            @Override
            public void onResponse(@NonNull Call<TransactionCreate> call,
                                   @NonNull Response<TransactionCreate> response) {
                if( response.isSuccessful())
                {
                    TransactionCreate resource = response.body();
                    animation.setValue(false);


                    assert resource != null;
                    String msg = resource.getMsg();
                    int id = resource.getTransaction();

                    transactionMessage.setValue(msg);
                    transactionCreation.setValue(id);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionCreate> call,
                                  @NonNull Throwable t) {

            }
        });
    }

    /**
     * @author Phong-Kaster
     * remove a transaction
     *
     * headers is the header of HTTP Request
     * id is the transaction's id that is being removing
     * */
    public void eradicateTransaction(Map<String ,String> headers, String id)
    {
        animation.setValue(true);
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<TransactionRemove> container = api.transactionRemove(headers, id);

        /*Step 3*/
        container.enqueue(new Callback<TransactionRemove>() {
            @Override
            public void onResponse(@NonNull Call<TransactionRemove> call,
                                   @NonNull Response<TransactionRemove> response) {
                animation.setValue(false);
                if(response.isSuccessful())
                {
                    TransactionRemove resource = response.body();
                    assert resource != null;
                    int result = resource.getResult();
                    transactionRemoval.setValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionRemove> call,
                                  @NonNull Throwable t) {
                animation.setValue(false);
            }
        });
    }

    public void updateTransaction(Map<String ,String> headers,
                                  int id,
                                  String categoryId,
                                  String accountId,
                                  String name,
                                  String amount,
                                  String reference,
                                  String transactionDate,
                                  String type,
                                  String description)
    {
        animation.setValue(true);
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<TransactionUpdate> container = api.transactionUpdate(headers, id,
                categoryId,
                accountId,
                name,
                amount,
                reference,
                transactionDate,
                type,
                description);


        /*Step 3*/
        container.enqueue(new Callback<TransactionUpdate>() {
            @Override
            public void onResponse(@NonNull Call<TransactionUpdate> call,
                                   @NonNull Response<TransactionUpdate> response) {
                animation.setValue(false);
                if(response.isSuccessful())
                {
                    TransactionUpdate resource = response.body();

                    assert resource != null;
                    int result = resource.getResult();
                    String msg = resource.getMsg();
                    transactionMessage.setValue(msg);
                    transactionUpdate.setValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionUpdate> call, @NonNull Throwable t) {
                animation.setValue(true);
            }
        });
    }

    /**
     * @author Phong-Kaster
     * create statement
     * */
    public void createStatement(Map<String, String> headers, Map<String, String> body)
    {
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);
        animation.setValue(true);

        /*Step 2*/
        Call<HomeLatestTransactions> container = api.homeLatestTransactions(headers, body);

        /*Step 3*/
        container.enqueue(new Callback<HomeLatestTransactions>() {
            @Override
            public void onResponse(@NonNull Call<HomeLatestTransactions> call, @NonNull Response<HomeLatestTransactions> response) {
                if(response.isSuccessful())
                {
                    animation.setValue(false);
                    HomeLatestTransactions resource = response.body();
                    transactionCreationStatement.postValue(resource);
                    return;
                }else{
                    transactionCreationStatement.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeLatestTransactions> call, @NonNull Throwable t) {
                animation.setValue(false);
                transactionCreationStatement.postValue(null);
            }
        });
    }
}
