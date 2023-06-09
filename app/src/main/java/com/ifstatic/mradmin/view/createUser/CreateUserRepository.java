package com.ifstatic.mradmin.view.createUser;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;
import com.ifstatic.mradmin.utilities.Constants;

public class CreateUserRepository {

    public MutableLiveData<String> addUserToServer(UserModel userModel){
        MutableLiveData<String> responseintent = new MutableLiveData<>();
        DatabaseReference databaseReference = FirebaseHelper.getInstance().getDatabaseAdminReference();

        databaseReference.child("Users").push().setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseintent.setValue(Constants.SUCCESS);
                System.out.println("===========SUCCESS USER CREATED ===============");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseintent.setValue(Constants.FAILED);
                System.out.println("======== ERROR ========= "+e.getMessage());
            }
        });
        return responseintent;
    }
    }

