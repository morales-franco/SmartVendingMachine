package com.proyectofinal.smartvendingmachine.services;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.proyectofinal.smartvendingmachine.utils.Api;
import org.json.JSONException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by franc on 10/10/2016.
 */

public class usuarioService {

    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();

   // @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Call Autenticar(String url, String userName, String password, Callback callback) {
        //RequestBody body = RequestBody.create(JSON, json); Si se envia un JSON

        final MediaType text = MediaType.parse("text/x-markdown; charset=utf-8");
        String postBody =  "?userID=" + userName + "&password=" + password;
        String urlAux = url + postBody;
        Request request = new Request.Builder()
                .url(urlAux)
                .post(RequestBody.create(text, postBody))
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public Call GetSaldo(String url, String userName, Callback callback) {
        final MediaType text = MediaType.parse("text/x-markdown; charset=utf-8");
        String postBody =  "?userID=" + userName;
        String urlAux = url + postBody;
        Request request = new Request.Builder()
                .url(urlAux)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

}
