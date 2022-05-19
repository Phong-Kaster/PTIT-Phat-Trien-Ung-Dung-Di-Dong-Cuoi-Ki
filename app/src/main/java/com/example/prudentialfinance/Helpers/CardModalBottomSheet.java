package com.example.prudentialfinance.Helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.prudentialfinance.Activities.Card.CardCreationActivity;
import com.example.prudentialfinance.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CardModalBottomSheet extends BottomSheetDialogFragment{

    private ModalBottomSheetListener listener;
    private AppCompatButton button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_card_introduce, container, false);

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
        listener = (ModalBottomSheetListener) context;
    }

    private void setControl(View view) {
        button = view.findViewById(R.id.cardButtonCreate);
    }

    private void setEvent() {
        button.setOnClickListener(view->{
            Intent intent = new Intent((Context) listener, CardCreationActivity.class);
            startActivity(intent);
        });
    }
}
