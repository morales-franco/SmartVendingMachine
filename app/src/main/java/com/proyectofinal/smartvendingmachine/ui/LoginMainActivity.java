package com.proyectofinal.smartvendingmachine.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginMainActivity extends AppCompatActivity {
    UsuarioRepository usuarioRepo;
    private Button mLoginButton;
    private EditText mUserNameTxt;
    private EditText mPasswordTxt;
    public static final String TAG = LoginMainActivity.class.getSimpleName();
    ProgressDialog progressDialog = null;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usuarioRepo = UsuarioRepository.GetInstance(getApplicationContext());
        Usuario currentUser = usuarioRepo.GetCurrentuser();

        if(currentUser != null) {
            ((ApplicationHelper) this.getApplication()).setCurrentUser(currentUser);
            startMainActivity();
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);



        mLoginButton = (Button) findViewById(R.id.loginButton);
        mUserNameTxt = (EditText) findViewById(R.id.txtUserName);
        mPasswordTxt = (EditText) findViewById(R.id.txtPassword);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                if(mUserNameTxt.getText().toString().trim().equalsIgnoreCase("")) {
                    mUserNameTxt.setError( "Ingrese nombre de usuario");
                    isValid = false;
                }

                if(mPasswordTxt.getText().toString().trim().equalsIgnoreCase("")) {
                    mPasswordTxt.setError( "Ingrese contraseña");
                    isValid = false;
                }

                if(!isValid)
                    return;

                validateUser(mUserNameTxt.getText().toString().trim(),mPasswordTxt.getText().toString().trim());
            }
        });
    }

    private void validateUser(String userName, String password){
        if (NetworkHelper.isNetworkAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            progressDialog = ProgressDialog.show(this, "", "Espere por favor...", true);
            usuarioService service = new usuarioService();

            service.Autenticar(Api.UrlPermitirAcceso, userName, password, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastHelper.backgroundThreadShortToast(getApplicationContext(),"Fallo al conectarse con el servidor" + e.getMessage(), Toast.LENGTH_LONG);
                    if(progressDialog != null)
                        progressDialog.dismiss();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        try {
                            JSONObject jsonResponse = new JSONObject(responseStr);

                            if(jsonResponse.getBoolean("autenticado")){
                                Usuario currentUser = new Usuario();
                                currentUser.setUserID(jsonResponse.getString("userID"));
                                currentUser.setUserName(jsonResponse.getString("userName"));
                                currentUser.setNombreCompleto(jsonResponse.getString("nombreCompleto"));
                                currentUser.setSaldo(jsonResponse.getDouble("saldo"));
                                ((ApplicationHelper) LoginMainActivity.this.getApplication()).setCurrentUser(currentUser);
                                usuarioRepo.Insert(currentUser);
                                startMainActivity();

                            }else {
                                ToastHelper.backgroundThreadShortToast(getApplicationContext(),"Usuario y/o contraseña incorrecta", Toast.LENGTH_SHORT);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        ToastHelper.backgroundThreadShortToast(getApplicationContext(),"Respuesta del Servidor Incorrecta", Toast.LENGTH_SHORT);
                    }
                    if(progressDialog != null)
                        progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, R.string.error_red_no_disponible, Toast.LENGTH_LONG).show();
        }
    }


    private void startMainActivity(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        //TODO: Aca se mandarian los extra que haya que mandarle a la main activity
        //intent.putExtra(getString(R.string.key_name),name);
        startActivity(intent);
    }



}
