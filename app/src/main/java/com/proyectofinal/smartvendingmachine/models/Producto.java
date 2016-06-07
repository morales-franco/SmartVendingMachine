package com.proyectofinal.smartvendingmachine.models;

/**
 * Created by franc on 6/6/2016.
 */
public class Producto {
    private long mProductoID;
    private String mNombre;

    public long getProductoID() {
        return mProductoID;
    }

    public void setProductoID(long productoID) {
        mProductoID = productoID;
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }
}
