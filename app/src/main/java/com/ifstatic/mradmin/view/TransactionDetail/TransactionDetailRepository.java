package com.ifstatic.mradmin.view.TransactionDetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.ifstatic.mradmin.repositories.remote.FirebaseHelper;
import com.ifstatic.mradmin.utilities.Constants;

public class TransactionDetailRepository {
    public MutableLiveData<String> deleteTransactionFromServer(String transactionid) {

        MutableLiveData<String> deleteresponseintent = new MutableLiveData<>();
        DatabaseReference databaseBillingReference = FirebaseHelper.getInstance().getDatabaseBillingReference();

        databaseBillingReference.child("Transaction")
                .child(transactionid) // Use the stored key to reference the specific child
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
