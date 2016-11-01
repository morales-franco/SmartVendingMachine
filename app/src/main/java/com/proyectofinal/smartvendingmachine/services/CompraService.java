package com.proyectofinal.smartvendingmachine.services;

import com.proyectofinal.smartvendingmachine.utils.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;




public class CompraService {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();

    // @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Call Comprar(String url, String json, Callback callback) {
        //RequestBody body = RequestBody.create(JSON, json); Si se envia un JSON

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Api.UrlSubmitCompra)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

}
