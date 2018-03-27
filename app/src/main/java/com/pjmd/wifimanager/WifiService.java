package com.pjmd.wifimanager;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by pjmd on 27/03/18.
 */

public class WifiService extends IntentService {

    public WifiService() {
        super("WifiService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(isInternetDown()) {
            reconnectToInternet();
        }
    }

    private void reconnectToInternet() {
    }

    private boolean isInternetDown() {
        return false;
    }
}
