package com.ifstatic.mradmin.view.PartyDetailActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.RecentTransactionModel;

import java.util.List;

public class PartyDetailViewModel extends ViewModel {


    private PartyDetailRepository partyDetailRepository = new PartyDetailRepository();
    private MutableLiveData<List<RecentTransactionModel>> transactionListMutableLiveData;

    public LiveData<List<RecentTransactionModel>> getTransactionListFromRepository(String partyName) {

        if (transactionListMutableLiveData == null) {
            transactionListMutableLiveData = partyDetailRepository.getTransactionFromServer(partyName);
        }
        return transactionListMutableLiveData;
    }
}
