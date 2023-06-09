package com.ifstatic.mradmin.view.Users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.adapter.UserAdapter;
import com.ifstatic.mradmin.databinding.ActivityUsersBinding;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.view.Dashboard.DashboardActivity;
import com.ifstatic.mradmin.view.UserDetails.UserDetailActivity;
import com.ifstatic.mradmin.view.createUser.CreateUserActivity;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    ActivityUsersBinding usersBinding;
    UserAdapter userAdapter;
    UserViewModel userViewModel;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersBinding = DataBindingUtil.setContentView(this, R.layout.activity_users);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setListners();
        setUserAdapter();


        if (AppBoiler.isInternetConnected(this)) {
            progressDialog = AppBoiler.setProgressDialog(this);
            getUsersFromServer();
        } else {
            AppBoiler.showSnackBarForInternet(this, usersBinding.getRoot());
        }
    }

    private void getUsersFromServer() {


        LiveData<List<UserModel>> usernamelist = userViewModel.getUserListFromRepository();

        usernamelist.observe(this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                if (userModels.size() == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(UsersActivity.this, "NO USERS FOUND", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressDialog.dismiss();
                }
                notifyUserAdapter(userModels);
            }
        });

    }


    private void setUserAdapter() {
        userAdapter = new UserAdapter(this);
        usersBinding.recyclerviewUsers.setAdapter(userAdapter);

        userAdapter.initItemClickListener(new UserAdapter.UserItemClickListener() {
            @Override
            public void onClickItem(int position, UserModel model) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("User_data",model);
                Toast.makeText(UsersActivity.this, "item clicked", Toast.LENGTH_SHORT).show();
                AppBoiler.navigateToActivity(UsersActivity.this, UserDetailActivity.class,bundle);

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setListners() {
        usersBinding.Userheader.titleTextView.setText("Users");
        usersBinding.createuserbutton.setOnClickListener(v->{
            AppBoiler.navigateToActivity(UsersActivity.this, CreateUserActivity.class,null);
        });
    }


    private void notifyUserAdapter(List<UserModel> userlist) {
        userAdapter.notifyListItemChanged(userlist);
    }

}