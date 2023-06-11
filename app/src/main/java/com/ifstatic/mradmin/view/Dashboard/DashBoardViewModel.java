package com.ifstatic.mradmin.view.Dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.models.RecentTransactionModel;

import java.util.List;

public class DashBoardViewModel extends ViewModel {

    private final DashBoardRepository dashboardrepository=new DashBoardRepository();

    MutableLiveData<Integer> totalTransactions=new MutableLiveData<>();
    MutableLiveData<Float> totalAmount=new MutableLiveData<>();

    float amount,count;

    private LiveData<List<PartyModel>> myPartiesListLiveData;


    public LiveData<List<RecentTransactionModel>> getRecentTransactionsFromRepository(){
        return dashboardrepository.getRecentTransactionsFromServer();
    }
    public LiveData<List<RecentTransactionModel>> getRecentTransactionsFilteredFromRepository(String username){
        return dashboardrepository.getRecentTransactionsFilteredFromServer(username);
    }

    public  LiveData<List<String>> getUserNameListFromRepository(){
        return dashboardrepository.getUserNameList();
    }


    public LiveData<List<PartyModel>> getPartiesModelListFromRepository(){

        if(myPartiesListLiveData == null){
            myPartiesListLiveData =  dashboardrepository.getMyPartiesFromServer();
        }
        return myPartiesListLiveData;
    }

    public LiveData<List<Float>> getCardDetailsFromRepository(String date){
        return dashboardrepository.getcardDetailsfromserver(date);
    }




}
