package com.example.prudentialfinance.Activities.Card;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.CardViewModel;

import java.util.Map;

public class CardUpdateActivity extends AppCompatActivity {

    private ImageButton buttonGoBack;
    private AppCompatButton buttonCreate;
    private ImageButton buttonMore;
    private MenuPopupHelper menuHelper;
    private MenuBuilder menuBuilder;


    private EditText cardNumber, cardBalance, cardBank, cardDescription;
    private CardViewModel viewModel;

    LoadingDialog loadingDialog;
    private Alert alert;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_update);

        /*this command belows prevent keyboard from popping up automatically*/
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /*get headers from global variable*/
        Map<String, String> headers = ((GlobalVariable) getApplication()).getHeaders();


        /*get account from recycle view*/
        Account account = getIntent().getParcelableExtra("account");


        setControl();
        setViewModel();
        setEvent(account, headers);

        viewModel.getAccountRemoval().observe(CardUpdateActivity.this, s -> {
            if( s.length() > 0)
            {
                alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertDefault), R.drawable.ic_close);
            }
        });

        viewModel.getAccountUpdateResource().observe(this, accountEdit -> {
            int result = accountEdit.getResult();
            if( result == 1)
            {
                alert.showAlert("Thành công", "Thao tác đã được thực hiện thành công", R.drawable.ic_check);
            }
            else
            {
                alert.showAlert(getString(R.string.alertTitle), accountEdit.getMsg(), R.drawable.ic_close);
            }
        });

        viewModel.getAnimation().observe(this, aBoolean -> {
            if( aBoolean )
            {
                loadingDialog.startLoadingDialog();
            }
            else
            {
                loadingDialog.dismissDialog();
            }
        });

        alert.btnOK.setOnClickListener(view->finish());
    }


    /**
     * @author Phong
     * listening event for every component.
     * */
    @SuppressLint("RestrictedApi")
    private void setControl() {
        buttonGoBack = findViewById(R.id.cardUpdateButtonGoBack);
        buttonCreate = findViewById(R.id.cardUpdateButtonCreate);
        buttonMore = findViewById(R.id.cardUpdateButtonMore);


        cardNumber = findViewById(R.id.cardUpdateCardNumber);
        cardBalance = findViewById(R.id.cardUpdateCardBalance);
        cardDescription = findViewById(R.id.cardUpdateCardDescription);
        cardBank = findViewById(R.id.cardUpdateCardBank);

        loadingDialog = new LoadingDialog(CardUpdateActivity.this);
        alert = new Alert(this, 1);

        menuBuilder = new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.card_menu, menuBuilder);

        menuHelper = new MenuPopupHelper(this, menuBuilder, buttonMore);
        menuHelper.setForceShowIcon(true);
    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(this).get(CardViewModel.class);
    }

    /**
     * @author Phong-Kaster
     *
     * @view is the current context of the fragment
     * @headers is used to attach to HTTP Request headers include Access-Token and Content-Type
     *
     * Step 1: declare viewModel which will be used in this fragment
     * Step 2: retrieve data from API
     * Step 3: observe data if some data changes on server then
     * the data in this fragment is also updated automatically
     * */
    @SuppressLint({"SetTextI18n", "RestrictedApi"})
    private void setEvent(Account account,  Map<String, String > headers) {
        cardNumber.setFocusable(false);
        if( account == null)
            return;
        /*Step 1*/
        cardBank.setText( account.getName());
        cardDescription.setText( account.getDescription());
        cardBalance.setText( String.valueOf( account.getBalance()) );
        cardNumber.setText( account.getAccountnumber() );


        /*Step 2*/
        buttonGoBack.setOnClickListener(view-> finish());


        /*Step 3*/
        buttonCreate.setOnClickListener(view->{
            int id = account.getId();
            String name = cardBank.getText().toString().trim();
            String balance =  cardBalance.getText().toString().trim();
            String description = cardDescription.getText().toString().trim();
            String number = cardNumber.getText().toString().trim();


            viewModel.updateAccount(headers,id, name, balance, description, number);

        });

        /*Step 4*/
        buttonMore.setOnClickListener(view -> {
            menuHelper.show();
        });


        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.deleteMenu:
                        int id = account.getId();
                        viewModel.deleteAccount(headers, id);
                        break;
                    case R.id.chartMenu:
                        Intent intent = new Intent(CardUpdateActivity.this, AccountChartActivity.class);
                        intent.putExtra("account", (Parcelable) account);
                        startActivity(intent);
                        break;
                }
                return false;
            }

            @Override
            public void onMenuModeChange(@NonNull MenuBuilder menu) {

            }
        });

        alert.btnOK.setOnClickListener(view->finish());
    }
}