package com.ifstatic.mradmin.view.EditUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;
import com.ifstatic.mradmin.utilities.Constants;

public class EditUserRepository {

    public MutableLiveData<String> addUserToServer(UserModel userModel, String id){
        MutableLiveData<String> responseintent = new MutableLiveData<>();
        DatabaseReference databaseReference = FirebaseHelper.getInstance().getDatabaseAdminReference();

        databaseReference.child("Users").child(id).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public MutableLiveData<String> deleteUserFromServer(String userid) {

        MutableLiveData<String> deleteresponseintent = new MutableLiveData<>();
        DatabaseReference databaseAdminReference = FirebaseHelper.getInstance().getDatabaseAdminReference();

        databaseAdminReference.child("Users")
                .child(userid) // Use the stored key to reference the specific child
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error==null){
                        deleteresponseintent.setValue(Constants.SUCCESS);
                    }else{
                        deleteresponseintent.setValue(Constants.FAILED);
                    }
                    }

                });
        return deleteresponseintent;
    }
}
