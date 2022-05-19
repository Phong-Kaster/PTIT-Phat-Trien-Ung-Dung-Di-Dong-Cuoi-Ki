package com.example.prudentialfinance.Container.Accounts;

import com.example.prudentialfinance.Model.AccountBalance;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AccountBalanceResponse {
    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("data")
    @Expose
    private ArrayList<AccountBalance> data;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ArrayList<AccountBalance> getData() {
        return data;
    }

    public void setData(ArrayList<AccountBalance> data) {
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
        return "AccountBalanceResponse{" +
                "result=" + result +
                ", data=" + data +
                ", method='" + method + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
