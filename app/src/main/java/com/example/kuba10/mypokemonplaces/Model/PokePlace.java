package com.example.kuba10.mypokemonplaces.Model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PokePlace implements Serializable {

    @SerializedName("desctription")
    @Expose
    private String desctription;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("long")
    @Expose
    private Double lng;
    @SerializedName("listPosition")
    @Expose
    private String listPosition;
    @SerializedName("favourite")
    @Expose
    private int favourite;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("pokemonId")
    @Expose
    private int pokemonId;

    @SerializedName("globalID")
    @Expose
    private long globalID;

    public long getGlobalID() {
        return globalID;
    }

    public void setGlobalID(long globalID) {
        this.globalID = globalID;
    }

    public PokePlace() {
    }

//    public PokePlace(String desctription, Double lat, Double lng, String listPosition, String test2, String title, int favourite, int pokemonId) {
//        this.desctription = desctription;
//        this.lat = lat;
//        this.lng = lng;
//        this.listPosition = listPosition;
//        this.favourite = favourite;
//        this.pokemonId = pokemonId;
//        this.title = title;
//    }


    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

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

    public void setLong(Double lng) {
        this.lng = lng;
    }

    public String getListPosition() {
        return listPosition;
    }

    public void setListPosition(String listPosition) {
        this.listPosition = listPosition;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



   public  interface PlaceBuilderInterface {
        PokePlace build();

        PlaceBuilder setGlobalID(final long globalID);

        PlaceBuilder setPokemonId(final int pokemonId);

        PlaceBuilder setListPosition ( final String listPosition);

        PlaceBuilder setLat(final Double lat);

        PlaceBuilder setLong(final Double lng);

        PlaceBuilder setFavourite( final int favourite);

        PlaceBuilder setTitle(final String title);

        PlaceBuilder setDesctription( final String desctription);
    }

    public static class PlaceBuilder implements PlaceBuilderInterface {

        private PokePlace place;

        public PlaceBuilder() {
            this.place = new PokePlace();
        }

        @Override
        public PokePlace build() {
            return place;
        }

        @Override
        public PlaceBuilder setGlobalID(long globalID) {
            place.setGlobalID(globalID);
            return this;
        }

        @Override
        public PlaceBuilder setPokemonId(int pokemonId) {
            place.setPokemonId(pokemonId);
            return this;
        }

        @Override
        public PlaceBuilder setListPosition(String listPosition) {
            place.setListPosition(listPosition);
            return this;
        }

        @Override
        public PlaceBuilder setLat(Double lat) {
            place.setLat(lat);
            return this;
        }

        @Override
        public PlaceBuilder setLong(Double lng) {
            place.setLong(lng);
            return this;
        }

        @Override
        public PlaceBuilder setFavourite(int favourite) {
            place.setFavourite(favourite);
            return this;
        }

        @Override
        public PlaceBuilder setTitle(String title) {
            place.setTitle(title);
            return this;
        }

        @Override
        public PlaceBuilder setDesctription(String desctription) {
            place.setDesctription(desctription);
            return this;
        }


    }
}



