package com.example.prudentialfinance.Container;

import com.example.prudentialfinance.Model.Notification;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class NotificationResponse {
    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("data")
    @Expose
    private Notification data;

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

    public Notification getData() {
        return data;
    }

    public void setData(Notification data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NotificationResponse{" +
                "method='" + method + '\'' +
                ", result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }
}
