package com.ifstatic.mradmin.view.Transactions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.RecentTransactionModel;

import java.util.List;

public class TransactionViewModel extends ViewModel {
    TransactionRepository transactionRepository=new TransactionRepository();

    public LiveData<List<RecentTransactionModel>> getRecentTransactionsFromRepository(String username,String startdate,String enddate){
        return transactionRepository.getRecentTransactionsFromServer(username,startdate,enddate);
    }

    public  LiveData<List<String>> getUserNameListFromRepository(){
        return transactionRepository.getUserNameList();
    }

}
