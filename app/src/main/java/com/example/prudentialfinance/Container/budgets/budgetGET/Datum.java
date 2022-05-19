package com.example.prudentialfinance.Container.budgets.budgetGET;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Datum implements Serializable {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("category")
    @Expose
    public Category category;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("amount")
    @Expose
    public int amount;
    @SerializedName("fromdate")
    @Expose
    public String fromdate;
    @SerializedName("todate")
    @Expose
    public String todate;
    @SerializedName("description")
    @Expose
    public String description;

    public Datum(int id)
    {
        this.id = id;
        fromdate = "31-5-2022";
        todate = "31-5-2022";
    }
    public Datum(int id, Category category, User user, int amount, String fromdate, String todate, String description) {
        this.id = id;
        this.category = category;
        this.user = user;
        this.amount = amount;
        this.fromdate = fromdate;
        this.todate = todate;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Datum{" +
                "id=" + id +
                (category == null ? "null" : ( ", category="+ " " + category.getId() + category.getName() + " " + category.getDescription()))+
                ", user=" + user +
                ", amount=" + amount +
                ", fromdate='" + fromdate + '\'' +
                ", todate='" + todate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return this.category == null ? new Category(0) : category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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
