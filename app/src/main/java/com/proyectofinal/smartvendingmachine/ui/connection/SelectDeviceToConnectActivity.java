package com.proyectofinal.smartvendingmachine.ui.connection;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectDeviceToConnectActivity extends AppCompatActivity {

    private ConnectDeviceAdapter mAdapter;
    private ArrayList<BluetoothDevice> mAvailableDevicesList;

    @BindView(R.id.lv_paired) ListView mDevicesToConnectListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_device_to_connect);
        ButterKnife.bind(this);

        mAvailableDevicesList = getIntent().getExtras().getParcelableArrayList("device.list");
        mDevicesToConnectListView = (ListView) findViewById(R.id.lv_paired);
        mAdapter		= new ConnectDeviceAdapter(this);
        mAdapter.setData(mAvailableDevicesList);

        mAdapter.setListener(new ConnectDeviceAdapter.OnPairButtonClickListener() {
            @Override
            public void onPairButtonClick(int position) {
                BluetoothDevice device = mAvailableDevicesList.get(position);

                Intent intent = new Intent(SelectDeviceToConnectActivity.this, BeginPurchaseActivity.class);

                intent.putExtra("device_name",device.getName());
                intent.putExtra("device_address",device.getAddress());
                startActivity(intent);

            }
        });

        mDevicesToConnectListView.setAdapter(mAdapter);

//        registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
    }

//    @Override
//    public void onDestroy() {
//        unregisterReceiver(mPairReceiver);
//
//        super.onDestroy();
//    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

//    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            showToast("Paso por aca! rarisimo");
//            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
//                final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
//                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
//
//                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
//                    showToast("Paired");
//                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
//                    showToast("Unpaired");
//                }
//
//                mAdapter.notifyDataSetChanged();
//            }
//        }
//    };

}
