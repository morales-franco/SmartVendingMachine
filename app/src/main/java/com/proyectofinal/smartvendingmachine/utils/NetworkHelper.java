package com.proyectofinal.smartvendingmachine.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by franc on 10/10/2016.
 */

public class NetworkHelper {

    public static boolean isNetworkAvailable(Object systemService) {
        ConnectivityManager manager = (ConnectivityManager) systemService;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
}
