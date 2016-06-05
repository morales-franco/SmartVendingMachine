package com.proyectofinal.smartvendingmachine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.proyectofinal.smartvendingmachine.R;

public class MainMenuActivity extends AppCompatActivity {
    private Button mUserAccountButton;
    private Button mUserHistoryButton;
    private Button mBeginPurchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

//      TODO: Aca habria que cargar los intetent con algo asi como lo que esta abajo:
//      Intent intent = getIntent();
//      mName  = intent.getStringExtra(getString(R.string.key_name));

        mUserAccountButton = (Button) findViewById(R.id.userAccountButton);
        mUserHistoryButton = (Button) findViewById(R.id.userHistoryButton);
        mBeginPurchaseButton = (Button) findViewById(R.id.beginPurchaseButton);

        mUserAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserAccountActivity();
            }
        });

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
    private void startUserAccountActivity(){
        Intent intent = new Intent(this, UserAccountActivity.class);
        //TODO: Aca se mandarian los extra que haya que mandarle a la main activity
        //intent.putExtra(getString(R.string.key_name),name);
        startActivity(intent);
    }

    private void startUserHistoryActivity(){
        Intent intent = new Intent(this, UserHistortyActivity.class);
        //TODO: Aca se mandarian los extra que haya que mandarle a la main activity
        //intent.putExtra(getString(R.string.key_name),name);
        startActivity(intent);
    }

    private void startBeginPurchaseActivity(){
        Intent intent = new Intent(this, BeginPurchaseActivity.class);
        //TODO: Aca se mandarian los extra que haya que mandarle a la main activity
        //intent.putExtra(getString(R.string.key_name),name);
        startActivity(intent);
    }
}
