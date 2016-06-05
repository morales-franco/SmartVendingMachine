package com.proyectofinal.smartvendingmachine.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.proyectofinal.smartvendingmachine.R;

public class LoginMainActivity extends AppCompatActivity {
    private Button mLoginButton;
    private EditText mDocumentTextEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        mLoginButton = (Button) findViewById(R.id.loginButton);
        mDocumentTextEdit = (EditText) findViewById(R.id.documentEditText);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: hacer todos los chequeos de seguridad correspondientes.
                startMainActivity();
            }
        });
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        //TODO: Aca se mandarian los extra que haya que mandarle a la main activity
        //intent.putExtra(getString(R.string.key_name),name);
        startActivity(intent);
    }
}
