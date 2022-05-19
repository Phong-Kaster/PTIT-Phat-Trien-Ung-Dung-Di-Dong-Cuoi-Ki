package com.example.prudentialfinance.Container.budgets.budgetGET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Root{
    @SerializedName("result")
    @Expose
    public int result;
    @SerializedName("method")
    @Expose
    public String method;
    @SerializedName("summary")
    @Expose
    public Summary summary;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("data")
    @Expose
    public ArrayList<Datum> data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Root(int result, String method, Summary summary, String currency, ArrayList<Datum> data) {
        this.result = result;
        this.method = method;
        this.summary = summary;
        this.currency = currency;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Root{" +
                "result=" + result +
                ", method='" + method + '\'' +
                ", summary=" + summary +
                ", currency='" + currency + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }
}