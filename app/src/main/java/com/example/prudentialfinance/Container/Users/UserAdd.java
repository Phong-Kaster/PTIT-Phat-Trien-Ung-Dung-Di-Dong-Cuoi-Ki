package com.example.prudentialfinance.Container.Users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAdd {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("user")
    @Expose
    private int user;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("method")
    @Expose
    private String method;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "UserAdd{" +
                "result=" + result +
                ", user=" + user +
                ", msg='" + msg + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
