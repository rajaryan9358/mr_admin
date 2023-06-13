package com.ifstatic.mradmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class   RecentTransactionModel implements Parcelable {

    private String mrNo ;
    private String party;
    private String paymentMode ;
    private String amount;
    private String date;
    private String address;
    private String userId;
    private String username;
    private String transactionId;
    private ChequeDataModel chequeDataModel;
    private UpiDetailModel upiDetail;
    private OnlineDetailModel onlineDetail;

    public RecentTransactionModel() {
    }

    public RecentTransactionModel(String mrNo, String party, String paymentMode, String amount, String date, String address, String userId, String username, String transactionId, ChequeDataModel chequeDataModel, UpiDetailModel upiDetail, OnlineDetailModel onlineDetail) {
        this.mrNo = mrNo;
        this.party = party;
        this.paymentMode = paymentMode;
        this.amount = amount;
        this.date = date;
        this.address = address;
        this.userId = userId;
        this.username = username;
        this.transactionId = transactionId;
        this.chequeDataModel = chequeDataModel;
        this.upiDetail = upiDetail;
        this.onlineDetail = onlineDetail;
    }

    protected RecentTransactionModel(Parcel in) {
        mrNo = in.readString();
        party = in.readString();
        paymentMode = in.readString();
        amount = in.readString();
        date = in.readString();
        address = in.readString();
        userId = in.readString();
        username = in.readString();
        transactionId = in.readString();
        chequeDataModel = in.readParcelable(ChequeDataModel.class.getClassLoader());
        upiDetail = in.readParcelable(UpiDetailModel.class.getClassLoader());
        onlineDetail = in.readParcelable(OnlineDetailModel.class.getClassLoader());
    }

    public static final Creator<RecentTransactionModel> CREATOR = new Creator<RecentTransactionModel>() {
        @Override
        public RecentTransactionModel createFromParcel(Parcel in) {
            return new RecentTransactionModel(in);
        }

        @Override
        public RecentTransactionModel[] newArray(int size) {
            return new RecentTransactionModel[size];
        }
    };

    public String getMrNo() {
        return mrNo;
    }

    public void setMrNo(String mrNo) {
        this.mrNo = mrNo;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public ChequeDataModel getChequeDataModel() {
        return chequeDataModel;
    }

    public void setChequeDataModel(ChequeDataModel chequeDataModel) {
        this.chequeDataModel = chequeDataModel;
    }

    public UpiDetailModel getUpiDetail() {
        return upiDetail;
    }

    public void setUpiDetail(UpiDetailModel upiDetail) {
        this.upiDetail = upiDetail;
    }

    public OnlineDetailModel getOnlineDetail() {
        return onlineDetail;
    }

    public void setOnlineDetail(OnlineDetailModel onlineDetail) {
        this.onlineDetail = onlineDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(mrNo);
        parcel.writeString(party);
        parcel.writeString(paymentMode);
        parcel.writeString(amount);
        parcel.writeString(date);
        parcel.writeString(address);
        parcel.writeString(userId);
        parcel.writeString(username);
        parcel.writeString(transactionId);
        parcel.writeParcelable(chequeDataModel, i);
        parcel.writeParcelable(upiDetail, i);
        parcel.writeParcelable(onlineDetail, i);
    }
}
