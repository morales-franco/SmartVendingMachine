package com.proyectofinal.smartvendingmachine.models;

/**
 * Created by franc on 6/6/2016.
 */
//todo: va?
public class Usuario {
    private  String mUserID;
    private  String mUserName;
    private  String mNombreCompleto;
    private  Double mSaldo;

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public String getNombreCompleto() {
        return mNombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        mNombreCompleto = nombreCompleto;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public Double getSaldo() {
        return mSaldo;
    }

    public void setSaldo(Double saldo) {
        mSaldo = saldo;
    }



}
