package com.pjmd.wifimanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by pjmd on 27/03/18.
 * Sends notification and clears old ones.
 * Notification ID are predetermined 10, 11 ...
 */

public class NotificationSender {
    static private Set<Integer> notification_ids = new TreeSet<Integer>();

    static public void sendNotification(Context context, String message , int id, String tag) {
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
                .append(message);
        Log.d(tag, tickerText.toString());
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
        Log.d(tag, message + " notification sent");
    }

    static private void clearOtherNotifications(NotificationManager nm, int id) {
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


    static private int registerNotificationId(int i) {
        notification_ids.add(new Integer(i));
        return i;
    }

}
