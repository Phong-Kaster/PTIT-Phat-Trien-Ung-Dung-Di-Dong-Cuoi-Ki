package com.example.prudentialfinance.Container.Settings;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvatarUpload {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("image")
    @Expose
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @NonNull
    @Override
    public String toString() {
        return "AvatarUpload{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", image='" + image + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
