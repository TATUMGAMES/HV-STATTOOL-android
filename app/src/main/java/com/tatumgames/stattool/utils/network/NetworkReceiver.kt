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

package com.tatumgames.stattool.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

import com.tatumgames.stattool.logger.Logger
import com.tatumgames.stattool.utils.Utils

import java.util.ArrayList
import java.util.Locale

class NetworkReceiver : BroadcastReceiver() {

    /**
     * Retrieve observer list size
     *
     * @return The observer list size
     */
    val observerSize: Int
        get() = mObserverList.size

    override fun onReceive(context: Context, intent: Intent) {
        Logger.i(TAG, "onReceive() broadcast")
        val disconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        val isNetworkConnectedCurrent: Boolean

        if (disconnected) {
            isNetworkConnectedCurrent = false
        } else {
            val networkInfo: NetworkInfo
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            assert(cm != null)
            networkInfo = cm.activeNetworkInfo
            isNetworkConnectedCurrent = !Utils.checkIfNull(networkInfo) && networkInfo.isConnectedOrConnecting
        }

        if (isNetworkConnectedCurrent != isNetworkConnected) {
            isNetworkConnected = isNetworkConnectedCurrent
            Logger.d(TAG, "NetworkStatus.onReceive - isNetworkConnected: $isNetworkConnected")
            notifyObservers(isNetworkConnected)
        }
    }

    /**
     * Lets all [NetworkStatusObserver]s know if the device is connected to a network
     *
     * @param isNetworkConnectedCurrent True if device is connected to a network
     */
    private fun notifyObservers(isNetworkConnectedCurrent: Boolean?) {
        for (networkStatusObserver in mObserverList) {
            networkStatusObserver.notifyConnectionChange(isNetworkConnectedCurrent!!)
        }
    }

    /**
     * Add observer to observer list
     *
     * @param observer List of observers that track network activity
     */
    fun addObserver(observer: NetworkStatusObserver) {
        mObserverList.add(observer)
    }

    /**
     * Remove observer from observer list
     *
     * @param observer List of observers that track network activity
     */
    fun removeObserver(observer: NetworkStatusObserver) {
        mObserverList.remove(observer)
    }

    /**
     * Check if receiver is added to observer list
     *
     * @param observer List of observers that track network activity
     * @return True if receiver is added to observer list
     */
    operator fun contains(observer: NetworkStatusObserver): Boolean {
        return mObserverList.contains(observer)
    }

    /**
     * Method is used to print observer list
     */
    fun printObserverList() {
        Logger.i(TAG, "===== PRINT OBSERVER LIST ===== ")
        for (i in mObserverList.indices) {
            Logger.i(TAG, String.format(Locale.US, "item(%d): %s", i, mObserverList[i].toString()))
        }
    }

    /**
     * Interface for monitoring network status change
     */
    interface NetworkStatusObserver {
        fun notifyConnectionChange(isConnected: Boolean)
    }

    companion object {
        private val TAG = NetworkReceiver::class.java!!.getSimpleName()

        private val mObserverList = ArrayList<NetworkStatusObserver>()
        private var isNetworkConnected = true
    }
}
