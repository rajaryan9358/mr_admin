package com.ifstatic.mradmin.view.createUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.databinding.ActivityCreateUserBinding;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.utilities.Constants;

import java.util.UUID;

public class CreateUserActivity extends AppCompatActivity {

    ActivityCreateUserBinding createUserBinding;
    private CreateUserViewModel createUserViewModel;
    private Dialog progressDialog;
    String textuserid,textusername,textuserpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createUserBinding= DataBindingUtil.setContentView(this,R.layout.activity_create_user);

        createUserViewModel=new ViewModelProvider(this).get(CreateUserViewModel.class);
        setinit();
        setlistners();
    }

    private void setlistners() {
        createUserBinding.createnewuserbutton.setOnClickListener(v->{
             if (createUserBinding.userName.getText().toString().trim().isEmpty()){
                createUserBinding.userName.setError("Enter User Name");
                createUserBinding.userName.requestFocus();
            }else if (createUserBinding.userPassword.getText().toString().trim().isEmpty()){
                createUserBinding.userPassword.setError("Enter Password");
                createUserBinding.userPassword.requestFocus();
            }else{

                if(AppBoiler.isInternetConnected(this)){
                    addUserToFirebase();
                } else{
                    AppBoiler.showSnackBarForInternet(this,createUserBinding.getRoot());
                }

            }
        });
    }

    private void addUserToFirebase() {

        progressDialog = AppBoiler.setProgressDialog(this);
        textusername=createUserBinding.userName.getText().toString();
        textuserpassword=createUserBinding.userPassword.getText().toString();
        UserModel user=new UserModel(textuserid,textusername,textuserpassword);
        LiveData<String> responseLiveData = createUserViewModel.addUserResponseLiveData(user,textuserid);

        responseLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();

                if(s.equals(Constants.SUCCESS)){
                    Toast.makeText(CreateUserActivity.this, "User Added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(CreateUserActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void setinit() {
        createUserBinding.createuserheader.titleTextView.setText("Create New User");
        textuserid= UUID.randomUUID().toString().substring(11);
        createUserBinding.userID.setText(textuserid);
    }



}