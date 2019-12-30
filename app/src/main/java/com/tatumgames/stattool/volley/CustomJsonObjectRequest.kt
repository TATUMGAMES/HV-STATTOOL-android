/**
 * Copyright 2013-present Tatum Games, LLC.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tatumgames.stattool.volley

import android.util.Log

import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.tatumgames.stattool.utils.Utils
import com.tatumgames.stattool.volley.listeners.GsonObjectListener

import org.json.JSONException
import org.json.JSONObject

import java.io.UnsupportedEncodingException

class CustomJsonObjectRequest : JsonObjectRequest {
    private var mCustomHeaders: Map<String, String>? = null
    private var mRequestName: String? = null

    /**
     * Method is used to make JSONObjectRequest
     *
     * A request for retrieving a JSONObject response body at a given URL, allowing for an
     * optional JSONObject to be passed in as part of the request body.
     *
     * @param httpMethod    HTTP defines a set of request methods to indicate the desired action to be
     * performed for a given resource
     * @param url           URL is the abbreviation of Uniform Resource Locator and is defined
     * as the global address of documents and other resources on the World Wide Web
     * @param jsonRequest   A modifiable set of name/value mappings
     * @param listener      Interface for when request is received
     * @param errorListener Interface to track error with request
     * @param requestName   Name of request
     */
    internal constructor(httpMethod: Int, url: String, jsonRequest: JSONObject,
                         listener: GsonObjectListener<*>, errorListener: Response.ErrorListener,
                         requestName: String) : super(httpMethod, url, jsonRequest, listener, errorListener) {
        setRequest(httpMethod, url, jsonRequest, requestName, null)
    }

    /**
     * Method is used to make JSONObjectRequest with custom headers
     *
     * A request for retrieving a JSONObject response body at a given URL, allowing for an
     * optional JSONObject to be passed in as part of the request body.
     *
     * @param httpMethod    HTTP defines a set of request methods to indicate the desired action to be
     * performed for a given resource
     * @param url           URL is the abbreviation of Uniform Resource Locator and is defined
     * as the global address of documents and other resources on the World Wide Web
     * @param jsonRequest   A modifiable set of name/value mappings
     * @param listener      Interface for when request is received
     * @param errorListener Interface to track error with request
     * @param requestName   Name of request
     * @param headers       The Content-Type entity header is used to indicate the media type of the resource
     */
    internal constructor(httpMethod: Int, url: String, jsonRequest: JSONObject,
                         listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener,
                         requestName: String, headers: Map<String, String>) : super(httpMethod, url, jsonRequest, listener, errorListener) {
        setRequest(httpMethod, url, jsonRequest, requestName, headers)
    }

    /**
     * Method is used to set request information for JSONObjectRequest
     *
     * A request for retrieving a JSONObject response body at a given URL, allowing for an
     * optional JSONObject to be passed in as part of the request body.
     *
     * @param httpMethod  HTTP defines a set of request methods to indicate the desired action to be
     * performed for a given resource
     * @param url         URL is the abbreviation of Uniform Resource Locator and is defined
     * as the global address of documents and other resources on the World Wide Web
     * @param jsonRequest A modifiable set of name/value mappings
     * @param requestName Name of request
     * @param headers     The Content-Type entity header is used to indicate the media type of the resource
     */
    private fun setRequest(httpMethod: Int, url: String, jsonRequest: JSONObject,
                           requestName: String, headers: Map<String, String>?) {
        retryPolicy = DefaultRetryPolicy(DEFAULT_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        setCustomHeaders(headers)
        // set request name
        mRequestName = requestName.replace("RQ".toRegex(), "").replace("RS".toRegex(), "")
        // log request and url
        Log.d(TAG_REQ + mRequestName, jsonRequest.toString())
        Log.d(TAG_URL + mRequestName, url)
    }

    /**
     * Set custom headers
     *
     * @param headers The Content-Type entity header is used to indicate the media type of the resource
     */
    internal fun setCustomHeaders(headers: Map<String, String>?) {
        mCustomHeaders = headers
    }

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        return if (!Utils.checkIfNull<Map<String, String>>(mCustomHeaders) && mCustomHeaders!!.size > 0)
            mCustomHeaders
        else
            super.getHeaders()
    }

    override fun getPriority(): Request.Priority {
        return Request.Priority.NORMAL
    }

    override fun deliverError(error: VolleyError) {
        super.deliverError(error)
        if (!Utils.checkIfNull(error.networkResponse)) {
            if (!Utils.isStringEmpty(mRequestName)) {
                Log.e(TAG_ERROR_RES + mRequestName, String(error.networkResponse.data))
            }
        }
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        try {
            val jsonString = String(response.data, UTF)
            if (!Utils.isStringEmpty(mRequestName)) {
                Log.d(TAG_RES + mRequestName, jsonString)
            }
            return Response.success(JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return Response.error(ParseError(e))
        } catch (e: JSONException) {
            e.printStackTrace()
            return Response.error(ParseError(e))
        }

    }

    companion object {
        private val TAG_ERROR_RES = "res_error-"
        private val TAG_REQ = "req-"
        private val TAG_RES = "res-"
        private val TAG_URL = "url-"
        private val UTF = "UTF-8"

        private val DEFAULT_TIMEOUT = 60000
    }

}
