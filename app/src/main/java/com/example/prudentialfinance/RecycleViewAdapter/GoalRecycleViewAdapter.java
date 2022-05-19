package com.example.prudentialfinance.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Activities.General.AddGoalActivity;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.Goal;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.Activities.General.DepositActivity;
import com.example.prudentialfinance.Activities.General.GoalDetailActivity;

import java.util.ArrayList;

public class GoalRecycleViewAdapter extends RecyclerView.Adapter<GoalRecycleViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Goal> objects;
    ActivityResultLauncher<Intent> addGoalActivity;
    SiteSettings appInfo;

    public GoalRecycleViewAdapter(Context context, ArrayList<Goal> objects, ActivityResultLauncher<Intent> addGoalActivity, SiteSettings appInfo) {
        this.context = context;
        this.objects = objects;
        this.appInfo = appInfo;
        this.addGoalActivity = addGoalActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_goal_element, parent, false);
        return new GoalRecycleViewAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Goal entry = objects.get(position);

        holder.progressBar.setMax(100);
        long progress =Math.round(((double)(entry.getDeposit()+entry.getBalance())/(double)entry.getAmount())*100);
        if(progress==0)
        {
            holder.progressBar.setProgress(1);
        }else
        holder.progressBar.setProgress((int)progress);

//        SET CONTROL
        if(entry.getStatus()==1)
        {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }else if(entry.getStatus()==2)
        {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#03DAC5")));
        }else if(entry.getStatus()==3)
        {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }

        holder.goal_name.setText(Helper.truncate_string(entry.getName(),25, "...", true));
        holder.goal_deadline.setText(Helper.truncate_string(entry.getDeadline(), 25, "...", true));
        holder.goal_amount.setText(Helper.truncate_string(Helper.formatNumber((int)entry.getAmount())+ appInfo.getCurrency(), 25, "...", true));
        if(progress>=100)
            holder.goal_balance.setText(context.getString(R.string.done));
        else
        holder.goal_balance.setText(Helper.truncate_string(context.getString(R.string.had_money)+'\t'+Helper.formatNumber((int)(entry.getDeposit()+entry.getBalance()))+appInfo.getCurrency(), 70, "...", true));

        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle(context.getString(R.string.action));
        String[] types ;
        if(entry.getStatus()!=1)
            types = context.getResources().getStringArray(R.array.action_forgoal23);
        else
            types = context.getResources().getStringArray(R.array.action);
        b.setItems(types, (dialog, which) -> {
            dialog.dismiss();
            switch(which){
                case 0:
                    Intent intent = new Intent (context, GoalDetailActivity.class);
                    intent.putExtra("goal", entry);
                    context.startActivity(intent);
                    break;
                case 1:
                    Intent intent1 = new Intent (context, AddGoalActivity.class);
                    intent1.putExtra("goal", entry);
                    addGoalActivity.launch(intent1);
                    break;
                case 2:
                    Intent intent2 = new Intent (context, DepositActivity.class);
                    intent2.putExtra("goal", entry);
                    addGoalActivity.launch(intent2);
            }
        });

        holder.goal_layout.setOnClickListener(view -> {
            b.show();
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView goal_name, goal_deadline,goal_amount,goal_balance;
        private LinearLayout goal_layout;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setControl(itemView);
        }

        private void setControl(View itemView)
        {
            goal_name = itemView.findViewById(R.id.goal_name);
            goal_deadline = itemView.findViewById(R.id.goal_deadline);
            goal_amount = itemView.findViewById(R.id.goal_amount);
            goal_balance = itemView.findViewById(R.id.goal_balance);
            goal_layout = itemView.findViewById(R.id.goal_layout);
            progressBar = itemView.findViewById(R.id.progressBar_Goal);
        }
    }
}
