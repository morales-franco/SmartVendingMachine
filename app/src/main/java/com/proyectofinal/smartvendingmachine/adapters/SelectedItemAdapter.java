package com.proyectofinal.smartvendingmachine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.models.Item;

import java.util.ArrayList;

public class SelectedItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Item> mItems;

    public SelectedItemAdapter(Context context, ArrayList<Item> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.purchase_selected_item, null);

            holder = new ViewHolder();

            holder.mItemLabel = (TextView) convertView.findViewById(R.id.itemDescripcionLabel);
            holder.mMontoLabel = (TextView) convertView.findViewById(R.id.itemMontoLabel);
            holder.mCantidadLabel = (TextView) convertView.findViewById(R.id.itemCantidadLabel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = mItems.get(position);

        holder.mItemLabel.setText(item.getDescripcion() + "");
        holder.mCantidadLabel.setText(" X" + item.getCantidad() + "");
        holder.mMontoLabel.setText("$" + item.getPrecioUnitario()*item.getCantidad() + "");

        return convertView;
    }

    private static class ViewHolder {
        public TextView mItemLabel;
        public TextView mMontoLabel;
        public TextView mCantidadLabel;
    }
}
