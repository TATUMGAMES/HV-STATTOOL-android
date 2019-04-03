package com.tatumgames.stattool.requests.app;

import com.google.gson.annotations.SerializedName;
import com.tatumgames.stattool.requests.model.UpdateBaseStats;
import com.tatumgames.stattool.volley.BaseEntityRQ;

import java.util.ArrayList;

public class UpdateGuardianBaseStatsBulkRQ extends BaseEntityRQ {

    @SerializedName("guardianId")
    public int guardianId;

    @SerializedName("stats")
    public ArrayList<UpdateBaseStats> stats = new ArrayList<>();
}
