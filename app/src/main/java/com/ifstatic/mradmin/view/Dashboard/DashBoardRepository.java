package com.ifstatic.mradmin.view.Dashboard;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DashBoardRepository {

    private DatabaseReference databaseBillingReference, databaseAdminReference;

    public DashBoardRepository() {
        databaseBillingReference = FirebaseHelper.getInstance().getDatabaseBillingReference();
        databaseAdminReference = FirebaseHelper.getInstance().getDatabaseAdminReference();

    }

    public MutableLiveData<List<RecentTransactionModel>> getRecentTransactionsFromServer() {

        MutableLiveData<List<RecentTransactionModel>> recentTransactionMutableLiveData = new MutableLiveData<>();
        databaseBillingReference.child("Transaction").limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<RecentTransactionModel> modelList = new ArrayList<>();
                if (snapshot.exists()) {

                    for (DataSnapshot keySnapshot : snapshot.getChildren()) {
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

    public MutableLiveData<List<RecentTransactionModel>> getRecentTransactionsFilteredFromServer(String username) {

        MutableLiveData<List<RecentTransactionModel>> recentTransactionFilteredMutableLiveData = new MutableLiveData<>();
        databaseBillingReference.child("Transaction").limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<RecentTransactionModel> modelList = new ArrayList<>();
                if (snapshot.exists()) {

                    for (DataSnapshot keySnapshot : snapshot.getChildren()) {
                        RecentTransactionModel model = keySnapshot.getValue(RecentTransactionModel.class);
                        if (model.getParty().equals(username)) {
                            modelList.add(model);
                        }
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


    public MutableLiveData<List<String>> getUserNameList() {
        MutableLiveData<List<String>> userlist = new MutableLiveData<>();
        databaseAdminReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> usernameList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot keysnapshot : snapshot.getChildren()) {
                        UserModel user = keysnapshot.getValue(UserModel.class);
                        usernameList.add(user.getUsername());
                    }
                }
                userlist.setValue(usernameList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userlist;
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

    public MutableLiveData<List<Float>> getcardDetailsfromserver(String date) {

        MutableLiveData<List<RecentTransactionModel>> recentTransactionMutableLiveData = new MutableLiveData<>();
        MutableLiveData<List<Float>> count=new MutableLiveData<>();
        databaseBillingReference.child("Transaction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Float> list=new ArrayList<>();
                List<RecentTransactionModel> modelList = new ArrayList<>();
                if (snapshot.exists()) {
                        float tamount=0;
                    for (DataSnapshot keySnapshot : snapshot.getChildren()) {
                        RecentTransactionModel model = keySnapshot.getValue(RecentTransactionModel.class);
                        if (model.getDate().equals(date)){
                            tamount= Float.parseFloat(model.getAmount())+tamount;
                        modelList.add(model);}

                    }
                list.add(tamount);
                    list.add((float) modelList.size());
                }

                count.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                recentTransactionMutableLiveData.setValue(null);
            }
        });
        return count;
    }


}
