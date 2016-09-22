package com.proyectofinal.smartvendingmachine.models;

public class HistorialCompras {
    private double mCredito;
    private long mUserId;
    private Compra[] mCompras;

    public double getCredito() {
        return mCredito;
    }

    public void setCredito(double credito) {
        mCredito = credito;
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }

    public Compra[] getCompras() {
        return mCompras;
    }

    public void setCompras(Compra[] compras) {
        mCompras = compras;
    }
}
