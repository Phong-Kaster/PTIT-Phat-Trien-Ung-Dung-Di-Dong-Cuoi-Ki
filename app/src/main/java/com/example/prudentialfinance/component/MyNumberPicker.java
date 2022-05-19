package com.example.prudentialfinance.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)//For backward-compability
public class MyNumberPicker extends NumberPicker {

    public MyNumberPicker(Context context) {
        super(context);
    }

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributeSet(attrs);
    }

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttributeSet(attrs);
    }
    private void processAttributeSet(AttributeSet attrs) {
        //This method reads the parameters given in the xml file and sets the properties according to it
        this.setValue(10);
        String type = attrs.getAttributeValue(null, "type");
        switch(type)
        {
            case "month":
                this.setMaxValue(12);
                this.setMinValue(1);
                this.setValue(5);
                break;
            case "year":
                this.setMaxValue(2100);
                this.setMinValue(2000);
                this.setValue(2022);
                break;
            default:
                this.setMaxValue(100);
                this.setMinValue(0);
                this.setValue(0);
        }
    }
}