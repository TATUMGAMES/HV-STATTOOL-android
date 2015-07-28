package com.tatumgames.stattool.logger;

import android.util.Log;

import com.tatumgames.stattool.Constants.Constants;


public class Logger {
	
    /**
     * Helper method for logging e-verbose
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if(Constants.DEBUG) {
            if (msg != null) {
                Log.e(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging d-verbose
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if(Constants.DEBUG) {
            if (msg != null) {
                Log.d(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging i-verbose
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if(Constants.DEBUG) {
            if (msg != null) {
                Log.i(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging v-verbose
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if(Constants.DEBUG) {
            if (msg != null) {
                Log.v(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging w-verbose
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if(Constants.DEBUG) {
            if (msg != null) {
                Log.w(tag, msg);
            }
        }
    }

    /**
     * Helper method to display data on Console
     *
     * @param msg	message to be displayed
     * @return null
     */
    public static void printOnConsole(String msg){
        if(Constants.DEBUG){
            System.out.println(msg);
        }
    }	

}
