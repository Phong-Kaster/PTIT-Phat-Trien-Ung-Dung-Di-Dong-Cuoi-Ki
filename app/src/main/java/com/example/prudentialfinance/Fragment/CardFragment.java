package com.example.prudentialfinance.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.prudentialfinance.Activities.Card.AccountReportActivity;
import com.example.prudentialfinance.Activities.Card.CardCreationActivity;
import com.example.prudentialfinance.Helpers.CardModalBottomSheet;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.CardRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.CardFragmentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment implements CardModalBottomSheet.ModalBottomSheetListener {

    private LoadingDialog loadingDialog;
    private ImageButton buttonCreate, chartBtn;
    private RecyclerView recycleView;
    private CardFragmentViewModel viewModel;

    private List<Account> objects = new ArrayList<>();
    private CardRecycleViewAdapter adapter;

    private TextView notice;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HashMap<String, String> headers = null;

    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        viewModel.getAccounts().observe((LifecycleOwner) requireContext(), accounts -> {
            if( accounts.size() > 0)
            {
                objects.clear();
                objects.addAll(accounts);
                adapter.notifyDataSetChanged();

                notice.setVisibility(View.GONE);
                recycleView.setVisibility(View.VISIBLE);
            }
            else
            {
                notice.setVisibility(View.VISIBLE);
                recycleView.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);


        // Retrieve bundle's arguments
        assert this.getArguments() != null;
        String accessToken = this.getArguments().getString("accessToken");
        String contentType = this.getArguments().getString("contentType");


        /*initialize headers to attach HTTP Request*/
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        headers.put("Content-Type", contentType);


        setControl(view);
        setViewModel(view, headers);
        setEvent();
        setRecycleView(view);

        
        viewModel.getAnimation().observe((LifecycleOwner)requireContext(), aBoolean -> {
            if( aBoolean )
            {
                loadingDialog.startLoadingDialog();
            }
            else
            {
                loadingDialog.dismissDialog();
            }
        });


        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.instanciate(headers);
            objects.clear();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });


        return view;
    }

    private void setControl(View view)
    {
        loadingDialog = new LoadingDialog(getActivity());
        recycleView = view.findViewById(R.id.cardFragmentRecycleView);
        buttonCreate = view.findViewById(R.id.cardFragmentButtonCreate);
        chartBtn = view.findViewById(R.id.chartBtn);
        notice = view.findViewById(R.id.cardFragmentNotice);
        swipeRefreshLayout = view.findViewById(R.id.cardFragmentSwipeRefreshLayout);
    }

    /**
     * @author Phong-Kaster
     *
     * @param view is the current context of the fragment
     * @param headers is used to attach to HTTP Request headers include Access-Token and Content-Type
     *
     * Step 1: declare viewModel which will be used in this fragment
     * Step 2: retrieve data from API
     * Step 3: observe data if some data changes on server then
     * the data in this fragment is also updated automatically
     * */
    @SuppressLint({"NotifyDataSetChanged", "FragmentLiveDataObserve"})
    private void setViewModel(View view, Map<String, String> headers) {

        Context context = view.getContext();
        /*Step 1*/
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CardFragmentViewModel.class);

        viewModel.instanciate(headers);
        /*Step 2*/

    }

    private void setRecycleView(View view) {
        /*Step 0*/
        Context context = view.getContext();

        /*Step 1*/
        adapter = new CardRecycleViewAdapter(context, objects);
        recycleView.setAdapter(adapter);

        /*Step 2*/
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(manager);
    }

    private void setEvent()
    {
        buttonCreate.setOnClickListener(view->
                showCardModalBottomView());

        chartBtn.setOnClickListener(view -> {
            Intent chartIntent = new Intent(getContext(), AccountReportActivity.class);
            startActivity(chartIntent);
        });
    }

    private void showCardModalBottomView()
    {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.activity_card_introduce, null);

        Button button = view.findViewById(R.id.cardButtonCreate);
        button.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), CardCreationActivity.class);
            startActivity(intent);
        });

        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity());
        dialog.setContentView(view);
        dialog.show();
    }

}