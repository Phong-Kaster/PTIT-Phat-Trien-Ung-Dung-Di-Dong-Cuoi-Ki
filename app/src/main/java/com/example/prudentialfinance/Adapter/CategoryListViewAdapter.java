package com.example.prudentialfinance.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.prudentialfinance.Activities.Transaction.TransactionCreationActivity;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.R;

import java.util.ArrayList;

public class CategoryListViewAdapter extends ArrayAdapter<String> {

    private ArrayList<String> categories = new ArrayList<>();
    private Context context;


    public CategoryListViewAdapter(Context context, ArrayList<String> categories) {
        super(context, 0, categories);
        this.categories = categories;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return instanciate(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return categories.size();
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
                    .inflate(R.layout.spinner_category_element, parent,false);
        }

        TextView categoryTitle = convertView.findViewById(R.id.categoryTitle);


        String category = categories.get(position);

        categoryTitle.setText(category);
        return convertView;
    }
}
