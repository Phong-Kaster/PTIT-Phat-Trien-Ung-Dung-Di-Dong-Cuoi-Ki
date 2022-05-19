package com.example.prudentialfinance.Container;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Model.Summary;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeLatestTransactions {

    @SerializedName("result")
    @Expose
    private int result;


    @SerializedName("summary")
    @Expose
    private Summary summary;


    @SerializedName("data")
    @Expose
    private List<TransactionDetail> data;

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

    public List<TransactionDetail> getData() {
        return data;
    }

    public void setData(List<TransactionDetail> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
