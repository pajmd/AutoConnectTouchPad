package com.pjmd.wifimanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static android.net.wifi.WifiManager.EXTRA_WIFI_STATE;

/**
 * Created by pjmd on 04/03/18.
 */

public class MyWifiReceiver extends BroadcastReceiver {

    private static final boolean FINAL_CONSTANT_IS_LOCAL = true;
    private static final String TAG = MyWifiReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getLogTagWithMethod(), "intent=" + intent);
        String extras = getAllExtras(intent);
        int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 5555);
        Log.d(getLogTagWithMethod(), "Receiver received intent with extras: %s - the wifi state is: %d".format(extras, state));
        StringBuilder message = new StringBuilder("Wifi monitoring: ");
        message.append(extras)
                .append(" will connect to internet if nedded");
        NotificationSender.sendNotification(context, message.toString(), 10, getLogTagWithMethod());
        if ( !InternetConnectivity.isConnectivity(context, getLogTagWithMethod())) {
            Log.d(getLogTagWithMethod(), "Connecting to WIFI");
            InternetConnectivity.connectWifi(context, getLogTagWithMethod());
        }
    }

    private String getAllExtras(Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Extras: ");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras != null) {
                for (String key : extras.keySet()) {
                    try {
                        Object value = extras.get(key);
                        Log.d(getLogTagWithMethod(), String.format("Extra: %s value: %s type: (%s)", key,
                                value.toString(), value.getClass().getName()));
                        sb
                                .append("key: ").append(key)
                                .append(" value: ").append(value.toString())
                                .append("\n");
                    } catch (Exception e) {
                        Log.d(getLogTagWithMethod(), "Oops getting extra " + key);
                    }
                }
            }
        }
        return sb.toString();
    }

    private String getLogTagWithMethod() {
        if (FINAL_CONSTANT_IS_LOCAL) {
            Throwable stack = new Throwable().fillInStackTrace();
            StackTraceElement[] trace = stack.getStackTrace();
            return trace[0].getClassName() + "." + trace[0].getMethodName() + ":" + trace[0].getLineNumber();
        } else {
            return TAG;
        }
    }
}
