package com.tatumgames.stattool.volley;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leonard on 5/2/2018.
 */
public class BaseEntityRS {

    @SerializedName("status")
    public Status status;

    public class Status {

        @SerializedName("status_code")
        public String statusCode;

        @SerializedName("status_message")
        public String statusMessage;

    }
}
