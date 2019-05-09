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

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.tatumgames.stattool.utils.Utils;

import java.util.LinkedHashMap;

/**
 * Created by leonard on 5/2/2018.
 */
public class BaseRequest {
    private static final String TAG = BaseRequest.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static BaseRequest mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;

    /**
     * Instantiate context and volley request queue
     *
     * @param context Interface to global information about an application environment
     */
    protected BaseRequest(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Singleton pattern
     * <p>Implemented a singleton class that encapsulates BaseRequest and other Volley functionality</p>
     *
     * @param context Interface to global information about an application environment
     * @return Instance of {@link BaseRequest}
     */
    public static synchronized BaseRequest getInstance(Context context) {
        if (Utils.checkIfNull(mInstance)) {
            mInstance = new BaseRequest((context));
        }
        return mInstance;
    }

    /**
     * The RequestQueue manages worker threads for running the network operations, reading from
     * and writing to the cache, and parsing responses
     * <p>A RequestQueue needs two things to do its job: a network to perform transport of the
     * requests, and a cache to handle caching</p>
     *
     * @return The {@link com.android.volley.RequestQueue} object
     */
    protected RequestQueue getRequestQueue() {
        if (Utils.checkIfNull(mRequestQueue) && !Utils.checkIfNull(mContext)) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    /**
     * Method is used to load image into ImageView instead of NetworkImageView
     *
     * @return Multithread image loading (asynchronous or synchronous)
     */
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (Utils.checkIfNull(mImageLoader)) {
            mImageLoader = new ImageLoader(mInstance.mRequestQueue, new LruBitmapCache());
        }
        return mInstance.mImageLoader;
    }

    /**
     * Method is used to add request with custom tag
     *
     * @param request Request that moves through the pipeline, gets serviced, and has its
     *                raw response parsed and delivered
     * @param tag     The tag name
     * @param <T>     Generics
     */
    protected <T> void addToRequestQueue(final CustomJsonObjectRequest request, String tag) {
        // set the default tag if tag is empty
        request.setTag(Utils.isStringEmpty(tag) ? TAG : tag);
        request.setCustomHeaders(getDefaultHeaders());
        getRequestQueue().add(request);
    }

    /**
     * Method is used to add request with default tag
     *
     * @param request Request that moves through the pipeline, gets serviced, and has its
     *                raw response parsed and delivered
     * @param <T>     Generics
     */
    protected <T> void addToRequestQueue(final CustomJsonObjectRequest request) {
        request.setTag(TAG);
        request.setCustomHeaders(getDefaultHeaders());
        getRequestQueue().add(request);
    }

    /**
     * Method is used to cancel pending requests by tag
     *
     * @param tag The tag name
     */
    protected void cancelPendingRequests(Object tag) {
        if (!Utils.checkIfNull(mRequestQueue)) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Method is used to get headers
     *
     * @return The REST headers and parameters contain a wealth of information that can
     * help you track down issues when you encounter them
     */
    private LinkedHashMap<String, String> getDefaultHeaders() {
        return new LinkedHashMap<>();
    }

}
