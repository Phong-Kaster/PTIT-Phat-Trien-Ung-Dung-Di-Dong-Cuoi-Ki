package com.example.prudentialfinance.Container.Transactions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionUpdate {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("method")
    @Expose
    private String method;

    public int getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public String getMethod() {
        return method;
    }
}
