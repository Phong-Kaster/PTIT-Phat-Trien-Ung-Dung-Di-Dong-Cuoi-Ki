package com.example.prudentialfinance.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Activities.Settings.AddUserActivity;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserRecycleViewAdapter extends RecyclerView.Adapter<UserRecycleViewAdapter.ViewHolder> {
    private ArrayList<User> objects;
    private Context context;
    private ActivityResultLauncher<Intent> updateUserActivity;

    public UserRecycleViewAdapter(Context context, ArrayList<User> objects, ActivityResultLauncher<Intent> updateUserActivity) {
            this.objects = objects;
            this.context = context;
            this.updateUserActivity = updateUserActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.user_management_element, parent, false);
            return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull UserRecycleViewAdapter.ViewHolder holder, int position) {
            User entry = objects.get(position);

            holder.user_name.setText(entry.getFirstname() + " " + entry.getLastname());
            holder.user_email.setText(entry.getEmail());
            if(entry.getIs_active()){
                holder.user_active.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            }else{
                holder.user_active.setImageResource(R.drawable.ic_baseline_close_24);
            }

            if(entry.getAccount_type().equals("admin")){
                holder.user_admin.setVisibility(View.VISIBLE);
                holder.user_admin.setImageResource(R.drawable.ic_baseline_admin_panel_settings_24);
            }else{
                holder.user_admin.setVisibility(View.INVISIBLE);
            }

            Picasso
                .get()
                .load(HTTPService.UPLOADS_URL + "/"+ entry.getAvatar())
                .fit()
                .placeholder(R.drawable.someone)
                .error(R.drawable.someone)
                .transform(Helper.getRoundedTransformationBuilder1())
                .into(holder.user_avatar);


            holder.user_layout.setOnClickListener(view1 -> {
                Intent intent = new Intent(context, AddUserActivity.class);
                intent.putExtra("user", entry);
                updateUserActivity.launch(intent);
            });
    }

    @Override
    public int getItemCount() {
            return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView user_avatar, user_active, user_admin;
        private TextView user_name, user_email;
        private LinearLayout user_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setControl(itemView);
        }

        private void setControl(View itemView)
        {
            user_avatar = itemView.findViewById(R.id.user_avatar);
            user_admin = itemView.findViewById(R.id.user_admin);
            user_active = itemView.findViewById(R.id.user_active);
            user_name = itemView.findViewById(R.id.user_name);
            user_email = itemView.findViewById(R.id.user_email);
            user_layout = itemView.findViewById(R.id.user_layout);
        }
    }
}
