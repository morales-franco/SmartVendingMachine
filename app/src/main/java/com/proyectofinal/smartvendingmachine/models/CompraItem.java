package com.proyectofinal.smartvendingmachine.models;

/**
 * Created by franc on 6/6/2016.
 */
public class CompraItem {
    private Producto mProducto;
    private int mCantidad;
    private double mPrecio;

    public Producto getProducto() {
        return mProducto;
    }

    public void setProducto(Producto producto) {
        mProducto = producto;
    }

    public int getCantidad() {
        return mCantidad;
    }

    public void setCantidad(int cantidad) {
        mCantidad = cantidad;
    }

    public double getPrecio() {
        return mPrecio;
    }

    public void setPrecio(double precio) {
        mPrecio = precio;
    }

}
