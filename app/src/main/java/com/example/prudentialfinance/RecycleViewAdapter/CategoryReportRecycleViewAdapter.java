package com.example.prudentialfinance.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Activities.General.AddCategoryActivity;
import com.example.prudentialfinance.Activities.Report.TransactionsByCategoryActivity;
import com.example.prudentialfinance.Container.Report.CategoryReport;
import com.example.prudentialfinance.Container.Report.DateRange;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.R;

import java.util.ArrayList;

public class CategoryReportRecycleViewAdapter extends RecyclerView.Adapter<CategoryReportRecycleViewAdapter.ViewHolder> {
    private ArrayList<CategoryReport> objects;
    private Context context;
    private DateRange dateRange;
    private SiteSettings appInfo;


    public CategoryReportRecycleViewAdapter(Context context, ArrayList<CategoryReport> objects, DateRange dateRange, SiteSettings appInfo) {
        this.objects = objects;
        this.context = context;
        this.dateRange = dateRange;
        this.appInfo = appInfo;

    }

    @NonNull
    @Override
    public CategoryReportRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_report_category_element, parent, false);
        return new CategoryReportRecycleViewAdapter.ViewHolder(view, parent);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CategoryReportRecycleViewAdapter.ViewHolder holder, int position) {
        CategoryReport entry = objects.get(position);

        holder.cat_color.getBackground().setColorFilter(Color.parseColor(entry.getColor()), PorterDuff.Mode.SRC);
        holder.cat_name.setText(Helper.truncate_string(entry.getName(), 70, "...", true));
        holder.cat_amount.setText("x" + Helper.formatNumber(entry.getTotal()));
        holder.cat_value.setText(Helper.formatNumber(entry.getAmount()) + " " + appInfo.getCurrency());

        Context parentContext = holder.parent.getContext();
        holder.cat_layout.setOnClickListener(view1 -> {
            Intent intent = new Intent(parentContext, TransactionsByCategoryActivity.class);
            intent.putExtra("dateRange", dateRange);
            intent.putExtra("categoryReport", entry);
            parentContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageButton cat_color;
        private TextView cat_name, cat_value, cat_amount;
        private LinearLayout cat_layout;
        private ViewGroup parent;

        public ViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            setControl(itemView);
            this.parent = parent;
        }

        private void setControl(View itemView)
        {
            cat_color = itemView.findViewById(R.id.cat_color);
            cat_name = itemView.findViewById(R.id.cat_name);
            cat_value = itemView.findViewById(R.id.cat_value);
            cat_amount = itemView.findViewById(R.id.cat_amount);
            cat_layout = itemView.findViewById(R.id.cat_layout);
        }
    }
}