package com.example.prudentialfinance.Activities.Transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.prudentialfinance.Adapter.AccountAdapter;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Accounts.AccountViewModel;
import com.example.prudentialfinance.ViewModel.CategoryViewModel;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;
import com.example.prudentialfinance.ViewModel.TransactionViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class TransactionCreationActivity extends AppCompatActivity{


    private Spinner accountSpinner;
    private AppCompatSpinner categorySpinner;
    private AppCompatButton buttonSave;
    private ImageButton buttonGoBack;

    private AccountViewModel viewModel;
    private CategoryViewModel categoryViewModel;
    private TransactionViewModel transactionViewModel;

    private LoadingDialog loadingDialog;

    private LiveData<ArrayList<Account>> accounts;
    private LiveData<ArrayList<Category>> categories;

    private EditText transactionName, transactionAmount, transactionReference,
             transactionDescription, transactionDate;


    private final Calendar myCalendar= Calendar.getInstance();
    private static Map<String, String > headers = null;


    private String categoryId;
    private String accountId;
    private String name;
    private String amount;
    private String reference;
    private String date;
    private String type;
    private String description;

    private Alert alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_creation);

        /*this command belows prevent keyboard from popping up automatically*/
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        type = getIntent().getStringExtra("type").length() > 0 ? getIntent().getStringExtra("type").trim() : "1";

        /*Step 1*/
        headers = ((GlobalVariable)getApplication()).getHeaders();


        /*Step 2*/
        setControl();
        setViewModel(headers);


        /*Step 3*/
        accounts = viewModel.getAccounts();
        categories = categoryViewModel.getCategories();
        LiveData<Integer> transactionCreation = transactionViewModel.getTransactionCreation();
        setEvent();


        /*Step 4*/
        transactionCreation.observe(this, integer -> {
            if( integer > 0)
            {
                alert.showAlert("Thành công", "Thao tác đã được thực hiện thành công", R.drawable.ic_check);
            }
            else
            {
                alert.showAlert("Thất bại", transactionViewModel.getTransactionMessage().getValue(), R.drawable.ic_close);
            }
        });

        transactionViewModel.getAnimation().observe(this, aBoolean -> {
            if( aBoolean )
            {
                loadingDialog.startLoadingDialog();
            }
            else
            {
                loadingDialog.dismissDialog();
            }
        });
    }


    private void setControl() {

        loadingDialog = new LoadingDialog(TransactionCreationActivity.this);

        accountSpinner = findViewById(R.id.transactionCreationAccountSpinner);
        categorySpinner = findViewById(R.id.transactionCreationCategorySpinner);

        transactionName = findViewById(R.id.transactionCreationName);
        transactionAmount = findViewById(R.id.transactionCreationAmount);


        transactionReference = findViewById(R.id.transactionCreationReference);
        transactionDate = findViewById(R.id.transactionCreationDate);
        transactionDescription = findViewById(R.id.transactionCreationDescription);

        buttonSave = findViewById(R.id.transactionCreationButtonSave);
        buttonGoBack = findViewById(R.id.transactionCreationButtonGoBack);

        alert = new Alert(TransactionCreationActivity.this, 1);
    }

    private void setViewModel(Map<String, String> headers) {
        /*Step 1*/
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        viewModel.initialize(headers);

        /*Step 2*/
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.instanciate(headers, type);

        /*Step 3*/
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        /*Step 4*/
        HomeFragmentViewModel homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        homeFragmentViewModel.instanciate(headers);
    }

    /**
     * @author Phong-Kaster
     * set event for each component
     * */
    private void setEvent()
    {
        /*pour ATM into*/
        accounts.observe(this, this::initializeAccountSpinner);

        /*listen user click on date picker*/
        initializeDatePicker();

        /*pour category into*/
        categories.observe(this, this::initializeCategorySpinner);


        /*listen button save*/
        buttonSave.setOnClickListener(view->{
            /*Step 1*/
            /*categoryId & account Id is save from 2 functions: initializeAccountSpinner & initializeCategorySpinner*/
            name = transactionName.getText().toString();
            amount = transactionAmount.getText().toString();

            reference = transactionReference.getText().toString();
            String input = transactionDate.getText().toString();
            date = Helper.convertStringToValidDate( input);

            description = transactionDescription.getText().toString();

            transactionViewModel.createTransaction(headers,
                    categoryId,
                    accountId,
                    name,
                    amount,
                    reference,
                    date,
                    type,
                    description);
        });

        /*listen button goback*/
        buttonGoBack.setOnClickListener(view-> finish());

        alert.btnOK.setOnClickListener(view->finish());
    }

    /**
     * @author Phong-Kaster
     * call API and fetch into Account Spinner
     * */
    private void initializeAccountSpinner(ArrayList<Account> accounts)
    {
        SpinnerAdapter adapter = new AccountAdapter(this, accounts);
        accountSpinner.setAdapter(adapter);
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*get account id which is selected from account spinner*/
                accountId = String.valueOf(accounts.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * @author Phong-Kaster
     * this function has a problem is using customized array adapter. Then, open the spinner. Item can not be selected
     *
     * Solution: get all category's name into an array list. Then, we use default contructor to create this spinner
     * */
    private void initializeCategorySpinner(ArrayList<Category> categories)
    {
        ArrayList<String> categoriesName = new ArrayList<>();

        /* get all category's name into an arrayList*/
        for(Category e: categories)
        {
            categoriesName.add(e.getName());
        }

        SpinnerAdapter categoryAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categoriesName);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                /*get category id which is selected from category spinner*/
                categoryId = categories.get(i).getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * @author Phong-Kaster
     * initialize DatePicker
     * */
    private void initializeDatePicker()
    {
        DatePickerDialog.OnDateSetListener datePicker = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);


            /*set text to date*/
            String format = "dd/MM/yyyy";
            SimpleDateFormat dateFormat=new SimpleDateFormat(format, Locale.CHINESE);
            transactionDate.setText(dateFormat.format(myCalendar.getTime()));
        };

        transactionDate.setOnClickListener(view-> new DatePickerDialog(TransactionCreationActivity.this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show());
    }
}