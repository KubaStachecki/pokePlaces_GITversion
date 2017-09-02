package com.example.kuba10.mypokemonplaces.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class PokemonGo implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("num")
    @Expose
    private String num;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("candy")
    @Expose
    private String candy;
    @SerializedName("candy_count")
    @Expose
    private Integer candyCount;
    @SerializedName("egg")
    @Expose
    private String egg;
    @SerializedName("spawn_chance")
    @Expose
    private Double spawnChance;
    @SerializedName("avg_spawns")
    @Expose
    private Double avgSpawns;
    @SerializedName("spawn_time")
    @Expose
    private String spawnTime;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }



    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCandy() {
        return candy;
    }

    public void setCandy(String candy) {
        this.candy = candy;
    }

    public Integer getCandyCount() {
        return candyCount;
    }

    public void setCandyCount(Integer candyCount) {
        this.candyCount = candyCount;
    }

    public String getEgg() {
        return egg;
    }

    public void setEgg(String egg) {
        this.egg = egg;
    }

    public Double getSpawnChance() {
        return spawnChance;
    }

    public void setSpawnChance(Double spawnChance) {
        this.spawnChance = spawnChance;
    }

    public Double getAvgSpawns() {
        return avgSpawns;
    }

    public void setAvgSpawns(Double avgSpawns) {
        this.avgSpawns = avgSpawns;
    }

    public String getSpawnTime() {
        return spawnTime;
    }

    public void setSpawnTime(String spawnTime) {
        this.spawnTime = spawnTime;
    }



}
