package com.proyectofinal.smartvendingmachine.models;

public class HistorialCompras {
    private String mUserId;
    private Compra[] mCompras;

    public String getUserId() { return mUserId; }

    public void setUserId(String userId) { mUserId = userId; }

    public Compra[] getCompras() {
        return mCompras;
    }

    public void setCompras(Compra[] compras) {
        mCompras = compras;
    }
}
