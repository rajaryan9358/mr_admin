package com.ifstatic.mradmin.view.EditTransaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.models.UserModel;

import java.util.List;

public class EditTransactionViewModel extends ViewModel {
    EditTransactionRepository editTransactionRepository=new EditTransactionRepository();


    public LiveData<String> createTransactionToServer(RecentTransactionModel model,String transactionid) {
        return editTransactionRepository.addTransactionToServer(model,transactionid);
    }
//
//    public LiveData<String> updateTransactionToServer(RecentTransactionModel model,String transactionid) {
//        return editTransactionRepository.updateTransactionToServer(model,transactionid);
//    }

    public  LiveData<List<UserModel>> getUserNameListFromRepository(){
        return editTransactionRepository.getUserNameList();
    }

    public  LiveData<List<String>> getPartyNameListFromRepository(){
        return editTransactionRepository.getPartyNamelist();
    }

}
