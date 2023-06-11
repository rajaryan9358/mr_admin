package com.ifstatic.mradmin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PartyModel implements Parcelable {
    private String party;
    private String address;


    public PartyModel(String party, String address) {
        this.party = party;
        this.address = address;
    }

    public PartyModel() {
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.party);
        dest.writeString(this.address);
    }

    public void readFromParcel(Parcel source) {
        this.party = source.readString();
        this.address = source.readString();
    }

    protected PartyModel(Parcel in) {
        this.party = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<PartyModel> CREATOR = new Parcelable.Creator<PartyModel>() {
        @Override
        public PartyModel createFromParcel(Parcel source) {
            return new PartyModel(source);
        }

        @Override
        public PartyModel[] newArray(int size) {
            return new PartyModel[size];
        }
    };

}
