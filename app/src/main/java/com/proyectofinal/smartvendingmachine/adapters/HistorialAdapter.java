package com.proyectofinal.smartvendingmachine.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.models.Compra;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private Compra[] mCompras;

    public HistorialAdapter(Compra[] compras){
        mCompras = compras;
    }

    @Override
    public HistorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_compras_list_item,parent,false);
        HistorialViewHolder viewHolder = new HistorialViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistorialViewHolder holder, int position) {
        holder.bindHistorial(mCompras[position]);
    }

    @Override
    public int getItemCount() {
        return mCompras.length;
    }

    public class HistorialViewHolder extends RecyclerView.ViewHolder {

        public TextView mFechaLabel;
        public TextView mItemLabel;
        public TextView mMontoLabel;


        public HistorialViewHolder(View itemView) {
            super(itemView);
            //todo: bindear con butterknife
            mFechaLabel = (TextView) itemView.findViewById(R.id.fechaLabel);
            mItemLabel  = (TextView) itemView.findViewById(R.id.itemLabel);
            mMontoLabel = (TextView) itemView.findViewById(R.id.montoLabel);

        }

    public void bindHistorial(Compra compra){
        mFechaLabel.setText(compra.getFechaCompra()+"");
        mItemLabel.setText(compra.getItem().getDescripcion());//compra.getCompraItems()
        mMontoLabel.setText("$ "+compra.getItem().getPrecio());
    }
    }
}
