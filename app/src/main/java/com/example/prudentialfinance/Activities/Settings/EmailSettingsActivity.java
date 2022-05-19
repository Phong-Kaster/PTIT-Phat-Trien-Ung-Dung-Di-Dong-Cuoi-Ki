package com.example.prudentialfinance.Activities.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.EmailSettings;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Settings.EmailSettingsViewModel;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmailSettingsActivity extends AppCompatActivity {
    ImageButton backBtn;
    AppCompatButton saveBtn;

    GlobalVariable global;
    EmailSettingsViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;

    Map<String, String> headers;

    LinearLayout authSMTP;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch swAuth;

    EditText username_email, password_email, txtHost, txtFrom, txtPort;
    Spinner spnEncryption;
    ArrayAdapter<String> adapter;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_settings);

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
        loadingDialog = new LoadingDialog(EmailSettingsActivity.this);
        alert = new Alert(EmailSettingsActivity.this, 1);
        viewModel = new ViewModelProvider(this).get(EmailSettingsViewModel.class);
    }

    private void setDataToControl(EmailSettings data) {
        swAuth.setChecked(data.getAuth());

        username_email.setText(data.getUsername());
        password_email.setText(data.getPassword());

        txtHost.setText(data.getHost());
        txtFrom.setText(data.getFrom());
        txtPort.setText(data.getPort());

        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            if (item.equals(data.getEncryption().toUpperCase())) {
                spnEncryption.setSelection(i);
                break;
            }
        }

    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> finish());

        saveBtn.setOnClickListener(view -> updateData());

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        swAuth.setOnCheckedChangeListener(
                (compoundButton, b) -> authSMTP.setVisibility(b ? View.VISIBLE : View.INVISIBLE));

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
                    FancyToast.makeText(EmailSettingsActivity.this, object.getMsg(), FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS, R.drawable.ic_check, true).show();
                }
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });
    }

    private void updateData() {

        String host = txtHost.getText().toString().trim();
        String port = txtPort.getText().toString().trim();
        String encryption = spnEncryption.getSelectedItem().toString().toLowerCase();
        Boolean auth = swAuth.isChecked();
        String username = username_email.getText().toString().trim();
        String password = password_email.getText().toString().trim();
        String from = txtFrom.getText().toString().trim();
        String action = "save";

        viewModel.updateData(headers, action, host, port, encryption, auth, username, password, from);
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);

        swAuth = findViewById(R.id.swAuth);
        authSMTP = findViewById(R.id.authSMTP);

        username_email = findViewById(R.id.username_email);
        password_email = findViewById(R.id.password);

        txtHost = findViewById(R.id.txtHost);
        txtPort = findViewById(R.id.txtPort);
        txtFrom = findViewById(R.id.txtFrom);

        spnEncryption = findViewById(R.id.spnEncryption);

        list.add("None");
        list.add("TLS");
        list.add("SSL");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spnEncryption.setAdapter(adapter);

    }
}