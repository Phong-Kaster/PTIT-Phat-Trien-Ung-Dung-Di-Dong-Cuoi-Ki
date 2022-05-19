package com.example.prudentialfinance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.prudentialfinance.Activities.General.AddGoalActivity;
import com.example.prudentialfinance.Activities.General.CategoriesActivity;
import com.example.prudentialfinance.Activities.General.GoalActivity;
import com.example.prudentialfinance.Activities.Transaction.TransactionCreationActivity;
import com.example.prudentialfinance.Fragment.CardFragment;
import com.example.prudentialfinance.Fragment.HomeFragment;
import com.example.prudentialfinance.Fragment.ReportFragment;
import com.example.prudentialfinance.Fragment.SettingsFragment;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.Goal;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.ViewModel.CardFragmentViewModel;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;
import com.example.prudentialfinance.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Fragment fragment = null;

    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private boolean isOpen = false;

    private CardFragmentViewModel cardFragmentViewModel = null;
    private HomeFragmentViewModel homeFragmentViewModel = null;
    private Map<String, String> headers;


    @Override
    public void onBackPressed() {
        Alert alert = new Alert(this,0);
        alert.btnOK.setOnClickListener(view->{
            HomeActivity.super.onBackPressed();
            alert.dismiss();
        } );
        alert.btnCancel.setOnClickListener(view-> alert.dismiss());
        alert.showAlert("Thoát","Bạn có muốn thoát khỏi ứng dụng",R.drawable.ic_close);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardFragmentViewModel = new ViewModelProvider(this).get(CardFragmentViewModel.class);
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        headers = ((GlobalVariable)getApplication()).getHeaders();

        setControl();

        /*bind data from home activity*/
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*set up default fragment is home fragment*/
        fragment = new HomeFragment();
        enableFragment(fragment);


        /*Binding bottomNavigationMenu*/
        binding.bottomNavigationMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.shortcutHome:
                    fragment = new HomeFragment();
                    break;
                case R.id.shortcutCard:
                    fragment = new CardFragment();
                    break;
                case R.id.shortcutReport:
                    fragment = new ReportFragment();
                    break;
                case R.id.shortcutSettings:
                    fragment = new SettingsFragment();
                    break;
            }
            enableFragment(fragment);
            return true;
        });


        binding.fab.setOnClickListener(view->{
            setVisibility();
            setAnimation();
            isOpen = !isOpen;
        });

        binding.transactionFab.setOnClickListener(view -> {
            Intent intent = new Intent(this, TransactionCreationActivity.class);
            intent.putExtra("type", "1");
            startActivity(intent);
        });
        binding.budgetFab.setOnClickListener(view -> System.out.println("budgetFab"));
        binding.categoryFab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CategoriesActivity.class);
            startActivity(intent);
        });
        binding.goalFab.setOnClickListener(view -> {
            Intent intent = new Intent (this, AddGoalActivity.class);
            intent.putExtra("goal", new Goal(0));
            startActivity(intent);
        });


    }

    private void setVisibility() {
        if(isOpen){
            binding.budgetFab.setVisibility(View.INVISIBLE);
            binding.transactionFab.setVisibility(View.INVISIBLE);
            binding.categoryFab.setVisibility(View.INVISIBLE);
            binding.goalFab.setVisibility(View.INVISIBLE);
        }else{
            binding.transactionFab.setVisibility(View.VISIBLE);
            binding.budgetFab.setVisibility(View.VISIBLE);
            binding.categoryFab.setVisibility(View.VISIBLE);
            binding.goalFab.setVisibility(View.VISIBLE);
        }
    }

    private void setAnimation() {
        if(isOpen){
            binding.transactionFab.startAnimation(toBottom);
            binding.budgetFab.startAnimation(toBottom);
            binding.categoryFab.startAnimation(toBottom);
            binding.goalFab.startAnimation(toBottom);
            binding.fab.startAnimation(rotateClose);
        }else{
            binding.transactionFab.startAnimation(fromBottom);
            binding.budgetFab.startAnimation(fromBottom);
            binding.categoryFab.startAnimation(fromBottom);
            binding.goalFab.startAnimation(fromBottom);
            binding.fab.startAnimation(rotateOpen);
        }
    }

    private void setControl() {
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom);
    }

    /**
     * @author Phong-Kaster
     * activate a fragment right away
     * */
    public void enableFragment(Fragment fragment)
    {
        /*Step 1*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        /*Step 2*/
        Map<String, String > headers = ((GlobalVariable)getApplication()).getHeaders();
        String accessToken = headers.get("Authorization");
        String contentType = headers.get("Content-Type");

        User AuthUser = ((GlobalVariable)getApplication()).getAuthUser();
        SiteSettings appInfo = ((GlobalVariable)getApplication()).getAppInfo();


        /*Step 3*/
        Bundle bundle = new Bundle();

        bundle.putString("accessToken", accessToken);
        bundle.putString("contentType", contentType);
        bundle.putParcelable("AuthUser", AuthUser);
        bundle.putParcelable("appInfo", appInfo);
        fragment.setArguments(bundle);


        /*Step 4*/
        transaction.replace(R.id.frameLayout, fragment, "myFragment");
        transaction.commit();
    }

    /**
     * Every single time user navigates to the another Activity and return HomeActivity
     * The application will request to retrieve latest data.
     * */
    @Override
    protected void onResume() {
        super.onResume();
        cardFragmentViewModel.instanciate(headers);
        homeFragmentViewModel.instanciate(headers);
    }
}