package com.tatumgames.stattool.volley;

import com.android.volley.Response;
import com.tatumgames.stattool.volley.listeners.GsonObjectListener;

/**
 * Created by leonard on 7/31/20175/2/2018.
 */

public class RequestQueue {

    public int HTTPMethod;
    public String url;
    public BaseEntityRQ request;
    public GsonObjectListener<?> listener;
    public Response.ErrorListener errListener;
}
