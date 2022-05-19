package com.example.prudentialfinance.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SiteSettings implements Parcelable {
    private String site_name;
    private String site_description;
    private String site_keywords;
    private String currency;
    private String logomark;
    private String logotype;
    private String site_slogan;
    private String language;

    public SiteSettings() {
    }

    public SiteSettings(String site_name, String site_description, String site_keywords, String currency, String logomark, String logotype, String site_slogan, String language) {
        this.site_name = site_name;
        this.site_description = site_description;
        this.site_keywords = site_keywords;
        this.currency = currency;
        this.logomark = logomark;
        this.logotype = logotype;
        this.site_slogan = site_slogan;
        this.language = language;
    }


    protected SiteSettings(Parcel in) {
        site_name = in.readString();
        site_description = in.readString();
        site_keywords = in.readString();
        currency = in.readString();
        logomark = in.readString();
        logotype = in.readString();
        site_slogan = in.readString();
        language = in.readString();
    }

    public static final Creator<SiteSettings> CREATOR = new Creator<SiteSettings>() {
        @Override
        public SiteSettings createFromParcel(Parcel in) {
            return new SiteSettings(in);
        }

        @Override
        public SiteSettings[] newArray(int size) {
            return new SiteSettings[size];
        }
    };

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getSite_description() {
        return site_description;
    }

    public void setSite_description(String site_description) {
        this.site_description = site_description;
    }

    public String getSite_keywords() {
        return site_keywords;
    }

    public void setSite_keywords(String site_keywords) {
        this.site_keywords = site_keywords;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLogomark() {
        return logomark;
    }

    public void setLogomark(String logomark) {
        this.logomark = logomark;
    }

    public String getLogotype() {
        return logotype;
    }

    public void setLogotype(String logotype) {
        this.logotype = logotype;
    }

    public String getSite_slogan() {
        return site_slogan;
    }

    public void setSite_slogan(String site_slogan) {
        this.site_slogan = site_slogan;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "SiteSettings{" +
                "site_name='" + site_name + '\'' +
                ", site_description='" + site_description + '\'' +
                ", site_keywords='" + site_keywords + '\'' +
                ", currency='" + currency + '\'' +
                ", logomark='" + logomark + '\'' +
                ", logotype='" + logotype + '\'' +
                ", site_slogan='" + site_slogan + '\'' +
                ", language='" + language + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(site_name);
        parcel.writeString(site_description);
        parcel.writeString(site_keywords);
        parcel.writeString(currency);
        parcel.writeString(logomark);
        parcel.writeString(logotype);
        parcel.writeString(site_slogan);
        parcel.writeString(language);
    }
}
