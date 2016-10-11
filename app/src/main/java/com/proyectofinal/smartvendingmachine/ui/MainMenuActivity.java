package com.proyectofinal.smartvendingmachine.ui;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.proyectofinal.smartvendingmachine.R;
import com.proyectofinal.smartvendingmachine.models.Usuario;
import com.proyectofinal.smartvendingmachine.utils.ApplicationHelper;

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


//      TODO: Aca habria que cargar los intetent con algo asi como lo que esta abajo:
//      Intent intent = getIntent();
//      mName  = intent.getStringExtra(getString(R.string.key_name));

 //       mUserHistoryButton = (Button) findViewById(R.id.userHistoryButton);
 //       mBeginPurchaseButton = (Button) findViewById(R.id.beginPurchaseButton);

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
        //TODO: Aca se mandarian los extra que haya que mandarle a la main activity
        //intent.putExtra(getString(R.string.key_name),name);
        startActivity(intent);
    }

    private void startBeginPurchaseActivity(){
        Intent intent = new Intent(this, ConnectDeviceActivity.class);
        //TODO: Aca se mandarian los extra que haya que mandarle a la main activity
        //intent.putExtra(getString(R.string.key_name),name);
        startActivity(intent);
    }
}
