package com.tatumgames.stattool.volley.listeners

import com.android.volley.Response
import com.android.volley.VolleyError

interface ErrorListener : Response.ErrorListener {

    /**
     * Interface for when Volley requests fail
     *
     * @param volleyError Volley error
     */
    override fun onErrorResponse(volleyError: VolleyError)

}
