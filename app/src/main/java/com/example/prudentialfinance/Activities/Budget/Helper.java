package com.example.prudentialfinance.Activities.Budget;

import androidx.annotation.NonNull;

public class Helper {

    public static String getDay(@NonNull String date){return date.split("-")[2];}
    public static String getMonth(@NonNull String date){return date.split("-")[1];}
    public static String getYear(@NonNull String date)
    {
        return date.split("-")[0];
    }
    public static int getMonthInt(String date){return Integer.parseInt(getMonth(date));}
    public static int getYearInt(String date){return Integer.parseInt(getYear(date));}
    public static String createDate(String day, String month, String year)
    {
        return year + "-" + month + "-" + day;
    }
}
