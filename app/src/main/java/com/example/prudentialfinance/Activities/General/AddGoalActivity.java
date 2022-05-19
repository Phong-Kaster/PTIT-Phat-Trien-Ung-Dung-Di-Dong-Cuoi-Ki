package com.example.prudentialfinance.Activities.General;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prudentialfinance.Activities.Transaction.TransactionCreationActivity;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Helpers.NumberTextWatcher;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.Goal;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.Categories.AddCategoryViewModel;
import com.example.prudentialfinance.ViewModel.Goal.GoalAddViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class AddGoalActivity extends AppCompatActivity {

    private EditText goal_name,goal_amount,goal_balance,goal_deadline;
    private TextView topTitle,tv_currency_amount,tv_currency_balance;
    private AppCompatButton btn_add;
    private ImageButton btn_back;
    private Goal goal;
    private final Calendar myCalendar= Calendar.getInstance();



    private GoalAddViewModel viewModel;
    private GlobalVariable global;
    private Map<String,String> headers;
    private LoadingDialog loadingDialog;
    private Alert alert;
    private User authUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        Intent intent = getIntent();
        goal = (Goal) intent.getSerializableExtra("goal");
        setControl();
        initializeDatePicker();
        setComponent();
        setCurrency();
        setData();
        setEvent();
    }

    private void setControl()
    {
        topTitle = findViewById(R.id.goal_topTitle);
        goal_name = findViewById(R.id.goal_name_add);
        goal_amount = findViewById(R.id.goal_amount_add);
        goal_balance = findViewById(R.id.goal_balance_add);
        goal_deadline = findViewById(R.id.goal_date_add);
        btn_add = findViewById(R.id.Btn_Add_Goal);
        btn_back = findViewById(R.id.backBtnAddGoal);
        tv_currency_balance = findViewById(R.id.tv_currency_balance);
        tv_currency_amount = findViewById(R.id.tv_currency_amount);
    }

    private void setCurrency()
    {
        tv_currency_balance.setText(global.getAppInfo().getCurrency());
        tv_currency_amount.setText(global.getAppInfo().getCurrency());
    }

    private void setComponent() {
        global = (GlobalVariable) getApplication();
        headers = ((GlobalVariable)getApplication()).getHeaders();
        loadingDialog = new LoadingDialog(AddGoalActivity.this);
        alert = new Alert(this, 1);
        viewModel = new ViewModelProvider(this).get(GoalAddViewModel.class);
        authUser = global.getAuthUser();
    }

    private void setData(){
        if(goal.getId()==0)
        {
            btn_add.setText(getString(R.string.goal_add));
            topTitle.setText(getString(R.string.goal_add));
        }else{
            btn_add.setText(getString(R.string.confirm));
            topTitle.setText(getString(R.string.goal_edit));
            goal_name.setText(goal.getName());
            goal_amount.setText(Helper.formatNumber((int)goal.getAmount()));
            goal_balance.setText(Helper.formatNumber((int)goal.getBalance()));
            goal_deadline.setText(goal.getDeadline());
        }
    }

    private void setEvent()
    {


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goal_name.getText().toString().isEmpty())
                {
                    alert.showAlert(getResources().getString(R.string.alertTitle), "Vui lòng nhập tên mục tiêu.", R.drawable.ic_info);
                    return;
                }else if (goal_amount.getText().toString().isEmpty())
                {
                    alert.showAlert(getResources().getString(R.string.alertTitle), "Vui lòng nhập số tiền mục tiêu.", R.drawable.ic_info);
                    return;
                }else if (goal_balance.getText().toString().isEmpty())
                {
                    alert.showAlert(getResources().getString(R.string.alertTitle), "Vui lòng nhập số dư cho mục tiêu.", R.drawable.ic_info);
                    return;
                }else if (goal_deadline.getText().toString().isEmpty())
                {
                    alert.showAlert(getResources().getString(R.string.alertTitle), "Vui lòng nhập ngày hết hạn mục tiêu.", R.drawable.ic_info);
                    return;
                }
                try
                {
                    int amount = Integer.parseInt(goal_amount.getText().toString().replace(",",""));
                    int balance = Integer.parseInt(goal_balance.getText().toString().replace(",",""));

                    if(amount<balance)
                    {
                        alert.showAlert(getResources().getString(R.string.alertTitle), "Số dư phải nhỏ hơn mục tiêu.", R.drawable.ic_info);
                        return;
                    }
                    goal.setName(goal_name.getText().toString());
                    goal.setAmount(amount);
                    goal.setBalance(balance);
                    goal.setDeadline(goal_deadline.getText().toString());

                }catch (NumberFormatException e)
                {
                    alert.showAlert(getResources().getString(R.string.alertTitle), getString(R.string.number_format_exception), R.drawable.ic_info);
                    return;
                }



                if(goal.getId()==0)
                {
                    viewModel.saveData(headers,goal);
                }else{
                    viewModel.updateData(headers,goal);

                }
            }
        });

        alert.btnOK.setOnClickListener(view -> alert.dismiss());

        viewModel.getObject().observe(this, object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                goal.setId(object.getGoal());
                Intent intent = new Intent();
                intent.putExtra("goal_entry", goal);
                setResult(78, intent);
                finish();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });
//        Format 1000 -> 1.000 ...
        goal_amount.addTextChangedListener(new NumberTextWatcher(goal_amount));
        goal_balance.addTextChangedListener(new NumberTextWatcher(goal_balance));

    }

    private void initializeDatePicker()
    {
        DatePickerDialog.OnDateSetListener datePicker = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);


            /*set text to date*/
            String format = "yyyy-MM-dd";
            SimpleDateFormat dateFormat=new SimpleDateFormat(format, Locale.CHINESE);
            goal_deadline.setText(dateFormat.format(myCalendar.getTime()));
        };

        goal_deadline.setOnClickListener(view-> new DatePickerDialog(AddGoalActivity.this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show());
    }
}