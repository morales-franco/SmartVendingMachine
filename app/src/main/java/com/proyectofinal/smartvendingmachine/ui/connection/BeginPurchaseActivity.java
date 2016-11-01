package com.proyectofinal.smartvendingmachine.ui.connection;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.adapters.SelectedItemAdapter;
import com.proyectofinal.smartvendingmachine.models.Compra;
import com.proyectofinal.smartvendingmachine.models.Item;
import com.proyectofinal.smartvendingmachine.models.Usuario;
import com.proyectofinal.smartvendingmachine.repository.UsuarioRepository;
import com.proyectofinal.smartvendingmachine.services.CompraService;
import com.proyectofinal.smartvendingmachine.services.usuarioService;
import com.proyectofinal.smartvendingmachine.ui.LoginMainActivity;
import com.proyectofinal.smartvendingmachine.utils.Api;
import com.proyectofinal.smartvendingmachine.utils.ApplicationHelper;
import com.proyectofinal.smartvendingmachine.utils.NetworkHelper;
import com.proyectofinal.smartvendingmachine.utils.ToastHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BeginPurchaseActivity extends ListActivity {
    private static final String TRUE = "1";
    private static final String FALSE = "0";

    private static final String TAG = "BeginPurchaseActivity";
    private String DEVICE_ADDRESS = "";
    Usuario currentUser;
    UsuarioRepository usuarioRepo;

    private UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    boolean deviceConnected = false;
    byte buffer[];
    boolean stopThread;

    private double mSaldoUsuario;

    private String mStringCompraBuffer = "";
    private ArrayList<Item> mItemsCompra = new ArrayList<>();
    private Long mExhibidorId;

    SelectedItemAdapter mAdapter = new SelectedItemAdapter(this, mItemsCompra);


    //    @BindView(R.id.textViewDebugg)
    //    TextView textView;
    @BindView(R.id.textViewTotal)
    TextView textViewTotal;
    @BindView(R.id.textViewMontoCompra)
    TextView textViewMontoCompra;
    @BindView(R.id.saldoLabel)
    TextView saldoTextView;
    @BindView(R.id.confirmarCompraButton)
    Button mConfirmarCompraButton;
    @BindView(R.id.cancelarCompraButton)
    Button mCancelarCompraButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_purchase);
        ButterKnife.bind(this);

        usuarioRepo = UsuarioRepository.GetInstance(getApplicationContext());
        currentUser = ((ApplicationHelper) this.getApplication()).getCurrentUser();

        mSaldoUsuario = currentUser.getSaldo();
        mostrarSaldo(mSaldoUsuario);

        setListAdapter(mAdapter);

        //todo este scroll no va.
//        textViewDebugg.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        DEVICE_ADDRESS = intent.getStringExtra("device_address");


        //todo: borrar
        showToast("Direccion: " + DEVICE_ADDRESS);
        showToast("Nombre:  " + intent.getStringExtra("device_name"));

        setUiEnabled(false);
        habilitarConfirmarCompra(false);

        if (BTinit()) {
            if (BTconnect()) {
                setUiEnabled(true);
                deviceConnected = true;
                beginListenForData();
                showToast("\nConexion abierta\n");
            }
        }

        mConfirmarCompraButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Compra compra = armarCompra(mItemsCompra);
                SweetAlertDialog pDialog = new SweetAlertDialog(BeginPurchaseActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                try {
                    postCompra(compra, pDialog);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //todo: limpio el array mItemsCompra??
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

    private void showCompraExitosaDialog(String saldoActualizado) {
        SweetAlertDialog pDialog = new SweetAlertDialog(BeginPurchaseActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Exito");
        pDialog.setContentText("Muchas gracias por su compra! Su nuevo saldo es de : "+ saldoActualizado);
        pDialog.setConfirmText("Aceptar");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                try {
                    terminateConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        pDialog.show();
    }

    private void mostrarSaldo(Double saldo) {
        saldoTextView.setText("$" + saldo);
    }

    private void habilitarConfirmarCompra(boolean b) {
        mConfirmarCompraButton.setEnabled(b);
        mCancelarCompraButton.setEnabled(!b);
    }

    private Compra armarCompra(ArrayList<Item> itemsCompra) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-3:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        String currentDateTime = date.format(currentLocalTime);

        Compra compra = new Compra();
        compra.setUserId(currentUser.getUserID());
        compra.setExhibidorId(mExhibidorId);
        compra.setFechaCompra(currentDateTime);
        compra.setItems(itemsCompra);

        long montoTotal = getMontoTotal(itemsCompra);

        compra.setMonto(montoTotal);

        return compra;
    }



    private long getMontoTotal(ArrayList<Item> itemsCompra) {
        Iterator<Item> it = itemsCompra.iterator();
        long montoTotal = 0;
        while (it.hasNext()) {
            Item item = it.next();
            montoTotal += item.getPrecioUnitario() * item.getCantidad();
        }
        return montoTotal;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void postCompra(Compra compra, final SweetAlertDialog procesandoCompraDialog) throws IOException, JSONException {
        if (NetworkHelper.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    procesandoCompraDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    procesandoCompraDialog.setTitleText("Aguarde por favor...");
                    procesandoCompraDialog.setCancelable(false);
                    procesandoCompraDialog.show();
                }
            });

//            progressDialog = ProgressDialog.show(this, "", "Espere por favor...", true);
            CompraService service = new CompraService();
            PostCompra compraParser = new PostCompra();
            String jsonCompra = compraParser.createJsonCompra(compra);

            service.Comprar(Api.UrlSubmitCompra, jsonCompra, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    ToastHelper.backgroundThreadShortToast(getApplicationContext(),"Fallo al conectarse con el servidor" + e.getMessage(), Toast.LENGTH_LONG);
//                    if(progressDialog != null)
//                        progressDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            procesandoCompraDialog.hide();
                            showErrorDialog("Error al conectarse con el servidor", "Por favor contáctese con el administrador.");
                              }
                });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        try {
                            JSONObject jsonResponse = new JSONObject(responseStr);

                            if(jsonResponse.getBoolean("success")){
                                mItemsCompra.clear();
                                final double saldoActualizado = jsonResponse.getDouble("saldoActualizado");
                                ((ApplicationHelper) BeginPurchaseActivity.this.getApplication()).updateSaldo(saldoActualizado);
                                usuarioRepo.UpdateSaldo(currentUser.getUserID(), saldoActualizado);
                                ToastHelper.backgroundThreadShortToast(getApplicationContext(),saldoActualizado+"", Toast.LENGTH_SHORT);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        procesandoCompraDialog.hide();
                                        showCompraExitosaDialog("$"+saldoActualizado);
                                    }
                                });

                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        procesandoCompraDialog.hide();
                                        showErrorDialog("Error al conectarse con el servidor", "Por favor contáctese con el administrador.");
                                    }
                                });
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                procesandoCompraDialog.hide();
                                showErrorDialog("Respuesta del Servidor Incorrecta", "Por favor contáctese con el administrador.");
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {procesandoCompraDialog.hide();  }
                    });
//                    if(progressDialog != null)
//                        progressDialog.dismiss();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    procesandoCompraDialog.hide();
                    showErrorDialog("No esta conectado a la red", "Por favor contáctese con el administrador.");
                }
            });
        }





       /////////////////////////////////////////////////
        //PostCompra postCompra = new PostCompra();
        //String response = postCompra.post(compra);

        //final JSONObject jsonResponse = new JSONObject(response);

        //String responseText = "Success: " + jsonResponse.getString("success") + "\r\\\n" +
         //       "Saldo Actualizado : " + jsonResponse.getString("saldoActualizado");

        //Actualizo nuevo saldo
//        if (success) {

            //showCompraExitosaDialog();
            //ProgressDialog progressDialog  = ProgressDialog.show(BeginPurchaseActivity.this, "", "Espere por favor...", true);
//            double saldoActualizado = jsonResponse.getDouble("saldoActualizado");
//            ((ApplicationHelper) BeginPurchaseActivity.this.getApplication()).updateSaldo(saldoActualizado);
//            usuarioRepo.UpdateSaldo(currentUser.getUserID(), saldoActualizado);
//            progressDialog.dismiss();
//
//        } else {
//            Log.v("BeginPuerchase", "Error en la compra. Por favor contactese con el administrador. " + jsonResponse.getString("response"));
//        }

//        textViewDebugg.setText(responseText.replace("\\\n", System.getProperty("line.separator")));
    }

    public void setUiEnabled(boolean bool) {
//        startButton.setEnabled(!bool);
//        sendButton.setEnabled(bool);
//        stopButton.setEnabled(bool);
//        textViewDebugg.setEnabled(bool);
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
//                textViewDebugg.append("\nConexion abierta\n");
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
                                    mStringCompraBuffer = mStringCompraBuffer + string;
                                    if (mStringCompraBuffer.toLowerCase().contains("}".toLowerCase())) {
//                                      textViewDebugg.append(mStringCompraBuffer);
                                        setListAdapter(mAdapter);
                                        try {
                                            procesarAccion(mStringCompraBuffer);
                                            setListAdapter(mAdapter);
                                            if (mItemsCompra.size() == 0) {
                                                mostrarTotalActualizando(false);

                                            } else {
                                                mostrarTotalActualizando(true);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        mStringCompraBuffer = "";
                                    }
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

    private void mostrarTotalActualizando(boolean b) {
        if (b == true) {
            textViewMontoCompra.setText("$" + getMontoTotal(mItemsCompra));
            textViewMontoCompra.setVisibility(View.VISIBLE);
            textViewTotal.setVisibility(View.VISIBLE);
            if (mSaldoUsuario >= getMontoTotal(mItemsCompra)) {
                textViewTotal.setTextColor(Color.parseColor("#FFAAAAAA"));
                textViewMontoCompra.setTextColor(Color.parseColor("#FFAAAAAA"));
                mConfirmarCompraButton.setEnabled(true);
            } else {
                textViewTotal.setTextColor(Color.parseColor("#FFDC2424"));
                textViewMontoCompra.setTextColor(Color.parseColor("#FFDC2424"));
                mConfirmarCompraButton.setEnabled(false);
                showErrorDialog("Saldo Insuficiente","Por favor, devuelva el producto al exhibidor.");
            }
        } else {
            textViewMontoCompra.setVisibility(View.INVISIBLE);
            textViewTotal.setVisibility(View.INVISIBLE);
        }
    }

    private void showErrorDialog(String title, String text){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(text)
                .show();
    }

    private void procesarAccion(String stringCompraBuffer) throws JSONException {
        JSONObject jsonItem = new JSONObject(stringCompraBuffer);
        Item item = new Item();

        item.setDescripcion(jsonItem.getString("ProductoDescripcion"));
        item.setProductoID(Long.parseLong(jsonItem.getString("IdProducto"), 10));
        item.setIdBalanza(Long.parseLong(jsonItem.getString("IdBalanza")));

        item.setCantidad(Long.parseLong(jsonItem.getString("Cantidad")));
        item.setPrecioUnitario(Double.parseDouble(jsonItem.getString("PrecioUnitario")));

        String esDevolucion = jsonItem.getString("EsDevolucion");
        String tipoTransaccion = jsonItem.getString("TipoTransaccion");

        showToast("Tipo Transaccion: "+tipoTransaccion);

        if (mItemsCompra.size() == 0) {
            habilitarConfirmarCompra(true);
            mExhibidorId = Long.parseLong(jsonItem.getString("IdExhibidor"), 10);
            mItemsCompra.add(item);
        } else {
            Iterator<Item> it = mItemsCompra.iterator();
            String encontrado = FALSE;
            if (esDevolucion == TRUE) {
                while (it.hasNext() && (encontrado.equals(FALSE))) {
                    Item targetItem = it.next();
                    if ((targetItem.getProductoID() == item.getProductoID())) {
                        if (targetItem.getCantidad() <= 1) {
                            it.remove();
                            encontrado = TRUE;
                        } else {
                            targetItem.setCantidad(targetItem.getCantidad() - item.getCantidad());
                        }
                    }
                }
                if (mItemsCompra.size() == 0) {
                    habilitarConfirmarCompra(false);
                }
            } else {
                habilitarConfirmarCompra(true);
                while (it.hasNext() && (encontrado == FALSE)) {
                    Item targetItem = it.next();
                    if ((targetItem.getProductoID() == item.getProductoID())) {
                        targetItem.setCantidad(targetItem.getCantidad() + item.getCantidad());
                        encontrado = TRUE;
                    }
                }
                if (encontrado == FALSE) {
                    mItemsCompra.add(item);
                }
            }
        }

    }

    public void onClickSend(View view) {
//        String string = editText.getText().toString();
//        string.concat("\n");
//        try {
//            outputStream.write(string.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        textViewDebugg.append("\nInformacion enviada: " + string + "\n");
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
//        textViewDebugg.append("\nFin compra\n");
    }

    public void onClickClear(View view) {
//        textViewDebugg.setText("");
    }

}
