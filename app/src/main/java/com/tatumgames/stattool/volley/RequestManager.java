/**
 * Copyright 2013-present Tatum Games, LLC.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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