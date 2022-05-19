package com.example.prudentialfinance.Activities.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LanguageManager;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.MainActivity;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.AppearanceViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DarkModeActivity extends AppCompatActivity {

    SwitchCompat switchCompat;
    ImageButton backBtn;
    Spinner spnLanguage;

    SpinnerAdapter adapter;
    HashMap<String, String> listLanguage = new HashMap<>();
    String[] spinnerOptions;

    GlobalVariable global;
    Alert alert;
    Alert alertConfirm;
    LanguageManager languageManager;
    LoadingDialog loadingDialog;
    Map<String, String> headers;
    AppearanceViewModel viewModel;
    String selectedLang;
    String shortLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode);

        setComponent();

        setControl();

        setEvent();

    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(this);
        languageManager = new LanguageManager(this, global.getAppName());
        alertConfirm = new Alert(this, 2);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(AppearanceViewModel.class);

        listLanguage = languageManager.getList();

    }

    private void setEvent() {
        /**
         * DO NOT REMOVE THIS BLOCK OF CODE BELOW
         * IT REPRESENTS FOR TESTING DARK-MODE FOR EACH SCREEN
         * */
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_PrudentialFinance_Dark);
            switchCompat.setChecked(true);
        } else {
            setTheme(R.style.Theme_PrudentialFinance);
            switchCompat.setChecked(false);
        }

        backBtn.setOnClickListener(view -> finish());

        switchCompat.setOnClickListener(view->{
            if( AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES )
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLang = spinnerOptions[i];
                shortLang = listLanguage.get(selectedLang);

                if(!languageManager.getCurrent().equals(shortLang)){
                    alertConfirm.showAlert(getString(R.string.alertTitle), getString(R.string.warning_change_language), R.drawable.ic_info);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                restart();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        alertConfirm.btnCancel.setOnClickListener(view -> alertConfirm.dismiss());
        alertConfirm.btnOK.setOnClickListener(view -> {
            languageManager.setLang(shortLang);
            languageManager.updateResource();
            viewModel.updateLanguage(headers, languageManager.getCurrent());
            alertConfirm.dismiss();
        });

        viewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        switchCompat = findViewById(R.id.darkModeSwitch);
        spnLanguage = findViewById(R.id.spnLanguage);


        spinnerOptions = listLanguage.keySet().toArray(new String[0]);
        adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item , spinnerOptions);
        spnLanguage.setAdapter(adapter);



        int index = 0;
        for(Map.Entry<String, String> entry : listLanguage.entrySet()) {
            String value = entry.getValue();

            if (value.equals(languageManager.getCurrent())) {
                spnLanguage.setSelection(index);
                break;
            }
            index++;
        }
    }

    public void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
    }
}