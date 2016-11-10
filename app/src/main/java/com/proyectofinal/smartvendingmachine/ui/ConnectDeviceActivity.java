package com.proyectofinal.smartvendingmachine.ui;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.ui.connection.ManageBondedDevicesActivity;
import com.proyectofinal.smartvendingmachine.ui.connection.SelectDeviceToConnectActivity;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.proyectofinal.smartvendingmachine.R.drawable.ic_bluetooth_black_48dp;

public class ConnectDeviceActivity extends AppCompatActivity {

    private ArrayList<BluetoothDevice> mBondedBluetoothDeviceList = new ArrayList<>();
    private ArrayList<BluetoothDevice> mAvailableBluetoothDeviceList = new ArrayList<>();

    private BluetoothAdapter mBluetoothAdapter;

    private ProgressDialog mProgressDialog;

    @BindView(R.id.statusBluetoothTextView) TextView mStatusBluetoothTextView;
    @BindView(R.id.enableBluetoothSwitch) Switch mEnableBluetoothSwitch;
    @BindView(R.id.scanBluetoothDevicesButton) ImageButton mScanBluetoothDevicesButton;
    @BindView(R.id.bondBluetoothDevicesButton) Button mBondBluetoothDevicesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connect_device);
        ButterKnife.bind(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //TODO cambiar el progressDialog por algo mas cheto.
        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage(getString(R.string.discovery_dialog_message));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel_discovery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mBluetoothAdapter.cancelDiscovery();
            }
        });

        if (mBluetoothAdapter == null){
            showUnsupported();
        } else {

            int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

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
                        showToast(getString(R.string.no_devices_available_message));
                    }else{
                        startBondDeviceActivity(bondedDevices);
                    }
                    enableBondButton();
                }
            });

            mEnableBluetoothSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualizarBluetoothSwitch();

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
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);

        registerReceiver(mReceiver, filter);
    }

    private void actualizarBluetoothSwitch() {
        if (mBluetoothAdapter.isEnabled()){
            mStatusBluetoothTextView.animate().translationY(0);
            mBluetoothAdapter.disable();
            showDisabled();
        }else{
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1000);

        }
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
        mStatusBluetoothTextView.setText(R.string.bluetooth_unsopported_message);
//        mStatusBluetoothTextView.animate().translationY(200);
        //TODO: QUE HACEMOS ACA? SALIR DE LA APLICACION?
        //mActivateBtn.setText("Enable");
        //mActivateBtn.setEnabled(false);

        //mPairedBtn.setEnabled(false);
        //mScanBtn.setEnabled(false);
    }

    private void showEnabled() {
        mStatusBluetoothTextView.setText(R.string.bluetooth_on);
        mStatusBluetoothTextView.setTextColor(Color.BLUE);
        mEnableBluetoothSwitch.setText(R.string.bluetooth_disable);

        mScanBluetoothDevicesButton.setEnabled(true);
        mEnableBluetoothSwitch.setChecked(true);

        mScanBluetoothDevicesButton.setColorFilter(Color.parseColor("#a9a9a9"));
        mScanBluetoothDevicesButton.setImageResource(R.drawable.ic_bluetooth_searching_black_48dp);
        mScanBluetoothDevicesButton.setEnabled(true);
        enableBondButton();
    }

    private void showDisabled() {
        mStatusBluetoothTextView.setText(R.string.bluetooth_off);

        mStatusBluetoothTextView.setTextColor(Color.RED);

        mEnableBluetoothSwitch.setText(R.string.bluetooth_enable);
        mEnableBluetoothSwitch.setChecked(false);

        mScanBluetoothDevicesButton.setColorFilter(Color.parseColor("#a9a9a9"));
        mScanBluetoothDevicesButton.setImageResource(R.drawable.ic_bluetooth_disabled_black_48dp);
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

                    showToast(getString(R.string.bluetooth_on));
                    showEnabled();
                    mStatusBluetoothTextView.animate().translationY(-50);

                } else if(state == BluetoothAdapter.STATE_OFF){
                    showDisabled();
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                mBondedBluetoothDeviceList = new ArrayList<>();
                mAvailableBluetoothDeviceList = new ArrayList<>();
                mProgressDialog.show();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                mProgressDialog.dismiss();

                if (mAvailableBluetoothDeviceList.size() <= 0) {

                    showToast(getString(R.string.no_devices_available_message));

                }else if(mBondedBluetoothDeviceList.size() > 0) {

                    Intent newIntent = new Intent(ConnectDeviceActivity.this, SelectDeviceToConnectActivity.class);
                    newIntent.putParcelableArrayListExtra("device.list", mBondedBluetoothDeviceList);
                    startActivity(newIntent);

                }else if (mBondedBluetoothDeviceList.size() <= 0 && mAvailableBluetoothDeviceList.size() > 0){

                    showToast("Nuevos dispositivos no sincronizados disponibles. Abriendo menu de sincronización");
                    startBondDeviceActivity(mBluetoothAdapter.getBondedDevices());

                }
                enableBondButton();

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {

                    mBondedBluetoothDeviceList.add(device);
                    showToast("Dispositivo sincronizado: " + device.getName());

                }
                mAvailableBluetoothDeviceList.add(device);
//                showToast("Dispositivo encontrado: " + device.getName());

            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {

                //Device is now connected
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                showToast("Dispositivo conectado: " + device.getName());

            }
        }
    };

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }



}
