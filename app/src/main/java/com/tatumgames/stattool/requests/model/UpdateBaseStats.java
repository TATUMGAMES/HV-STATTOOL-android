package com.tatumgames.stattool.requests.model;

import com.google.gson.annotations.SerializedName;

public class UpdateBaseStats {

    @SerializedName("id")
    public int id;

    @SerializedName("statId")
    public int statId;

    @SerializedName("statValue")
    public int statValue;

    @SerializedName("active")
    public int active;

    @SerializedName("archived")
    public int archived;

    @SerializedName("deleted")
    public int deleted;
}
