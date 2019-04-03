package com.tatumgames.stattool.requests.model;

import com.google.gson.annotations.SerializedName;

public class Skins {

    @SerializedName("id")
    public String id;

    @SerializedName("characterId")
    public String characterId;

    @SerializedName("description")
    public String description;

    @SerializedName("name")
    public String name;

    @SerializedName("url")
    public String url;

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
}
