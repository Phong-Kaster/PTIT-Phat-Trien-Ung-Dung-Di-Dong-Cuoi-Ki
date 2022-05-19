package com.example.prudentialfinance.Container.Report;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateRange implements Parcelable {
    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("to")
    @Expose
    private String to;

    public DateRange() {
    }

    public DateRange(String from, String to) {
        this.from = from;
        this.to = to;
    }

    protected DateRange(Parcel in) {
        from = in.readString();
        to = in.readString();
    }

    public static final Creator<DateRange> CREATOR = new Creator<DateRange>() {
        @Override
        public DateRange createFromParcel(Parcel in) {
            return new DateRange(in);
        }

        @Override
        public DateRange[] newArray(int size) {
            return new DateRange[size];
        }
    };

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(from);
        parcel.writeString(to);
    }
}
