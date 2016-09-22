package com.proyectofinal.smartvendingmachine.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.adapters.HistorialAdapter;
import com.proyectofinal.smartvendingmachine.models.AlertDialogFragment;
import com.proyectofinal.smartvendingmachine.models.Compra;
import com.proyectofinal.smartvendingmachine.models.CompraItem;
import com.proyectofinal.smartvendingmachine.models.HistorialCompras;
import com.proyectofinal.smartvendingmachine.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserHistortyActivity extends AppCompatActivity {

    private HistorialCompras mHistorialCompras;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.saldoLabel) TextView mSaldoLabel;

    public static final String TAG = UserHistortyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_historty);
        ButterKnife.bind(this);

        String historyJsonURL = "https://api.myjson.com/bins/2hsym";

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(historyJsonURL).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mHistorialCompras = getUserDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Excepcion en el request", e);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Excepcion en JSON", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, R.string.error_red_no_disponible, Toast.LENGTH_LONG).show();
        }
    }

    private void updateDisplay() {
        HistorialAdapter adapter = new HistorialAdapter(mHistorialCompras.getCompras());
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mSaldoLabel.setText("$ "+mHistorialCompras.getCredito());
    }

    private HistorialCompras getUserDetails(String jsonData) throws JSONException {
        JSONObject dataUsuario = new JSONObject(jsonData);
        JSONObject historialConsumos = dataUsuario.getJSONObject("consumos");

        long idUsuario =  historialConsumos.getLong("idUsuario");
        Log.i(TAG, "From Json: "+ idUsuario);

        HistorialCompras historialCompras = new HistorialCompras();
        historialCompras.setCredito(historialConsumos.getDouble("credito"));
        historialCompras.setUserId(historialConsumos.getLong("idUsuario"));

        historialCompras.setCompras(getUserHistory(jsonData));

        return historialCompras;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }



    private Compra[] getUserHistory(String jsonData) throws JSONException {
        JSONObject dataConsumos = new JSONObject(jsonData);
        JSONObject historial = dataConsumos.getJSONObject("consumos");
        JSONArray data = historial.getJSONArray("data");

        Compra[] compras =  new Compra[data.length()];

        for(int i = 0; i < data.length(); i++){
            JSONObject jsonCompra = data.getJSONObject(i);
            Compra compra = new Compra();

            Item item = new Item();
            item.setDescripcion(jsonCompra.getString("item"));
            item.setItemID(jsonCompra.getLong("idItem"));
            item.setPrecio(jsonCompra.getDouble("precio"));

            compra.setFechaCompra(jsonCompra.getLong("fecha"));
            compra.setUsuarioId(jsonCompra.getLong("idItem"));

            compra.setItem(item);

            compras[i] = compra;
        }
        return compras;
    }


}
