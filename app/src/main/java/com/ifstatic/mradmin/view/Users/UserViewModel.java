package com.ifstatic.mradmin.view.Users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.UserModel;

import java.util.List;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository=new UserRepository();

    public LiveData<List<UserModel>> getUserListFromRepository(){
        return userRepository.getUserList();
    }
}
