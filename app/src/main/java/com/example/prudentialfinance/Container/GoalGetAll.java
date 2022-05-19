package com.example.prudentialfinance.Container;

import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.Goal;
import com.example.prudentialfinance.Model.Summary;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoalGetAll {
    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("summary")
    @Expose
    private Summary summary;

    @SerializedName("data")
    @Expose
    private ArrayList<Goal> data;

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("currency")
    @Expose
    private String currency;


    @SerializedName("msg")
    @Expose
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "GoalGetAll{" +
                "method=" + method +
                ", summary=" + summary +
                ", data=" + data +
                ", result='" + result + '\'' +
                ", currency='" + currency + '\'' +
                '}';
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

    public ArrayList<Goal> getData() {
        return data;
    }

    public void setData(ArrayList<Goal> data) {
        this.data = data;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
