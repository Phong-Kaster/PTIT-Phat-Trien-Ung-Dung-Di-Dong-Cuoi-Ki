package com.example.prudentialfinance.Helpers;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    public static String convertStringToDate(String input) throws ParseException {
        String output = "01/05";

        String day = input.substring(8,10);
        String month = input.substring(5,7);

        output = day + "/" + month;
        return output;
    }

    /**
     * https://stackoverflow.com/questions/8573250/android-how-can-i-convert-string-to-date
     * @param input
     * @param pattern
     * @return
     */
    public static Date convertStringToDate(String input, String pattern){
        if(pattern.isEmpty()) pattern = "yyyy-MM-dd";
        try{
            // here set the pattern as you date in string was containing like date/month/year
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date d = sdf.parse(input);
            return d;
        }catch(ParseException ex){
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
            return new Date();
        }
    }

    /**
     * @author Phong-Kaster
     * convert application date to server-date date
     * For example 12-05-2022 -> 2022-05-12
     * */
    public static String convertStringToValidDate(String input)
    {
        if( input.length() == 0)
        {
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat forrmatter =
                    new SimpleDateFormat("yyyy-MM-dd");
            return forrmatter.format(date);
        }
        String day = input.substring(0,2);
        String month = input.substring(3,5);
        String year = input.substring(6,10);

        return year + "-" + month + "-" +day;
    }


    /**
     * @author Phong-Kaster
     * convert server-side date to application date
     * for example 2022-05-12 -> 12-05-2022
     * */
    public static String revertStringToReadableDate(String input)
    {
        if( input.length() == 0)
        {
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat forrmatter =
                    new SimpleDateFormat("yyyy-MM-dd");
            return forrmatter.format(date);
        }
        String day = input.substring(8,10);
        String month = input.substring(5,7);
        String year = input.substring(0,4);

        return day + "-" + month + "-" + year;
    }

    /*
    * 123456 -> 123,456
    * */
    public static String formatNumber(int input)
    {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(input);
    }

    public static String formatNumberForListener(int input)
    {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(input)+"-"+input;
    }

    public static String formatNumber(Double input)
    {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(input);
    }

    public static String formatCardNumber(String input)
    {
        if( input.length() <= 4)
            return input;

        int first = (input.length() - 1) % 4 + 1;
        StringBuilder ouput = new StringBuilder(input.substring(0, first));

        for( int i = first ; i <input.length() ;i+=4)
        {
            String fourNumber = input.substring(i, i+4);
            ouput.append(' ').append( fourNumber );
        }

        return ouput.toString();
    }

    public static Transformation getRoundedTransformationBuilder()
    {
        return new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(50)
                .oval(false)
                .build();
    }

    public static Transformation getRoundedTransformationBuilder1()
    {
        return new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(50)
                .oval(false)
                .build();
    }

    /**
     * Truncate string
     * @param  text  String
     * @param  max_length  Max length of result
     * @param  ellipsis ellipsis
     * @param  trim trim
     * @return string
     */
    public static String truncate_string(String text, int max_length, String ellipsis, boolean trim){
        if (max_length < 1) {
            max_length = 50;
        }

        if (trim) {
            text = text.trim();
        }

        if(ellipsis.isEmpty()){
            ellipsis = "...";
        }

        int string_length = text.length();
        int ellipsis_length = ellipsis.length();
        if(string_length > max_length){
            if (ellipsis_length >= max_length) {
                text = ellipsis.substring(0, max_length);
            } else {
                text = text.substring(0, max_length - ellipsis_length) + ellipsis;
            }
        }

        return text;
    }

    /**
     * Convert hexColor #12345678 of ColorPicker to real HexColor #123456
     * @param  hexColor String
     * @return string
     */
    public static String getRealColor(String hexColor){
        String prefix = "#";
        if (hexColor.length() == 6) {
            return prefix + hexColor;
        } else if (hexColor.length() > 6) {
            return prefix + hexColor.substring(hexColor.length() - 6);
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("word has fewer than 6 characters!");
        }
    }
}
