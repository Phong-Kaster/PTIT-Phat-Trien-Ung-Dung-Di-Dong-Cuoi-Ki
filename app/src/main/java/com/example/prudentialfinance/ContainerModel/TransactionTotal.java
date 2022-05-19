package com.example.prudentialfinance.ContainerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionTotal {
    @SerializedName("totalbalance")
    @Expose
    private double totalBalance;

    @SerializedName("month")
    @Expose
    private double month;

    @SerializedName("week")
    @Expose
    private double week;

    @SerializedName("day")
    @Expose
    private double day;

    @SerializedName("year")
    @Expose
    private double year;

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public double getMonth() {
        return month;
    }

    public void setMonth(double month) {
        this.month = month;
    }

    public double getWeek() {
        return week;
    }

    public void setWeek(double week) {
        this.week = week;
    }

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getYear() {
        return year;
    }

    public void setYear(double year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "TransactionTotal{" +
                "totalBalance=" + totalBalance +
                ", month=" + month +
                ", week=" + week +
                ", day=" + day +
                ", year=" + year +
                '}';
    }
}
