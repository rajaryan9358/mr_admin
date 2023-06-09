package com.ifstatic.mradmin.view.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.adapter.RecentTransactionAdapter;
import com.ifstatic.mradmin.databinding.ActivityDashboardBinding;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.view.TransactionDetail.TransactionDetailActivity;
import com.ifstatic.mradmin.view.Transactions.TransactionsActivity;
import com.ifstatic.mradmin.view.Users.UsersActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends AppCompatActivity{
    private ActivityDashboardBinding dashboardBinding;
    private DashBoardViewModel dashBoardViewModel;

    LiveData<List<String>> usernamelistLiveData;

    RecentTransactionAdapter transactionAdapter;
    private String currentMrNo ;
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding= DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        dashBoardViewModel=new ViewModelProvider(this).get(DashBoardViewModel.class);


        setListners();
        setinit();
        setRecentTransactionAdapter();


        if(AppBoiler.isInternetConnected(this)){

            progressDialog = AppBoiler.setProgressDialog(this);
            getRecentTransactionFromServer();
            getUserNameListFromServer();
//            getMyPartiesFromServer();

        } else {
            AppBoiler.showSnackBarForInternet(this,dashboardBinding.getRoot());
        }

    }


    private void getUserNameListFromServer() {

        usernamelistLiveData=dashBoardViewModel.getUserNameListFromRepository();

        usernamelistLiveData.observe(this, new Observer<List<String>>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onChanged(List<String> strings) {
                if (strings.size()==0){
                    Toast.makeText(DashboardActivity.this, "no data found for usernames", Toast.LENGTH_SHORT).show();
                }else if(strings.size()>0) {
//                    String [] username=strings.toArray(new String[strings.size()]);
//
//                    Toast.makeText(DashboardActivity.this, username[0]+""+strings.size(), Toast.LENGTH_SHORT).show();

                    String [] username=strings.toArray(new String[strings.size()]);
                    ArrayAdapter<String> usernameadapter = new ArrayAdapter<>(DashboardActivity.this, android.R.layout.simple_dropdown_item_1line, username);
                    dashboardBinding.usernamedropdown.setAdapter(usernameadapter);

                    dashboardBinding.usernamedropdown.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            dashboardBinding.usernamedropdown.showDropDown();
                            return false;
                        }
                    });
                }

            }
        });
    }

    private void setinit() {
    }

    private void setListners() {

        dashboardBinding.usernamedropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()){
                    getRecentTransactionFromServer();
                }else{
                    String username=editable.toString();
                    getRecentTransactionFilteredFromServer(username);
                }
            }
        });

        dashboardBinding.headingUsers.setOnClickListener(v->{
            AppBoiler.navigateToActivity(DashboardActivity.this, UsersActivity.class,null);
        });

        dashboardBinding.headingSeeAll.setOnClickListener(v->{
            AppBoiler.navigateToActivity(DashboardActivity.this, TransactionsActivity.class,null);
        });

    }


    private void getRecentTransactionFromServer(){

        LiveData<List<RecentTransactionModel>> recentTransactionModelLiveData = dashBoardViewModel.getRecentTransactionsFromRepository();

        recentTransactionModelLiveData.observe(this, new Observer<List<RecentTransactionModel>>() {
            @Override
            public void onChanged(List<RecentTransactionModel> recentTransactionModels) {
                if(recentTransactionModels == null){
                    Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(recentTransactionModels.size() > 0){
                    progressDialog.dismiss();
                    currentMrNo = recentTransactionModels.get(recentTransactionModels.size()-1).getMrNo();
                    System.out.println("================= CURRENT MR ========= "+currentMrNo);
                } else {
                }
                notifyRecentTransactionAdapter(recentTransactionModels);
            }
        });

    }

    private void getRecentTransactionFilteredFromServer(String username){

        LiveData<List<RecentTransactionModel>> recentTransactionModelLiveData = dashBoardViewModel.getRecentTransactionsFilteredFromRepository(username);

        recentTransactionModelLiveData.observe(this, new Observer<List<RecentTransactionModel>>() {
            @Override
            public void onChanged(List<RecentTransactionModel> recentTransactionModels) {
                if(recentTransactionModels == null){
                    Toast.makeText(DashboardActivity.this, "Incorrect UserName", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(recentTransactionModels.size() > 0){
                    progressDialog.dismiss();
                    currentMrNo = recentTransactionModels.get(recentTransactionModels.size()-1).getMrNo();
                    System.out.println("================= CURRENT MR ========= "+currentMrNo);
                } else {
                }
                notifyRecentTransactionAdapter(recentTransactionModels);
            }
        });

    }


    private void setRecentTransactionAdapter() {

        transactionAdapter = new RecentTransactionAdapter(this);
        dashboardBinding.recenttransactioRecyclerView.setAdapter(transactionAdapter);

        transactionAdapter.initItemClickListener(new RecentTransactionAdapter.TransactionItemClickListner(){
            @Override
            public void onItemClicked(RecentTransactionModel transactionModel, int posittion) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("transaction_data",transactionModel);
                AppBoiler.navigateToActivity(DashboardActivity.this, TransactionDetailActivity.class,null);
            }

        });
    }


    private void notifyRecentTransactionAdapter(List<RecentTransactionModel> transactionModelList){
        transactionAdapter.notifyListItemChanged(transactionModelList);
    }

}