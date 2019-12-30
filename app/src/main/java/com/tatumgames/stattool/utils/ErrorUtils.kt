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

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.tatumgames.stattool.R

import de.keyboardsurfer.android.widget.crouton.Configuration
import de.keyboardsurfer.android.widget.crouton.Crouton
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback

/**
 * Created by leonard on 5/2/2018.
 */

class ErrorUtils {
    /**
     * Method is used to check if message is showing
     *
     * @return True if message is showing, otherwise false
     */
    var isShowing: Boolean = false
        private set
    private var mCrouton: Crouton? = null

    /**
     * @param activity An activity is a single, focused thing that the user can do
     * @param error    The error to display
     */
    fun showError(activity: Activity, error: String) {
        if (!isShowing) {
            try {
                val view = activity.layoutInflater.inflate(R.layout.error_layout, null, false)
                val CONFIGURATION_INFINITE = Configuration.Builder().setDuration(DURATION).build()
                mCrouton = Crouton.make(activity, view, R.id.iv_error_close, CONFIGURATION_INFINITE)
                // set listener for crouton
                mCrouton!!.setLifecycleCallback(object : LifecycleCallback {
                    override fun onDisplayed() {
                        // set flag
                        isShowing = true
                    }

                    override fun onRemoved() {
                        // reset
                        isShowing = false
                    }
                })
                // instantiate views
                val tvErrorMessage = view.findViewById<TextView>(R.id.tv_error)
                val ivClose = view.findViewById<ImageView>(R.id.iv_error_close)
                // set text
                tvErrorMessage.text = error
                // set listener for close button
                ivClose.setOnClickListener {
                    // close crouton
                    mCrouton!!.hide()
                }

                // show error message
                mCrouton!!.show()
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Method is used to dismiss error message
     */
    fun dismiss() {
        if (!Utils.checkIfNull<Crouton>(mCrouton) && isShowing) {
            mCrouton!!.hide()
        }
    }

    companion object {
        // how long to display Crouton before it dismisses
        private val DURATION = 7000
    }
}
