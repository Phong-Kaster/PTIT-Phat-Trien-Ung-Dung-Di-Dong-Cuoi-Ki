package com.example.prudentialfinance.Activities.Budget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.prudentialfinance.Container.budgets.budgetGET.Datum;
import com.example.prudentialfinance.Container.budgets.budgetGET.DatumAdd;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.CategoryViewModel;
import com.example.prudentialfinance.ViewModel.budget.BudgetAddViewModel;
import com.example.prudentialfinance.component.MyNumberPicker;

import java.util.ArrayList;
import java.util.Map;

public class AddBudgetActivity extends AppCompatActivity {

    private TextView topTitle, currencylable;
    private AppCompatButton btnAdd;
    private ImageButton btnBack;
    private Datum datum;
    private Spinner spinner;
    private MyNumberPicker monthPicker, yearPicker;
    private EditText budgetDescription, budgetAmount;

    private BudgetAddViewModel budgetAddViewModel;
    private Map<String,String> headers;
    private LoadingDialog loadingDialog;
    private Alert alert;
    private LiveData<ArrayList<Category>> categories;
    private CategoryViewModel categoryViewModel;

    private String catID;
    private String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_add);

        Intent intent = getIntent();
        try {
            datum = (Datum) intent.getSerializableExtra("budget");
        }
        catch (Exception ex)
        {
            datum = new Datum(0);
        }

        setControl();
        setComponent();
        categories = categoryViewModel.getCategories();
        setData();
        setEvent();
    }

    private void setControl()
    {
        topTitle = findViewById(R.id.budget_title);
        spinner = findViewById(R.id.budget_categories);
        monthPicker = findViewById(R.id.budget_month_picker);
        yearPicker = findViewById(R.id.budget_year_picker);
        budgetDescription= findViewById(R.id.budget_description);
        budgetAmount = findViewById(R.id.budget_budget);
        btnAdd = findViewById(R.id.btn_budget);
        currencylable = findViewById(R.id.currency_lable);
        btnBack = findViewById(R.id.budget_back);
    }

    private void setComponent() {
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(AddBudgetActivity.this);
        alert = new Alert(this, 1);
        currency = ((GlobalVariable)getApplication()).getAppInfo().getCurrency();
        budgetAddViewModel = new ViewModelProvider(this).get(BudgetAddViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.instanciate(headers, "1");
        currencylable.setText(currency);
    }
    private void initializeCategorySpinner(ArrayList<Category> categories)
    {
        ArrayList<String> categoriesName = new ArrayList<>();
        for(Category e: categories)
        {
            categoriesName.add(e.getName());
            Log.i("Categories to name", e.getId() + e.getName());
        }

        SpinnerAdapter categoryAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categoriesName);
        spinner.setAdapter(categoryAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catID = categories.get(i).getId().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void setData(){
        String name;
        if(datum.getId()==0)
        {
            name = getResources().getString(R.string.budget_add_title);

        }else{
            name = getResources().getString(R.string.budget_edit_title);
            monthPicker.setValue(Integer.parseInt(Helper.getMonth(datum.getTodate())));
            yearPicker.setValue(Integer.parseInt(Helper.getYear(datum.getTodate())));
            budgetDescription.setText(datum.getDescription());
            budgetAmount.setText(String.valueOf(datum.getAmount()));
        }
        topTitle.setText(name);
        btnAdd.setText(name);
    }
    private void setEvent()
    {
        btnBack.setOnClickListener(view -> finish());
        alert.btnOK.setOnClickListener(view -> alert.dismiss());
        btnAdd.setOnClickListener(view -> {
            String day = Helper.getDay(datum.getTodate() == null || datum.getTodate().isEmpty()  ?"2022-05-31" : datum.getTodate());
            String month = String.valueOf(monthPicker.getValue());
            String year = String.valueOf(yearPicker.getValue());
            String error = "";

            if(budgetDescription.getText().toString().isEmpty()) error = getResources().getString(R.string.description_title);
            if(budgetAmount.getText().toString().isEmpty()) error = getResources().getString(R.string.budget);
            if(!error.isEmpty()) {
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.budget_error) + " " + error, R.drawable.ic_close);
                return;
            }
            String amount =budgetAmount.getText().toString();
            String description = budgetDescription.getText().toString();
            String date  = Helper.createDate(day, month, year);
            DatumAdd datumAdd = new DatumAdd(datum.getId(), Integer.parseInt(catID), 0, amount, date, date, description);
            try {
                if(datumAdd.getId()==0)
                {
                    budgetAddViewModel.saveData(headers,datumAdd);
                }else{
                    budgetAddViewModel.updateData(headers,datumAdd);
                }
            }catch(Exception ex)
            {
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
            }
        });

        budgetAddViewModel.isLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });
        budgetAddViewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }
            if (object.getResult() == 1) {
                datum.setId(object.getBudget());
                Intent intent = new Intent();
                intent.putExtra("budget_entry", datum);
                setResult(78, intent);
                finish();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }

        });
        categories.observe(this, this::initializeCategorySpinner);
    }
}
