package com.ifstatic.mradmin.view.PartyDetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.adapter.RecentTransactionAdapter;
import com.ifstatic.mradmin.databinding.ActivityPartyDetailBinding;
import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.models.RecentTransactionModel;

import java.util.List;

public class PartyDetailActivity extends AppCompatActivity {


    private ActivityPartyDetailBinding partybinding;

    private PartyModel partyModel;
    private PartyDetailViewModel partyDetailViewModel;
    private RecentTransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partybinding=DataBindingUtil.setContentView(this,R.layout.activity_party_detail);
        initListeners();
        getBundles();
        initViews();
        getTransactionOfPartyFromViewModel();
        setAdapterForTransaction();
    }


    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            partyModel = bundle.getParcelable("party_data");
        }
    }

    private void initViews() {
        partyDetailViewModel = new ViewModelProvider(this).get(PartyDetailViewModel.class);

        partybinding.header.titleTextView.setText("Party Details");
        partybinding.addressTextView.setText(partyModel.getAddress());
        partybinding.partyNameTextView.setText(partyModel.getParty());

//        partybinding.headerRecentTransaction.partyTextView.setVisibility(View.GONE);
    }

    private void initListeners() {

        partybinding.header.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getTransactionOfPartyFromViewModel() {

        LiveData<List<RecentTransactionModel>> transactionLiveData = partyDetailViewModel.getTransactionListFromRepository(partyModel.getParty());
        transactionLiveData.observe(this, new Observer<List<RecentTransactionModel>>() {
            @Override
            public void onChanged(List<RecentTransactionModel> transactionModelList) {
                notifyAdapter(transactionModelList);
            }
        });
    }

    private void setAdapterForTransaction() {
        transactionAdapter = new RecentTransactionAdapter(this);
        partybinding.partyDetailRecyclerView.setAdapter(transactionAdapter);
    }

    private void notifyAdapter(List<RecentTransactionModel> transactionModelList) {
        transactionAdapter.notifyListItemChanged(transactionModelList);
    }
}