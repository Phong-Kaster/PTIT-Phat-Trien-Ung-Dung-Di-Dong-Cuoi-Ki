package com.example.prudentialfinance.RecycleViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.Model.Menu;
import com.example.prudentialfinance.R;

import java.util.ArrayList;

public class MenuRecycleViewAdapter extends RecyclerView.Adapter<MenuRecycleViewAdapter.ViewHolder> {

    private ArrayList<Menu> objects = new ArrayList<Menu>();
    private Context context;

    public MenuRecycleViewAdapter(Context context, ArrayList<Menu> objects)
    {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_menu_element, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Menu menu = objects.get(position);

        holder.icon.setImageResource(menu.getIcon());
        holder.name.setText(menu.getName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, menu.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView icon;
        private TextView name;
        private RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.menuIcon);
            name = itemView.findViewById(R.id.menuName);
            parentLayout = itemView.findViewById(R.id.fragmentMenuParentLayout);
        }
    }
}
