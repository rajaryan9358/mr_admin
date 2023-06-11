package com.ifstatic.mradmin.view.ViewAllParty;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ViewAllPartyRepository {

    private DatabaseReference databaseBillingReference, databaseAdminReference;

    public ViewAllPartyRepository() {
        databaseBillingReference = FirebaseHelper.getInstance().getDatabaseBillingReference();
        databaseAdminReference = FirebaseHelper.getInstance().getDatabaseAdminReference();
    }

    public MutableLiveData<List<PartyModel>> getMyPartiesFromServer(){

        MutableLiveData<List<PartyModel>> myPartiesMutableList = new MutableLiveData<>();

        Query query = databaseAdminReference.child("Party").orderByKey().limitToFirst(8);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<PartyModel> modelList = new ArrayList<>();

                if(snapshot.exists()){
                    for(DataSnapshot keySnapshot : snapshot.getChildren()){
                        PartyModel model = keySnapshot.getValue(PartyModel.class);
                        modelList.add(model);
                    }
                }
                myPartiesMutableList.setValue(modelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                myPartiesMutableList.setValue(null);
            }
        });

        return myPartiesMutableList;

    }
}
