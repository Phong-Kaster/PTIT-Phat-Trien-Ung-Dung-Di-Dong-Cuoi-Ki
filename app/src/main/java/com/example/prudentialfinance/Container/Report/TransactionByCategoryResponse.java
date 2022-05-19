package com.example.prudentialfinance.Container.Report;


import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Model.Summary;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TransactionByCategoryResponse {


    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("summary")
    @Expose
    private Summary summary;

    @SerializedName("data")
    @Expose
    private ArrayList<TransactionDetail> data;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("msg")
    @Expose
    private String msg;

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

    public ArrayList<TransactionDetail> getData() {
        return data;
    }

    public void setData(ArrayList<TransactionDetail> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TransactionByCategoryResponse{" +
                "result=" + result +
                ", summary=" + summary +
                ", data=" + data +
                ", method='" + method + '\'' +
                '}';
    }
}
