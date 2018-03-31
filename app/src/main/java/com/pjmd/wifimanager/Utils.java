package com.pjmd.wifimanager;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by pjmd on 18/03/18.
 */

public class Utils {

    public static WifiManager reconectWifi(String TAG,  Context app_context) {
        WifiManager wifi_manager = app_context.getSystemService(WifiManager.class);

        List<WifiConfiguration> confs = wifi_manager.getConfiguredNetworks();
        WifiConfiguration conf = null;
        if (confs != null) {
            StringBuilder sidb = new StringBuilder("\"")
                    .append(app_context.getResources().getString(R.string.fifi_name))
                    .append("\"");
            String sid = sidb.toString();
            for (WifiConfiguration confi : confs) {
                if (confi.SSID.equals(sid)) {
                    conf = confi;
                    break;
                }
            }
            if (conf == null) {
                conf = new WifiConfiguration();
                conf.SSID = sid;
                StringBuilder key = new StringBuilder("\"")
                        .append(app_context.getResources().getString(R.string.fifi_pass))
                        .append("\"");
                conf.preSharedKey = key.toString();
                Log.v(TAG, "Connecting to: " + conf.toString());
            }
            int net_id = wifi_manager.addNetwork(conf);
            wifi_manager.disconnect();
            boolean enable_network = wifi_manager.enableNetwork(net_id, true);
            Log.v(TAG, "enable_network : " + (enable_network ? "True" : "False"));

            boolean reconnect = wifi_manager.reconnect();
            Log.v(TAG, "reconnected : " + (reconnect ? "True" : "False"));

            return wifi_manager;

        } else {
            Log.d(TAG, "There is no configuration, Airplane mode?");
            return null;
        }
    }
}
