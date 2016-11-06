package com.proyectofinal.smartvendingmachine.ui.connection;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;

import java.lang.reflect.Method;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageBondedDevicesActivity extends AppCompatActivity {

    //private ListView mListView; //PairedDevicesListView
    private BondDeviceAdapter mBondDeviceAdapter;
    private ArrayList<BluetoothDevice> mBondedDeviceList;

    @BindView(R.id.lv_paired) ListView mPairedDevicesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_bonded_devices);
        ButterKnife.bind(this);

        mBondedDeviceList	= getIntent().getExtras().getParcelableArrayList("device.list");

        mPairedDevicesListView = (ListView) findViewById(R.id.lv_paired);

        mBondDeviceAdapter = new BondDeviceAdapter(this);

        mBondDeviceAdapter.setData(mBondedDeviceList);

        mBondDeviceAdapter.setListener(new BondDeviceAdapter.OnBondButtonClickListener() {
            @Override
            public void onBondButtonClick(int position) {
                BluetoothDevice device = mBondedDeviceList.get(position);

                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unpairDevice(device);
                } else {
                    showToast("Sincronizando...");

                    pairDevice(device);
                }
            }
        });

        mPairedDevicesListView.setAdapter(mBondDeviceAdapter);

        registerReceiver(mBondReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mBondReceiver);

        super.onDestroy();
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver mBondReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    showToast("Sincronizado");
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    showToast("Desincronizar");
                }

                mBondDeviceAdapter.notifyDataSetChanged();
            }
        }
    };
}
