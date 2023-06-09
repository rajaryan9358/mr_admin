package com.ifstatic.mradmin.view.Users;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private DatabaseReference databaseAdminReference;

    public UserRepository() {
         databaseAdminReference= FirebaseHelper.getInstance().getDatabaseAdminReference();
    }

    public MutableLiveData<List<UserModel>> getUserList(){
        MutableLiveData<List<UserModel>> userlist=new MutableLiveData<>();
        databaseAdminReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserModel> usernameList=new ArrayList<>();
                if (snapshot.exists()){
                    for (DataSnapshot keysnapshot: snapshot.getChildren()) {
                        UserModel user=keysnapshot.getValue(UserModel.class);
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
}
