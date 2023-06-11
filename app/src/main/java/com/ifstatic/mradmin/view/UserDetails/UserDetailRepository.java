package com.ifstatic.mradmin.view.UserDetails;

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

public class UserDetailRepository {
    private DatabaseReference databaseBillingReference,databaseAdminReference;

    public UserDetailRepository() {
        databaseBillingReference = FirebaseHelper.getInstance().getDatabaseBillingReference();
        databaseAdminReference = FirebaseHelper.getInstance().getDatabaseAdminReference();
    }

    public MutableLiveData<List<RecentTransactionModel>> getRecentTransactionsFilteredFromServer(String username){

        MutableLiveData<List<RecentTransactionModel>> recentTransactionFilteredMutableLiveData = new MutableLiveData<>();
        databaseBillingReference.child("Transaction").limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<RecentTransactionModel> modelList = new ArrayList<>();
                if(snapshot.exists()){

                    for(DataSnapshot keySnapshot : snapshot.getChildren()){

                        RecentTransactionModel model = keySnapshot.getValue(RecentTransactionModel.class);
                        if (model.getParty().equals(username)){
                            modelList.add(model);}

                    }
                }
                recentTransactionFilteredMutableLiveData.setValue(modelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                recentTransactionFilteredMutableLiveData.setValue(null);
            }
        });
        return recentTransactionFilteredMutableLiveData;
    }

}
