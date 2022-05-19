package com.example.prudentialfinance.Container.budgets.budgetGET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BudgetAdd implements Serializable {

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("budget")
    @Expose
    private int budget;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("msg")
    @Expose
    private String msg;

    public BudgetAdd(int i) {
        this.budget = i;

    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
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
        return "GoalAdd{" +
                "result=" + result +
                ", budget=" + budget +
                ", method='" + method + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
