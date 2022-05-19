package com.example.prudentialfinance.Container;

import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.Summary;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryGetAll {

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("summary")
    @Expose
    private Summary summary;

    @SerializedName("data")
    @Expose
    private ArrayList<Category> data;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("msg")
    @Expose
    private String msg;

    public void setResult(Integer result) {
        this.result = result;
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

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public ArrayList<Category> getData() {
        return data;
    }

    public void setData(ArrayList<Category> data) {
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
        return "CategoryGetAll{" +
                "result=" + result +
                ", summary=" + summary +
                ", data=" + data +
                ", method='" + method + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
