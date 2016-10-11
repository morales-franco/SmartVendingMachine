package com.proyectofinal.smartvendingmachine.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by franc on 10/10/2016.
 * Creación de BD o actualización de la misma
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "SmartVending.sqlite"; //Nombre del archivo de la BD
    private  static final int DB_SCHEME_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UsuarioRepository.GetScriptCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsuarioRepository.TABLE_NAME);
        onCreate(db);
    }
}
