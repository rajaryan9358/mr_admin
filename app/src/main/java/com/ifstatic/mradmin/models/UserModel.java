package com.ifstatic.mradmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserModel implements Parcelable {
    String userID;
    String username;
    String userpassword;

    public UserModel() {
    }

    public UserModel(String userID, String username, String userpassword) {
        this.userID = userID;
        this.username = username;
        this.userpassword = userpassword;
    }

    protected UserModel(Parcel in) {
        userID = in.readString();
        username = in.readString();
        userpassword = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        this.userID=parcel.toString();
        this.username=parcel.toString();
        this.userpassword=parcel.toString();
    }
}
