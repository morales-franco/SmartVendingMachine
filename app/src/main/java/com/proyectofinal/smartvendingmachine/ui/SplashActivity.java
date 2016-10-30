package com.proyectofinal.smartvendingmachine.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.models.Usuario;
import com.proyectofinal.smartvendingmachine.repository.UsuarioRepository;
import com.proyectofinal.smartvendingmachine.services.usuarioService;
import com.proyectofinal.smartvendingmachine.utils.Api;
import com.proyectofinal.smartvendingmachine.utils.ApplicationHelper;
import com.proyectofinal.smartvendingmachine.utils.NetworkHelper;
import com.proyectofinal.smartvendingmachine.utils.ToastHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UsuarioRepository usuarioRepo = UsuarioRepository.GetInstance(getApplicationContext());
        Usuario currentUser = usuarioRepo.GetCurrentuser();


        if(currentUser != null) {
            getSaldoActual(currentUser.getUserID());
        }

        Intent intent = new Intent(this, LoginMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getSaldoActual(String userID){
        if (NetworkHelper.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            OkHttpClient client = new OkHttpClient();
            UsuarioRepository usuarioRepo = UsuarioRepository.GetInstance(getApplicationContext());
            Usuario currentUser = usuarioRepo.GetCurrentuser();

            String saldoJsonURL = Api.UrlGetSaldo+ "?userID=" + currentUser.getUserID();
            Request request = new Request.Builder().url(saldoJsonURL).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastHelper.backgroundThreadShortToast(getApplicationContext(),"Fallo al conectarse con el servidor" + e.getMessage(), Toast.LENGTH_LONG);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        try {
                            JSONObject jsonResponse = new JSONObject(responseStr);
                            if(jsonResponse != null){
                                double saldoActualizado = jsonResponse.getDouble("saldo");
                                ((ApplicationHelper) SplashActivity.this.getApplication()).updateSaldo(saldoActualizado);
                            }else {
                                ToastHelper.backgroundThreadShortToast(getApplicationContext(),"Respuesta del Servidor Incorrecta", Toast.LENGTH_SHORT);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        ToastHelper.backgroundThreadShortToast(getApplicationContext(),"Respuesta del Servidor Incorrecta", Toast.LENGTH_SHORT);
                    }

                }
            });
        } else {
            Toast.makeText(this, R.string.error_red_no_disponible, Toast.LENGTH_LONG).show();
        }
    }
}
