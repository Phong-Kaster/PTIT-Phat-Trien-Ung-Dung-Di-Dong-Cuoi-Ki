package com.example.prudentialfinance.RecycleViewAdapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.Notification;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.ViewModel.NotificationViewModel;

import java.util.List;

public class NotificationRecycleViewAdapter extends RecyclerView.Adapter<NotificationRecycleViewAdapter.ViewHolder> {

    private List<Notification> objects;
    private Context context;
    private Dialog dialog;
    private NotificationViewModel viewModel;

    public NotificationRecycleViewAdapter(Context context, List<Notification> objects, Dialog dialog, NotificationViewModel viewModel) {
        this.objects = objects;
        this.context = context;
        this.dialog = dialog;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public NotificationRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_home_notification_element, parent, false);

        return new NotificationRecycleViewAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NotificationRecycleViewAdapter.ViewHolder holder, int position) {
        /*Step 1*/
        Notification detail = objects.get(position);

        if(detail.getId() == 0){
            holder.titleNotif.setText(context.getString(R.string.masked_as_read));
            holder.titleNotif.setTextColor(context.getColor(R.color.colorTheme));
            holder.titleNotif.setTextSize(14);
            holder.titleNotif.setTypeface(null, Typeface.NORMAL);
            holder.titleNotif.setGravity(Gravity.CENTER);
        }else{
            holder.titleNotif.setText(Helper.truncate_string(detail.getTitle(), 25, "...", true));
        }

        holder.notifLayout.setOnClickListener(view -> {
            if(detail.getId() == 0){
                viewModel.maskedAsRead();
            }else{
                viewModel.maskedAsReadOne(detail.getId());
                TextView titleNotif = dialog.findViewById(R.id.titleNotif);
                TextView contentNotif = dialog.findViewById(R.id.contentNotif);
                TextView dateNotif = dialog.findViewById(R.id.dateNotif);

                titleNotif.setText(detail.getTitle());
                contentNotif.setText(detail.getContent());
                dateNotif.setText(detail.getCreated_at());

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleNotif;
        LinearLayout notifLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setControl(itemView);
        }

        private void setControl(View itemView)
        {
            titleNotif = itemView.findViewById(R.id.titleNotif);
            notifLayout = itemView.findViewById(R.id.notifLayout);
        }
    }
}