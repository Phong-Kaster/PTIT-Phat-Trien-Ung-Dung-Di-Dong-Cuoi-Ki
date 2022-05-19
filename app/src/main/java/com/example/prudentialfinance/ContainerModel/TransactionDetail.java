package com.example.prudentialfinance.ContainerModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionDetail implements Parcelable {
    @SerializedName("amount")
    @Expose
    private Integer amount;


    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("reference")
    @Expose
    private String reference;


    @SerializedName("transactiondate")
    @Expose
    private String transactiondate;


    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("type")
    @Expose
    private Integer type;


    @SerializedName("account")
    @Expose
    private Account account;


    @SerializedName("category")
    @Expose
    private Category category;

    protected TransactionDetail(Parcel in) {
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readInt();
        }
        description = in.readString();
        name = in.readString();
        reference = in.readString();
        transactiondate = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readInt();
        }
        category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<TransactionDetail> CREATOR = new Creator<TransactionDetail>() {
        @Override
        public TransactionDetail createFromParcel(Parcel in) {
            return new TransactionDetail(in);
        }

        @Override
        public TransactionDetail[] newArray(int size) {
            return new TransactionDetail[size];
        }
    };

    public Integer getAmount() {
        return amount;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }


    public String getTransactiondate() {
        return transactiondate;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (amount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(amount);
        }
        parcel.writeString(description);
        parcel.writeString(name);
        parcel.writeString(reference);
        parcel.writeString(transactiondate);
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        if (type == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(type);
        }
        parcel.writeParcelable(category, i);
    }
}
