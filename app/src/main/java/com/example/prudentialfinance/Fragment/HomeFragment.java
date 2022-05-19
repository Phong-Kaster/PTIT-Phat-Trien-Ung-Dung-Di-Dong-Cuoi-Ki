package com.example.prudentialfinance.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.prudentialfinance.Activities.Budget.BudgetsActivity;
import com.example.prudentialfinance.Activities.General.CategoriesActivity;
import com.example.prudentialfinance.Activities.General.GoalActivity;
import com.example.prudentialfinance.Activities.Transaction.TransactionActivity;
import com.example.prudentialfinance.ContainerModel.TransactionDetail;
import com.example.prudentialfinance.Helpers.Alert;
import com.example.prudentialfinance.Helpers.Helper;
import com.example.prudentialfinance.Helpers.LoadingDialog;
import com.example.prudentialfinance.Model.Notification;
import com.example.prudentialfinance.Model.SiteSettings;
import com.example.prudentialfinance.Model.User;
import com.example.prudentialfinance.R;
import com.example.prudentialfinance.RecycleViewAdapter.NotificationRecycleViewAdapter;
import com.example.prudentialfinance.RecycleViewAdapter.TransactionRecycleViewAdapter;
import com.example.prudentialfinance.ViewModel.HomeFragmentViewModel;
import com.example.prudentialfinance.ViewModel.NotificationViewModel;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ImageButton buttonTransaction;
    private LoadingDialog loadingDialog;

    private ImageButton buttonCategory;
    private ImageButton buttonBudget, ringBellBtn;

    private ImageButton buttonButtonGoal;
    private RecyclerView recycleView, rvNotifications;

    private HomeFragmentViewModel viewModel;
    private NotificationViewModel viewModelNotif;
    private TextView name, remaining, seeAll;

    private RelativeLayout transactionsContainer;
    private TextView notice;

    private FrameLayout layoutNotif;
    private ProgressBar progress_bar;
    private Alert alert;
    private User AuthUser;
    private SiteSettings appInfo;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TransactionRecycleViewAdapter adapter;
    private AppCompatImageView ivHaveNotif;

    private ArrayList<Notification> dataNotif = new ArrayList<>();
    private NotificationRecycleViewAdapter adapterNotif;
    private Dialog dialog;

    private boolean isShow = false;
    private boolean isNotificationRemaning = false;
    private final Map<String, String> headers = new HashMap<>();


    private List<TransactionDetail> objects = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        /*get parameters from home activity sends to this fragment*/
        assert this.getArguments() != null;
        AuthUser = this.getArguments().getParcelable("AuthUser");
        appInfo = this.getArguments().getParcelable("appInfo");
        String accessToken = this.getArguments().getString("accessToken");
        String contentType = this.getArguments().getString("contentType");


        /*initialize headers to attach HTTP Request*/
        headers.put("Authorization", accessToken);
        headers.put("Content-Type", contentType);


        /*establish necessary functions*/
        setControl(view);
        setViewModel(view, headers);
        setEvent();
        setScreen();
        setRecycleView(HomeFragment.this);

        if( objects.size() > 0)
        {
            viewModel.getAnimation().observe(getViewLifecycleOwner(), aBoolean -> {
                if( aBoolean )
                {
                    loadingDialog.startLoadingDialog();
                }
                else
                {
                    loadingDialog.dismissDialog();
                }
            });
        }


        viewModelNotif.isLoading().observe(getViewLifecycleOwner(), aBoolean -> {
            if( aBoolean )
            {
                progress_bar.setVisibility(View.VISIBLE);
                rvNotifications.setVisibility(View.INVISIBLE);
            }
            else
            {
                progress_bar.setVisibility(View.INVISIBLE);
                rvNotifications.setVisibility(View.VISIBLE);
            }
        });




        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactionDetails -> {
            if( transactionDetails.size() > 0)
            {
                objects.clear();
                objects.addAll(transactionDetails);
                adapter.notifyDataSetChanged();


                notice.setVisibility(View.GONE);
                transactionsContainer.setVisibility(View.VISIBLE);
            }
            else
            {
                notice.setVisibility(View.VISIBLE);
                transactionsContainer.setVisibility(View.GONE);
            }
        });



        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.instanciate(headers);
            objects.clear();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });


        viewModelNotif.getData();

        return view;
    }

    /**
     * @author Phong-Kaster
     * set up button and recycle view to listen event
     * */
    private void setControl(View view) {

        loadingDialog = new LoadingDialog(getActivity());
        swipeRefreshLayout = view.findViewById(R.id.homeFragmentSwipeRefreshLayout);

        buttonTransaction       = view.findViewById(R.id.fragmentHomeButtonTransactions);
        buttonCategory  = view.findViewById(R.id.fragmentHomeButtonCategory);
        layoutNotif  = view.findViewById(R.id.layoutNotif);

        buttonBudget = view.findViewById(R.id.fragmentHomeButtonBudget);
        buttonButtonGoal        = view.findViewById(R.id.fragmentHomeButtonGoal);
        ringBellBtn        = view.findViewById(R.id.ringBellBtn);
        progress_bar        = view.findViewById(R.id.progress_bar);

        recycleView  = view.findViewById(R.id.fragmentHomeRecentTransactions);
        rvNotifications  = view.findViewById(R.id.rvNotifications);
        name = view.findViewById(R.id.fragmentHomeAuthName);

        remaining = view.findViewById(R.id.fragmentHomeAuthRemaining);
        ivHaveNotif = view.findViewById(R.id.ivHaveNotif);

        seeAll = view.findViewById(R.id.homeFragmentSeeAll);
        notice = view.findViewById(R.id.homeFragmentNotice);

        transactionsContainer = view.findViewById(R.id.homeFragmentTransactionLayout);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.notif_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
    @SuppressLint({"NotifyDataSetChanged", "FragmentLiveDataObserve", "SetTextI18n"})
    private void setViewModel(View view, Map<String, String> headers) {

        Context context = view.getContext();

        /*Step 1*/
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(HomeFragmentViewModel.class);
        viewModelNotif = new ViewModelProvider((ViewModelStoreOwner) context).get(NotificationViewModel.class);
        viewModelNotif.setHeaders(headers);

        /*Step 2*/
        viewModel.getTotalBalance().observe((LifecycleOwner) context, aDouble -> {
            String value = Helper.formatNumber(aDouble);
            remaining.setText( value + " " + appInfo.getCurrency() );
        });

        alert = new Alert(getContext(), 1);

        viewModelNotif.getObject().observe(getViewLifecycleOwner(), object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }

            if (object.getResult() == 1) {
                dataNotif.clear();
                dataNotif.addAll(object.getData());
                dataNotif.add(new Notification(0));
                adapterNotif.notifyDataSetChanged();

                for (Notification item: dataNotif) {
                    if(item.getId() != 0 && !item.isIs_read()) {
                        isNotificationRemaning = true;
                        break;
                    }
                }
                ivHaveNotif.setVisibility(isNotificationRemaning ? View.VISIBLE : View.INVISIBLE);
            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });

        viewModelNotif.getObjectResponse().observe(getViewLifecycleOwner(), object -> {
            if(object == null){
                alert.showAlert(getResources().getString(R.string.alertTitle), getResources().getString(R.string.alertDefault), R.drawable.ic_close);
                return;
            }
            if (object.getResult() == 1) {
                if(object.getData() == null){
                    ivHaveNotif.setVisibility(View.INVISIBLE);
                }

                FancyToast.makeText(getContext(), object.getMsg(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                        R.drawable.ic_check, true).show();

            } else {
                alert.showAlert(getResources().getString(R.string.alertTitle), object.getMsg(), R.drawable.ic_close);
            }
        });
    }


    /**
     * @author Phong-Kaster
     * @param fragment is the current context of the fragment  */
    private void setRecycleView(HomeFragment fragment) {
        /*Step 1*/
        adapter = new TransactionRecycleViewAdapter(fragment.getContext(), objects);
        recycleView.setAdapter(adapter);

        /*Step 2*/
        LinearLayoutManager manager = new LinearLayoutManager(fragment.getContext());
        recycleView.setLayoutManager(manager);

        adapterNotif = new NotificationRecycleViewAdapter(getContext(), dataNotif, dialog, viewModelNotif);
        rvNotifications.setAdapter(adapterNotif);

        LinearLayoutManager manager2 = new LinearLayoutManager(fragment.getContext());
        rvNotifications.setLayoutManager(manager2);

    }

    /**
     * @author Phong
     * listening event for every component.
     * */
    private void setEvent() {

        buttonTransaction.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), TransactionActivity.class);
            startActivity(intent);
        });

        buttonCategory.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), CategoriesActivity.class);
            startActivity(intent);
        });

        buttonButtonGoal.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), GoalActivity.class);
            startActivity(intent);
        });

        buttonBudget.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), BudgetsActivity.class);
            startActivity(intent);
        });

        seeAll.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), TransactionActivity.class);
            startActivity(intent);
        });

        ringBellBtn.setOnClickListener(view -> {
            isShow = !isShow;
            if(isShow){
                layoutNotif.setVisibility(View.VISIBLE);
                viewModelNotif.getData();
            }else{
                layoutNotif.setVisibility(View.INVISIBLE);
            }
        });


    }

    /**
     * @author Phong-Kaster
     * after receiving data, show them on the screen of Home Fragment
     * */
    private void setScreen()
    {
        String fullName = AuthUser.getFirstname() + " " + AuthUser.getLastname();
        name.setText(fullName);
    }
}