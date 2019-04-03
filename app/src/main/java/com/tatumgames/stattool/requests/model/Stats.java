package com.tatumgames.stattool.requests.model;

import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("id")
    public String id;

    @SerializedName("statId")
    public String statId;

    @SerializedName("description")
    public String description;

    @SerializedName("name")
    public String name;

    @SerializedName("code")
    public String code;

    @SerializedName("ascension")
    public String ascension;

    @SerializedName("maxAscension")
    public String maxAscension;

    @SerializedName("value")
    public String value;

    @SerializedName("maxValue")
    public String maxValue;

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
