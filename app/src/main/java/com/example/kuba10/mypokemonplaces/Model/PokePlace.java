package com.example.kuba10.mypokemonplaces.Model;

/**
 * Created by Kuba10 on 14.08.2017.
 */


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PokePlace implements Parcelable {

    @SerializedName("desctription")
    @Expose
    private String desctription;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("long")
    @Expose
    private Double lng;
    @SerializedName("test1")
    @Expose
    private String test1;
    @SerializedName("test2")
    @Expose
    private String test2;
    @SerializedName("title")
    @Expose
    private String title;

    public PokePlace (){


    }

    protected PokePlace(Parcel in) {
        desctription = in.readString();
        test1 = in.readString();
        test2 = in.readString();
        title = in.readString();
    }

    public static final Creator<PokePlace> CREATOR = new Creator<PokePlace>() {
        @Override
        public PokePlace createFromParcel(Parcel in) {
            return new PokePlace(in);
        }

        @Override
        public PokePlace[] newArray(int size) {
            return new PokePlace[size];
        }
    };

    public String getDesctription() {
        return desctription;
    }

    public void setDesctription(String desctription) {
        this.desctription = desctription;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return lng;
    }

    public void setLong(Double _long) {
        this.lng = _long;
    }

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(desctription);
        parcel.writeString(test1);
        parcel.writeString(test2);
        parcel.writeString(title);
    }
}

