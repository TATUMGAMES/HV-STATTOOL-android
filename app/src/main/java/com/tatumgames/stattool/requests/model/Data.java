package com.tatumgames.stattool.requests.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("alias")
    public String alias;

    @SerializedName("description")
    public String description;

    @SerializedName("shortDescription")
    public String shortDescription;

    @SerializedName("code")
    public String code;

    @SerializedName("assetBundle")
    public String assetBundle;

    @SerializedName("created")
    public String created;

    @SerializedName("updated")
    public String updated;

    @SerializedName("active")
    public String active;

    @SerializedName("archived")
    public String archived;

    @SerializedName("deleted")
    public String deleted;

    @SerializedName("skins")
    public ArrayList<Skins> skins = new ArrayList<>();

    @SerializedName("stats")
    public ArrayList<Stats> stats = new ArrayList<>();

}
