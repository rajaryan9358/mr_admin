package com.ifstatic.mradmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UpiDetailModel  implements Parcelable {
    public static final Parcelable.Creator<UpiDetailModel> CREATOR = new Parcelable.Creator<UpiDetailModel>() {
        @Override
        public UpiDetailModel createFromParcel(Parcel source) {
            return new UpiDetailModel(source);
        }

        @Override
        public UpiDetailModel[] newArray(int size) {
            return new UpiDetailModel[size];
        }
    };
    private String upiId;
    private String date;

    public UpiDetailModel() {
    }

    public UpiDetailModel(String upiId, String date) {
        this.upiId = upiId;
        this.date = date;
    }

    protected UpiDetailModel(Parcel in) {
        this.date = in.readString();
        this.upiId = in.readString();

    }

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
        parcel.writeString(this.date);
        parcel.writeString(this.upiId);

    }

    public void readFromParcel(Parcel source) {
        this.date = source.readString();
        this.upiId = source.readString();

    }
}
