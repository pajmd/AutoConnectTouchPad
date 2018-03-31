package com.pjmd.wifimanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by pjmd on 27/03/18.
 */

public class InternetConnectivity {


    public static boolean isConnectivity(Context context, String tag) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        boolean bConnectivity = true;
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
            Log.d(tag, "Have Wifi Connection");
        else {
            bConnectivity = false;
            Log.d(tag, "Don't have Wifi Connection");
        }
        return bConnectivity;
    }

    public static boolean isConnectedToInternet(Context context, String tag) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.d(tag, isConnected ?  "connected to internet" : "not connected to internet");
        return isConnected;
    }


    public static void connectWifi(Context app_context, String tag) {
        WifiManager wifi_manager = Utils.reconectWifi(tag, app_context);
        if( wifi_manager != null) {
            WifiInfo current_wifi = wifi_manager.getConnectionInfo();
            NotificationSender.sendNotification(app_context, "Connected to " + current_wifi.getSSID(), 11, tag);
        }
        else {
            NotificationSender.sendNotification(app_context, "No WIFI configuration available", 12, tag);
        }
    }

}
