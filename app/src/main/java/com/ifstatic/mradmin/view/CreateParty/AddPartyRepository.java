package com.ifstatic.mradmin.view.CreateParty;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;
import com.ifstatic.mradmin.utilities.Constants;

public class AddPartyRepository {
    public MutableLiveData<String> addPartyToServer(PartyModel model){

        MutableLiveData<String> responseMutableLiveData = new MutableLiveData<>();
        DatabaseReference databaseAdminReference = FirebaseHelper.getInstance().getDatabaseAdminReference();

        databaseAdminReference.child("Party").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseMutableLiveData.setValue(Constants.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseMutableLiveData.setValue(Constants.FAILED);
                System.out.println("======== ERROR ========= "+e.getMessage());
            }
        });
        return responseMutableLiveData;
    }
}
