package com.example.prudentialfinance.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prudentialfinance.API.HTTPService;
import com.example.prudentialfinance.Activities.Auth.LoginActivity;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.Setting;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.SettingRecycleViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsFragment extends Fragment {

    ArrayList<Setting> data = new ArrayList<>();
    RecyclerView lvSettings;
    ImageView ivAvatar;
    TextView fullName, email;
    User authUser;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            authUser = this.getArguments().getParcelable("AuthUser");
        }
    }

    private void setAuthorizedToken( ) {
        GlobalVariable state = ((GlobalVariable) requireActivity().getApplication());
        state.setAccessToken("");
        state.setAuthUser(null);

        SharedPreferences preferences = state.getSharedPreferences(state.getAppName(), Context.MODE_PRIVATE);
        preferences.edit().putString("accessToken", "").apply();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        data.add(new Setting("dark_mode",getString(R.string.appearance), R.drawable.ic_baseline_dark_mode_24));
        data.add(new Setting("personal_information", getString(R.string.personal_information), R.drawable.ic_baseline_person_24));
        data.add(new Setting("change_password", getString(R.string.change_password), R.drawable.ic_baseline_lock_open_24));

        data.add(new Setting("header", "", 0));
        data.add(new Setting("categories", getString(R.string.category), R.drawable.ic_baseline_category_24));
        data.add(new Setting("goals", getString(R.string.goal), R.drawable.icon_goal));
        data.add(new Setting("budgets", getString(R.string.budget), R.drawable.ic_baseline_archive_24));


        if(authUser.getAccount_type().equals("admin")){
            data.add(new Setting("header", "", 0));
            data.add(new Setting("site_settings",getString(R.string.site_settings), R.drawable.ic_baseline_web_24));
            data.add(new Setting("email_settings",getString(R.string.email_information), R.drawable.ic_baseline_email_24));

            data.add(new Setting("header", "", 0));
            data.add(new Setting("user_management",getString(R.string.user_management), R.drawable.ic_baseline_people_24));
        }

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        lvSettings = view.findViewById(R.id.lvSettings);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        fullName = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.email);




        Picasso
                .get()
                .load(HTTPService.UPLOADS_URL + "/"+authUser.getAvatar())
                .fit()
                .transform(Helper.getRoundedTransformationBuilder())
                .into(ivAvatar);

        fullName.setText(authUser.getFirstname() + " " + authUser.getLastname());
        email.setText(authUser.getEmail());


        LinearLayoutManager manager = new LinearLayoutManager(requireActivity().getApplicationContext());
        lvSettings.setLayoutManager(manager);

        SettingRecycleViewAdapter adapter = new SettingRecycleViewAdapter(getActivity().getApplicationContext(), data);
        lvSettings.setAdapter(adapter);


        view.findViewById(R.id.logout).setOnClickListener(view1 -> {
            setAuthorizedToken();
            Intent intent = new Intent(requireActivity().getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().onBackPressed();
        });

        return view;
    }
}