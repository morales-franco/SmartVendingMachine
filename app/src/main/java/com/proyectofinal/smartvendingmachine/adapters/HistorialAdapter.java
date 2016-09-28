package com.proyectofinal.smartvendingmachine.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.models.CompraDeHistorial;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private CompraDeHistorial[] mCompraDeHistorials;

    public HistorialAdapter(CompraDeHistorial[] compraDeHistorials) {
        mCompraDeHistorials = compraDeHistorials;
    }

    @Override
    public HistorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_compras_list_item, parent, false);
        HistorialViewHolder viewHolder = new HistorialViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistorialViewHolder holder, int position) {
        holder.bindHistorial(mCompraDeHistorials[position]);
    }

    @Override
    public int getItemCount() {
        return mCompraDeHistorials.length;
    }

    public class HistorialViewHolder extends RecyclerView.ViewHolder {

        public TextView mFechaLabel;
        public TextView mItemLabel;
        public TextView mMontoLabel;


        public HistorialViewHolder(View itemView) {
            super(itemView);
            //todo: bindear con butterknife
            mFechaLabel = (TextView) itemView.findViewById(R.id.fechaLabel);
            mItemLabel = (TextView) itemView.findViewById(R.id.itemLabel);
            mMontoLabel = (TextView) itemView.findViewById(R.id.montoLabel);

        }

        public void bindHistorial(CompraDeHistorial compraDeHistorial) {
            mFechaLabel.setText(compraDeHistorial.getFechaCompra() + "");
            mItemLabel.setText(compraDeHistorial.getItem().getDescripcion() + " ( x" + compraDeHistorial.getItem().getCantidad() + " )");//compraDeHistorial.getItem().getDescripcion()compraDeHistorial.getCompraItems()
            mMontoLabel.setText("$ "+ compraDeHistorial.getItem().getPrecioUnitario()* compraDeHistorial.getItem().getCantidad());//+compraDeHistorial.getItem().getPrecio()
        }
    }
}
