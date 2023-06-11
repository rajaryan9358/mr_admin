package com.ifstatic.mradmin.view.EditUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.UserModel;

public class EditUserViewModel extends ViewModel {
    EditUserRepository editUserRepository=new EditUserRepository();

    public LiveData<String> addUserResponseLiveData(UserModel userModel, String id){
        return editUserRepository.addUserToServer(userModel,id);
    }

    public LiveData<String> deleteUserResponseLiveData(String userid){
        return editUserRepository.deleteUserFromServer(userid);
    }
}
