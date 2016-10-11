package com.proyectofinal.smartvendingmachine.ui.connection;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.proyectofinal.smartvendingmachine.models.Compra;
import com.proyectofinal.smartvendingmachine.models.Item;
import com.proyectofinal.smartvendingmachine.utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostCompra {
    //public static final String mUrlCompra = "http://smartvendingdev.somee.com/Backoffice/API/Compra/Compra/";

    public String mJsonCompra = "";

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
    String post(Compra compra) throws IOException, JSONException {
        String json = this.createJsonCompra(compra);

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(Api.UrlSubmitCompra)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private String createJsonCompra(Compra compra) throws JSONException {
        JSONObject jsonCompra = new JSONObject();
        JSONObject item = new JSONObject();

        jsonCompra.put("UserID", compra.getUserId());
        jsonCompra.put("ExhibidorID", compra.getExhibidorId());
        jsonCompra.put("FechaCompra", compra.getFechaCompra());
        jsonCompra.put("Monto", compra.getMonto());

        JSONArray jsonArrayItems = new JSONArray();

        Iterator<Item> it = compra.getItems().iterator();
        while (it.hasNext()) {
            Item targetItem = it.next();
            JSONObject itemJsonObject = new JSONObject();

            itemJsonObject.put("BalanzaID", targetItem.getIdBalanza());
            itemJsonObject.put("ProductoID", targetItem.getProductoID());
            itemJsonObject.put("Descripcion", targetItem.getDescripcion());
            itemJsonObject.put("Cantidad", targetItem.getCantidad());
            itemJsonObject.put("PrecioUnitario", targetItem.getPrecioUnitario());

            jsonArrayItems.put(itemJsonObject);
        }

        jsonCompra.put("Items",jsonArrayItems);

        return jsonCompra.toString();

    }
}
