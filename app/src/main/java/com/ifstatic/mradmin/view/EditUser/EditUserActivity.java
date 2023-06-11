package com.ifstatic.mradmin.view.EditUser;

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
import com.ifstatic.mradmin.databinding.ActivityCreateUserBinding;
import com.ifstatic.mradmin.databinding.ActivityEditUserBinding;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.utilities.Constants;
import com.ifstatic.mradmin.view.createUser.CreateUserActivity;
import com.ifstatic.mradmin.view.createUser.CreateUserViewModel;

import java.util.UUID;

public class EditUserActivity extends AppCompatActivity {
    ActivityEditUserBinding userBinding;
    private EditUserViewModel userViewModel;
    private Dialog progressDialog;
    String textuserid,textusername,textuserpassword;
    UserModel userModel=new UserModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBinding= DataBindingUtil.setContentView(this,R.layout.activity_edit_user);

        getBundles();
        setinit();
        setListners();
    }

    private void setListners() {
        userBinding.saveUserButton.setOnClickListener(c->{
            if (userBinding.edituserName.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Enter Valid UserName", Toast.LENGTH_SHORT).show();
            }else if(userBinding.edituserPassword.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
            }else{

                if(AppBoiler.isInternetConnected(this)){
                    addUserToFirebase();
                } else{
                    AppBoiler.showSnackBarForInternet(this,userBinding.getRoot());
                }

            }
        });

        userBinding.delteUserButton.setOnClickListener(v->{
            if(AppBoiler.isInternetConnected(this)){
                deleteUser(textuserid);
            } else{
                AppBoiler.showSnackBarForInternet(this,userBinding.getRoot());
            }

        });
    }

    @SuppressLint("SetTextI18n")
    private void setinit() {
        userViewModel=new ViewModelProvider(this).get(EditUserViewModel.class);
        userBinding.header.titleTextView.setText("Edit User");
    }

    private void addUserToFirebase() {

        progressDialog = AppBoiler.setProgressDialog(this);
        textusername=userBinding.edituserName.getText().toString();
        textuserpassword=userBinding.edituserPassword.getText().toString();
        UserModel user=new UserModel(textuserid,textusername,textuserpassword);
        LiveData<String> responseLiveData = userViewModel.addUserResponseLiveData(user,textuserid);

        responseLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();

                if(s.equals(Constants.SUCCESS)){
                    Toast.makeText(EditUserActivity.this, "User Added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(EditUserActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void deleteUser(String textuserid){
        progressDialog=AppBoiler.setProgressDialog(this);
        LiveData<String> responsedeleteUser = userViewModel.deleteUserResponseLiveData(textuserid);
        responsedeleteUser.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();
                if (s.equals(Constants.SUCCESS)){
                    Toast.makeText(EditUserActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    Toast.makeText(EditUserActivity.this, "Failed Deletion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        userModel=bundle.getParcelable("Users");
        if (userModel!=null) {
            Toast.makeText(this, "filled data", Toast.LENGTH_SHORT).show();
            userBinding.setUser(userModel);
            textuserid=userModel.getUsername();
        }else{
            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
        }
    }
}