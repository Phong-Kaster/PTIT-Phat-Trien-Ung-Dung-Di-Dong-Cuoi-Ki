package com.example.prudentialfinance.Container;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryAdd {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("category")
    @Expose
    private int category;

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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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