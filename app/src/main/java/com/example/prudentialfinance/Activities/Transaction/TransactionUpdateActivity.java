package com.example.prudentialfinance.Activities.Transaction;

import android.annotation.SuppressLint;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.prudentialfinance.Adapter.AccountAdapter;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;
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

public class TransactionUpdateActivity extends AppCompatActivity {

    private TransactionDetail transaction;
    private Account atm;
    private LoadingDialog loadingDialog;


    private EditText transactionAmount, transactionName, transactionReference, transactionDescription, transactionDate;
    private Spinner accountSpinner, categorySpinner;
    private ImageButton buttonGoBack;
    private AppCompatButton buttonSave;

    private MutableLiveData<ArrayList<Account>> accounts = new MutableLiveData<>();
    private LiveData<ArrayList<Category>> categories = new MutableLiveData<>();

    private final Calendar myCalendar= Calendar.getInstance();
    private static Map<String, String > headers;

    private AccountViewModel accountViewModel;
    private CategoryViewModel categoryViewModel;
    private TransactionViewModel transactionViewModel;

    private int id;
    private String categoryId;
    private String categoryName;
    private String accountId;
    private String accountName;
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
        setContentView(R.layout.activity_transaction_update);

        /*this command belows prevent keyboard from popping up automatically*/
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /*Step 1*/
        headers = ((GlobalVariable)getApplication()).getHeaders();

        /*Get bundle from transaction recycle view */
        Bundle bundle = getIntent().getExtras();
        if( bundle == null)
        {
            alert.showAlert("Thất bại", "Đã xảy ra sự cố", R.drawable.ic_close);
            return;
        }

        /*receive passed data from transaction Information Activity*/
        transaction = (TransactionDetail) bundle.get("transaction");
        atm = (Account) bundle.get("atm");


        setControl();
        setViewModel(headers);

        accounts = accountViewModel.getAccounts();
        categories = categoryViewModel.getCategories();
        LiveData<Integer> transactionUpdate = transactionViewModel.getTransactionUpdate();

        setContent();
        setEvent();

        /*Step 4*/
        transactionUpdate.observe(this, integer -> {
            if( integer > 0)
            {
                alert.showAlert("Thành công", "Thao tác đã được thực hiện thành công", R.drawable.ic_check);
                refreshPreviousActivity();
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
        transactionAmount = findViewById(R.id.transactionUpdateAmount);
        transactionName = findViewById(R.id.transactionUpdateName);

        transactionReference = findViewById(R.id.transactionUpdateReference);
        transactionDescription = findViewById(R.id.transactionUpdateDescription);

        transactionDate = findViewById(R.id.transactionUpdateDate);
        accountSpinner = findViewById(R.id.transactionUpdateAccountSpinner);
        categorySpinner = findViewById(R.id.transactionUpdateCategorySpinner);

        buttonGoBack = findViewById(R.id.transactionUpdateButtonGoBack);
        buttonSave = findViewById(R.id.transactionUpdateButtonSave);
        loadingDialog = new LoadingDialog(TransactionUpdateActivity.this);

        alert = new Alert(this, 1);
    }

    private void setViewModel(Map<String, String> headers) {
        /*Step 1*/
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.initialize(headers);

        /*Step 2*/
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.instanciate(headers, transaction.getType().toString());

        /*Step 3*/
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        /*Step 4*/
        HomeFragmentViewModel homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        homeFragmentViewModel.instanciate(headers);
    }




    @SuppressLint("SetTextI18n")
    private void setContent() {
        transactionAmount.setText( transaction.getAmount().toString() );
        transactionName.setText( transaction.getName() );

        transactionReference.setText( transaction.getReference());
        transactionDescription.setText( transaction.getDescription() );

        String input = transaction.getTransactiondate();
        String output = Helper.revertStringToReadableDate( input );
        transactionDate.setText(output);
    }

    private void setEvent() {
        accounts.observe(this, this::initializeAccountSpinner);

        /*listen user click on date picker*/
        initializeDatePicker();

        /*pour category into*/
        categories.observe(this, this::initializeCategorySpinner);

        /* go back previous activity*/
        buttonGoBack.setOnClickListener(view->finish());

        /**/
        buttonSave.setOnClickListener(view->{
            /*Step 1*/
            /*categoryId & account Id is save from 2 functions: initializeAccountSpinner & initializeCategorySpinner*/
            id = transaction.getId();
            name = transactionName.getText().toString();
            amount = transactionAmount.getText().toString();

            reference = transactionReference.getText().toString();
            String input = transactionDate.getText().toString();
            date = Helper.convertStringToValidDate( input);

            type = transaction.getType().toString();
            description = transactionDescription.getText().toString();


            transactionViewModel.updateTransaction(headers,
                    id,
                    categoryId,
                    accountId,
                    name,
                    amount,
                    reference,
                    date,
                    type,
                    description);


        });

        alert.btnOK.setOnClickListener(view->finish());
    }

    /**
     * @author Phong-Kaster
     * call API and fetch into Account Spinner
     * */
    private void initializeAccountSpinner(ArrayList<Account> accounts)
    {
        /*Find the position card of the transaction*/
        int position = 0;
        for(Account e: accounts)
        {
            int id = e.getId();
            if( id == atm.getId() )
            {
                break;
            }
            position++;
        }

        SpinnerAdapter adapter = new AccountAdapter(this, accounts);
        accountSpinner.setAdapter(adapter);
        accountSpinner.setSelection(position);/*set selected item of the spinner*/
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*get account id which is selected from account spinner*/
                accountId = String.valueOf(accounts.get(i).getId());
                accountName = accounts.get(i).getName();
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

        /* get all category's name into an arrayList & find the category of the transaction*/
        int position = 0;
        for( int i = 0; i< categories.size();i++)
        {
            String value = categories.get(i).getName();
            categoriesName.add( value );
            if( value.equals(transaction.getCategory().getName()))
            {
                position = i;
            }
        }

        SpinnerAdapter categoryAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categoriesName);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(position);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                /*get category id which is selected from category spinner*/
                categoryId = categories.get(i).getId().toString();
                categoryName = categories.get(i).getName();
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
            transactionDate.setText(dateFormat.format( myCalendar.getTime() ));
        };

        transactionDate.setOnClickListener(view-> new DatePickerDialog(TransactionUpdateActivity.this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show());
    }

    /**
     * @author Phong-Kaster
     * */
    private void refreshPreviousActivity()
    {
        id = transaction.getId();
        name = transactionName.getText().toString();
        amount = transactionAmount.getText().toString();

        reference = transactionReference.getText().toString();
        String input = transactionDate.getText().toString();
        date = Helper.revertStringToReadableDate( input);

        type = transaction.getType().toString();
        description = transactionDescription.getText().toString();

        TransactionInformationActivity
                .getmInstanceActivity()
                .setContentWithValue(name, date, amount, accountName, categoryName, reference, description);
    }
}