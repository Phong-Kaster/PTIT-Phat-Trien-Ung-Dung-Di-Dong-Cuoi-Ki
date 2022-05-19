package com.example.prudentialfinance.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Parcelable{

    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("balance")
    @Expose
    private Integer balance;


    @SerializedName("accountnumber")
    @Expose
    private String accountnumber;


    @SerializedName("description")
    @Expose
    private String description;

    protected Account(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        if (in.readByte() == 0) {
            balance = null;
        } else {
            balance = in.readInt();
        }
        accountnumber = in.readString();
        description = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public Account(Integer id, String name, Integer balance, String accountnumber, String description) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.accountnumber = accountnumber;
        this.description = description;
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
        if (balance == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(balance);
        }
        parcel.writeString(accountnumber);
        parcel.writeString(description);
    }
}
