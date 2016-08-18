package com.proyectofinal.smartvendingmachine.ui;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.ui.connection.ManageBondedDevicesActivity;
import com.proyectofinal.smartvendingmachine.ui.connection.SelectDeviceToConnectActivity;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectDeviceActivity extends AppCompatActivity {

    private ArrayList<BluetoothDevice> mBondedBluetoothDeviceList = new ArrayList<BluetoothDevice>();
    private ArrayList<BluetoothDevice> mAvailableBluetoothDeviceList = new ArrayList<BluetoothDevice>();

    private BluetoothAdapter mBluetoothAdapter;

    private ProgressDialog mProgressDialog;

    private TextView mStatusBluetoothTextView;
    private Button mEnableBluetoothButton;
    private Button mScanBluetoothDevicesButton;
    private Button mBondBluetoothDevicesButton;
//    @BindView(R.id.userHistoryButton) Button mUserHistoryButton;
//    @BindView(R.id.statusBluetoothTextView) TextView mStatusBluetoothTextView;
//    @BindView(R.id.btn_enable) Button mEnableBluetoothButton;
//    @BindView(R.id.scanBluetoothDevicesButton) Button mScanBluetoothDevicesButton;//scan
//    @BindView(R.id.bondBluetoothDevicesButton) Button mBondBluetoothDevicesButton;//sincronizar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connect_device);

        mStatusBluetoothTextView = (TextView) findViewById(R.id.statusBluetoothTextView);
        mEnableBluetoothButton = (Button) findViewById(R.id.btn_enable);
        mScanBluetoothDevicesButton = (Button) findViewById(R.id.scanBluetoothDevicesButton);
        mBondBluetoothDevicesButton = (Button) findViewById(R.id.bondBluetoothDevicesButton);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //TODO cambiar el progressDialog por algo mas cheto.
        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Escaneando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mBluetoothAdapter.cancelDiscovery();
            }
        });

        if (mBluetoothAdapter == null){
            showUnsupported();
        } else {

            mScanBluetoothDevicesButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mBluetoothAdapter.startDiscovery();
                }
            });

            mBondBluetoothDevicesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();

                    if ( (mAvailableBluetoothDeviceList == null || mAvailableBluetoothDeviceList.size() == 0) &&
                         (bondedDevices == null || bondedDevices.size() == 0)){
                        showToast("No hay dispositivos Bluetooth disponibles en este momento.");
                    }else{
                        startBondDeviceActivity(bondedDevices);
                    }
                    enableBondButton();
                }
            });

            mEnableBluetoothButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBluetoothAdapter.isEnabled()){
                        mBluetoothAdapter.disable();
                        showDisabled();
                    }else{
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, 1000);
                    }

                }
            });

            if (mBluetoothAdapter.isEnabled()) {
                showEnabled();
            } else {
                showDisabled();
            }
        }

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
    }

    private void startBondDeviceActivity(Set<BluetoothDevice> bondedDevices) {
        ArrayList<BluetoothDevice> list = new ArrayList<>();

        list.addAll(mAvailableBluetoothDeviceList);
        list.addAll(bondedDevices);

        Intent intent = new Intent(ConnectDeviceActivity.this, ManageBondedDevicesActivity.class);

        intent.putParcelableArrayListExtra("device.list", list);

        startActivity(intent);
    }

    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    private void showUnsupported() {
        mStatusBluetoothTextView.setText("Lamentablemente su dispositivo no soporta Bluetooth.");
        //TODO: QUE HACEMOS ACA? SALIR DE LA APLICACION?
        //mActivateBtn.setText("Enable");
        //mActivateBtn.setEnabled(false);

        //mPairedBtn.setEnabled(false);
        //mScanBtn.setEnabled(false);
    }

    private void showEnabled() {
        mStatusBluetoothTextView.setText("Bluetooth Encendido");
        mStatusBluetoothTextView.setTextColor(Color.BLUE);

        mEnableBluetoothButton.setText("Desactivar");
        mEnableBluetoothButton.setEnabled(true);

        //mPairedBtn.setEnabled(true);
        mScanBluetoothDevicesButton.setEnabled(true);
        enableBondButton();
    }

    private void showDisabled() {
        mStatusBluetoothTextView.setText("Bluetooth Apagado");
        mStatusBluetoothTextView.setTextColor(Color.RED);

        mEnableBluetoothButton.setText("Activar");
        mEnableBluetoothButton.setEnabled(true);

        //mPairedBtn.setEnabled(false);
        mScanBluetoothDevicesButton.setEnabled(false);
        enableBondButton();
    }

    private void enableBondButton() {
        if (mAvailableBluetoothDeviceList.size()>0 || mBluetoothAdapter.getBondedDevices().size() >0){
            mBondBluetoothDevicesButton.setEnabled(true);
        }else{
            mBondBluetoothDevicesButton.setEnabled(false);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            //The state of the local Bluetooth adapter has been changed.
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {

                    showToast("Activado");
                    showEnabled();

                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                mBondedBluetoothDeviceList = new ArrayList<>();
                mAvailableBluetoothDeviceList = new ArrayList<>();
                mProgressDialog.show();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                mProgressDialog.dismiss();

                if (mAvailableBluetoothDeviceList.size() <= 0) {

                    showToast("No hay dispositivos BT disponibles");

                }else if(mBondedBluetoothDeviceList.size() > 0) {

                    Intent newIntent = new Intent(ConnectDeviceActivity.this, SelectDeviceToConnectActivity.class);
                    newIntent.putParcelableArrayListExtra("device.list", mBondedBluetoothDeviceList);
                    startActivity(newIntent);

                }else if (mBondedBluetoothDeviceList.size() <= 0 && mAvailableBluetoothDeviceList.size() > 0){

                    showToast("Nuevos dispositivos no sincronizados disponinbles. Abriendo menu de sincronizacion");
                    startBondDeviceActivity(mBluetoothAdapter.getBondedDevices());

                }
                enableBondButton();

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {

                    mBondedBluetoothDeviceList.add(device);
                    showToast("Found device bonded" + device.getName());

                }
                mAvailableBluetoothDeviceList.add(device);
                showToast("Found device " + device.getName());

            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {

                //Device is now connected
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                showToast("Connected to device: " + device.getName());

            }
        }
    };

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }



}
