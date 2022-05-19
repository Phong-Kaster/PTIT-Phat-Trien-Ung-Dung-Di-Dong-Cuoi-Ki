package com.example.prudentialfinance.Container.Transactions;

import com.example.prudentialfinance.ContainerModel.TransactionTotal;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionGetTotal {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("data")
    @Expose
    private TransactionTotal data;

    @SerializedName("method")
    @Expose
    private String method;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public TransactionTotal getData() {
        return data;
    }

    public void setData(TransactionTotal data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "TransactionGetTotal{" +
                "result=" + result +
                ", data=" + data +
                ", method='" + method + '\'' +
                '}';
    }
}
