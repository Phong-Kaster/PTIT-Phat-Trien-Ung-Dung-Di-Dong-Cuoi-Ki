package com.example.prudentialfinance.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountMonthly {
    @SerializedName("jan")
    @Expose
    private double jan;

    @SerializedName("feb")
    @Expose
    private double feb;

    @SerializedName("mar")
    @Expose
    private double mar;

    @SerializedName("apr")
    @Expose
    private double apr;

    @SerializedName("may")
    @Expose
    private double may;

    @SerializedName("jun")
    @Expose
    private double jun;

    @SerializedName("jul")
    @Expose
    private double jul;

    @SerializedName("aug")
    @Expose
    private double aug;

    @SerializedName("sep")
    @Expose
    private double sep;

    @SerializedName("oct")
    @Expose
    private double oct;

    @SerializedName("nov")
    @Expose
    private double nov;

    @SerializedName("dec")
    @Expose
    private double dec;

    public double getJan() {
        return jan;
    }

    public void setJan(double jan) {
        this.jan = jan;
    }

    public double getFeb() {
        return feb;
    }

    public void setFeb(double feb) {
        this.feb = feb;
    }

    public double getMar() {
        return mar;
    }

    public void setMar(double mar) {
        this.mar = mar;
    }

    public double getApr() {
        return apr;
    }

    public void setApr(double apr) {
        this.apr = apr;
    }

    public double getMay() {
        return may;
    }

    public void setMay(double may) {
        this.may = may;
    }

    public double getJun() {
        return jun;
    }

    public void setJun(double jun) {
        this.jun = jun;
    }

    public double getJul() {
        return jul;
    }

    public void setJul(double jul) {
        this.jul = jul;
    }

    public double getAug() {
        return aug;
    }

    public void setAug(double aug) {
        this.aug = aug;
    }

    public double getSep() {
        return sep;
    }

    public void setSep(double sep) {
        this.sep = sep;
    }

    public double getOct() {
        return oct;
    }

    public void setOct(double oct) {
        this.oct = oct;
    }

    public double getNov() {
        return nov;
    }

    public void setNov(double nov) {
        this.nov = nov;
    }

    public double getDec() {
        return dec;
    }

    public void setDec(double dec) {
        this.dec = dec;
    }

    @Override
    public String toString() {
        return "AccountMonthly{" +
                "jan=" + jan +
                ", feb=" + feb +
                ", mar=" + mar +
                ", apr=" + apr +
                ", may=" + may +
                ", jun=" + jun +
                ", jul=" + jul +
                ", aug=" + aug +
                ", sep=" + sep +
                ", oct=" + oct +
                ", nov=" + nov +
                ", dec=" + dec +
                '}';
    }
}
