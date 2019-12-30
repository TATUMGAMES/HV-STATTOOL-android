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
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v4.content.ContextCompat
import android.view.View

import com.tatumgames.stattool.R
import com.tatumgames.stattool.utils.network.NetworkUtils

object DialogUtils {

    private var mDialog: Dialog? = null

    // click listener for default dialog
    private val mDefaultListener = DialogInterface.OnClickListener { dialog, which -> dismissDialog() }

    private var mNetworkDialog: AlertDialog? = null
    private var mProgressDialog: ProgressDialog? = null // display during processing requests/responses

    /**
     * Method is used to dismiss dialog
     */
    fun dismissDialog() {
        try {
            if (!Utils.checkIfNull<Dialog>(mDialog) && mDialog!!.isShowing) {
                mDialog!!.dismiss()
                mDialog = null
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    /**
     * Method is used to dismiss progress dialog
     */
    fun dismissProgressDialog() {
        try {
            if (!Utils.checkIfNull<ProgressDialog>(mProgressDialog) && mProgressDialog!!.isShowing) {
                mProgressDialog!!.dismiss()
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    /**
     * Method is used to dismiss no network dialog
     */
    fun dismissNoNetworkDialog() {
        try {
            if (!Utils.checkIfNull<AlertDialog>(mNetworkDialog) && mNetworkDialog!!.isShowing) {
                mNetworkDialog!!.dismiss()
                mNetworkDialog = null
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    /**
     * @param context Interface to global information about an application environment
     * @param title   The displayed title
     * @param msg     The displayed message
     */
    fun showDefaultNoNetworkAlert(context: Context, title: String, msg: String) {
        if ((context as Activity).isFinishing || !Utils.checkIfNull<AlertDialog>(mNetworkDialog) && mNetworkDialog!!.isShowing) {
            return
        }

        val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.retry), null)
        // create builder and set equal to dialog
        mNetworkDialog = builder.create()

        if (!Utils.checkIfNull<AlertDialog>(mNetworkDialog)) {
            if (!context.isFinishing) {
                // show dialog
                mNetworkDialog!!.show()
            }

            mNetworkDialog!!.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if (NetworkUtils.isNetworkAvailable(context) && NetworkUtils.isConnected(context)) {
                    dismissNoNetworkDialog()
                }
            }
        }
    }

    /**
     * Dialog constructor
     *
     * @param context  Interface to global information about an application environment
     * @param title    The displayed title
     * @param msg      The displayed message
     * @param listener Interface used to allow the creator of a dialog to run some code
     * when an item on the dialog is clicked
     */
    @JvmOverloads
    fun showDefaultOKAlert(context: Context, title: String, msg: String, listener: DialogInterface.OnClickListener? = null) {
        var listener = listener
        if ((context as Activity).isFinishing || !Utils.checkIfNull<Dialog>(mDialog) && mDialog!!.isShowing) {
            return
        }
        if (Utils.checkIfNull<OnClickListener>(listener)) {
            listener = mDefaultListener
        }

        val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok), listener)
        // create builder and set equal to dialog
        mDialog = builder.create()
        if (!context.isFinishing && !Utils.checkIfNull<Dialog>(mDialog)) {
            // show dialog
            mDialog!!.show()
        }
    }

    /**
     * Method is used to construct dialog with a message, and both negative and positive buttons
     *
     * @param context     Interface to global information about an application environment
     * @param msg         The displayed message
     * @param yesListener Interface used to allow the creator of a dialog to run some code
     * when an item on the dialog is clicked
     * @param noListener  Interface used to allow the creator of a dialog to run some code
     * when an item on the dialog is clicked
     */
    fun showYesNoAlert(context: Context, title: String, msg: String, yesText: String, noText: String,
                       yesListener: DialogInterface.OnClickListener, noListener: DialogInterface.OnClickListener) {
        var yesListener = yesListener
        var noListener = noListener
        if ((context as Activity).isFinishing || !Utils.checkIfNull<Dialog>(mDialog) && mDialog!!.isShowing) {
            return
        }

        val yes = if (!Utils.isStringEmpty(yesText))
            yesText
        else
            context.getResources().getString(R.string.yes)
        val no = if (!Utils.isStringEmpty(noText))
            noText
        else
            context.getResources().getString(R.string.no)

        if (Utils.checkIfNull<OnClickListener>(yesListener)) {
            yesListener = mDefaultListener
        }
        if (Utils.checkIfNull<OnClickListener>(noListener)) {
            noListener = mDefaultListener
        }

        val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(yes, yesListener)
                .setNegativeButton(no, noListener)
        // create builder and set equal to dialog
        mDialog = builder.create()
        if (!context.isFinishing && !Utils.checkIfNull<Dialog>(mDialog)) {
            // show dialog
            mDialog!!.show()
        }
    }

    /**
     * Method is used to display progress dialog. Call when processing requests/responses
     *
     * @param context Interface to global information about an application environment
     */
    fun showProgressDialog(context: Context) {
        if ((context as Activity).isFinishing || !Utils.checkIfNull<ProgressDialog>(mProgressDialog) && mProgressDialog!!.isShowing) {
            return
        }

        try {
            mProgressDialog = ProgressDialog.show(context, null, null, true, false)
            if (!Utils.checkIfNull<Window>(mProgressDialog!!.window)) {
                mProgressDialog!!.window!!.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent))
                mProgressDialog!!.setContentView(R.layout.dialog_progress)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }
}
/**
 * Dialog constructor
 *
 * @param context Interface to global information about an application environment
 * @param title   The displayed title
 * @param msg     The displayed message
 */
