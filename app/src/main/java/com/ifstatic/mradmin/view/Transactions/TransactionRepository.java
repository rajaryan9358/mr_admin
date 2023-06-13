package com.ifstatic.mradmin.view.Transactions;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    private DatabaseReference databaseBillingReference,databaseAdminReference;
    public TransactionRepository() {
        databaseBillingReference = FirebaseHelper.getInstance().getDatabaseBillingReference();
        databaseAdminReference = FirebaseHelper.getInstance().getDatabaseAdminReference();
    }

    public MutableLiveData<List<RecentTransactionModel>> getRecentTransactionsFromServer(String username,String startdate,String enddate){

        MutableLiveData<List<RecentTransactionModel>> recentTransactionMutableLiveData = new MutableLiveData<>();
        databaseBillingReference.child("Transaction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<RecentTransactionModel> modelList = new ArrayList<>();
                String uname,sdate,edate;
                if(snapshot.exists()){

                    for(DataSnapshot keySnapshot : snapshot.getChildren()){
                        RecentTransactionModel model = keySnapshot.getValue(RecentTransactionModel.class);
                        if (username.isEmpty()&&startdate.isEmpty()&&enddate.isEmpty()){
                        modelList.add(model);}
                        else if ((!username.isEmpty())&&startdate.isEmpty()&&enddate.isEmpty()){
                            uname=model.getParty();
                            if (uname.equals(username)){
                                modelList.add(model);
                            }
                        }else if (username.isEmpty()&&(!startdate.isEmpty())&&enddate.isEmpty()){
                            sdate=model.getDate();
                            if (startdate.compareTo(sdate)<=0){
                                modelList.add(model);
                            }
                        }else if (username.isEmpty()&&(!enddate.isEmpty())&&startdate.isEmpty()){
                            edate=model.getDate();
                                    if(enddate.compareTo(edate)>=0){
                                        modelList.add(model);
                                    }
                        }else if ((!username.isEmpty())&&(!startdate.isEmpty())&&enddate.isEmpty()){
                                uname=model.getParty();
                                sdate=model.getDate();
                                if (uname.equals(username)&&startdate.compareTo(sdate)<=0){
                                    modelList.add(model);
                                }
                        }else if ((!username.isEmpty())&&(!enddate.isEmpty())&&startdate.isEmpty()){
                            uname=model.getParty();
                            edate=model.getDate();
                            if (uname.equals(username)&&enddate.compareTo(edate)>=0){
                                modelList.add(model);
                            }

                        }else if ((!startdate.isEmpty())&&(!enddate.isEmpty())&&username.isEmpty()){
                            edate=model.getDate();
                            sdate=model.getDate();
                            if (enddate.compareTo(edate)>=0&&startdate.compareTo(sdate)<=0){
                                modelList.add(model);
                            }
                        }else if ((!startdate.isEmpty())&&(!enddate.isEmpty())&&(!username.isEmpty())){
                            uname=model.getParty();
                            sdate=model.getDate();
                            edate=model.getDate();
                            if (username.equals(uname)&&startdate.compareTo(sdate)<=0&&enddate.compareTo(edate)>=0){
                                modelList.add(model);
                            }
                        }
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
}
