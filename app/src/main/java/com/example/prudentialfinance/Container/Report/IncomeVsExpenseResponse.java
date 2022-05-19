package com.example.prudentialfinance.Container.Report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IncomeVsExpenseResponse {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("income")
    @Expose
    private ArrayList<DateReport> income;

    @SerializedName("expense")
    @Expose
    private ArrayList<DateReport> expense;

    @SerializedName("date")
    @Expose
    private DateRange date;

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

    public ArrayList<DateReport> getIncome() {
        return income;
    }

    public void setIncome(ArrayList<DateReport> income) {
        this.income = income;
    }

    public ArrayList<DateReport> getExpense() {
        return expense;
    }

    public void setExpense(ArrayList<DateReport> expense) {
        this.expense = expense;
    }

    public DateRange getDate() {
        return date;
    }

    public void setDate(DateRange date) {
        this.date = date;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "IncomeVsExpenseResponse{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", income=" + income +
                ", expense=" + expense +
                ", date=" + date +
                ", method='" + method + '\'' +
                '}';
    }
}
