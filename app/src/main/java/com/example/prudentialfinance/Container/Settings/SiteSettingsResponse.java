package com.example.prudentialfinance.Container.Settings;

import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.Model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SiteSettingsResponse {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private SiteSettings data;

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


    public SiteSettings getData() {
        return data;
    }

    public void setData(SiteSettings data) {
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
        return "SiteSettingsResponse{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", method='" + method + '\'' +
                '}';
    }
}
