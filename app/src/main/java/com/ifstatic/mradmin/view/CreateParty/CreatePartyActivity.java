package com.ifstatic.mradmin.view.CreateParty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.databinding.ActivityCreatePartyBinding;
import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.utilities.Constants;

public class CreatePartyActivity extends AppCompatActivity {
    ActivityCreatePartyBinding createPartyBinding;
    CreatePartyViewModel partyViewModel;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPartyBinding= DataBindingUtil.setContentView(this,R.layout.activity_create_party);
        setinit();
        setListners();
    }

    @SuppressLint("SetTextI18n")
    private void setinit() {
        partyViewModel=new ViewModelProvider(this).get(CreatePartyViewModel.class);
        createPartyBinding.header.titleTextView.setText("Add Party");
    }

    private void setListners() {
        createPartyBinding.saveButton.setOnClickListener(v->{
            if (createPartyBinding.partyEditText.getText().toString().trim().isEmpty()){
                createPartyBinding.partyInputLayout.setError("Enter Party Name");
                createPartyBinding.partyEditText.requestFocus();
            }else if (createPartyBinding.addressEditText.getText().toString().trim().isEmpty()){
                createPartyBinding.addressInputLayout.setError("Enter Address");
                createPartyBinding.addressEditText.requestFocus();
            }else{
                if(AppBoiler.isInternetConnected(CreatePartyActivity.this)){
                    addPartyToFirebase();
                } else{
                    AppBoiler.showSnackBarForInternet(CreatePartyActivity.this,createPartyBinding.getRoot());
                }
            }
        });
    }

    private void addPartyToFirebase() {

        progressDialog = AppBoiler.setProgressDialog(this);

        PartyModel partyModel = new PartyModel(createPartyBinding.partyEditText.getText().toString(),createPartyBinding.addressEditText.getText().toString());
        LiveData<String> responseLiveData = partyViewModel.addPartyResponseLiveData(partyModel);

        responseLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();

                if(s.equals(Constants.SUCCESS)){
                    Toast.makeText(CreatePartyActivity.this, "Party Added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(CreatePartyActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}