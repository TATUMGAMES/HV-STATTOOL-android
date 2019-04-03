package com.tatumgames.stattool.requests.app;

import com.google.gson.annotations.SerializedName;
import com.tatumgames.stattool.requests.model.Data;
import com.tatumgames.stattool.volley.BaseEntityRS;

public class UpdateGuardianBaseStatsBulkRS extends BaseEntityRS {

    @SerializedName("data")
    public Data data;
}
