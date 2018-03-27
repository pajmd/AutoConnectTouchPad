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
    

    private Set<Integer> notification_ids = new TreeSet<Integer>();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(getLogTagWithMethod(), "intent=" + intent);
        String extras = getAllExtras(intent);
        int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 5555);
        Log.d(getLogTagWithMethod(), "Receiver received intent with extras: %s - the wifi state is: %d".format(extras, state));
        this.sendNotification(context, extras, 10);
        if ( !isConnectivity(context)) {
            Log.d(getLogTagWithMethod(), "Connecting to WIFI");
            connectWifi(context);
        }
    }

    private int registerNotificationId(int i) {
        notification_ids.add(new Integer(i));
        return i;
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

    private void sendNotification(Context context, String extras , int id) {
        registerNotificationId(id);
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm =
                (NotificationManager)context.getSystemService(ns);
        clearOtherNotifications(nm, id);
        //Prepare Notification Object Details
        int icon = R.drawable.robot;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ - ");
        Date dt = new Date();
        String strDate = df.format(dt);
        StringBuilder tickerText = new StringBuilder();
        tickerText.append(strDate)
                .append("Wifi monitoring")
                .append(extras)
                .append(" will connect to internet if nedded");
        Log.d(getLogTagWithMethod(), tickerText.toString());
        long when = System.currentTimeMillis();
        //Create the notification object through the builder
        Notification notification =
                new Notification.Builder(context)
                        .setContentTitle("Wifi monitor")
                        .setContentText(tickerText.toString())
                        .setSmallIcon(icon)
                        .setWhen(when)
                        .setContentInfo("Addtional Information:Content Info")
                        .build();
        nm.notify(id, notification);
        Log.d(getLogTagWithMethod(), extras + " notification sent");
    }

    private void clearOtherNotifications(NotificationManager nm, int id) {
        Iterator<Integer> it = notification_ids.iterator();
        while(it.hasNext()) {
            Integer notif_id  = it.next();
        }
        for(int nid : notification_ids) {
            if( nid != id ) {
                nm.cancel(nid);
            }
        }
    }

    private void connectWifi(Context app_context) {
        WifiManager wifi_manager = Utils.reconectWifi(getLogTagWithMethod(), app_context);
        if( wifi_manager != null) {
            WifiInfo current_wifi = wifi_manager.getConnectionInfo();
            sendNotification(app_context, "Connected to " + current_wifi.getSSID(), 11);
        }
        else {
            sendNotification(app_context, "No WIFI configuration available", 12);
        }
    }

    private boolean isConnectivity(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        boolean bConnectivity = true;
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
            Log.d(getLogTagWithMethod(), "Have Wifi Connection");
        else {
            bConnectivity = false;
            Log.d(getLogTagWithMethod(), "Don't have Wifi Connection");
        }
        return bConnectivity;
    }

    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.d(getLogTagWithMethod(), isConnected ?  "connected to internet" : "not connected to internet");
        return isConnected;
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
