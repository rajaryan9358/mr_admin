package com.ifstatic.mradmin.view.TransactionDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TransactionDetailViewModel extends ViewModel {
    TransactionDetailRepository transactionDetailRepository=new TransactionDetailRepository();

    public LiveData<String> deleteTransactionResponseLiveData(String transactionID){
        return transactionDetailRepository.deleteTransactionFromServer(transactionID);
    }
}
