package com.example.prudentialfinance.Activities.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LanguageManager;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Settings.SiteSettingsViewModel;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteSettingsActivity extends AppCompatActivity {

    ImageButton backBtn;

    AppCompatButton saveBtn;
    EditText siteName, siteSlogan, siteKeyword, siteDescription, logoMark, logoType, currencyField;
    Spinner spnLanguage;

    GlobalVariable global;
    SiteSettingsViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;
    Map<String, String> headers;

    SpinnerAdapter adapter;
    HashMap<String, String> listLanguage = new HashMap<>();
    String[] spinnerOptions;
    LanguageManager languageManager;
    String shortLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_settings);

        setComponent();

        setControl();

        setEvent();

        loadData();
    }

    private void loadData() {
        viewModel.getData(headers);
    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable) getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(SiteSettingsActivity.this);
        alert = new Alert(SiteSettingsActivity.this, 1);
        viewModel = new ViewModelProvider(this).get(SiteSettingsViewModel.class);
        languageManager = new LanguageManager(this, global.getAppName());
        listLanguage = languageManager.getList();

    }

    private void setDataToControl(SiteSettings data) {
        siteName.setText(data.getSite_name());
        siteSlogan.setText(data.getSite_slogan());
        siteKeyword.setText(data.getSite_keywords());
        siteDescription.setText(data.getSite_description());

        logoMark.setText(data.getLogomark());
        logoType.setText(data.getLogotype());

        currencyField.setText(data.getCurrency());

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

    private void setEvent() {
        backBtn.setOnClickListener(view -> finish());

        saveBtn.setOnClickListener(view -> {
            String site_name = siteName.getText().toString().trim();
            String site_slogan = siteSlogan.getText().toString().trim();
            String site_description = siteDescription.getText().toString().trim();
            String site_keyword = siteKeyword.getText().toString().trim();

            String logo_mark = logoMark.getText().toString().trim();
            String logo_type = logoType.getText().toString().trim();

            String currency = currencyField.getText().toString().trim();
            String language = shortLang.trim();
            String action = "save";

            viewModel.updateData(headers, action, site_name, site_slogan, site_description, site_keyword, logo_type,
                    logo_mark, language, currency);
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                loadingDialog.startLoadingDialog();
            } else {
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(this, object -> {
            if (object == null) {
                alert.showAlert(getResources().getString(R.string.alertTitle),
                        getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                setDataToControl(object.getData());
                if (object.getMethod().equals("POST")) {
                    FancyToast.makeText(this, object.getMsg(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                            R.drawable.ic_check, true).show();
                }
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String key = spinnerOptions[i];
                shortLang = listLanguage.get(key);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);

        siteName = findViewById(R.id.siteName);
        siteSlogan = findViewById(R.id.siteSlogan);
        siteKeyword = findViewById(R.id.siteKeyword);
        siteDescription = findViewById(R.id.siteDescription);

        logoMark = findViewById(R.id.logoMark);
        logoType = findViewById(R.id.logoType);

        currencyField = findViewById(R.id.currency);
        spnLanguage = findViewById(R.id.spnLanguage);

        spinnerOptions = listLanguage.keySet().toArray(new String[0]);
        adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item , spinnerOptions);
        spnLanguage.setAdapter(adapter);

    }
}