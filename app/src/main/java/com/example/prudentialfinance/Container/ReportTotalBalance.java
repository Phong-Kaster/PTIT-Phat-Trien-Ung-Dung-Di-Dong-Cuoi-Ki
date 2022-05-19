package com.example.prudentialfinance.Container;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportTotalBalance {
    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("month")
    @Expose
    private Double month;
    @SerializedName("method")
    @Expose
    private String method;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Double getMonth() {
        return month;
    }

    public void setMonth(Double month) {
        this.month = month;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "ReportTotalBalance{" +
                "result=" + result +
                ", month=" + month +
                ", method='" + method + '\'' +
                '}';
    }
}
