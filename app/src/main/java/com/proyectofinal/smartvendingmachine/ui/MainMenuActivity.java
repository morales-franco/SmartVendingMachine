package com.proyectofinal.smartvendingmachine.ui;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.models.Usuario;
import com.proyectofinal.smartvendingmachine.services.usuarioService;
import com.proyectofinal.smartvendingmachine.ui.connection.BeginPurchaseActivity;
import com.proyectofinal.smartvendingmachine.utils.Api;
import com.proyectofinal.smartvendingmachine.utils.ApplicationHelper;
import com.proyectofinal.smartvendingmachine.utils.NetworkHelper;
import com.proyectofinal.smartvendingmachine.utils.ToastHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainMenuActivity extends AppCompatActivity {

    @BindView(R.id.userHistoryButton) Button mUserHistoryButton;
    @BindView(R.id.beginPurchaseButton) Button mBeginPurchaseButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);

        //Ejemplo de obtener el currentUser de la variable de aplicación
        Usuario currentUser = ((ApplicationHelper) this.getApplication()).getCurrentUser();
        ToastHelper.backgroundThreadShortToast(getApplicationContext(),"Bienvenido " + currentUser.getNombreCompleto(), Toast.LENGTH_SHORT);

        if (!NetworkHelper.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            ToastHelper.backgroundThreadShortToast(getApplicationContext(),"No se encuentra conectado a ninguna RED.", Toast.LENGTH_LONG);
            mUserHistoryButton.setEnabled(false);
            mBeginPurchaseButton.setEnabled(false);
        }else if( currentUser.getSaldo() <= 0 ){
            ToastHelper.backgroundThreadShortToast(getApplicationContext(),"Usted NO posee saldo por favor comuniquese con el administrador.", Toast.LENGTH_LONG);
            mBeginPurchaseButton.setEnabled(false);
        }

        mUserHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserHistoryActivity();
            }
        });

        mBeginPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBeginPurchaseActivity();
            }
        });

    }

    private void startUserHistoryActivity(){
        Intent intent = new Intent(this, UserHistortyActivity.class);
        startActivity(intent);
    }

    private void startBeginPurchaseActivity(){
        Intent intent = new Intent(this, ConnectDeviceActivity.class);
        startActivity(intent);
    }


}
