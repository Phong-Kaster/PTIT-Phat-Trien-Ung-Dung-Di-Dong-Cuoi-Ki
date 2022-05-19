package com.example.prudentialfinance.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountBalance {

    @SerializedName("balance")
    @Expose
    private double balance;

    @SerializedName("expense")
    @Expose
    private double expense;

    @SerializedName("income")
    @Expose
    private double income;

    @SerializedName("name")
    @Expose
    private String name;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "balance=" + balance +
                ", expense=" + expense +
                ", income=" + income +
                ", name='" + name + '\'' +
                '}';
    }
}
