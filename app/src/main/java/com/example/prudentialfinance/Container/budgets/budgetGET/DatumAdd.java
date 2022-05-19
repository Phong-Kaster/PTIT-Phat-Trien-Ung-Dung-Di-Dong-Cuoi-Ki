package com.example.prudentialfinance.Container.budgets.budgetGET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DatumAdd implements Serializable {
    public int id;
    public int category;
    public int user;
    public String amount;
    public String fromdate;
    public String todate;
    public String description;

    public DatumAdd(int id)
    {
        this.id = id;
    }
    @Override
    public String toString() {
        return "DatumAAS{" +
                "id=" + id +
                ", category=" + category + " " + category +
                ", user=" + user +
                ", amount=" + amount +
                ", fromdate='" + fromdate + '\'' +
                ", todate='" + todate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public DatumAdd(int id, int category, int user, String amount, String fromdate, String todate, String description) {
        this.id = id;
        this.category = category;
        this.user = user;
        this.amount = amount;
        this.fromdate = fromdate;
        this.todate = todate;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public String getIdString() {
        return String.valueOf(id);
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }
    public String getCategoryString() {
        return String.valueOf(category);
    }
    public void setCategory(int category) {
        this.category = category;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getAmount() {
        return amount;
    }
    public int getAmountInt() {
        return Integer.parseInt(amount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
