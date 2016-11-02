package com.proyectofinal.smartvendingmachine.models;

/**
 * Created by franc on 30/10/2016.
 */

public class Transaccion {
    private int mTransaccionID;
    private String mMensaje;
    private boolean mEsError;

    public long getTransaccionID() {
        return mTransaccionID;
    }

    public void setTransaccionID(int transaccionID) {
        mTransaccionID = transaccionID;
    }

    public String getMensaje() {
        return mMensaje;
    }

    public void setMensaje(String mensaje) {
        mMensaje = mensaje;
    }

    public boolean getEsError() {
        return mEsError;
    }

    public void setEsError(boolean error) {
        mEsError = error;
    }
}
