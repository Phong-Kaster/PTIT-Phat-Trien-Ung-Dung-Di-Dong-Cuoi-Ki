package com.example.prudentialfinance.Container.Transactions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionCreate {
    @SerializedName("name")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private  String msg;

    @SerializedName("transaction")
    @Expose
    private int transaction;

    @SerializedName("method")
    @Expose
    private String method;

    public int getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public int getTransaction() {
        return transaction;
    }

    public String getMethod() {
        return method;
    }
}
