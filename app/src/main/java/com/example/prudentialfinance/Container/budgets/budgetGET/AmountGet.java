package com.example.prudentialfinance.Container.budgets.budgetGET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmountGet {
    @SerializedName("result")
    @Expose
    int result;
    @SerializedName("totalamount")
    @Expose
    String totalamount;
    @SerializedName("method")
    @Expose
    String method;

    public AmountGet(int result, String totalamount, String method) {
        this.result = result;
        this.totalamount = totalamount;
        this.method = method;
    }

    public AmountGet() {
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
