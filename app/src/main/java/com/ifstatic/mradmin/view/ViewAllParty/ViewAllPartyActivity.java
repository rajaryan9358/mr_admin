package com.ifstatic.mradmin.view.ViewAllParty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.adapter.PartyAdapter;
import com.ifstatic.mradmin.databinding.ActivityViewAllPartyBinding;
import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.view.CreateParty.CreatePartyActivity;
import com.ifstatic.mradmin.view.Dashboard.DashboardActivity;
import com.ifstatic.mradmin.view.PartyDetailActivity.PartyDetailActivity;

import java.util.List;

public class ViewAllPartyActivity extends AppCompatActivity {
    
    ActivityViewAllPartyBinding viewAllPartyBinding;
    private List<PartyModel> partyModelList;
    private PartyAdapter partyAdapter;
    private ViewAllPartyViewModel partyViewModel;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewAllPartyBinding= DataBindingUtil.setContentView(this,R.layout.activity_view_all_party);
     
                setinit();
                setListners();
        setMyPartiesAdapter();
        if(AppBoiler.isInternetConnected(this)){

            progressDialog = AppBoiler.setProgressDialog(this);
            getMyPartiesFromServer("");

        } else {
            AppBoiler.showSnackBarForInternet(this,viewAllPartyBinding.getRoot());
        }
    }

    private void setListners() {
        viewAllPartyBinding.createparty.setOnClickListener(v->{
            AppBoiler.navigateToActivity(ViewAllPartyActivity.this, CreatePartyActivity.class,null);
        });
    }

    @SuppressLint("SetTextI18n")
    private void setinit() {
        partyViewModel=new ViewModelProvider(this).get(ViewAllPartyViewModel.class);
        viewAllPartyBinding.header.titleTextView.setText("All Parties");
        viewAllPartyBinding.header.backImageView.setOnClickListener(v->{
            onBackPressed();
        });

        viewAllPartyBinding.searchPartyNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String partyname=editable.toString();
                getMyPartiesFromServer(partyname);
            }
        });
    }

    private void setMyPartiesAdapter(){

        partyAdapter = new PartyAdapter(this);
        viewAllPartyBinding.myPartiesRecyclerView.setAdapter(partyAdapter);

        partyAdapter.initItemClickListener(new PartyAdapter.PartyItemClickListener() {
            @Override
            public void onClickItem(int position, PartyModel model) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("party_data",model);
                AppBoiler.navigateToActivity(ViewAllPartyActivity.this, PartyDetailActivity.class,bundle);
            }
        });
    }

    private void getMyPartiesFromServer(String partyname){

        LiveData<List<PartyModel>> myPartiesModelListLiveData = partyViewModel.getPartiesModelListFromRepository(partyname);
        myPartiesModelListLiveData.observe(this, new Observer<List<PartyModel>>() {
            @Override
            public void onChanged(List<PartyModel> partyModels) {

                progressDialog.dismiss();

//                if(partyModels.size() >0){
//                    viewAllPartyBinding.viewAllPartyTextView.setVisibility(View.VISIBLE);
//                } else {
//                    viewAllPartyBinding.viewAllPartyTextView.setVisibility(View.GONE);
//                }
                partyModelList = partyModels;
                notifyPartiesAdapter();
            }
        });
    }


    private void notifyPartiesAdapter() {
        partyAdapter.notifyListIsChanged(partyModelList);
    }

}