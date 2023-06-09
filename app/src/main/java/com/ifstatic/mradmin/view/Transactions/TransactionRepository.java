package com.ifstatic.mradmin.view.Transactions;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    private DatabaseReference databaseBillingReference,databaseAdminReference;
    public TransactionRepository() {
        databaseBillingReference = FirebaseHelper.getInstance().getDatabaseBillingReference();
        databaseAdminReference = FirebaseHelper.getInstance().getDatabaseAdminReference();
    }

    public MutableLiveData<List<RecentTransactionModel>> getRecentTransactionsFromServer(){

        MutableLiveData<List<RecentTransactionModel>> recentTransactionMutableLiveData = new MutableLiveData<>();
        databaseBillingReference.child("Transaction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<RecentTransactionModel> modelList = new ArrayList<>();
                if(snapshot.exists()){

                    for(DataSnapshot keySnapshot : snapshot.getChildren()){
                        RecentTransactionModel model = keySnapshot.getValue(RecentTransactionModel.class);
                        modelList.add(model);
                    }
                }
                recentTransactionMutableLiveData.setValue(modelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                recentTransactionMutableLiveData.setValue(null);
            }
        });
        return recentTransactionMutableLiveData;
    }
}
