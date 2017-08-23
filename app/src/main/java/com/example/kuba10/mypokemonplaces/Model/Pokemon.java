package com.example.kuba10.mypokemonplaces.Model;


import java.util.ArrayList;

public class Pokemon  {

    private int id;
    private String name;
    private ArrayList<Type> types;
    private int weight;
    private int height;



    public Pokemon(int id, String name, ArrayList<Type> types, int weight, int height) {
        this.id = id;
        this.name = name;
        this.types = types;
        this.weight = weight;
        this.height = height;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public String getImageUrl() {

        return "http://pokeapi.co/media/img/" + id + ".png";
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }
}
