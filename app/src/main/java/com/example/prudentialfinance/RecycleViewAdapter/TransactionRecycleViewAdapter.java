package com.example.prudentialfinance.RecycleViewAdapter;

import static com.example.prudentialfinance.Helpers.Helper.convertStringToDate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Activities.Transaction.TransactionInformationActivity;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRecycleViewAdapter extends RecyclerView.Adapter<TransactionRecycleViewAdapter.ViewHolder> {

    private List<TransactionDetail> objects;
    private Context context;

    public TransactionRecycleViewAdapter(Context context, List<TransactionDetail> objects) {
        this.objects = objects;
        this.context = context;

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setObjects(List<TransactionDetail> objects) {
        this.objects.clear();
        this.objects.addAll(objects);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_home_transaction_element, parent, false);

        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*Step 1*/
        TransactionDetail detail = objects.get(position);

        String transactionName     = detail.getName();
        String transactionCategory = detail.getCategory().getName();
        String transactionAmount   = Helper.formatNumber( detail.getAmount());
        String transactionDate     = detail.getTransactiondate();
        int transactionType  = detail.getType();


        /*Step 2*/
        try {
            transactionDate = Helper.convertStringToDate(transactionDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        /*Step 3*/
        holder.name.setText(transactionName);
        holder.category.setText(transactionCategory);
        holder.amount.setText(transactionAmount + " VND");
        if(transactionType == 2)
        {
            holder.amount.setTextColor(context.getColor(R.color.colorRed));
        }
        holder.date.setText(transactionDate);


        /*Step 4*/
        Bundle bundle = new Bundle();
        bundle.putParcelable("detail", detail);
        bundle.putParcelable("account", detail.getAccount());

        holder.parentLayout.setOnClickListener(view ->{
            Intent intent = new Intent(context, TransactionInformationActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView category;
        TextView amount;
        TextView date;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setControl(itemView);
        }

        private void setControl(View itemView)
        {
            name = itemView.findViewById(R.id.fragmentHomeTransactionName);
            category = itemView.findViewById(R.id.fragmentHomeTransactionCategory);
            amount = itemView.findViewById(R.id.fragmentHomeTransactionAmount);
            date = itemView.findViewById(R.id.fragmentHomeTransactionDate);
            parentLayout = itemView.findViewById(R.id.fragmentHomeTransactionLayout);
        }
    }
}
