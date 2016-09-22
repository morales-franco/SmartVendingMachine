package com.proyectofinal.smartvendingmachine.models;

/**
 * Created by franc on 6/6/2016.
 */

public class Item {
    private long mItemID;
    private String mDescripcion;
    private double mPrecio;

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String descripcion) {
        mDescripcion = descripcion;
    }

    public long getItemID() {
        return mItemID;
    }

    public void setItemID(long itemID) {
        mItemID = itemID;
    }

    public double getPrecio() {
        return mPrecio;
    }

    public void setPrecio(double precio) {
        mPrecio = precio;
    }
}
