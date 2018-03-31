package com.pjmd.wifimanager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by pjmd on 27/03/18.
 */

public class WifiService extends IntentService {
    private static final boolean FINAL_CONSTANT_IS_LOCAL = true;
    private static final String TAG = MyWifiReceiver.class.getSimpleName();

    private Context context;

    public WifiService() {
        super("WifiService");
    }

    @Override
    public void onCreate() {
        Log.d(getLogTagWithMethod(), "Service created");
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(getLogTagWithMethod(), "Service is startting");
        this.context = getApplicationContext();
        while(true) {
            if (isInternetDown()) {
                reconnectToInternet();
                sleep(5);
            }
            sleep(5);
        }
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        this.context = getApplicationContext();
//        return super.onStartCommand(intent, flags, startId);
//    }

    private void sleep(long second) {
        try {
            Thread.sleep(second * 1000);
        }
        catch(Exception e) {

        }
    }

    private void reconnectToInternet() {
        InternetConnectivity.connectWifi(this.context, getLogTagWithMethod());
    }

    private boolean isInternetDown() {
        if( InternetConnectivity.isConnectivity(this.context, getLogTagWithMethod())) {
//            if(InternetConnectivity.isConnectedToInternet(this.context, getLogTagWithMethod())) {
//                return false;
//            }
//            else {
//                return true;
//            }
            Log.d(getLogTagWithMethod(), "There is connectivity");
            return false;
        }
        else {
            Log.d(getLogTagWithMethod(), "There is no connectivity");
            return true;
        }
    }


    private String getLogTagWithMethod() {
        if (FINAL_CONSTANT_IS_LOCAL) {
            Throwable stack = new Throwable().fillInStackTrace();
            StackTraceElement[] trace = stack.getStackTrace();
            int function_index = 0;
            if( trace.length > 1) {
                function_index = 1;
            }
            return trace[function_index].getClassName() + "." + trace[function_index].getMethodName() + ":" + trace[function_index].getLineNumber();
        } else {
            return TAG;
        }
    }

}
