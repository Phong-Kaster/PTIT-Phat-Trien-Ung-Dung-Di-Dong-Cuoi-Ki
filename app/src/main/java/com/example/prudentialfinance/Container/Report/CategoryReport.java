package com.example.prudentialfinance.Container.Report;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryReport implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("amount")
    @Expose
    private Double amount;

    @SerializedName("total")
    @Expose
    private Double total;

    protected CategoryReport(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        color = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readDouble();
        }
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readDouble();
        }
    }

    public static final Creator<CategoryReport> CREATOR = new Creator<CategoryReport>() {
        @Override
        public CategoryReport createFromParcel(Parcel in) {
            return new CategoryReport(in);
        }

        @Override
        public CategoryReport[] newArray(int size) {
            return new CategoryReport[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CategoryReport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", amount=" + amount +
                ", total=" + total +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(name);
        parcel.writeString(color);
        if (amount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(amount);
        }
        if (total == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(total);
        }
    }
}
