package com.example.prudentialfinance.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prudentialfinance.API.HTTPRequest;
import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Container.Accounts.AccountCreate;
import com.example.prudentialfinance.Container.Accounts.AccountDelete;
import com.example.prudentialfinance.Container.Accounts.AccountEdit;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardViewModel extends ViewModel {
    private final MutableLiveData<Integer> accountCreation = new MutableLiveData<>();
    private final MutableLiveData<Integer> accountUpdate = new MutableLiveData<>();
    private final MutableLiveData<String> accountRemoval = new MutableLiveData<>();
    private final MutableLiveData<AccountEdit> accountUpdateResource = new MutableLiveData<>();
    private final MutableLiveData<AccountCreate> accountCreationResource = new MutableLiveData<>();
    private final MutableLiveData<String> accountMessage = new MutableLiveData<>();

    private final MutableLiveData<Boolean> animation = new MutableLiveData<>();

    public MutableLiveData<AccountEdit> getAccountUpdateResource() {
        return accountUpdateResource;
    }

    public MutableLiveData<String> getAccountMessage() {
        return accountMessage;
    }

    public MutableLiveData<Boolean> getAnimation() {
        return animation;
    }

    public MutableLiveData<String> getAccountRemoval() {
        return accountRemoval;
    }


    public MutableLiveData<Integer> getAccountUpdate() {
        return accountUpdate;
    }


    public MutableLiveData<Integer> getAccountCreation() {
        return accountCreation;
    }


    public MutableLiveData<AccountCreate> getAccountCreationResource() {
        return accountCreationResource;
    }

    /**
     * @author Phong-Kaster
     * send HTTP Request to create account
     * */
    public void createAccount(Map<String, String> headers, String name, String balance, String description, String accountnumber)
    {
        animation.setValue(true);
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<AccountCreate> container = api.accountCreate(headers, name, balance, description, accountnumber);


        /*Step 3*/
        container.enqueue(new Callback<AccountCreate>() {
            @Override
            public void onResponse(@NonNull Call<AccountCreate> call,@NonNull Response<AccountCreate> response) {
                if(response.isSuccessful())
                {
                    animation.setValue(false);
                    AccountCreate resource = response.body();
                    assert resource != null;
                    int result = Math.max(resource.getResult(), 0);
                    String msg = resource.getMsg();

                    System.out.println(result);
                    System.out.println(msg);

                    accountCreationResource.setValue(resource);

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
            public void onFailure(@NonNull Call<AccountCreate> call,@NonNull Throwable t) {

            }
        });
    }

    /**
     * @author Phong-Kaster
     * send HTTP Request to update account
     * */
    public void updateAccount(Map<String, String> headers, int id, String name, String balance, String description, String accountnumber) {
        /*Step 1*/
        animation.setValue(true);
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<AccountEdit> container = api.accountUpdate(headers, id,name, balance, description, accountnumber);

        /*Step 3*/
        container.enqueue(new Callback<AccountEdit>() {
            @Override
            public void onResponse(@NonNull Call<AccountEdit> call, @NonNull Response<AccountEdit> response) {
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
                    animation.setValue(false);
                    AccountEdit resource = response.body();

                    assert resource != null;


                    accountUpdateResource.setValue(resource);

                }

            }

            @Override
            public void onFailure(@NonNull Call<AccountEdit> call, @NonNull Throwable t) {

            }
        });
    }

    /**
     * @author Phong-Kaster
     * send HTTP Request to update account
     * */
    public void deleteAccount(Map<String, String> headers, int id) {
        animation.setValue(true);
        /*Step 1*/
        Retrofit service = HTTPService.getInstance();
        HTTPRequest api = service.create(HTTPRequest.class);


        /*Step 2*/
        Call<AccountDelete> container = api.accountDelete(headers, id);

        /*Step 3*/
        container.enqueue(new Callback<AccountDelete>() {
            @Override
            public void onResponse(@NonNull Call<AccountDelete> call, @NonNull Response<AccountDelete> response) {
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
                    animation.setValue(false);
                    AccountDelete resource = response.body();

                    assert resource != null;
                    String msg = resource.getMsg();

                    accountRemoval.setValue(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountDelete> call, @NonNull Throwable t) {

            }
        });


    }
}