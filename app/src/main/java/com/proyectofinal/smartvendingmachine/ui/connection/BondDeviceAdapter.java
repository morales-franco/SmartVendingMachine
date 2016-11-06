package com.proyectofinal.smartvendingmachine.ui.connection;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyectofinal.smartvendingmachine.R;

import java.util.List;

public class BondDeviceAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<BluetoothDevice> mData;
    private OnBondButtonClickListener mListener;

    public BondDeviceAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<BluetoothDevice> data) {
        mData = data;
    }

    public void setListener(OnBondButtonClickListener listener) {
        mListener = listener;
    }

    public int getCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView			=  mInflater.inflate(R.layout.list_item_manage_device, null);

            holder 				= new ViewHolder();

            holder.deviceNameTextView = (TextView) convertView.findViewById(R.id.deviceNameTextView);
            holder.deviceAddressTextView = (TextView) convertView.findViewById(R.id.deviceAddressTextView);
            holder.bondButton = (Button) convertView.findViewById(R.id.bondDeviceButton);
            holder.deviceImage = (ImageView) convertView.findViewById(R.id.imageViewDeviceIcon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device	= mData.get(position);
        String deviceName = device.getName();
        if(deviceName.equals("HC-05")){
            deviceName = "Exhibidor-01";
            holder.deviceImage.setImageResource(R.mipmap.ic_launcher);
        }

        holder.deviceNameTextView.setText(deviceName);
        holder.deviceAddressTextView.setText(device.getAddress());
        holder.bondButton.setText((device.getBondState() == BluetoothDevice.BOND_BONDED) ? "Desconectar" : "Sincronizar");
        holder.bondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBondButtonClick(position);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView deviceNameTextView;
        TextView deviceAddressTextView;
        TextView bondButton;
        ImageView deviceImage;
    }

    public interface OnBondButtonClickListener {
        void onBondButtonClick(int position);
    }
}
