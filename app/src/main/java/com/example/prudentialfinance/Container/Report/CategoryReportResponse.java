package com.example.prudentialfinance.Container.Report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryReportResponse {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private ArrayList<CategoryReport> data;

    @SerializedName("date")
    @Expose
    private DateRange date;

    @SerializedName("method")
    @Expose
    private String method;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<CategoryReport> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryReport> data) {
        this.data = data;
    }

    public DateRange getDate() {
        return date;
    }

    public void setDate(DateRange date) {
        this.date = date;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "CategoryReportResponse{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", date=" + date +
                ", method='" + method + '\'' +
                '}';
    }
}
