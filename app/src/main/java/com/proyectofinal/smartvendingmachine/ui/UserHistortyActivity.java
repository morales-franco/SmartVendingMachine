package com.proyectofinal.smartvendingmachine.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.adapters.HistorialAdapter;
import com.proyectofinal.smartvendingmachine.models.AlertDialogFragment;
import com.proyectofinal.smartvendingmachine.models.CompraDeHistorial;
import com.proyectofinal.smartvendingmachine.models.HistorialCompras;
import com.proyectofinal.smartvendingmachine.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserHistortyActivity extends AppCompatActivity {

    private HistorialCompras mHistorialCompras = new HistorialCompras();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.saldoLabel)
    TextView mSaldoLabel;

    public static final String TAG = UserHistortyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_historty);
        ButterKnife.bind(this);

        String userIDUrl = "?userID=ea56c62f-a883-470c-acdf-6afc2e31a7cb";
        String historyJsonURL = "http://smartvendingdev.somee.com/BackOffice/Api/Compra/HistorialCompras?userID=ea56c62f-a883-470c-acdf-6afc2e31a7cb";

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
                    } catch (JSONException e) {
                        Log.e(TAG, "Excepcion en JSON", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.error_red_no_disponible, Toast.LENGTH_LONG).show();
        }
    }

    private void updateDisplay() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-3:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        String currentDateTimeString = date.format(currentLocalTime);

        HistorialAdapter adapter = new HistorialAdapter(mHistorialCompras.getCompraDeHistorials());
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //todo: cambiar esto a el dato real.
        mSaldoLabel.setText(currentDateTimeString);
    }

    private HistorialCompras getUserDetails(String jsonData) throws JSONException {
        HistorialCompras historialCompras = new HistorialCompras();
        historialCompras.setCompraDeHistorials(getUserHistory(jsonData));

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


    private CompraDeHistorial[] getUserHistory(String jsonData) throws JSONException {
        JSONArray historialCompras = new JSONArray(jsonData);

        int lengthItems = 0;
        for (int i = 0; i < historialCompras.length(); i++) {
            JSONObject jsonCompra = historialCompras.getJSONObject(i);
            lengthItems += jsonCompra.getJSONArray("items").length();
        }

        CompraDeHistorial[] compraDeHistorials = new CompraDeHistorial[lengthItems];
        int itemsInsertados = 0;
        for (int k = 0; k < historialCompras.length(); k++) {

            JSONObject jsonCompra = historialCompras.getJSONObject(k);
            JSONArray items = jsonCompra.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {

                JSONObject jsonItem = items.getJSONObject(i);

                CompraDeHistorial compraDeHistorial = new CompraDeHistorial();
                Item item = new Item();

                item.setCantidad(jsonItem.getLong("cantidad"));
                item.setDescripcion(jsonItem.getString("descripcion"));
                item.setPrecioUnitario(jsonItem.getDouble("precioUnitario"));
                item.setProductoID(jsonItem.getLong("productoID"));
                item.setPrecio(item.getPrecio() * item.getCantidad());

                compraDeHistorial.setFechaCompra(historialCompras.getJSONObject(k).getString("fechaCompra"));
                mHistorialCompras.setUserId(historialCompras.getJSONObject(k).getString("userID"));

                compraDeHistorial.setItem(item);

                compraDeHistorials[itemsInsertados] = compraDeHistorial;
                itemsInsertados++;

            }
        }
        return compraDeHistorials;
    }
}
