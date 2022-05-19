package com.example.prudentialfinance.Helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prudentialfinance.Activities.Transaction.TransactionCreationActivity;
import com.example.prudentialfinance.Activities.Transaction.TransactionStatementActivity;
import com.example.prudentialfinance.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModalBottomSheet extends BottomSheetDialogFragment {

    private ModalBottomSheetListener listener;
    private Button inflow;
    private Button outflow;
    private Button statement;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_transaction_menu, container, false);

        setControl(view);
        setEvent();
        return view;
    }

    public interface ModalBottomSheetListener
    {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try
        {
            listener = (ModalBottomSheetListener) context;
        }
        catch(Exception ex)
        {
            throw new ClassCastException(context.toString() + "must implement ModalBottomSheetListener");
        }

    }

    private void setControl(View view)
    {
        inflow = view.findViewById(R.id.transactionMenuButtonCreateIncome);
        outflow = view.findViewById(R.id.transactionMenuButtonCreateExpense);
        statement = view.findViewById(R.id.transactionMenuButtonCreateStatement);
    }

    private void setEvent()
    {
        inflow.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), TransactionCreationActivity.class);
            intent.putExtra("type", "1");
            startActivity(intent);
        });

        outflow.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), TransactionCreationActivity.class);
            intent.putExtra("type", "2");
            startActivity(intent);
        });

        statement.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), TransactionStatementActivity.class);
            startActivity(intent);
        });
    }
}
