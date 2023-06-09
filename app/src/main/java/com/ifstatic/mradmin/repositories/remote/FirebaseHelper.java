package com.ifstatic.mradmin.repositories.remote;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ifstatic.mradmin.utilities.Constants;

public class FirebaseHelper {

    private static FirebaseHelper firebaseHelper;
    private DatabaseReference databaseAdminReference,databaseBillingReference;
    private StorageReference storageReference;

    private FirebaseHelper(){
        databaseAdminReference = FirebaseDatabase.getInstance().getReference(Constants.ADMIN_APP_NAME);
        databaseBillingReference = FirebaseDatabase.getInstance().getReference(Constants.BILLING_APP_NAME);
        storageReference = FirebaseStorage.getInstance().getReference(Constants.BILLING_APP_NAME);
    }

    public static FirebaseHelper getInstance(){
        if(firebaseHelper == null){
            firebaseHelper = new FirebaseHelper();
        }
        return firebaseHelper;
    }

    public DatabaseReference getDatabaseAdminReference() {
        return databaseAdminReference;
    }

    public DatabaseReference getDatabaseBillingReference() {
        return databaseBillingReference;
    }

    public StorageReference getStorageReference(){
        return storageReference;
    }
}
