package com.example.prudentialfinance.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("account_type")
    @Expose
    private String account_type;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("firstname")
    @Expose
    private String firstname;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("is_active")
    @Expose
    private Boolean is_active;

    @SerializedName("date")
    @Expose
    private String date;

    public User(String account_type, String email, String firstname, String lastname, String avatar, String language, Integer id, Boolean is_active, String date) {
        this.account_type = account_type;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.avatar = avatar;
        this.language = language;
        this.id = id;
        this.is_active = is_active;
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "account_type='" + account_type + '\'' +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", language='" + language + '\'' +
                ", id=" + id +
                ", is_active=" + is_active +
                ", date='" + date + '\'' +
                '}';
    }

    protected User(Parcel in) {
        account_type = in.readString();
        email = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        avatar = in.readString();
        language = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        byte tmpIs_active = in.readByte();
        is_active = tmpIs_active == 0 ? null : tmpIs_active == 1;
        date = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(account_type);
        parcel.writeString(email);
        parcel.writeString(firstname);
        parcel.writeString(lastname);
        parcel.writeString(avatar);
        parcel.writeString(language);
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeByte((byte) (is_active == null ? 0 : is_active ? 1 : 2));
        parcel.writeString(date);
    }
}
