package com.ifstatic.mradmin.view.EditTransaction;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;
import com.ifstatic.mradmin.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class EditTransactionRepository {
    DatabaseReference databaseBillingReference,databaseAdminReference;
    public EditTransactionRepository() {
        databaseBillingReference = FirebaseHelper.getInstance().getDatabaseBillingReference();
        databaseAdminReference = FirebaseHelper.getInstance().getDatabaseAdminReference();
    }


    public MutableLiveData<String> addTransactionToServer(RecentTransactionModel model,String transactionId) {

        MutableLiveData<String> responseMutableLiveData = new MutableLiveData<>();

        databaseBillingReference.child("Transaction").child(transactionId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseMutableLiveData.setValue(Constants.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseMutableLiveData.setValue(Constants.FAILED);
                System.out.println("======== ERROR ========= " + e.getMessage());
            }
        });
        return responseMutableLiveData;
    }

    public MutableLiveData<List<UserModel>> getUserNameList() {
        MutableLiveData<List<UserModel>> userlist = new MutableLiveData<>();
        databaseAdminReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserModel> usernameList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot keysnapshot : snapshot.getChildren()) {
                        UserModel user = keysnapshot.getValue(UserModel.class);
                        usernameList.add(user);
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
    public MutableLiveData<List<String>> getPartyNamelist() {
        MutableLiveData<List<String>> userlist = new MutableLiveData<>();
        databaseAdminReference.child("Party").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> usernameList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot keysnapshot : snapshot.getChildren()) {
                        PartyModel user = keysnapshot.getValue(PartyModel.class);
                        usernameList.add(user.getParty());
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
