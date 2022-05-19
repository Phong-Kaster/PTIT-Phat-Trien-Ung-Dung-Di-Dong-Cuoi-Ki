package com.example.prudentialfinance.Activities.General;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Categories.AddCategoryViewModel;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.Map;


public class AddCategoryActivity extends AppCompatActivity {

    ImageButton backBtn, cat_color;
    TextView category_name, category_desc, topTitle;
    AppCompatButton saveBtn;
    Category category;

    AddCategoryViewModel viewModel;
    GlobalVariable global;
    LoadingDialog loadingDialog;
    Alert alert;
    User authUser;
    Map<String, String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        Intent intent = getIntent();
        category = intent.getParcelableExtra("category");

        setComponent();

        setControl();
        setData();
        setEvent();
    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(AddCategoryActivity.this);
        alert = new Alert(AddCategoryActivity.this, 1);
        viewModel = new ViewModelProvider(this).get(AddCategoryViewModel.class);
        authUser = global.getAuthUser();
    }

    private void setData() {
        category_desc.setText(category.getDescription());
        category_name.setText(category.getName());
        cat_color.getBackground().setColorFilter(Color.parseColor(category.getColor()), PorterDuff.Mode.SRC);

        if(category.getId() == 0 ){
            String sub = "Thu nhập";
            if(category.getType() == 2){
                 sub = "Chi tiêu";
            }
            topTitle.setText(getString(R.string.add_category) + " " + sub);
        }else{
            topTitle.setText(getString(R.string.edit_category));
        }
    }

    private void setEvent() {
        backBtn.setOnClickListener(view -> finish());
        cat_color.setOnClickListener(view -> {
            new ColorPickerDialog.Builder(this)
                    .setTitle(getString(R.string.colorPicker))
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton(getString(R.string.confirm),
                            (ColorEnvelopeListener) (envelope, fromUser) -> {
                                category.setColor(Helper.getRealColor(envelope.getHexCode()));
                                cat_color.getBackground().setColorFilter(envelope.getColor(), PorterDuff.Mode.SRC);
                            })
                    .setNegativeButton(getString(R.string.cancel),
                            (dialogInterface, i) -> dialogInterface.dismiss())
                    .setBottomSpace(12)
                    .show();
        });

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                category.setId(object.getCategory());
                Intent intent = new Intent();
                intent.putExtra("category_entry", category);
                setResult(78, intent);
                finish();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        saveBtn.setOnClickListener(view -> {

            category.setDescription(category_desc.getText().toString());
            category.setName(category_name.getText().toString());

            if(category.getId() == 0){
                viewModel.saveData(headers, category);
            }else{
                viewModel.updateData(headers, category);
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

        category_desc = findViewById(R.id.category_desc);
        cat_color = findViewById(R.id.cat_color);
        category_name = findViewById(R.id.category_name);


        topTitle = findViewById(R.id.topTitle);

    }
}