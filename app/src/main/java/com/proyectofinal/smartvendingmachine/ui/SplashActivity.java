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
    UsuarioRepository usuarioRepo = null;
    Usuario currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuarioRepo = UsuarioRepository.GetInstance(getApplicationContext());
        currentUser = usuarioRepo.GetCurrentuser();

        if(currentUser != null)
        {
            ((ApplicationHelper) this.getApplication()).setCurrentUser(currentUser);
            this.getSaldoActual(currentUser.getUserID());
        }else {
            startLoginActivity();
            finish();
        }
    }

    private void getSaldoActual(String userID) {
        if (NetworkHelper.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            usuarioService service = new usuarioService();

            service.GetSaldo(Api.UrlGetSaldo, userID, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    ToastHelper.backgroundThreadShortToast(getApplicationContext(), "Fallo al conectarse con el servidor" + e.getMessage(), Toast.LENGTH_LONG);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        try {
                            JSONObject jsonResponse = new JSONObject(responseStr);
                            Double saldoActualizado = jsonResponse.getDouble("saldo");
                            usuarioRepo.UpdateSaldo(currentUser.getUserID(), saldoActualizado);
                            ((ApplicationHelper)SplashActivity.this.getApplication()).updateSaldo(saldoActualizado);
                            startMainActivity();
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        ToastHelper.backgroundThreadShortToast(getApplicationContext(), "Respuesta del Servidor Incorrecta", Toast.LENGTH_SHORT);
                    }
                }
            });
        }else {
            Toast.makeText(this, R.string.error_red_no_disponible, Toast.LENGTH_LONG).show();
        }
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, LoginMainActivity.class);
        startActivity(intent);
    }
}
