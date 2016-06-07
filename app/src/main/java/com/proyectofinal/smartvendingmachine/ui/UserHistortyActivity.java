package com.proyectofinal.smartvendingmachine.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.business.CompraBusiness;
import com.proyectofinal.smartvendingmachine.models.Compra;

import java.util.List;

public class UserHistortyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_historty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // List<Compra> compras = new CompraBusiness().GetList();

    }

}
