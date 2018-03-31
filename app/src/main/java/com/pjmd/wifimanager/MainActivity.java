package com.pjmd.wifimanager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WifiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayWifis();
        //registerReceiver();
        startSevice();
    }

    private void startSevice() {
        Context context = getApplicationContext();
        Intent intentService = new Intent(context, WifiService.class);
        Log.d(TAG, "Start wifi service");
        context.startService(intentService);
    }

    private void displayWifis() {
        Context app_context = this.getApplicationContext();
        WifiManager wifi_manager = app_context.getSystemService(WifiManager.class);

        List<WifiConfiguration> wifis = wifi_manager.getConfiguredNetworks();
        WifiInfo current_wifi = wifi_manager.getConnectionInfo();

        TextView textView = (TextView) findViewById(R.id.wifiInfo);
        ListView listView = (ListView)findViewById(R.id.listView);
        if( current_wifi != null) {
            textView.setText(current_wifi.getSSID());
        }
        else {
            textView.setText("No WIFI available");
        }
        if( wifis != null) {
            List<String> wifiArray = new ArrayList<String>();
            StringBuilder sb = new StringBuilder();
            int i = 0;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ ");
            for (WifiConfiguration wifi : wifis) {
                String ssid = wifi.SSID;
                Date dt = new Date();
                sb.append(df.format(dt))
                        .append("wifi (").append(i++).append("): ").append(ssid);
                Log.v(TAG, sb.toString());
                wifiArray.add(sb.toString());
                sb.setLength(0);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, wifiArray);
            listView.setAdapter(arrayAdapter);
        }

    }

    private void registerReceiver() {
        Context app_context = this.getApplicationContext();
        app_context.registerReceiver(new MyWifiReceiver(),new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED"));
        app_context.registerReceiver(new MyWifiReceiver(),new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public void connectWifi(View view ) {
        Context app_context = this.getApplicationContext();

        WifiManager wifi_manager = Utils.reconectWifi(TAG, app_context);
        TextView textView = (TextView) findViewById(R.id.wifiInfo);
        textView.setText("Connecting to wifi");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WifiInfo current_wifi = wifi_manager.getConnectionInfo();
        textView.setText(current_wifi.getSSID());

    }
}
