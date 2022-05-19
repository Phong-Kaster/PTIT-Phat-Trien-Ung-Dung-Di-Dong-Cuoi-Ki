package com.example.prudentialfinance.Container;

import com.example.prudentialfinance.Model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @SerializedName("data")
    @Expose
    private User data;

    @SerializedName("method")
    @Expose
    private String method;

    public int getResult() {
        return result;
    }

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("hash")
    @Expose
    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
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
        return "Login{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", data=" + data +
                ", method='" + method + '\'' +
                ", email='" + email + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
