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
    private ChequeDataModel chequeDataModel;

    public RecentTransactionModel() {
    }

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

    public ChequeDataModel getChequeDataModel() {
        return chequeDataModel;
    }

    public void setChequeDataModel(ChequeDataModel chequeDataModel) {
        this.chequeDataModel = chequeDataModel;
    }

    protected RecentTransactionModel(Parcel in) {
        mrNo = in.readString();
        party = in.readString();
        paymentMode = in.readString();
        amount = in.readString();
        date = in.readString();
        chequeDataModel = in.readParcelable(ChequeDataModel.class.getClassLoader());
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
        parcel.writeParcelable(chequeDataModel, i);
    }

    public void readFromParcel(Parcel source){
        this.mrNo= source.readString();
        this.party= source.readString();
        this.amount= source.readString();
        this.paymentMode= source.readString();
        this.date=source.readString();
        this.chequeDataModel=source.readParcelable(ChequeDataModel.class.getClassLoader());
    }
}
