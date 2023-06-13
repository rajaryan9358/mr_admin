package com.ifstatic.mradmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UpiDetailModel  implements Parcelable {

    private String upiId;
    private String date;

    public UpiDetailModel() {
    }

    public UpiDetailModel(String upiId, String date) {
        this.upiId = upiId;
        this.date = date;
    }

    protected UpiDetailModel(Parcel in) {
        upiId = in.readString();
        date = in.readString();
    }

    public static final Creator<UpiDetailModel> CREATOR = new Creator<UpiDetailModel>() {
        @Override
        public UpiDetailModel createFromParcel(Parcel in) {
            return new UpiDetailModel(in);
        }

        @Override
        public UpiDetailModel[] newArray(int size) {
            return new UpiDetailModel[size];
        }
    };

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(upiId);
        parcel.writeString(date);
    }
}
