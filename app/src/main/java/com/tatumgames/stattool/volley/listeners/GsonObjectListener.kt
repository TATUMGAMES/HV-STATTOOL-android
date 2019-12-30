package com.tatumgames.stattool.volley.listeners

import com.android.volley.ParseError
import com.android.volley.Response.Listener
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.tatumgames.stattool.utils.Utils

import org.json.JSONObject

class GsonObjectListener<T>
/**
 * Constructor
 *
 * @param responseListener Interface for when response is received
 * @param errorListener    Interface for when there is an with response
 * @param genericClass     Generics
 */
(private val mResponseListener: OnResponse<T>, private val mErrorListener: ErrorListener, private val mGenericClass: Class<T>) : Listener<JSONObject> {
    private var mGson: Gson? = null

    init {

        // instantiate objects
        mGson = Gson()
    }

    override fun onResponse(jsonObject: JSONObject) {
        if (Utils.checkIfNull<Gson>(mGson)) {
            mGson = Gson()
        }

        try {
            val t = mGson!!.fromJson(jsonObject.toString(), mGenericClass)
            if (!Utils.checkIfNull(mResponseListener)) {
                mResponseListener.onResponse(t)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val error = ParseError()

            if (!Utils.checkIfNull(mErrorListener)) {
                mErrorListener.onErrorResponse(error)
            }
        }

    }

    /**
     * Interface for when response is received
     *
     * @param <T> Generics
    </T> */
    interface OnResponse<T> {
        fun onResponse(response: T)
    }
}
