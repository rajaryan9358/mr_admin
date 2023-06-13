package com.ifstatic.mradmin.view.ViewAllParty;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ifstatic.mradmin.models.PartyModel;

import java.util.List;

public class ViewAllPartyViewModel extends ViewModel {

        private ViewAllPartyRepository viewAllPartyRepository=new ViewAllPartyRepository();

        private LiveData<List<PartyModel>> myPartiesListLiveData;


        public LiveData<List<PartyModel>> getPartiesModelListFromRepository(String partyname){

                if(myPartiesListLiveData == null){
                        myPartiesListLiveData =  viewAllPartyRepository.getMyPartiesFromServer(partyname);
                }
                return myPartiesListLiveData;
        }


}
