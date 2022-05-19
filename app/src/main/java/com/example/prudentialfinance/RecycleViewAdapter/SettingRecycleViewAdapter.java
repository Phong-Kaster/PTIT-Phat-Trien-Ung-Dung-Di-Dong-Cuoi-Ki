package com.example.prudentialfinance.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.example.prudentialfinance.Activities.Budget.BudgetsActivity;
import com.example.prudentialfinance.Activities.General.CategoriesActivity;
import com.example.prudentialfinance.Activities.General.GoalActivity;
import com.example.prudentialfinance.Activities.Settings.DarkModeActivity;
import com.example.prudentialfinance.Activities.Settings.UserManagementActivity;
import com.example.prudentialfinance.Model.Setting;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.Activities.Settings.EmailSettingsActivity;
import com.example.prudentialfinance.Activities.Settings.PasswordActivity;
import com.example.prudentialfinance.Activities.Settings.ProfileActivity;
import com.example.prudentialfinance.Activities.Settings.SiteSettingsActivity;

import java.util.List;

public class SettingRecycleViewAdapter extends RecyclerView.Adapter<SettingRecycleViewAdapter.ViewHolder> {
    private List<Setting> objects;
    private Context context;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    public SettingRecycleViewAdapter(Context context, List<Setting> objects) {
        this.objects = objects;
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position).getIcon() == 0 ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(viewType == TYPE_ITEM){
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.fragment_settings_element, parent, false);
            return new ViewHolder(view, parent);
        }
        view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_settings_element_header, parent, false);
        return new ViewHolderSeparator(view, parent);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull SettingRecycleViewAdapter.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            Setting entry = objects.get(position);

            holder.iconLeftList.setImageResource(entry.getIcon());
            holder.textList.setText(entry.getTitle());

            Context parentContext = holder.parent.getContext();
            holder.elementSettingLayout.setOnClickListener(view1 -> {
                switch (entry.getId()) {
                    case "dark_mode":
                        parentContext.startActivity(new Intent(parentContext, DarkModeActivity.class));
                        break;
                    case "personal_information":
                        parentContext.startActivity(new Intent(parentContext, ProfileActivity.class));
                        break;
                    case "change_password":
                        parentContext.startActivity(new Intent(parentContext, PasswordActivity.class));
                        break;
                    case "site_settings":
                        parentContext.startActivity(new Intent(parentContext, SiteSettingsActivity.class));
                        break;
                    case "email_settings":
                        parentContext.startActivity(new Intent(parentContext, EmailSettingsActivity.class));
                        break;
                    case "categories":
                        parentContext.startActivity(new Intent(parentContext, CategoriesActivity.class));
                        break;
                    case "goals":
                        parentContext.startActivity(new Intent(parentContext, GoalActivity.class));
                        break;
                    case "user_management":
                        parentContext.startActivity(new Intent(parentContext, UserManagementActivity.class));
                        break;
                    case "budgets":
                        parentContext.startActivity(new Intent(parentContext, BudgetsActivity.class));
                        break;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageButton iconLeftList;
        private TextView textList;
        private LinearLayout elementSettingLayout;
        private ViewGroup parent;

        public ViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            setControl(itemView);
            this.parent = parent;
        }

        private void setControl(View itemView)
        {
            iconLeftList = itemView.findViewById(R.id.iconLeftList);
            textList = itemView.findViewById(R.id.textList);
            elementSettingLayout = itemView.findViewById(R.id.elementSettingLayout);
        }
    }

    public class ViewHolderSeparator extends ViewHolder
    {
        public ViewHolderSeparator(@NonNull View itemView, ViewGroup parent) {
            super(itemView, parent);
        }
    }
}
