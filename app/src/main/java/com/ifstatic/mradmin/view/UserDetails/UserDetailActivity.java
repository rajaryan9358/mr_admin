package com.ifstatic.mradmin.view.UserDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.adapter.RecentTransactionAdapter;
import com.ifstatic.mradmin.databinding.ActivityUserDetailBinding;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.view.Dashboard.DashboardActivity;
import com.ifstatic.mradmin.view.EditUser.EditUserActivity;
import com.ifstatic.mradmin.view.TransactionDetail.TransactionDetailActivity;

import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    ActivityUserDetailBinding userDetailBinding;
    UserDetailViewModel userDetailViewModel;
    private Dialog progressDialog;
    UserModel userModel;
    String username,currentMrNo;
    RecentTransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDetailBinding= DataBindingUtil.setContentView(this,R.layout.activity_user_detail);
        userDetailViewModel=new ViewModelProvider(this).get(UserDetailViewModel.class);

        getBundles();
        setListners();
        setRecentTransactionAdapter();
        initView();
        if(AppBoiler.isInternetConnected(this)){

            progressDialog = AppBoiler.setProgressDialog(this);
            getRecentTransactionFilteredFromServer(username);

        } else {
            AppBoiler.showSnackBarForInternet(this,userDetailBinding.getRoot());
        }

    }

    private void initView() {
        userDetailBinding.setUser(userModel);
    }

    @SuppressLint("SetTextI18n")
    private void setListners() {
        userDetailBinding.header.titleTextView.setText("User Details");
        userDetailBinding.textEdit.setOnClickListener(c->{
            Bundle bundle1=new Bundle();
//            bundle.putString("userID",userModel.getUserID());
            bundle1.putParcelable("Users",userModel);
            Toast.makeText(this, userModel.getUsername(), Toast.LENGTH_SHORT).show();

            AppBoiler.navigateToActivity(UserDetailActivity.this, EditUserActivity.class,bundle1);

        });
    }

    private void getRecentTransactionFilteredFromServer(String username){

        LiveData<List<RecentTransactionModel>> recentTransactionModelLiveData = userDetailViewModel.getRecentTransactionsFilteredFromRepository(username);

        recentTransactionModelLiveData.observe(this, new Observer<List<RecentTransactionModel>>() {
            @Override
            public void onChanged(List<RecentTransactionModel> recentTransactionModels) {
                if(recentTransactionModels == null){
                    Toast.makeText(UserDetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                } else
                if(recentTransactionModels.size() > 0){
                    progressDialog.dismiss();
                    currentMrNo = recentTransactionModels.get(recentTransactionModels.size()-1).getMrNo();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UserDetailActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
                notifyRecentTransactionAdapter(recentTransactionModels);
            }
        });

    }

    private void setRecentTransactionAdapter() {

        transactionAdapter = new RecentTransactionAdapter(this);
        userDetailBinding.userTransactionRecyclerView.setAdapter(transactionAdapter);

        transactionAdapter.initItemClickListener(new RecentTransactionAdapter.TransactionItemClickListner(){
            @Override
            public void onItemClicked(RecentTransactionModel transactionModel, int posittion) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("transaction_data",transactionModel);
                AppBoiler.navigateToActivity(UserDetailActivity.this, TransactionDetailActivity.class,bundle);
            }

        });
    }

    private void notifyRecentTransactionAdapter(List<RecentTransactionModel> transactionModelList){
        transactionAdapter.notifyListItemChanged(transactionModelList);
    }
    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userModel = bundle.getParcelable("user_data");
            username=userModel.getUsername();
        }else if (bundle==null){
            Toast.makeText(this, "No Data Associated", Toast.LENGTH_SHORT).show();
        }
    }


}