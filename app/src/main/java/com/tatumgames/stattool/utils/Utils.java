package com.tatumgames.stattool.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by LJTat on 2/23/2017.
 */
public class Utils {

    private static final String EMPTY = "";
    private static final String NULL = "null";

    // click control threshold
    private static final int CLICK_THRESHOLD = 300;
    private static long mLastClickTime;

    /**
     * Method checks if String value is empty
     *
     * @param str
     * @return string
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0 || EMPTY.equals(str.trim()) || NULL.equals(str);
    }

    /**
     * Method is used to check if objects are null
     *
     * @param objectToCheck
     * @param <T>
     * @return true if objectToCheck is null
     */
    public static <T> boolean checkIfNull(T objectToCheck) {
        return objectToCheck == null;
    }

    /**
     * Method is used to get color by id
     *
     * @param context
     * @param id
     * @return
     */
    public static final int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /**
     * Method is used to get drawable by id
     *
     * @param context
     * @param id
     * @return
     */
    public static final Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    /**
     * Method is used to control clicks on views. Clicking views repeatedly and quickly will
     * sometime cause crashes when objects and views are not fully animated or instantiated.
     * This helper method helps minimize and control UI interaction and flow
     *
     * @return
     */
    public static boolean isViewClickable() {
        /*
         * @Note: Android queues button clicks so it doesn't matter how fast or slow
         * your onClick() executes, simultaneous clicks will still occur. Therefore solutions
         * such as disabling button clicks via flags or conditions statements will not work.
         * The best solution is to timestamp the click processes and return back clicks
         * that occur within a designated window (currently 300 ms) --LT
         */
        long mCurrClickTimestamp = SystemClock.uptimeMillis();
        long mElapsedTimestamp = mCurrClickTimestamp - mLastClickTime;
        mLastClickTime = mCurrClickTimestamp;
        return !(mElapsedTimestamp <= CLICK_THRESHOLD);
    }

    /**
     * Method is used to set visibility of views to VISIBLE
     *
     * @param params views to set visibility to VISIBLE
     */
    public static void setViewVisible(View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Method is used to set visibility of views to GONE
     *
     * @param params views to set visibility to GONE
     */
    public static void setViewGone(View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Method is used to show virtual keyboard
     *
     * @param context
     * @param binder
     */
    public static void showKeyboard(Context context, IBinder binder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(binder, InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Method is used to hide virtual keyboard
     *
     * @param context
     * @param binder
     */
    public static void hideKeyboard(Context context, IBinder binder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binder, 0);
    }
}
