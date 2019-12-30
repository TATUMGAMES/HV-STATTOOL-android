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

import android.annotation.SuppressLint
import android.content.Context

import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.tatumgames.stattool.utils.Utils

import java.util.LinkedHashMap

open class BaseRequest
/**
 * Instantiate context and volley request queue
 *
 * @param context Interface to global information about an application environment
 */
internal constructor(private val mContext: Context) {
    private var mRequestQueue: RequestQueue? = null
    private var mImageLoader: ImageLoader? = null

    /**
     * The RequestQueue manages worker threads for running the network operations, reading from
     * and writing to the cache, and parsing responses
     *
     * A RequestQueue needs two things to do its job: a network to perform transport of the
     * requests, and a cache to handle caching
     *
     * @return The [com.android.volley.RequestQueue] object
     */
    private val requestQueue: RequestQueue?
        get() {
            if (Utils.checkIfNull<RequestQueue>(mRequestQueue) && !Utils.checkIfNull(mContext)) {
                mRequestQueue = Volley.newRequestQueue(mContext)
            }
            return mRequestQueue
        }

    /**
     * Method is used to load image into ImageView instead of NetworkImageView
     *
     * @return Multithread image loading (asynchronous or synchronous)
     */
    val imageLoader: ImageLoader?
        get() {
            requestQueue
            if (Utils.checkIfNull<ImageLoader>(mImageLoader)) {
                mImageLoader = ImageLoader(mInstance!!.mRequestQueue, LruBitmapCache())
            }
            return mInstance!!.mImageLoader
        }

    /**
     * Method is used to get headers
     *
     * @return The REST headers and parameters contain a wealth of information that can
     * help you track down issues when you encounter them
     */
    private val defaultHeaders: LinkedHashMap<String, String>
        get() = LinkedHashMap()

    init {
        mRequestQueue = Volley.newRequestQueue(mContext)
    }

    /**
     * Method is used to add request with custom tag
     *
     * @param request Request that moves through the pipeline, gets serviced, and has its
     * raw response parsed and delivered
     * @param tag     The tag name
     * @param <T>     Generics
    </T> */
    protected fun <T> addToRequestQueue(request: CustomJsonObjectRequest, tag: String) {
        // set the default tag if tag is empty
        request.tag = if (Utils.isStringEmpty(tag)) TAG else tag
        request.setCustomHeaders(defaultHeaders)
        requestQueue!!.add<JSONObject>(request)
    }

    /**
     * Method is used to add request with default tag
     *
     * @param request Request that moves through the pipeline, gets serviced, and has its
     * raw response parsed and delivered
     * @param <T>     Generics
    </T> */
    internal fun <T> addToRequestQueue(request: CustomJsonObjectRequest) {
        request.tag = TAG
        request.setCustomHeaders(defaultHeaders)
        requestQueue!!.add<JSONObject>(request)
    }

    /**
     * Method is used to cancel pending requests by tag
     *
     * @param tag The tag name
     */
    protected fun cancelPendingRequests(tag: Any) {
        if (!Utils.checkIfNull<RequestQueue>(mRequestQueue)) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        private val TAG = BaseRequest::class.java!!.getSimpleName()

        @SuppressLint("StaticFieldLeak")
        private var mInstance: BaseRequest? = null

        /**
         * Singleton pattern
         *
         * Implemented a singleton class that encapsulates BaseRequest and other Volley functionality
         *
         * @param context Interface to global information about an application environment
         * @return Instance of [BaseRequest]
         */
        @Synchronized
        fun getInstance(context: Context): BaseRequest {
            if (Utils.checkIfNull<BaseRequest>(mInstance)) {
                mInstance = BaseRequest(context)
            }
            return mInstance
        }
    }

}
