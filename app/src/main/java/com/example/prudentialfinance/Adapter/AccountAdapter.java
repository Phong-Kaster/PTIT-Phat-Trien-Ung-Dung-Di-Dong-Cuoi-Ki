package com.example.prudentialfinance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.R;

import java.util.ArrayList;

public class AccountAdapter extends ArrayAdapter<Account> {

    public AccountAdapter(Context context, ArrayList<Account> accounts)
    {
        super(context,0, accounts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return instanciate(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return instanciate(position, convertView, parent);
    }

    private View instanciate(int position, View convertView, ViewGroup parent)
    {
        if( convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.spinner_account_element, parent,false);
        }

        TextView cardName = convertView.findViewById(R.id.cardName);
        TextView cardBalance = convertView.findViewById(R.id.cardBalance);

        Account account = getItem(position);
        String name = account.getName();
        String balance = Helper.formatNumber(account.getBalance());
        cardName.setText( name );
        cardBalance.setText( balance );

        return convertView;
    }
}
