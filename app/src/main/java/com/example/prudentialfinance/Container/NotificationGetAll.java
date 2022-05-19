package com.example.prudentialfinance.Container;

import com.example.prudentialfinance.Model.Notification;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationGetAll {
    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("data")
    @Expose
    private ArrayList<Notification> data;

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("msg")
    @Expose
    private String msg;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ArrayList<Notification> getData() {
        return data;
    }

    public void setData(ArrayList<Notification> data) {
        this.data = data;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "NotificationGetAll{" +
                "method='" + method + '\'' +
                ", data=" + data +
                ", result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }
}
