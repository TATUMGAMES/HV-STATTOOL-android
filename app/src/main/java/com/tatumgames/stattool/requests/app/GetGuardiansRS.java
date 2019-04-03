package com.tatumgames.stattool.requests.app;

import com.google.gson.annotations.SerializedName;
import com.tatumgames.stattool.requests.model.Data;
import com.tatumgames.stattool.volley.BaseEntityRS;

import java.util.ArrayList;

public class GetGuardiansRS extends BaseEntityRS {

    @SerializedName("data")
    public ArrayList<Data> data = new ArrayList<>();
}
