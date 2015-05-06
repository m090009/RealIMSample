package com.m090009.sample.realim.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.m090009.sample.realim.interfaces.OnNetworkStateChanges;
import com.m090009.sample.realim.models.Dialogs;

/**
 * Created by tohamy on 5/6/15.
 */
public class NetworkPreferences {
    private Context context;
    private ConnectivityManager connectivityManager;
    private BroadcastReceiver networkStateReceiver;
    private OnNetworkStateChanges callback;

    public NetworkPreferences(Context context, OnNetworkStateChanges callback) {
        this.context = context;
        this.callback = callback;
        init(callback);
    }

    public void init(OnNetworkStateChanges callback){
        listenToNetworkChange(context, callback);
    }


    public void listenToNetworkChange(Context context, final OnNetworkStateChanges callback){
        this.networkStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                callback.onNetworkStateChanges(isConnectedToNetwork(context));
                Log.w("Network Listener", "Network Type Changed");
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkStateReceiver, filter);
    }


    public static boolean isConnectedToNetwork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = false;
        if ( activeNetInfo != null )
        {
            isConnected = true;
//            Toast.makeText(context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT).show();
        } else{
//            Toast.makeText(context, "No Active Network : ", Toast.LENGTH_SHORT).show();
        }
        return isConnected;
    }

    public void removeReciver(){
        this.context.unregisterReceiver(this.networkStateReceiver);
    }


    public static ConnectivityManager getConectivityManager(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm;
    }



}
