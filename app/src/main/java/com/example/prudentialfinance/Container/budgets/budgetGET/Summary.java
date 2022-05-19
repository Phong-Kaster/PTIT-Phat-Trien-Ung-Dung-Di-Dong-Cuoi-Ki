package com.example.prudentialfinance.Container.budgets.budgetGET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Summary implements Serializable {
    @SerializedName("total_count")
    @Expose
    public int total_count;

    public Summary(int total_count) {
        this.total_count = total_count;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
}
