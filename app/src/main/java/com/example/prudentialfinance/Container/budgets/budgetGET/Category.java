package com.example.prudentialfinance.Container.budgets.budgetGET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("color")
    @Expose
    public String color;
    @SerializedName("description")
    @Expose
    public String description;

    public Category(int id, String name, int type, String color, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
        this.description = description;
    }
    public Category(int id)
    {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}



