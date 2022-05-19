package com.example.prudentialfinance.Container.Accounts;

import com.example.prudentialfinance.Model.Account;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountGetById {

    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("data")
    @Expose
    private Account data;

    @SerializedName("method")
    @Expose
    private String method;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
