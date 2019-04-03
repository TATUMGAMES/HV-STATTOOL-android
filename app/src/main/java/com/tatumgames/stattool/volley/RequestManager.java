package com.tatumgames.stattool.volley;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.google.gson.Gson;
import com.tatumgames.stattool.volley.listeners.ErrorListener;
import com.tatumgames.stattool.volley.listeners.GsonObjectListener;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leonard on 5/2/2018.
 */
public class RequestManager extends BaseRequest {
    private static final int DEFAULT_TIMEOUT = 60000;
    private static final String JSON_ERROR = "JSONObject cannot be null";

    /**
     * Instantiate context and volley request queue
     *
     * @param context Interface to global information about an application environment
     */
    public RequestManager(Context context) {
        super(context);
    }

    /**
     * Create POST request
     *
     * @param request     Request that moves through the pipeline, gets serviced, and has its
     *                    raw response parsed and delivered
     * @param listener    Interface for when request is received
     * @param errListener Interface to track error with request
     */
    public void createPostRequest(@NonNull final BaseEntityRQ request, @NonNull final GsonObjectListener<?> listener,
                                  @NonNull final ErrorListener errListener, @NonNull String url) {
        // setup gson to json
        Gson gson = new Gson();
        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject(gson.toJson(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // assert JSONObject not null
        Assert.assertNotNull(JSON_ERROR, jsonObj);
        CustomJsonObjectRequest customJsonObjectReq = new CustomJsonObjectRequest(Request.Method.POST, url,
                jsonObj, listener, errListener, request.getClass().getSimpleName());
        customJsonObjectReq.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // add to request queue
        addToRequestQueue(customJsonObjectReq);
    }

    /**
     * Create PUT request
     *
     * @param request     Request that moves through the pipeline, gets serviced, and has its
     *                    raw response parsed and delivered
     * @param listener    Interface for when request is received
     * @param errListener Interface to track error with request
     */
    public void createPutRequest(@NonNull final BaseEntityRQ request, @NonNull final GsonObjectListener<?> listener,
                                 @NonNull final ErrorListener errListener, @NonNull String url) {
        // setup gson to json
        Gson gson = new Gson();
        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject(gson.toJson(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // assert JSONObject not null
        Assert.assertNotNull(JSON_ERROR, jsonObj);
        CustomJsonObjectRequest customJsonObjectReq = new CustomJsonObjectRequest(Request.Method.PUT, url,
                jsonObj, listener, errListener, request.getClass().getSimpleName());
        customJsonObjectReq.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // add to request queue
        addToRequestQueue(customJsonObjectReq);
    }

    /**
     * Create GET request
     *
     * @param request     Request that moves through the pipeline, gets serviced, and has its
     *                    raw response parsed and delivered
     * @param listener    Interface for when request is received
     * @param errListener Interface to track error with request
     */
    public void createGetRequest(@NonNull final BaseEntityRQ request, @NonNull final GsonObjectListener<?> listener,
                                 @NonNull final ErrorListener errListener, @NonNull String url) {
        // setup gson to json
        Gson gson = new Gson();
        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject(gson.toJson(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // assert JSONObject not null
        Assert.assertNotNull(JSON_ERROR, jsonObj);
        CustomJsonObjectRequest customJsonObjectReq = new CustomJsonObjectRequest(Request.Method.GET, url,
                jsonObj, listener, errListener, request.getClass().getSimpleName());
        customJsonObjectReq.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // add to request queue
        addToRequestQueue(customJsonObjectReq);
    }

}