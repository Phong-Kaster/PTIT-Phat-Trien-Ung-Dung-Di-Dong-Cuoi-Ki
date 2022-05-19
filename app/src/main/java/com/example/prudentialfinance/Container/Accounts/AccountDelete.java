package com.example.prudentialfinance.Container.Accounts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountDelete {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("account")
    @Expose
    private int account;

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
