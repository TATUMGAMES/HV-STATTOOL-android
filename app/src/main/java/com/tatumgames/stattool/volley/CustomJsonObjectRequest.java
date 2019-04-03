package com.tatumgames.stattool.volley;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tatumgames.stattool.utils.Utils;
import com.tatumgames.stattool.volley.listeners.GsonObjectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by leonard on 5/2/2018.
 */
public class CustomJsonObjectRequest extends JsonObjectRequest {
    private static final String TAG_ERROR_RES = "res_error-";
    private static final String TAG_REQ = "req-";
    private static final String TAG_RES = "res-";
    private static final String TAG_URL = "url-";
    private static final String UTF = "UTF-8";

    private final static int DEFAULT_TIMEOUT = 60000;
    private Map<String, String> mCustomHeaders;

    @NonNull
    private Request.Priority mOptPriority = Priority.NORMAL;
    private String mRequestName;

    /**
     * Method is used to make JSONObjectRequest
     *
     * @param method        HTTP verb e.g. POST, GET, PUT, PATCH, and DELETE
     * @param url           The request URL
     * @param jsonRequest   A modifiable set of name/value mappings
     * @param listener      Interface for when request is received
     * @param errorListener Interface to track error with request
     * @param requestName   Name of request
     */
    public CustomJsonObjectRequest(int method, String url, @NonNull JSONObject jsonRequest,
                                   @NonNull GsonObjectListener<?> listener, @NonNull Response.ErrorListener errorListener,
                                   @NonNull String requestName) {
        super(method, url, jsonRequest, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // set request name
        mRequestName = requestName.replaceAll("RQ", "").replaceAll("RS", "");
        // log request and url
        Log.d(TAG_REQ.concat(mRequestName), jsonRequest.toString());
        Log.d(TAG_URL.concat(mRequestName), url);
    }

    /**
     * Method is used to make JSONObjectRequet with custom headers
     *
     * @param method        HTTP verb e.g. POST, GET, PUT, PATCH, and DELETE
     * @param url           The request URL
     * @param jsonRequest   A modifiable set of name/value mappings
     * @param listener      Interface for when request is received
     * @param errorListener Interface to track error with request
     * @param requestName   Name of request
     * @param headers       The Content-Type entity header is used to indicate the media type of the resource
     */
    public CustomJsonObjectRequest(int method, String url, @NonNull JSONObject jsonRequest,
                                   @NonNull Response.Listener<JSONObject> listener, @NonNull Response.ErrorListener errorListener,
                                   @NonNull String requestName, @NonNull Map<String, String> headers) {
        super(method, url, jsonRequest, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        setCustomHeaders(headers);
        // set request name
        mRequestName = requestName.replaceAll("RQ", "").replaceAll("RS", "");
        // log request and url
        Log.d(TAG_REQ.concat(mRequestName), jsonRequest.toString());
        Log.d(TAG_URL.concat(mRequestName), url);
    }

    /**
     * Set custom headers
     *
     * @param headers The Content-Type entity header is used to indicate the media type of the resource
     */
    public void setCustomHeaders(Map<String, String> headers) {
        mCustomHeaders = headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return !Utils.checkIfNull(mCustomHeaders) && mCustomHeaders.size() > 0 ?
                mCustomHeaders : super.getHeaders();
    }

    @Override
    public Request.Priority getPriority() {
        if (!Utils.checkIfNull(getOptPriority())) {
            return getOptPriority();
        } else {
            return super.getPriority();
        }
    }

    @Override
    public void deliverError(@NonNull VolleyError error) {
        super.deliverError(error);
        if (!Utils.checkIfNull(error.networkResponse)) {
            if (!Utils.isStringEmpty(mRequestName)) {
                Log.e(TAG_ERROR_RES.concat(mRequestName), new String(error.networkResponse.data));
            }
        }
    }

    @NonNull
    private Request.Priority getOptPriority() {
        return mOptPriority;
    }

    @NonNull
    @Override
    protected Response<JSONObject> parseNetworkResponse(@NonNull NetworkResponse response) {
        try {
            String jsonString = new String(response.data, UTF);
            if (!Utils.isStringEmpty(mRequestName)) {
                Log.d(TAG_RES.concat(mRequestName), jsonString);
            }
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

}
