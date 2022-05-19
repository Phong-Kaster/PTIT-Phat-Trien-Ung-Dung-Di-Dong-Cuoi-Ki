package com.example.prudentialfinance.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class CategoryAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public String getFragmentTitle(int position) {
        return fragmentTitle.get(position);
    }

    public CategoryAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }
}
