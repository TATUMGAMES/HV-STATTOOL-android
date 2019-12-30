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

import android.content.Context

import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.google.gson.Gson
import com.tatumgames.stattool.volley.listeners.ErrorListener
import com.tatumgames.stattool.volley.listeners.GsonObjectListener

import junit.framework.Assert

import org.json.JSONException
import org.json.JSONObject

class RequestManager
/**
 * Instantiate context and volley request queue
 *
 * @param context Interface to global information about an application environment
 */
(context: Context) : BaseRequest(context) {

    /**
     * Create POST request
     *
     * @param request     Request that moves through the pipeline, gets serviced, and has its
     * raw response parsed and delivered
     * @param listener    Interface for when request is received
     * @param errListener Interface to track error with request
     * @param url         URL is the abbreviation of Uniform Resource Locator and is defined
     * as the global address of documents and other resources on the World Wide Web
     */
    fun createPostRequest(request: BaseEntityRQ, listener: GsonObjectListener<*>,
                          errListener: ErrorListener, url: String) {
        // make request
        makeRequest(request, listener, errListener, url, Request.Method.POST)
    }

    /**
     * Create PUT request
     *
     * @param request     Request that moves through the pipeline, gets serviced, and has its
     * raw response parsed and delivered
     * @param listener    Interface for when request is received
     * @param errListener Interface to track error with request
     * @param url         URL is the abbreviation of Uniform Resource Locator and is defined
     * as the global address of documents and other resources on the World Wide Web
     */
    fun createPutRequest(request: BaseEntityRQ, listener: GsonObjectListener<*>,
                         errListener: ErrorListener, url: String) {
        // make request
        makeRequest(request, listener, errListener, url, Request.Method.PUT)
    }

    /**
     * Create GET request
     *
     * @param request     Request that moves through the pipeline, gets serviced, and has its
     * raw response parsed and delivered
     * @param listener    Interface for when request is received
     * @param errListener Interface to track error with request
     * @param url         URL is the abbreviation of Uniform Resource Locator and is defined
     * as the global address of documents and other resources on the World Wide Web
     */
    fun createGetRequest(request: BaseEntityRQ, listener: GsonObjectListener<*>,
                         errListener: ErrorListener, url: String) {
        // make request
        makeRequest(request, listener, errListener, url, Request.Method.GET)
    }

    /**
     * @param request     Request that moves through the pipeline, gets serviced, and has its
     * raw response parsed and delivered
     * @param listener    Interface for when request is received
     * @param errListener Interface to track error with request
     * @param url         URL is the abbreviation of Uniform Resource Locator and is defined
     * as the global address of documents and other resources on the World Wide Web
     * @param httpMethod  HTTP defines a set of request methods to indicate the desired action to be
     * performed for a given resource
     */
    private fun makeRequest(request: BaseEntityRQ, listener: GsonObjectListener<*>,
                            errListener: ErrorListener, url: String, httpMethod: Int) {
        // setup gson to json
        val gson = Gson()
        var jsonObj: JSONObject? = null

        try {
            jsonObj = JSONObject(gson.toJson(request))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // assert JSONObject not null
        Assert.assertNotNull(JSON_ERROR, jsonObj)
        val customJsonObjectReq = CustomJsonObjectRequest(httpMethod, url,
                jsonObj!!, listener, errListener, request.javaClass.getSimpleName())
        customJsonObjectReq.retryPolicy = DefaultRetryPolicy(DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        // add to request queue
        addToRequestQueue<Any>(customJsonObjectReq)
    }

    companion object {
        private val DEFAULT_TIMEOUT = 60000
        private val JSON_ERROR = "JSONObject cannot be null"
    }

}