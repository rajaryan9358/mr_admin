package com.ifstatic.mradmin.view.Transactions;

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
import com.ifstatic.mradmin.databinding.ActivityTransactionsBinding;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.view.Dashboard.DashboardActivity;
import com.ifstatic.mradmin.view.TransactionDetail.TransactionDetailActivity;

import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    ActivityTransactionsBinding transactionsBinding;
    RecentTransactionAdapter transactionAdapter;
    TransactionViewModel transactionViewModel;
    Dialog progressDialog;
    String currentMrNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionsBinding= DataBindingUtil.setContentView(this,R.layout.activity_transactions);
        transactionViewModel=new ViewModelProvider(this).get(TransactionViewModel.class);
        setRecentTransactionAdapter();

        setListners();

        if(AppBoiler.isInternetConnected(this)){

            progressDialog = AppBoiler.setProgressDialog(this);
            getRecentTransactionFromServer();
        } else {
            AppBoiler.showSnackBarForInternet(this,transactionsBinding.getRoot());
        }
    }

    @SuppressLint("SetTextI18n")
    private void setListners() {
        transactionsBinding.header.titleTextView.setText("Transactions");
    }

    private void getRecentTransactionFromServer(){

        LiveData<List<RecentTransactionModel>> recentTransactionModelLiveData = transactionViewModel.getRecentTransactionsFromRepository();

        recentTransactionModelLiveData.observe(this, new Observer<List<RecentTransactionModel>>() {
            @Override
            public void onChanged(List<RecentTransactionModel> recentTransactionModels) {
                if(recentTransactionModels == null){
                    Toast.makeText(TransactionsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
        transactionsBinding.transactionRecyclerview.setAdapter(transactionAdapter);


        transactionAdapter.initItemClickListener(new RecentTransactionAdapter.TransactionItemClickListner(){
            @Override
            public void onItemClicked(RecentTransactionModel transactionModel, int posittion) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("transaction_data",transactionModel);
                AppBoiler.navigateToActivity(TransactionsActivity.this, TransactionDetailActivity.class,null);
            }

        });

    }

    private void notifyRecentTransactionAdapter(List<RecentTransactionModel> transactionModelList){
        transactionAdapter.notifyListItemChanged(transactionModelList);
    }



}