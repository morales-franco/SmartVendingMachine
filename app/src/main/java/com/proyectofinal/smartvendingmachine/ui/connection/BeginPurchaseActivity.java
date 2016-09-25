package com.proyectofinal.smartvendingmachine.ui.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BeginPurchaseActivity extends AppCompatActivity {
    private static final String TAG = "BeginPurchaseActivity";
    private String DEVICE_ADDRESS = "";
    private UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    boolean deviceConnected = false;
    byte buffer[];
    boolean stopThread;

    @BindView(R.id.buttonStart) Button startButton;
    @BindView(R.id.buttonSend) Button sendButton;
    @BindView(R.id.buttonClear) Button clearButton;
    @BindView(R.id.buttonStop) Button stopButton;
    @BindView(R.id.editText) EditText editText;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.confirmarCompraButton) Button mConfirmarCompraButton;
    @BindView(R.id.cancelarCompraButton) Button mCancelarCompraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_purchase);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        DEVICE_ADDRESS = intent.getStringExtra("device_address");

        showToast("DEVICE_ADDRESS: " + DEVICE_ADDRESS);
        showToast("DEVICE_NAME: " + intent.getStringExtra("device_name"));

        setUiEnabled(false);

        mConfirmarCompraButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            postJson();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
                thread.start();
                showToast("Compra Confirmada");
            }
        });

        mCancelarCompraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    terminateConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void postJson() throws IOException, JSONException {
        PostCompra postCompra = new PostCompra();
        String response = postCompra.post(postCompra.getUrlCompra(),postCompra.getJsonCompra());

        JSONObject jsonResponse = new JSONObject(response);

        String responseText = "Success: "+jsonResponse.getString("success") +"\r\\\n"+
                "Saldo Actulizado: " + jsonResponse.getString("saldoActualizado");


        textView.setText(responseText.replace("\\\n", System.getProperty("line.separator")));
    }

    public void setUiEnabled(boolean bool) {
        startButton.setEnabled(!bool);
        sendButton.setEnabled(bool);
        stopButton.setEnabled(bool);
        textView.setEnabled(bool);
    }

    public boolean BTinit() {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), R.string.bluetooth_unsopported_message, Toast.LENGTH_SHORT).show();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if (bondedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.pair_device_first_message, Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                if (iterator.getAddress().equals(DEVICE_ADDRESS)) {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect() {
        boolean connected = true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
        if (connected) {
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connected;
    }

    public void onClickStart(View view) {
        if (BTinit()) {
            if (BTconnect()) {
                setUiEnabled(true);
                deviceConnected = true;
                beginListenForData();
                textView.append("\nConnection Opened!\n");
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    void beginListenForData() {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {
                        int byteCount = inputStream.available();
                        if (byteCount > 0) {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string = new String(rawBytes, "UTF-8");
                            handler.post(new Runnable() {
                                public void run() {
                                    textView.append(string);
                                }
                            });

                        }
                    } catch (IOException ex) {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }

    public void onClickSend(View view) {
        String string = editText.getText().toString();
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.append("\nSent Data:" + string + "\n");

    }

    public void onClickStop(View view) throws IOException {
        terminateConnection();
    }

    private void terminateConnection() throws IOException {
        stopThread = true;
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        if (socket != null) {
            socket.close();
        }
        setUiEnabled(false);
        deviceConnected = false;
        textView.append("\nConnection Closed!\n");
    }

    public void onClickClear(View view) {
        textView.setText("");
    }

}
