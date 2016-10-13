package com.proyectofinal.smartvendingmachine.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import okhttp3.internal.framed.FrameReader;

/**
 * Created by franc on 11/10/2016.
 */

public class ToastHelper {

    public static void backgroundThreadShortToast(final Context context, final String msg, final  int duration) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, duration).show();
                }
            });
        }
    }
}
