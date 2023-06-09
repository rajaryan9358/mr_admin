package com.ifstatic.mradmin.view.createUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.UserModel;

public class CreateUserViewModel extends ViewModel {

    private final CreateUserRepository createUserRepository=new CreateUserRepository();

    public LiveData<String> addUserResponseLiveData(UserModel userModel){
        return createUserRepository.addUserToServer(userModel);
    }
}
