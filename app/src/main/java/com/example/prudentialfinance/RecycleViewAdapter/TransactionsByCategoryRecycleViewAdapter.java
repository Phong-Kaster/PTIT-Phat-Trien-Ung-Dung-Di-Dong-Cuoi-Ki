package com.example.prudentialfinance.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Activities.Transaction.TransactionInformationActivity;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransactionsByCategoryRecycleViewAdapter extends RecyclerView.Adapter<TransactionsByCategoryRecycleViewAdapter.ViewHolder> {
    private ArrayList<TransactionDetail> objects;
    private Context context;
    private SiteSettings appInfo;


    public TransactionsByCategoryRecycleViewAdapter(Context context, ArrayList<TransactionDetail> objects, SiteSettings appInfo) {
        this.objects = objects;
        this.context = context;
        this.appInfo = appInfo;
    }

    @NonNull
    @Override
    public TransactionsByCategoryRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_transactions_by_category_element, parent, false);
        return new TransactionsByCategoryRecycleViewAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull TransactionsByCategoryRecycleViewAdapter.ViewHolder holder, int position) {
        TransactionDetail entry = objects.get(position);

        holder.transaction_amount.setText(Helper.formatNumber(entry.getAmount()) + " " + appInfo.getCurrency());
        Date transaction_date = Helper.convertStringToDate(entry.getTransactiondate(), "yyyy-MM-dd");

        String[] locales = appInfo.getLanguage().split("-");
        if(locales.length != 2) locales = new String[]{ "en", "US"};
        holder.transaction_date.setText(DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale(locales[0], locales[1])).format(transaction_date));

        holder.transaction_layout.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("detail", entry);
            bundle.putParcelable("account", entry.getAccount());

            Intent intent = new Intent(context, TransactionInformationActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView transaction_date, transaction_amount;
        private LinearLayout transaction_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setControl(itemView);
        }

        private void setControl(View itemView)
        {
            transaction_date = itemView.findViewById(R.id.transaction_date);
            transaction_amount = itemView.findViewById(R.id.transaction_amount);
            transaction_layout = itemView.findViewById(R.id.transaction_layout);
        }
    }
}
