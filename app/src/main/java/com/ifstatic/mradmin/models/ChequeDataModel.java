package com.ifstatic.mradmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ChequeDataModel implements Parcelable {

    private String date;
    private String bankName;
    private String chequeNo;

    public ChequeDataModel() {
    }

    protected ChequeDataModel(Parcel in) {
        date = in.readString();
        bankName = in.readString();
        chequeNo = in.readString();
    }

    public static final Creator<ChequeDataModel> CREATOR = new Creator<ChequeDataModel>() {
        @Override
        public ChequeDataModel createFromParcel(Parcel in) {
            return new ChequeDataModel(in);
        }

        @Override
        public ChequeDataModel[] newArray(int size) {
            return new ChequeDataModel[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(bankName);
        parcel.writeString(chequeNo);
    }

    public void readFromParcel(Parcel source) {
        this.date = source.readString();
        this.bankName = source.readString();
        this.chequeNo = source.readString();
    }
}
