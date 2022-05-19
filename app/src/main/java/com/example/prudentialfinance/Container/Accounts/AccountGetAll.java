package com.example.prudentialfinance.Container.Accounts;

import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.Summary;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AccountGetAll {

    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("summary")
    @Expose
    private Summary summary;


    @SerializedName("data")
    @Expose
    private ArrayList<Account> data;


    @SerializedName("method")
    @Expose
    private String method;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public ArrayList<Account> getData() {
        return data;
    }

    public void setData(ArrayList<Account> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
