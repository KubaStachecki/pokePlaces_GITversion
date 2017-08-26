package com.example.kuba10.mypokemonplaces.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kuba10 on 26.08.2017.
 */

public class AppInfoText implements Parcelable {

    private String text;
    private String title;

    public AppInfoText() {
    }

    protected AppInfoText(Parcel in) {
        text = in.readString();
        title = in.readString();
    }

    public static final Creator<AppInfoText> CREATOR = new Creator<AppInfoText>() {
        @Override
        public AppInfoText createFromParcel(Parcel in) {
            return new AppInfoText(in);
        }

        @Override
        public AppInfoText[] newArray(int size) {
            return new AppInfoText[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AppInfoText(String text, String title) {

        this.text = text;
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(title);
    }
}
