package com.example.prudentialfinance.RecycleViewAdapter.Budget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Activities.Budget.AddBudgetActivity;
import com.example.prudentialfinance.Activities.Budget.BudgetReportCategoriesActivity;
import com.example.prudentialfinance.Container.budgets.budgetGET.Datum;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.R;

import java.util.ArrayList;

public class BudgetRecycleViewAdapter extends RecyclerView.Adapter<BudgetRecycleViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Datum> objects;
    ActivityResultLauncher<Intent> addRootActivity;

    public BudgetRecycleViewAdapter(Context content, ArrayList<Datum> objects, ActivityResultLauncher<Intent> addRootActivity) {
        this.context = context;
        this.objects = objects;
        this.addRootActivity = addRootActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_budget_element, parent, false);
        return new BudgetRecycleViewAdapter.ViewHolder(view, parent);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Datum entry = objects.get(position);
        String name = entry.getCategory().getName() == null ? "Tên" : entry.getCategory().getName();
        String description  = entry.getDescription() == null ? "Mô tả" : entry.getDescription();
        holder.budgetName.setText(Helper.truncate_string(name,70, "...", true));
        holder.budgetDescription.setText(Helper.truncate_string(description, 70, "...", true));
        holder.budgetDate.setText(Helper.truncate_string(entry.getTodate(), 70, "...", true));
        Context parentContext = holder.parent.getContext();
        holder.budgetEdit.setOnClickListener(view -> {
            Intent intent = new Intent (parentContext, AddBudgetActivity.class);
            intent.putExtra("budget", entry);
            addRootActivity.launch(intent);
        });
        holder.budgetElementParent.setOnClickListener(view -> {
            Intent intent = new Intent (parentContext, BudgetReportCategoriesActivity.class);
            Datum d = objects.get(position);
            String id = String.valueOf(d.getId());
            String date = com.example.prudentialfinance.Activities.Budget.Helper.getYear(d.getTodate()) + "-" + com.example.prudentialfinance.Activities.Budget.Helper.getMonth(d.getTodate());
            int amount = d.getAmount();

            intent.putExtra("id", id);
            intent.putExtra("date", date);
            intent.putExtra("amount", amount);
            addRootActivity.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView budgetName, budgetDescription, budgetDate;
        private LinearLayout budgetElementParent;
        private ImageButton budgetEdit;
        private ViewGroup parent;

        public ViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            setControl(itemView);
            this.parent = parent;
        }

        private void setControl(View itemView)
        {
            budgetName = itemView.findViewById(R.id.budget_element_name);
            budgetDescription = itemView.findViewById(R.id.budget_element_description);
            budgetDate = itemView.findViewById(R.id.budget_element_date);
            budgetElementParent = itemView.findViewById(R.id.budget_element_parent);
            budgetEdit = itemView.findViewById(R.id.budget_icon);
        }
    }
}
