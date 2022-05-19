package com.example.prudentialfinance.Activities.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Users.AddUserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    ImageButton backBtn;
    EditText txtFirstname, txtLastname, txtEmail;
    TextView topTitle;
    AppCompatButton saveBtn;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch swActive;
    Spinner spnAccountType;
    ArrayAdapter<String> adapter;
    List<String> list = new ArrayList<>();

    User user;

    AddUserViewModel viewModel;
    GlobalVariable global;
    LoadingDialog loadingDialog;
    Alert alert;
    User authUser;
    Map<String, String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        setComponent();

        setControl();
        setData();
        setEvent();
    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(AddUserActivity.this);
        alert = new Alert(AddUserActivity.this, 1);
        viewModel = new ViewModelProvider(this).get(AddUserViewModel.class);
        authUser = global.getAuthUser();
    }

    private void setData() {
        txtEmail.setText(user.getEmail());
        txtFirstname.setText(user.getFirstname());
        txtLastname.setText(user.getLastname());
        swActive.setChecked(user.getIs_active());

        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            if(item.toLowerCase().equals(user.getAccount_type().toLowerCase())){
                spnAccountType.setSelection(i);
                break;
            }
        }

        if(user.getId() == 0 ){
            topTitle.setText(getString(R.string.add_user));
        }else{
            topTitle.setText(getString(R.string.edit_user));
        }
    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> finish());

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                user.setId(object.getUser());
                Intent intent = new Intent();
                intent.putExtra("user_entry", user);
                setResult(78, intent);
                finish();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        saveBtn.setOnClickListener(view -> {

            user.setFirstname(txtFirstname.getText().toString().trim());
            user.setEmail(txtEmail.getText().toString().trim());
            user.setLastname(txtLastname.getText().toString().trim());
            user.setAccount_type(spnAccountType.getSelectedItem().toString().toLowerCase().trim());
            user.setIs_active(swActive.isChecked());

            if(user.getId() == 0){
                viewModel.saveData(headers, user);
            }else{
                viewModel.updateData(headers, user);
            }
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
        saveBtn = findViewById(R.id.saveBtn);

        topTitle = findViewById(R.id.topTitle);

        txtFirstname = findViewById(R.id.txtFirstname);
        txtLastname = findViewById(R.id.txtLastname);
        txtEmail = findViewById(R.id.txtEmail);


        swActive = findViewById(R.id.swActive);
        spnAccountType = findViewById(R.id.spnAccountType);

        list.add("Admin");
        list.add("Member");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spnAccountType.setAdapter(adapter);



    }
}