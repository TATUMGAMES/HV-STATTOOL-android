package com.tatumgames.stattool.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.tatumgames.stattool.logger.Logger;
import com.tatumgames.stattool.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkReceiver.class.getSimpleName();

    private static final List<NetworkStatusObserver> mObserverList = new ArrayList<>();
    private static boolean isNetworkConnected = true;

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        Logger.i(TAG, "onReceive() broadcast");
        boolean disconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        boolean isNetworkConnectedCurrent;

        if (disconnected) {
            isNetworkConnectedCurrent = false;
        } else {
            NetworkInfo networkInfo;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            networkInfo = cm.getActiveNetworkInfo();
            isNetworkConnectedCurrent = !Utils.checkIfNull(networkInfo) && networkInfo.isConnectedOrConnecting();
        }

        if (isNetworkConnectedCurrent != isNetworkConnected) {
            isNetworkConnected = isNetworkConnectedCurrent;
            Logger.d(TAG, "NetworkStatus.onReceive - isNetworkConnected: " + isNetworkConnected);
            notifyObservers(isNetworkConnected);
        }
    }

    /**
     * Lets all {@link NetworkStatusObserver}s know if the device is connected to a network
     *
     * @param isNetworkConnectedCurrent True if device is connected to a network
     */
    private void notifyObservers(Boolean isNetworkConnectedCurrent) {
        for (NetworkStatusObserver networkStatusObserver : mObserverList) {
            networkStatusObserver.notifyConnectionChange(isNetworkConnectedCurrent);
        }
    }

    /**
     * Add observer to observer list
     *
     * @param observer List of observers that track network activity
     */
    public void addObserver(NetworkStatusObserver observer) {
        mObserverList.add(observer);
    }

    /**
     * Remove observer from observer list
     *
     * @param observer List of observers that track network activity
     */
    public void removeObserver(NetworkStatusObserver observer) {
        mObserverList.remove(observer);
    }

    /**
     * Retrieve observer list size
     *
     * @return The observer list size
     */
    public int getObserverSize() {
        return mObserverList.size();
    }

    /**
     * Check if receiver is added to observer list
     *
     * @param observer List of observers that track network activity
     * @return True if receiver is added to observer list
     */
    public boolean contains(NetworkStatusObserver observer) {
        return mObserverList.contains(observer);
    }

    /**
     * Method is used to print observer list
     */
    public void printObserverList() {
        Logger.i(TAG, "===== PRINT OBSERVER LIST ===== ");
        for (int i = 0; i < mObserverList.size(); i++) {
            Logger.i(TAG, String.format(Locale.US, "item(%d): %s", i, mObserverList.get(i).toString()));
        }
    }

    /**
     * Interface for monitoring network status change
     */
    public interface NetworkStatusObserver {
        void notifyConnectionChange(boolean isConnected);
    }
}
