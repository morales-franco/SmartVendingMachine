package com.proyectofinal.smartvendingmachine.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.adapters.HistorialAdapter;
import com.proyectofinal.smartvendingmachine.models.Compra;
import com.proyectofinal.smartvendingmachine.models.CompraItem;
import com.proyectofinal.smartvendingmachine.models.Producto;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserHistortyActivity extends AppCompatActivity {

    private Compra[] mCompras;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_historty);
        ButterKnife.bind(this);

        Producto producto = new Producto();
        producto.setNombre("Manaos 660cc.");
        producto.setProductoID(12);

        CompraItem[] compraItems = new CompraItem[1];
        compraItems[0] = new CompraItem();
        compraItems[0].setCantidad(1);
        compraItems[0].setProducto(producto);
        compraItems[0].setPrecio(12);



        Compra[] compras = new Compra[101];
        for (int i=0; i<=100; i++) {
            compras[i] = new Compra();
            compras[i].setFechaCompra(new Date());
            compras[i].setTotal(123.2);
            compras[i].setCompraItems(compraItems);
        }
        //no usar esto. hacer como correpsonde con json

        HistorialAdapter adapter = new HistorialAdapter(compras);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


    }

}
