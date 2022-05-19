package com.example.prudentialfinance.Activities.General;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prudentialfinance.Activities.General.AddGoalActivity;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.Goal;
import com.example.prudentialfinance.R;

public class GoalDetailActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> addGoalActivity;
    private ImageButton btn_back;
    private AppCompatButton btn_edit,btn_deposit,btn_export;
    private TextView goal_name,goal_amount,goal_deadline,goal_balance,goal_deposit;
    private Goal goal;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail);
        Intent intent = getIntent();
        goal = (Goal) intent.getSerializableExtra("goal");
        setControl();
        setData(goal);
        setEvent();
    }

    private void setControl(){
        progress = findViewById(R.id.progressBar_detail);
        btn_back = findViewById(R.id.Btn_back_detail);
        goal_name = findViewById(R.id.goal_name_detail);
        goal_amount = findViewById(R.id.goal_amount_detail);
        goal_deadline = findViewById(R.id.goal_deadline_detail);
        goal_balance = findViewById(R.id.goal_balance_detail);
        goal_deposit = findViewById(R.id.goal_deposit_detail);
        btn_edit = findViewById(R.id.btn_edit_detail);
        btn_deposit = findViewById(R.id.btn_deposit_detail);

    }

    private void setData(Goal goal){
        progress.setMax(100);
        int percent = (int)((goal.getBalance()+goal.getDeposit())*100 /goal.getAmount());
        progress.setProgress(percent);
        goal_name.setText(goal.getName());
        goal_amount.setText(Helper.formatNumber((int)goal.getAmount()));
        goal_deadline.setText(goal.getDeadline());
        goal_balance.setText(Helper.formatNumber((int)goal.getBalance()));
        goal_deposit.setText(Helper.formatNumber((int)goal.getDeposit()));

    }

    private void setEvent(){
        btn_back.setOnClickListener(view ->{
            finish();

        });


    }


}