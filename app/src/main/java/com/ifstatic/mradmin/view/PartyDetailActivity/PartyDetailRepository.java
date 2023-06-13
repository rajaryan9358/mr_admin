package com.ifstatic.mradmin.view.PartyDetailActivity;

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

public class PartyDetailRepository {
    private DatabaseReference databaseBillingReference;

    public PartyDetailRepository() {
        databaseBillingReference = FirebaseHelper.getInstance().getDatabaseBillingReference();
    }

    public MutableLiveData<List<RecentTransactionModel>> getTransactionFromServer(String partyName) {

        MutableLiveData<List<RecentTransactionModel>> transactionMutableLiveData = new MutableLiveData<>();

        databaseBillingReference.child("Transaction").orderByChild("party").equalTo(partyName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<RecentTransactionModel> modelList = new ArrayList<>();

                if (snapshot.exists()) {

                    for (DataSnapshot keySnapshot : snapshot.getChildren()) {

                        RecentTransactionModel model = keySnapshot.getValue(RecentTransactionModel.class);
                        modelList.add(model);
                    }
                    transactionMutableLiveData.setValue(modelList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("=========== ERROR ======== ");
                transactionMutableLiveData.setValue(null);
            }
        });
        return transactionMutableLiveData;
    }
}
