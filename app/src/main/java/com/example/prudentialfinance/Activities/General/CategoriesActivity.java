package com.example.prudentialfinance.Activities.General;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.prudentialfinance.Adapter.CategoryAdapter;
import com.example.prudentialfinance.Fragment.Categories.CategoriesExpenseFragment;
import com.example.prudentialfinance.Fragment.Categories.CategoriesIncomeFragment;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.GlobalVariable;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Map;

public class CategoriesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private SearchView searchView;
    private ViewPager2 viewPager;
    private CategoryAdapter categoryAdapter;
    private ImageButton backBtn, btnAdd;
    private View closeButton;
    private int viewType;

    CategoriesIncomeFragment categoriesIncomeFragment;
    CategoriesExpenseFragment categoriesExpenseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setControl();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setEvent() {
        categoryAdapter = new CategoryAdapter(this);


        Bundle bundle = new Bundle();

        Map<String, String > headers = ((GlobalVariable)getApplication()).getHeaders();
        String accessToken = headers.get("Authorization");
        String contentType = headers.get("Content-Type");

        User AuthUser = ((GlobalVariable)getApplication()).getAuthUser();
        bundle.putString("accessToken", accessToken);
        bundle.putString("contentType", contentType);
        if(AuthUser != null){
            bundle.putParcelable("AuthUser", AuthUser);
        }

        categoriesIncomeFragment = new CategoriesIncomeFragment();
        categoriesIncomeFragment.setArguments(bundle);

        categoriesExpenseFragment = new CategoriesExpenseFragment();
        categoriesExpenseFragment.setArguments(bundle);

        categoryAdapter.addFragment(categoriesIncomeFragment, getResources().getString(R.string.income));
        categoryAdapter.addFragment(categoriesExpenseFragment, getResources().getString(R.string.expense));

        viewPager.setAdapter(categoryAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(categoryAdapter.getFragmentTitle(position))).attach();


        backBtn.setOnClickListener(view -> finish());
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddCategoryActivity.class);
            intent.putExtra("category", new Category(0, "", viewType+1, "#FF0000", ""));

            addCategoryActivity.launch(intent);
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                viewType = position;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(viewType == 0){
                    categoriesIncomeFragment.getData(query);
                }else{
                    categoriesExpenseFragment.getData(query);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        closeButton.setOnClickListener(v -> {
            if(viewType == 0){
                categoriesIncomeFragment.getData("");
            }else{
                categoriesExpenseFragment.getData("");
            }
            searchView.setQuery("", false);
        });

    }

    private void setControl() {
        tabLayout = findViewById(R.id.tabLayout);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        viewPager = findViewById(R.id.viewPager);
        viewPager.setUserInputEnabled(false);

        backBtn = findViewById(R.id.backBtn);
        btnAdd = findViewById(R.id.btnAdd);


        closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> addCategoryActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 78) {
                    // There are no request codes
                    Intent data = result.getData();
                    assert data != null;
                    Category dataFromActivity = data.getParcelableExtra("category_entry");

                    System.out.println(dataFromActivity.toString());
                    if(viewType == 0){
                        categoriesIncomeFragment.addData(dataFromActivity);
                    }else{
                        categoriesExpenseFragment.addData(dataFromActivity);
                    }
                }
            });
}