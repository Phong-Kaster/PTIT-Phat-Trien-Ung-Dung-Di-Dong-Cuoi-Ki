package com.example.prudentialfinance.Activities.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Settings.PasswordViewModel;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Map;

public class PasswordActivity extends AppCompatActivity {

    ImageButton backBtn;
    EditText password, oldPassword, confirmPassword;
    AppCompatButton saveBtn;

    GlobalVariable global;
    PasswordViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;

    Map<String, String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        setComponent();

        setControl();
        setEvent();

    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable) getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(PasswordActivity.this);
        alert = new Alert(PasswordActivity.this, 1);
        viewModel = new ViewModelProvider(this).get(PasswordViewModel.class);
    }

    private void setAuthorizedToken(String accessToken) {
        String token = "JWT " + accessToken.trim();
        GlobalVariable state = ((GlobalVariable) this.getApplication());

        state.setAccessToken(token);
        SharedPreferences preferences = this.getApplication().getSharedPreferences(state.getAppName(), MODE_PRIVATE);
        preferences.edit().putString("accessToken", accessToken.trim()).apply();
    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> finish());

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        saveBtn.setOnClickListener(view -> updateData());

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
                setAuthorizedToken(object.getAccessToken());
                global.setAuthUser(object.getData());
                FancyToast.makeText(this, object.getMsg(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                        R.drawable.ic_check, true).show();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });
    }

    private void setControl() {
        backBtn = findViewById(R.id.backBtn);
        password = findViewById(R.id.password);
        oldPassword = findViewById(R.id.oldPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        saveBtn = findViewById(R.id.saveBtn);
    }

    private void updateData() {

        String newPass = password.getText().toString().trim();
        String oldPass = oldPassword.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        viewModel.updateData(headers, newPass, confirmPass, oldPass);
    }
}