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

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log

import com.tatumgames.stattool.utils.Utils

import java.util.Locale

/**
 * Created by leonard on 5/2/2018.
 */

object NetworkUtils {
    private val TAG = NetworkUtils::class.java!!.getSimpleName()

    /**
     * Method is used to check is network is available e.g. both connected and available
     *
     * @param context Interface to global information about an application environment
     * @return True if network is available otherwise false
     */
    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        //check general connectivity
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (!Utils.checkIfNull(conMgr)) {
            if (!Utils.checkIfNull(conMgr.activeNetworkInfo) &&
                    conMgr.activeNetworkInfo.isConnected &&
                    conMgr.activeNetworkInfo.isAvailable) {
                Log.i(TAG, "Active Connection")
                return true
            }
        }
        Log.i(TAG, "No Connection")
        return false
    }

    /**
     * Method is used to retrieve network IP Address
     *
     * @param context Interface to global information about an application environment
     * @return
     */
    fun getIPAddr(context: Context): String {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ip = wifiInfo.ipAddress

        val strIPAddr = String.format(Locale.US, "%d.%d.%d.%d",
                ip and 0xff,
                ip shr 8 and 0xff,
                ip shr 16 and 0xff,
                ip shr 24 and 0xff)
        Log.d(TAG, strIPAddr)
        return strIPAddr
    }

    /**
     * Method is used to get the network info
     *
     * @param context Interface to global information about an application environment
     * @return Details about the currently active default data network
     */
    @SuppressLint("MissingPermission")
    fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * Method is used to get the state of Airplane Mode
     *
     * @param context Interface to global information about an application environment
     * @return True if airplane mode is enabled, otherwise false
     */
    fun isAirplaneModeOn(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.getInt(context.contentResolver, Settings.System.AIRPLANE_MODE_ON, 0) == 1
        } else {
            Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        }
    }

    /**
     * Check if there is any connectivity
     *
     * @param context Interface to global information about an application environment
     * @return True if network is active otherwise false
     */
    fun isConnected(context: Context): Boolean {
        val info = NetworkUtils.getNetworkInfo(context)
        return info != null && info.isConnectedOrConnecting
    }

    /**
     * Check if there is any connectivity to a WIFI network
     *
     * @param context Interface to global information about an application environment
     * @return True if network is active otherwise false
     */
    fun isConnectedWifi(context: Context): Boolean {
        val info = NetworkUtils.getNetworkInfo(context)
        return info != null && info.isConnectedOrConnecting && info.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context Interface to global information about an application environment
     * @return True if network is active otherwise false
     */
    fun isConnectedMobile(context: Context): Boolean {
        val info = NetworkUtils.getNetworkInfo(context)
        return info != null && info.isConnectedOrConnecting && info.type == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context Interface to global information about an application environment
     * @return True if network is active otherwise false
     */
    fun isConnectedFast(context: Context): Boolean {
        val info = NetworkUtils.getNetworkInfo(context)
        return info != null && info.isConnectedOrConnecting && NetworkUtils.isConnectionFast(info.type, info.subtype)
    }

    /**
     * Check if there is fast connectivity
     *
     * @param type    Mobile data type
     * @param subType Network-type-specific integer describing the subtype of the network
     * @return True if connection is fast
     */
    private fun isConnectionFast(type: Int, subType: Int): Boolean {
        if (type == ConnectivityManager.TYPE_WIFI) {
            Log.i(TAG, "WIFI Connection")
            return true
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            when (subType) {
                TelephonyManager.NETWORK_TYPE_1xRTT -> {
                    Log.v(TAG, "WEAK: ~ 50-100 kbps")
                    return false // ~ 50-100 kbps
                }
                TelephonyManager.NETWORK_TYPE_CDMA -> {
                    Log.v(TAG, "WEAK: ~ 14-64 kbps")
                    return false // ~ 14-64 kbps
                }
                TelephonyManager.NETWORK_TYPE_EDGE -> {
                    Log.v(TAG, "WEAK: ~ 50-100 kbps")
                    return false // ~ 50-100 kbps
                }
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> {
                    Log.v(TAG, "STRONG: ~ 400-1000 kbps")
                    return true // ~ 400-1000 kbps
                }
                TelephonyManager.NETWORK_TYPE_EVDO_A -> {
                    Log.v(TAG, "STRONG: ~ 600-1400 kbps")
                    return true // ~ 600-1400 kbps
                }
                TelephonyManager.NETWORK_TYPE_GPRS -> {
                    Log.v(TAG, "WEAK: ~ 100 kbps")
                    return false // ~ 100 kbps
                }
                TelephonyManager.NETWORK_TYPE_HSDPA -> {
                    Log.v(TAG, "STRONG: ~ 2-14 Mbps")
                    return true // ~ 2-14 Mbps
                }
                TelephonyManager.NETWORK_TYPE_HSPA -> {
                    Log.v(TAG, "STRONG: ~ 700-1700 kbps")
                    return true // ~ 700-1700 kbps
                }
                TelephonyManager.NETWORK_TYPE_HSUPA -> {
                    Log.v(TAG, "STRONG: ~ 1-23 Mbps")
                    return true // ~ 1-23 Mbps
                }
                TelephonyManager.NETWORK_TYPE_UMTS -> {
                    Log.v(TAG, "STRONG: ~ 400-7000 kbps")
                    return true // ~ 400-7000 kbps
                }
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                TelephonyManager.NETWORK_TYPE_EHRPD // API level 11
                -> {
                    Log.v(TAG, "STRONG: ~ 1-2 Mbps")
                    return true // ~ 1-2 Mbps
                }
                TelephonyManager.NETWORK_TYPE_EVDO_B // API level 9
                -> {
                    Log.v(TAG, "STRONG: ~ 5 Mbps")
                    return true // ~ 5 Mbps
                }
                TelephonyManager.NETWORK_TYPE_HSPAP // API level 13
                -> {
                    Log.v(TAG, "STRONG: ~ 10-20 Mbps")
                    return true // ~ 10-20 Mbps
                }
                TelephonyManager.NETWORK_TYPE_IDEN // API level 8
                -> {
                    Log.v(TAG, "WEAK: ~25 kbps")
                    return false // ~25 kbps
                }
                TelephonyManager.NETWORK_TYPE_LTE // API level 11
                -> {
                    Log.v(TAG, "STRONG: ~ 10+ Mbps")
                    return true // ~ 10+ Mbps
                }
                // Unknown
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> return false
                else -> return false
            }
        } else {
            Log.e(TAG, "No Network Info")
            return false
        }
    }
}
