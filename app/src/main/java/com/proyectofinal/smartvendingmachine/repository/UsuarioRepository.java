package com.proyectofinal.smartvendingmachine.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.sax.EndTextElementListener;

import com.proyectofinal.smartvendingmachine.models.Usuario;

/**
 * Created by franc on 6/6/2016.
 * Estructura de Tabla y metodos CRUD
 * Singleton de DbHelper y UsuarioRepo
 */
public class UsuarioRepository {

    public static final String TABLE_NAME = "Usuario";
    public static final String COL_ID = "id"; //Por SQLlite
    public static final String COL_USER_ID = "UserID"; //UserID de la BD server
    public static final String COL_USER_NAME = "UserName";
    public static final String COL_NOMBRE_COMPLETO = "NombreCompleto";
    public static final String COL_SALDO = "Saldo";

    private static DbHelper dataBaseService = null;
    private static UsuarioRepository instance = null;

    private UsuarioRepository(){

    }

    public static UsuarioRepository GetInstance(Context context){
        if(instance == null)
        {
            instance = new UsuarioRepository();
            dataBaseService = new DbHelper(context);
        }
        return instance;
    }




    public static String GetScriptCreateTable() {
        String script = "";
        script = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL," +
                        "%s REAL NOT NULL )",
                UsuarioRepository.TABLE_NAME, UsuarioRepository.COL_ID, UsuarioRepository.COL_USER_ID, UsuarioRepository.COL_USER_NAME,
                UsuarioRepository.COL_NOMBRE_COMPLETO, UsuarioRepository.COL_SALDO);
        return  script;
    }



    public Usuario GetCurrentuser(){
        SQLiteDatabase db = dataBaseService.getWritableDatabase();
        Usuario currentUser = null;

        String columns[] = {
                COL_USER_ID, COL_USER_NAME,
                COL_NOMBRE_COMPLETO, COL_SALDO
        };

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        if(cursor.moveToFirst()){
            currentUser = new Usuario();
            currentUser.setUserID(cursor.getString(0));
            currentUser.setUserName(cursor.getString(1));
            currentUser.setNombreCompleto(cursor.getString(2));
            currentUser.setSaldo(cursor.getDouble(3));
        }

        return  currentUser;
    }

    public void Insert(Usuario entity){
        SQLiteDatabase db = dataBaseService.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COL_USER_ID, entity.getUserID());
        valores.put(COL_NOMBRE_COMPLETO, entity.getNombreCompleto());
        valores.put(COL_USER_NAME, entity.getUserName());
        valores.put(COL_SALDO, entity.getSaldo());
        db.insertOrThrow(TABLE_NAME, null,  valores);
    }

    public void UpdateSaldo(String userID, double saldo){
        SQLiteDatabase db = dataBaseService.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COL_SALDO, saldo);
        db.update(TABLE_NAME, valores, COL_USER_ID + " = ?", new String[] {userID});
    }
}
