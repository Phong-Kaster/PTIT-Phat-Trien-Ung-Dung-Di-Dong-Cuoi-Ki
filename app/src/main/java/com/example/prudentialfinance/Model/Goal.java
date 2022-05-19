package com.example.prudentialfinance.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Goal implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("balance")
    @Expose
    private long balance;

    @SerializedName("amount")
    @Expose
    private long amount;

    public int getId() {
        return id;
    }


    @SerializedName("deposit")
    @Expose
    private long deposit;

    @SerializedName("deadline")
    @Expose
    private String deadline;

    @SerializedName("status")
    @Expose
    private int status;

    public Goal(int id) {
        this.id = id;
    }

    public Goal(int id, String name, long balance, long amount, long deposit, String deadline, int status) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.amount = amount;
        this.deposit = deposit;
        this.deadline = deadline;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", amount=" + amount +
                ", deposit=" + deposit +
                ", deadline='" + deadline + '\'' +
                ", status=" + status +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getDeposit() {
        return deposit;
    }

    public void setDeposit(long deposit) {
        this.deposit = deposit;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
