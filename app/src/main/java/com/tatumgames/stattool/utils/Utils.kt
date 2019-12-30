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

package com.tatumgames.stattool.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by LJTat on 2/23/2017.
 */
object Utils {

    private val EMPTY = ""
    private val NULL = "null"

    // click control threshold
    private val CLICK_THRESHOLD = 300
    private var mLastClickTime: Long = 0

    /**
     * Method is used to control clicks on views. Clicking views repeatedly and quickly will
     * sometime cause crashes when objects and views are not fully animated or instantiated.
     * This helper method helps minimize and control UI interaction and flow
     *
     * @return
     */
    /*
         * @Note: Android queues button clicks so it doesn't matter how fast or slow
         * your onClick() executes, simultaneous clicks will still occur. Therefore solutions
         * such as disabling button clicks via flags or conditions statements will not work.
         * The best solution is to timestamp the click processes and return back clicks
         * that occur within a designated window (currently 300 ms) --LT
         */ val isViewClickable: Boolean
        get() {
            val mCurrClickTimestamp = SystemClock.uptimeMillis()
            val mElapsedTimestamp = mCurrClickTimestamp - mLastClickTime
            mLastClickTime = mCurrClickTimestamp
            return mElapsedTimestamp > CLICK_THRESHOLD
        }

    /**
     * Method checks if String value is empty
     *
     * @param str String value to be checked if null
     * @return string True if string is empty, otherwise false
     */
    fun isStringEmpty(str: String?): Boolean {
        return str == null || str.length == 0 || EMPTY == str.trim { it <= ' ' } || NULL == str
    }

    /**
     * Method is used to check if objects are null
     *
     * @param objectToCheck Generic object to be checked if null
     * @param <T>           Generics
     * @return true if objectToCheck is null
    </T> */
    fun <T> checkIfNull(objectToCheck: T?): Boolean {
        return objectToCheck == null
    }

    /**
     * Method is used to get color by id
     *
     * @param context Interface to global information about an application environment
     * @param id      Resource id
     * @return
     */
    fun getColor(context: Context, id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, id)
        } else {
            context.resources.getColor(id)
        }
    }

    /**
     * Method is used to get drawable by id
     *
     * @param context Interface to global information about an application environment
     * @param id      Resource id
     * @return
     */
    fun getDrawable(context: Context, id: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getDrawable(context, id)
        } else {
            context.resources.getDrawable(id)
        }
    }

    /**
     * Method is used to set visibility of views to VISIBLE
     *
     * @param params views to set visibility to VISIBLE
     */
    fun setViewVisible(vararg params: View) {
        for (v in params) {
            if (!checkIfNull(v)) {
                v.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Method is used to set visibility of views to GONE
     *
     * @param params views to set visibility to GONE
     */
    fun setViewGone(vararg params: View) {
        for (v in params) {
            if (!checkIfNull(v)) {
                v.visibility = View.GONE
            }
        }
    }

    /**
     * Method is used to show virtual keyboard
     *
     * @param context Interface to global information about an application environment
     * @param binder  Base interface for a remotable object, the core part of a lightweight
     * remote procedure call mechanism designed for high performance when
     * performing in-process and cross-process calls
     */
    fun showKeyboard(context: Context, binder: IBinder) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(binder, InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * Method is used to hide virtual keyboard
     *
     * @param context Interface to global information about an application environment
     * @param binder  Base interface for a remotable object, the core part of a lightweight
     * remote procedure call mechanism designed for high performance when
     * performing in-process and cross-process calls
     */
    fun hideKeyboard(context: Context, binder: IBinder) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binder, 0)
    }
}
