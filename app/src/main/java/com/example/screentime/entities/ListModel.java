package com.example.screentime.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class ListModel implements Parcelable {
    private String denumire;

    public ListModel(String denumire) {
        this.denumire = denumire;
    }
    public ListModel() {

    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    @Override
    public String toString() {
        return "ListModel{" +
                "denumire='" + denumire + '\'' +
                '}';
    }

    private ListModel(Parcel source) {

        denumire = source.readString();
    }

    public static Creator<ListModel> CREATOR = new Creator<ListModel>() {
        @Override
        public ListModel createFromParcel(Parcel source) {
            return new ListModel(source);
        }

        @Override
        public ListModel[] newArray(int size) {
            return new ListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(denumire);

    }
}
