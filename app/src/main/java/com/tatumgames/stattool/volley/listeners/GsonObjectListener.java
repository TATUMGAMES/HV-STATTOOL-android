package com.tatumgames.stattool.volley.listeners;

import com.android.volley.ParseError;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tatumgames.stattool.utils.Utils;

import org.json.JSONObject;

/**
 * Created by leonard on 5/2/2018.
 */
public class GsonObjectListener<T> implements Listener<JSONObject> {

    ErrorListener mErrorListener;
    OnResponse<T> mResponseListener;
    Class<T> mGenericClass;
    Gson mGson;

    /**
     * Constructor
     *
     * @param responseListener Interface for when response is received
     * @param errorListener    Interface for when there is an with response
     * @param genericClass     Generics
     */
    public GsonObjectListener(OnResponse<T> responseListener, ErrorListener errorListener, Class<T> genericClass) {
        super();

        // instantiate objects
        mGson = new Gson();
        mGenericClass = genericClass;
        mResponseListener = responseListener;
        mErrorListener = errorListener;
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        if (Utils.checkIfNull(mGson)) {
            mGson = new Gson();
        }

        try {
            T t = mGson.fromJson(jsonObject.toString(), mGenericClass);
            if (!Utils.checkIfNull(mResponseListener)) {
                mResponseListener.onResponse(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            VolleyError error = new ParseError();

            if (!Utils.checkIfNull(mErrorListener)) {
                mErrorListener.onErrorResponse(error);
            }
        }
    }

    /**
     * Interface for when response is received
     *
     * @param <T> Generics
     */
    public interface OnResponse<T> {
        void onResponse(T response);
    }
}
