package com.example.prudentialfinance.Activities.Transaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.TransactionViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionPreviewStatementActivity extends AppCompatActivity {

    private TextView accountOwner , accountName, today, email, dateStart,dateEnd;
    private TableLayout table;
    private String dateFrom, dateTo, keyword, quantity, column, nature;
    private User AuthUser;
    private Alert alert;

    private ImageButton buttonGoBack;

    private Map<String, String> headers = null;
    private TransactionViewModel transactionViewModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_preview_statement);

        headers = ((GlobalVariable)getApplication()).getHeaders();
        AuthUser = ((GlobalVariable)getApplication()).getAuthUser();

        /*GET passed strings from transaction statement activity*/
        Intent previousIntent = getIntent();
        dateFrom = previousIntent.getStringExtra("dateFrom");
        dateTo = previousIntent.getStringExtra("dateTo");
        keyword = previousIntent.getStringExtra("keyword");
        quantity = previousIntent.getStringExtra("quantity");
        column = previousIntent.getStringExtra("column");
        nature = previousIntent.getStringExtra("nature");

        /*Step 1*/
        setComponent();
        setViewModel(headers);

        /*Step 2*/
        setContent();
        buttonGoBack.setOnClickListener(view->finish());
    }

    private void setComponent() {
        accountOwner = findViewById(R.id.accountOwner);
        accountName = findViewById(R.id.accountName);

        dateStart = findViewById(R.id.dateFrom);
        dateEnd = findViewById(R.id.dateTo);

        today = findViewById(R.id.today);
        email = findViewById(R.id.accountEmail);

        table = findViewById(R.id.transactionTable);
        buttonGoBack = findViewById(R.id.transactionPreviewButtonGoBack);

        alert = new Alert(this, 1);

    }

    /**
     * @author Phong-Kaster
     * */
    @SuppressLint("SetTextI18n")
    private void setContent() {
        accountOwner.setText( AuthUser.getFirstname()+ " " + AuthUser.getLastname() );
        accountName.setText( AuthUser.getFirstname()+ " " + AuthUser.getLastname() );

        Date currentDay = new Date();
        SimpleDateFormat conventer = new SimpleDateFormat("dd-MM-yyyy");
        today.setText( conventer.format(currentDay) );

        dateStart.setText( dateFrom );
        dateEnd.setText( dateTo );

        email.setText( AuthUser.getEmail() );
    }


    /***
     * @author Phong-Kaster
     * query to server to get transactions which is filtered by body request below
     * */
    private void setViewModel(Map<String, String> headers)
    {
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        /*Step 2*/
        Map<String, String> body = new HashMap<>();
        body.put("fromdate", dateFrom);
        body.put("todate", dateTo);
        body.put("length", quantity);
        body.put("order[column]",column);
        body.put("order[dir]",nature);
        body.put("search",keyword);
        transactionViewModel.createStatement(headers, body);

        /*Step 3*/
        transactionViewModel.getTransactionCreationStatement().observe(this, data -> {
            int result = data.getResult();
            List<TransactionDetail> transactions = data.getData();
            if( result == 1)
            {
                setTableTransaction(transactions);
            }
            else
            {
                alert.showAlert(getString(R.string.alertTitle), getString(R.string.alertDefault), R.drawable.ic_close);
            }
        });
    }

    /***
     * @author Phong-Kaster
     * insert transactions which is retrieved from api into tablelayout
     * */
    @SuppressLint("SetTextI18n")
    private void setTableTransaction(List<TransactionDetail> transactions )
    {
        for( int i = 0; i< transactions.size(); i++)
        {
            TableRow row = new TableRow(this);
            String dateContent = transactions.get(i).getTransactiondate();
            String referenceContent = transactions.get(i).getReference();
            String nameContent = transactions.get(i).getName();
            String descriptionContent = transactions.get(i).getDescription();
            String creditContent = transactions.get(i).getAmount().toString();

            TextView numerialOrder = new TextView(this);
            numerialOrder.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            numerialOrder.setText( String.valueOf(i+1) + "\t\t\t" );
            numerialOrder.setTextColor(getResources().getColor(R.color.black));


            TextView date = new TextView(this);
            date.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            date.setText( dateContent + "\t\t\t" );
            date.setTextColor(getResources().getColor(R.color.black));


            TextView reference = new TextView(this);
            reference.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            reference.setText( referenceContent + "\t\t\t" );
            reference.setTextColor(getResources().getColor(R.color.black));

            TextView name = new TextView(this);
            name.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            name.setText( nameContent + "\t\t\t" );
            name.setTextColor(getResources().getColor(R.color.black));

            TextView description = new TextView(this);
            description.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            description.setText( descriptionContent + "\t\t\t" );
            description.setTextColor(getResources().getColor(R.color.black));

            TextView credit = new TextView(this);
            credit.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            credit.setText( creditContent + "\t\t\t" );
            credit.setTextColor(getResources().getColor(R.color.black));

            row.addView(numerialOrder);
            row.addView(date);
            row.addView(reference);
            row.addView(name);
            row.addView(description);
            row.addView(credit);
            table.addView(row);
        }
    }
}