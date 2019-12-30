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

import android.graphics.Bitmap
import android.support.v4.util.LruCache

import com.android.volley.toolbox.ImageLoader

class LruBitmapCache
/**
 * Constructor
 *
 * @param sizeInKiloBytes Returns the maximum number of bytes the heap can expand to
 */
private constructor(sizeInKiloBytes: Int) : LruCache<String, Bitmap>(sizeInKiloBytes), ImageLoader.ImageCache {

    /**
     * Retrieve the default Lru cache size
     *
     * @return Returns the maximum number of bytes the heap can expand to
     */
    private// use 1/8th of the available memory for this memory cache
    val defaultLruCacheSize: Int
        get() {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            return maxMemory / 8
        }

    /**
     * Constructor
     */
    internal constructor() : this(defaultLruCacheSize) {}

    override fun sizeOf(key: String?, value: Bitmap): Int {
        return value.rowBytes * value.height / 1024
    }

    override fun getBitmap(url: String): Bitmap {
        return get(url)
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        put(url, bitmap)
    }
}
