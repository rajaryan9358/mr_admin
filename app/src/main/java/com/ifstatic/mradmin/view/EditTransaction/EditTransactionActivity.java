package com.ifstatic.mradmin.view.EditTransaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ifstatic.mradmin.R;
import com.ifstatic.mradmin.databinding.ActivityEditTransactionBinding;
import com.ifstatic.mradmin.models.ChequeDataModel;
import com.ifstatic.mradmin.models.OnlineDetailModel;
import com.ifstatic.mradmin.models.PartyModel;
import com.ifstatic.mradmin.models.RecentTransactionModel;
import com.ifstatic.mradmin.models.UpiDetailModel;
import com.ifstatic.mradmin.models.UserModel;
import com.ifstatic.mradmin.utilities.AppBoiler;
import com.ifstatic.mradmin.utilities.Constants;
import com.ifstatic.mradmin.utilities.DateFormat;
import com.ifstatic.mradmin.view.Dashboard.DashboardActivity;

import java.lang.invoke.MutableCallSite;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class EditTransactionActivity extends AppCompatActivity {
    ActivityEditTransactionBinding editTransactionBinding;
    String paymentMode;
    ChequeDataModel chequeDataModel=new ChequeDataModel();
    OnlineDetailModel onlineDetailModel=new OnlineDetailModel();
    UpiDetailModel upiDetailModel=new UpiDetailModel();
    String transactionId=null,UserId;
    Dialog progressDialog;
    boolean mode=false;
    RecentTransactionModel transactionModel=new RecentTransactionModel();
    EditTransactionViewModel transactionViewModel;
    LiveData<List<UserModel>> usernamelistLiveData;
    LiveData<List<String>> partylistLiveData;
    List<UserModel> userslist=new ArrayList<>();
    String [] username ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editTransactionBinding= DataBindingUtil.setContentView(this,R.layout.activity_edit_transaction);
        mode= getBundles();
        setinit();
        setListners();

        getUserNameListFromServer();
        getPartyNameListFromServer();
    }

        private boolean getBundles() {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                transactionModel = bundle.getParcelable("transaction_data");
                editTransactionBinding.partynamedropdown.setText(transactionModel.getParty());
                editTransactionBinding.transactiondate.setText(transactionModel.getDate());
                editTransactionBinding.addressEditText.setText(transactionModel.getAddress());
                editTransactionBinding.amountEditText.setText(transactionModel.getAmount());
                editTransactionBinding.mrNoTedittext.setText(transactionModel.getMrNo());
                transactionId=transactionModel.getTransactionId();
                switch (transactionModel.getPaymentMode()){
                    case "Cheque":
                        editTransactionBinding.chequeRadioButton.setSelected(true);
                        editTransactionBinding.chequeNoEditText.setText(transactionModel.getChequeDataModel().getChequeNo());
                        editTransactionBinding.bankNameEditText.setText(transactionModel.getChequeDataModel().getBankName());
                        editTransactionBinding.dateEditText.setText(transactionModel.getChequeDataModel().getDate());
                        break;
                    case "Cash": editTransactionBinding.cashRadioButton.setSelected(true);
                        break;
                    case "UPI":editTransactionBinding.UPIRadioButton.setSelected(true);
                        editTransactionBinding.upiIdEditText.setText(transactionModel.getUpiDetail().getUpiId());
                        editTransactionBinding.dateUpiIdEditText.setText(transactionModel.getUpiDetail().getDate());
                        break;
                    case "Online": editTransactionBinding.onlineRadioButton.setSelected(true);
                                    editTransactionBinding.onlineDateEditText.setText(transactionModel.getOnlineDetail().getDate());
                                    editTransactionBinding.onlineReferenceIdEditText.setText(transactionModel.getOnlineDetail().getReferenceId());
                        break;
                }

                return true;

            }else {
                transactionId=UUID.randomUUID().toString().substring(16);
                return false;
            }
        }


    private void setListners() {

        editTransactionBinding.saveButton.setOnClickListener(v->{
            if(cheakDetial()){
                Toast.makeText(this, transactionId, Toast.LENGTH_SHORT).show();
                transactionModel.setDate(editTransactionBinding.transactiondate.getText().toString());
                transactionModel.setAddress(editTransactionBinding.addressEditText.getText().toString());
                transactionModel.setAmount(editTransactionBinding.amountEditText.getText().toString());
                transactionModel.setMrNo(editTransactionBinding.mrNoTedittext.getText().toString());
                transactionModel.setParty(editTransactionBinding.partynamedropdown.getText().toString());
                transactionModel.setTransactionId(transactionId);
                transactionModel.setUserId("UserId");
                transactionModel.setParty(editTransactionBinding.partynamedropdown.getText().toString());
                transactionModel.setPaymentMode(paymentMode);
                transactionModel.setChequeDataModel(chequeDataModel);
                transactionModel.setOnlineDetail(onlineDetailModel);
                transactionModel.setUpiDetail(upiDetailModel);
                 createTransactionToServer(transactionModel,transactionId);

            }
        });

        editTransactionBinding.transactiondate.setOnClickListener(v->{
            DateFormat.getDateFromCalender(EditTransactionActivity.this, new DateFormat.DateSelectListener() {
                @Override
                public void onSelectedDate(String date) {
                    editTransactionBinding.transactiondate.setText(date);
                }
            });
        });
           editTransactionBinding.onlineDateEditText.setOnClickListener(v->{
            DateFormat.getDateFromCalender(EditTransactionActivity.this, new DateFormat.DateSelectListener() {
                @Override
                public void onSelectedDate(String date) {
                    editTransactionBinding.onlineDateEditText.setText(date);
                }
            });
        });
           editTransactionBinding.dateUpiIdEditText.setOnClickListener(v->{
            DateFormat.getDateFromCalender(EditTransactionActivity.this, new DateFormat.DateSelectListener() {
                @Override
                public void onSelectedDate(String date) {
                    editTransactionBinding.dateUpiIdEditText.setText(date);
                }
            });
        });
        editTransactionBinding.dateEditText.setOnClickListener(v->{
            DateFormat.getDateFromCalender(EditTransactionActivity.this, new DateFormat.DateSelectListener() {
                @Override
                public void onSelectedDate(String date) {
                    editTransactionBinding.dateEditText.setText(date);
                }
            });
        });




        editTransactionBinding.paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = editTransactionBinding.paymentRadioGroup.getCheckedRadioButtonId();

                switch (i) {
                    case R.id.chequeRadioButton:
                        paymentMode = "Cheque";
                       changevisibility(editTransactionBinding.chequeDetailsConstraintLayout);
                        break;

                    case R.id.onlineRadioButton:
                        paymentMode = "Online";
                        changevisibility(editTransactionBinding.onlineDetailsConstraintLayout);

                        break;

                    case R.id.UPIRadioButton:
                        paymentMode = "UPI";
                        changevisibility(editTransactionBinding.upiIdDetailsConstraintLayout);
                        break;

                    case R.id.cashRadioButton:
                        paymentMode = "Cash";
                        changevisibility(null);
                        break;

                }
            }
        });

        /////////////////////////////


    }

    private boolean cheakDetial() {
        if (transactionId.isEmpty()||transactionId==null){
            Toast.makeText(this, "empty TransactionId", Toast.LENGTH_SHORT).show();
        }
        if (editTransactionBinding.mrNoTedittext.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter Mr n0", Toast.LENGTH_SHORT).show();
            return false;
        }else if (editTransactionBinding.amountEditText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
            return false;
        }else if (editTransactionBinding.usernamedropdown.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
            return false;
        }else if (editTransactionBinding.transactiondate.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Enter Valid Date", Toast.LENGTH_SHORT).show();
            return false;
        }else if (paymentMode.isEmpty()){
            Toast.makeText(this, "Select Payment Mode", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if (paymentMode.equals("Cheque")){
                if (editTransactionBinding.dateEditText.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "enter cheque date", Toast.LENGTH_SHORT).show();
                }else if (editTransactionBinding.bankNameEditText.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "enter bank name", Toast.LENGTH_SHORT).show();
                }else if(editTransactionBinding.chequeNoEditText.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "enter cheque number", Toast.LENGTH_SHORT).show();
                }
                else{
                    chequeDataModel.setDate(editTransactionBinding.dateEditText.getText().toString());
                    chequeDataModel.setChequeNo(editTransactionBinding.chequeNoEditText.getText().toString());
                    chequeDataModel.setBankName(editTransactionBinding.bankNameEditText.getText().toString());
                    onlineDetailModel=null;
                    upiDetailModel=null;
                    return true;

                }
            }
            if (paymentMode.equals("Online")){
                if (editTransactionBinding.onlineDateEditText.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "enter online date", Toast.LENGTH_SHORT).show();
                }
                else if (editTransactionBinding.onlineReferenceIdEditText.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "enter online refernce Id", Toast.LENGTH_SHORT).show();
                }
                else{
                    onlineDetailModel.setDate(editTransactionBinding.onlineDateEditText.getText().toString());
                    onlineDetailModel.setReferenceId(editTransactionBinding.onlineReferenceIdEditText.getText().toString());
                    chequeDataModel=null;
                    upiDetailModel=null;
                    return true;

                }
            }
            if (paymentMode.equals("UPI")){
                if (editTransactionBinding.upiIdEditText.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "Enter valid upi id", Toast.LENGTH_SHORT).show();
                }else if (editTransactionBinding.dateUpiIdEditText.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "Enter Upi Date", Toast.LENGTH_SHORT).show();
                }
                else{
//                    String uname=editTransactionBinding.usernamedropdown.getText().toString();
//                    for (int i=0;i<userslist.size();i++){
//                        if (uname.equals(userslist.get(i).getUsername())){
//                            UserId=userslist.get(i).getUserID();
//                        }
//                    }
//                    if (UserId==null){
//                        UserId="Unindentified";
//                    }

                    upiDetailModel.setDate(editTransactionBinding.dateUpiIdEditText.getText().toString());
                    upiDetailModel.setUpiId(editTransactionBinding.upiIdEditText.getText().toString());
                    onlineDetailModel=null;
                    chequeDataModel=null;
                    return true;

                }

            }
            if (paymentMode.equals("Cash")){
                return true;
            }
        }
        return false;
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void setinit() {

        transactionViewModel = new ViewModelProvider(this).get(EditTransactionViewModel.class);

        editTransactionBinding.header.titleTextView.setText(" Create Transaction");


    }


    public void changevisibility(ConstraintLayout layoutitem){
        editTransactionBinding.chequeDetailsConstraintLayout.setVisibility(View.GONE);
        editTransactionBinding.upiIdDetailsConstraintLayout.setVisibility(View.GONE);
        editTransactionBinding.onlineDetailsConstraintLayout.setVisibility(View.GONE);
        if (layoutitem!=null){
            layoutitem.setVisibility(View.VISIBLE);
        }
    }


    // create transaction

    private void createTransactionToServer(RecentTransactionModel transactionModel,String transactionId) {
        Toast.makeText(this, "method called", Toast.LENGTH_SHORT).show();
        if (AppBoiler.isInternetConnected(EditTransactionActivity.this)) {

            progressDialog = AppBoiler.setProgressDialog(this);

            LiveData<String> responseLiveData = transactionViewModel.createTransactionToServer(transactionModel,transactionId);
            responseLiveData.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    progressDialog.dismiss();

                    if (s.equals(Constants.SUCCESS)) {
                        Toast.makeText(EditTransactionActivity.this, "Transaction Added", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(EditTransactionActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            AppBoiler.showSnackBarForInternet(EditTransactionActivity.this, editTransactionBinding.getRoot());
        }
    }

    // username list

    private void getUserNameListFromServer() {

        usernamelistLiveData=transactionViewModel.getUserNameListFromRepository();

        usernamelistLiveData.observe(this, new Observer<List<UserModel>>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onChanged(List<UserModel> userModels) {
                if (userModels.size()==0){
                    Toast.makeText(EditTransactionActivity.this, "no data found for usernames", Toast.LENGTH_SHORT).show();
                }else if(userModels.size()>0) {
                        userslist=userModels;
                    Toast.makeText(EditTransactionActivity.this, "runnning", Toast.LENGTH_SHORT).show();
                    for (int i=0;i<userModels.size();i++){
                        username=new String[userModels.size()];
                        username[i]=userModels.get(i).getUsername();
                    }
//                    ArrayAdapter<String> usernameadapter = new ArrayAdapter<>(EditTransactionActivity.this, android.R.layout.simple_dropdown_item_1line, username);
//                    editTransactionBinding.usernamedropdown.setAdapter(usernameadapter);
//
//                    editTransactionBinding.usernamedropdown.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View view, MotionEvent motionEvent) {
////                            editTransactionBinding.usernamedropdown.showDropDown();
//                            return false;
//                        }
//                    });
                }
            }
        });
    }





    private void getPartyNameListFromServer() {

        partylistLiveData=transactionViewModel.getPartyNameListFromRepository();

        partylistLiveData.observe(this, new Observer<List<String>>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onChanged(List<String> strings) {
                if (strings.size()==0){
                    Toast.makeText(EditTransactionActivity.this, "no data found for usernames", Toast.LENGTH_SHORT).show();
                }else if(strings.size()>0) {

                    String [] partylist=strings.toArray(new String[strings.size()]);
                    ArrayAdapter<String> usernameadapter = new ArrayAdapter<>(EditTransactionActivity.this, android.R.layout.simple_dropdown_item_1line, partylist);
                    editTransactionBinding.partynamedropdown.setAdapter(usernameadapter);

                    editTransactionBinding.partynamedropdown.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            editTransactionBinding.partynamedropdown.showDropDown();
                            return false;
                        }
                    });

                }

            }
        });
    }


}