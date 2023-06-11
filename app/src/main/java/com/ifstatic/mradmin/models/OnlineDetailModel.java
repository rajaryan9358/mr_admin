package com.ifstatic.mradmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OnlineDetailModel implements Parcelable {

    public static final Creator<OnlineDetailModel> CREATOR = new Creator<OnlineDetailModel>() {
        @Override
        public OnlineDetailModel createFromParcel(Parcel in) {
            return new OnlineDetailModel(in);
        }

        @Override
        public OnlineDetailModel[] newArray(int size) {
            return new OnlineDetailModel[size];
        }
    };
    private String referenceId;
    private String date;


    public OnlineDetailModel() {
    }

    public OnlineDetailModel(String referenceId, String date) {
        this.referenceId = referenceId;
        this.date = date;
    }

    protected OnlineDetailModel(Parcel in) {
        this.date = in.readString();
        this.referenceId = in.readString();

    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
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
        parcel.writeString(this.referenceId);
    }

    public void readFromParcel(Parcel source) {
        this.date = source.readString();
        this.referenceId = source.readString();

    }
}
