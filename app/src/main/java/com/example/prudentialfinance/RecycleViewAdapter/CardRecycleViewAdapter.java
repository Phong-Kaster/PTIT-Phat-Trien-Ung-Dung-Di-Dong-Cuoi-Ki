package com.example.prudentialfinance.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Activities.Card.CardUpdateActivity;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.R;

import java.util.ArrayList;
import java.util.List;

public class CardRecycleViewAdapter extends RecyclerView.Adapter<CardRecycleViewAdapter.ViewHolder> {

    private List<Account> accounts = new ArrayList<>();
    private Context context;

    public CardRecycleViewAdapter(Context context, List<Account> accounts) {
        this.accounts = accounts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_card_element, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*Step 1*/
        Account element = accounts.get(position);
        String elementNumber = Helper.formatCardNumber(element.getAccountnumber());
        String elementBalance = Helper.formatNumber(element.getBalance());
        String elementBank = element.getName();
        String elementDescription = element.getDescription();

        /*Step 2*/
        holder.number.setText(elementNumber);
        holder.balance.setText(elementBalance + " VND");
        holder.bank.setText(elementBank);
        holder.description.setText(elementDescription);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CardUpdateActivity.class);
                intent.putExtra("account", (Parcelable) element);
                intent.putExtra("accountName", elementBank);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView number;
        private TextView balance;
        private TextView bank;
        private TextView description;
        private RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setControl(itemView);
        }

        private void setControl(View itemView) {
            number = itemView.findViewById(R.id.cardNumber);
            balance = itemView.findViewById(R.id.cardBalance);

            bank = itemView.findViewById(R.id.cardBank);
            description = itemView.findViewById(R.id.cardDescription);
            parentLayout = itemView.findViewById(R.id.cardParentLayout);
        }
    }
}
