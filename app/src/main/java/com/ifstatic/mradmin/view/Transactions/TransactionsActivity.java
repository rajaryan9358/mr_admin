package com.ifstatic.mradmin.view.Transactions;

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
import android.widget.Toast;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.adapter.RecentTransactionAdapter;
import com.ifstatic.mradmin.databinding.ActivityTransactionsBinding;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.utilities.DateFormat;
import com.ifstatic.mradmin.view.Dashboard.DashboardActivity;
import com.ifstatic.mradmin.view.TransactionDetail.TransactionDetailActivity;

import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    ActivityTransactionsBinding transactionsBinding;
    RecentTransactionAdapter transactionAdapter;
    TransactionViewModel transactionViewModel;
    Dialog progressDialog;
    String currentMrNo,startdate="",enddate="",username="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionsBinding= DataBindingUtil.setContentView(this,R.layout.activity_transactions);
        transactionViewModel=new ViewModelProvider(this).get(TransactionViewModel.class);
        setRecentTransactionAdapter();

        setListners();

        if(AppBoiler.isInternetConnected(this)){

            progressDialog = AppBoiler.setProgressDialog(this);
            getRecentTransactionFromServer(username,startdate,enddate);
        } else {
            AppBoiler.showSnackBarForInternet(this,transactionsBinding.getRoot());
        }
    }

    @SuppressLint("SetTextI18n")
    private void setListners() {
        transactionsBinding.header.titleTextView.setText("Transactions");
        transactionsBinding.header.backImageView.setOnClickListener(v->{
            onBackPressed();
        });
        transactionsBinding.startdate.setOnClickListener(v->{
            DateFormat.getDateFromCalender(TransactionsActivity.this, new DateFormat.DateSelectListener() {
                @Override
                public void onSelectedDate(String date) {
                    transactionsBinding.startdate.setText(date);
                    username=transactionsBinding.usernamedropdown.getText().toString();
                    startdate=transactionsBinding.startdate.getText().toString();
                    enddate=transactionsBinding.enddate.getText().toString();
                    getRecentTransactionFromServer(username,date,enddate);
                }
            });
        });

        transactionsBinding.enddate.setOnClickListener(v->{
            DateFormat.getDateFromCalender(TransactionsActivity.this, new DateFormat.DateSelectListener() {
                @Override
                public void onSelectedDate(String date) {
                    transactionsBinding.enddate.setText(date);
                    username=transactionsBinding.usernamedropdown.getText().toString();
                    startdate=transactionsBinding.startdate.getText().toString();
                    enddate=transactionsBinding.enddate.getText().toString();
                    getRecentTransactionFromServer(username,startdate,date);
                }
            });
        });

        transactionsBinding.usernamedropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                username=transactionsBinding.usernamedropdown.getText().toString();
                startdate=transactionsBinding.startdate.getText().toString();
                enddate=transactionsBinding.enddate.getText().toString();
                getRecentTransactionFromServer(username,startdate,enddate);

            }
        });

        transactionsBinding.startdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username=transactionsBinding.usernamedropdown.getText().toString();
                startdate=transactionsBinding.startdate.getText().toString();
                enddate=transactionsBinding.enddate.getText().toString();
                getRecentTransactionFromServer(username,startdate,enddate);
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                username=transactionsBinding.usernamedropdown.getText().toString();
//                startdate=transactionsBinding.startdate.getText().toString();
//                enddate=transactionsBinding.enddate.getText().toString();
//                getRecentTransactionFromServer(username,startdate,enddate);
            }
        });
        transactionsBinding.enddate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username=transactionsBinding.usernamedropdown.getText().toString();
                startdate=transactionsBinding.startdate.getText().toString();
                enddate=transactionsBinding.enddate.getText().toString();
                getRecentTransactionFromServer(username,startdate,enddate);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                username=transactionsBinding.usernamedropdown.getText().toString();
//                startdate=transactionsBinding.startdate.getText().toString();
//                enddate=transactionsBinding.enddate.getText().toString();
//                getRecentTransactionFromServer(username,startdate,enddate);
            }
        });
    }

    private void getRecentTransactionFromServer(String username,String startdate,String enddate){

        LiveData<List<RecentTransactionModel>> recentTransactionModelLiveData = transactionViewModel.getRecentTransactionsFromRepository(username,startdate,enddate);

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
                AppBoiler.navigateToActivity(TransactionsActivity.this, TransactionDetailActivity.class,bundle);
            }

        });

    }

    private void notifyRecentTransactionAdapter(List<RecentTransactionModel> transactionModelList){
        transactionAdapter.notifyListItemChanged(transactionModelList);
    }

}