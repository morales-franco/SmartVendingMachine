package com.proyectofinal.smartvendingmachine.models;

public class HistorialCompras {
    private String mUserId;
    private CompraDeHistorial[] mCompraDeHistorials;

    public String getUserId() { return mUserId; }

    public void setUserId(String userId) { mUserId = userId; }

    public CompraDeHistorial[] getCompraDeHistorials() {
        return mCompraDeHistorials;
    }

    public void setCompraDeHistorials(CompraDeHistorial[] compraDeHistorials) {
        mCompraDeHistorials = compraDeHistorials;
    }
}
