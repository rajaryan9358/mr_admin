package com.ifstatic.mradmin.view.CreateParty;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.PartyModel;

public class CreatePartyViewModel extends ViewModel {

    private final AddPartyRepository repository=new AddPartyRepository();

    public LiveData<String> addPartyResponseLiveData(PartyModel model){
        return repository.addPartyToServer(model);
    }
}
