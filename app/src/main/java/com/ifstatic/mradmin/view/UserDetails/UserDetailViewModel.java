package com.ifstatic.mradmin.view.UserDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.RecentTransactionModel;

import java.util.List;

public class UserDetailViewModel extends ViewModel {

    UserDetailRepository userDetailRepository=new UserDetailRepository();
    public LiveData<List<RecentTransactionModel>> getRecentTransactionsFilteredFromRepository(String username){
        return userDetailRepository.getRecentTransactionsFilteredFromServer(username);
    }
}
