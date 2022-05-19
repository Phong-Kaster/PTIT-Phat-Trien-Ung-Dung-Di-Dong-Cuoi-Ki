package com.example.prudentialfinance.Helpers;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prudentialfinance.R;

public class Alert {
    private View viewAlert;
    private AlertDialog alert;
    private Context context;
    private TextView msgText, alertTitle;
    private ImageView iconAlert;
    public Button btnOK;
    public Button btnCancel;

    public Alert(Context context) {
        this.context = context;
    }

    public Alert(Context context, int type) {
        this.context = context;
        if(type == 1){
            this.normal();
        }else{
            this.confirm();
        }
    }

    public void normal(){
        viewAlert = View.inflate(context, R.layout.normal_alert, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewAlert);

        alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.setCancelable(false);

        setControl();
    }

    public void confirm(){
        viewAlert = View.inflate(context, R.layout.confirm_alert, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewAlert);

        alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.setCancelable(false);

        setControl();
    }

    private void setControl(){
        msgText = viewAlert.findViewById(R.id.msgText);
        alertTitle = viewAlert.findViewById(R.id.alertTitle);
        iconAlert = viewAlert.findViewById(R.id.iconAlert);
        btnOK = viewAlert.findViewById(R.id.btnOK);
        btnCancel = viewAlert.findViewById(R.id.btnCancel);
    }

    @SuppressLint("NonConstantResourceId")
    public void showAlert(String title, String msg, Integer ico){
        switch (ico){
            case R.drawable.ic_close:
                iconAlert.setBackgroundResource(R.drawable.bg_alert_danger);
                break;
            case R.drawable.ic_info:
                iconAlert.setBackgroundResource(R.drawable.bg_alert_info);
                break;
            case R.drawable.ic_check:
                iconAlert.setBackgroundResource(R.drawable.bg_alert_success);
                break;
        }
        iconAlert.setImageResource(ico);
        msgText.setText(msg);
        alertTitle.setText(title);
        alert.show();
    }

    @SuppressLint("NonConstantResourceId")
    public void showAlert(Integer resid, String msg, Integer ico){
        String title = context.getResources().getString(resid);
        switch (ico){
            case R.drawable.ic_close:
                iconAlert.setBackgroundResource(R.drawable.bg_alert_danger);
                break;
            case R.drawable.ic_info:
                iconAlert.setBackgroundResource(R.drawable.bg_alert_info);
                break;
            case R.drawable.ic_check:
                iconAlert.setBackgroundResource(R.drawable.bg_alert_success);
                break;
        }
        iconAlert.setImageResource(ico);
        msgText.setText(msg);
        alertTitle.setText(title);
        alert.show();
    }

    public void dismiss(){
        alert.dismiss();
    }
}