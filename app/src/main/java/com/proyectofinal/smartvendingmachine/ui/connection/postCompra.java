package com.proyectofinal.smartvendingmachine.ui.connection;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostCompra {

    public String mUrlCompra = "http://smartvendingdev.somee.com/Backoffice/API/Compra/Compra/";

    public String mJsonCompra = "{\n" +
            "    \"UserID\": \"ea56c62f-a883-470c-acdf-6afc2e31a7cb\",\n" +
            "    \"ExhibidorID\": 6,\n" +
            "    \"FechaCompra\": \"01/09/2016 16:45\",\n" +
            "    \"Monto\": 100,\n" +
            "    \"Items\": [\n" +
            "        {\n" +
            "            \"BalanzaID\": 1,\n" +
            "            \"ProductoID\": 2,\n" +
            "            \"Descripcion\": \"Sandwich jamón y queso\",\n" +
            "            \"Cantidad\": 6,\n" +
            "            \"PrecioUnitario\": 10\n" +
            "        },\n" +
            "        {\n" +
            "            \"BalanzaID\": 1,\n" +
            "            \"ProductoID\": 4,\n" +
            "            \"Descripcion\": \"Empanadas de Pollo\",\n" +
            "            \"Cantidad\": 2,\n" +
            "            \"PrecioUnitario\": 15\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public String getUrlCompra() {
        return mUrlCompra;
    }

    public void setUrlCompra(String urlCompra) {
        mUrlCompra = urlCompra;
    }

    public String getJsonCompra() {
        return mJsonCompra;
    }

    public void setJsonCompra(String jsonCompra) {
        mJsonCompra = jsonCompra;
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
