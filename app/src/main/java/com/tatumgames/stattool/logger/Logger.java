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

package com.tatumgames.stattool.logger;

import android.util.Log;

import com.tatumgames.stattool.BuildConfig;

/**
 * Created by Tatum on 7/24/2015.
 */
public class Logger {

    /**
     * Helper method for logging e-verbose
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs
     * @param msg The message you would like logged
     */
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if (msg != null) {
                Log.e(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging d-verbose
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs
     * @param msg The message you would like logge
     */
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if (msg != null) {
                Log.d(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging i-verbose
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs
     * @param msg The message you would like logge
     */
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if (msg != null) {
                Log.i(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging v-verbose
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs
     * @param msg The message you would like logge
     */
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if (msg != null) {
                Log.v(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging w-verbose
     *
     * @param tag Used to identify the source of a log message. It usually identifies the class or
     *            activity where the log call occurs
     * @param msg The message you would like logge
     */
    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if (msg != null) {
                Log.w(tag, msg);
            }
        }
    }

    /**
     * Helper method to display data on Console
     *
     * @param msg message to be displayed
     */
    public static void printOnConsole(String msg) {
        if (BuildConfig.DEBUG) {
            System.out.println(msg);
        }
    }

}
