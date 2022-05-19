package com.example.prudentialfinance.Fragment.Categories;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.CategoryRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.Categories.CategoriesIncomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CategoriesIncomeFragment extends Fragment {

    CategoriesIncomeViewModel viewModel;
    LoadingDialog loadingDialog;
    Alert alert;
    Alert alertConfirm;
    Map<String, String> headers;
    User authUser;

    RecyclerView lvCategory;
    CategoryRecycleViewAdapter adapter;
    LinearLayoutManager manager;

    ArrayList<Category> data;
    int positionItem;
    Category entry;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.categories_income_fragment, container, false);


        setComponent();

        setControl(view);

        setEvent();

        loadData();

        setSwipe();
        return view;
    }

    private void setSwipe() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Take action for the swiped item
                int position = viewHolder.getLayoutPosition();
                entry = data.get(position);
                positionItem = position;
                data.remove(position);
                adapter.notifyItemRemoved(position);

                alertConfirm.showAlert(getString(R.string.alertWarning), getString(R.string.alertConfirm), R.drawable.ic_info);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
                        .addActionIcon(R.drawable.ic_close)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(lvCategory);

    }

    private void setControl(View view) {
        lvCategory = view.findViewById(R.id.lvCategory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    public void getData(String query){
        data.clear();
        viewModel.getData(headers, query);
    }


    private void loadData() {
        data = new ArrayList<>();
        viewModel.getData(headers, "");

        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        lvCategory.setLayoutManager(manager);

        adapter = new CategoryRecycleViewAdapter(getActivity().getApplicationContext(), data, editCategoryActivity);
        lvCategory.setAdapter(adapter);

    }

    private void setComponent() {
        assert this.getArguments() != null;
        authUser = this.getArguments().getParcelable("authUser");
        String accessToken = this.getArguments().getString("accessToken");
        String contentType = this.getArguments().getString("contentType");


        // initialize headers to attach HTTP Request
        headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        headers.put("Content-Type", contentType);


        loadingDialog = new LoadingDialog(this.getActivity());
        alert = new Alert(this.getContext(), 1);
        alertConfirm = new Alert(this.getContext(), 2);
        viewModel = new ViewModelProvider(this).get(CategoriesIncomeViewModel.class);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void setEvent() {

        alert.btnOK.setOnClickListener(view -> alert.dismiss());


        alertConfirm.btnOK.setOnClickListener(view -> {
            viewModel.deteteItem(headers, entry.getId());
            alertConfirm.dismiss();
        });

        alertConfirm.btnCancel.setOnClickListener(view -> {
            data.add(positionItem, entry);
            adapter.notifyItemInserted(positionItem);
            alertConfirm.dismiss();
        });



        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading){
                loadingDialog.startLoadingDialog();
            }else{
                loadingDialog.dismissDialog();
            }
        });

        viewModel.getObject().observe(getViewLifecycleOwner(), object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                data.clear();
                data.addAll(object.getData());
                adapter.notifyDataSetChanged();
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            data.clear();
            viewModel.getData(headers, "");
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public void addData(Category entry){
        boolean isAdd = true;
        for (Category item: data) {
            if(item.getId().equals(entry.getId())){
                item.setType(entry.getType());
                item.setDescription(entry.getDescription());
                item.setName(entry.getName());
                item.setColor(entry.getColor());
                isAdd = false;
                break;
            }
        }

        if(isAdd){
            data.add(0, entry);
        }
        adapter.notifyDataSetChanged();
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> editCategoryActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 78) {
                    // There are no request codes
                    Intent data = result.getData();
                    assert data != null;
                    Category dataFromActivity = data.getParcelableExtra("category_entry");
                    addData(dataFromActivity);
                }
            });
}