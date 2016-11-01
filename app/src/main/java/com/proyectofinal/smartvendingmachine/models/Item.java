package com.proyectofinal.smartvendingmachine.models;

/**
 * Created by franc on 6/6/2016.
 */

public class Item {
    private long mProductoID;
    private String mDescripcion;
    private double mPrecioUnitario;
    private long mCantidad;
    private double mPrecio;
    private long mIdBalanza;

    public long getProductoID() {
        return mProductoID;
    }

    public void setProductoID(long productoID) {
        mProductoID = productoID;
    }

    public long getIdBalanza() {
        return mIdBalanza;
    }

    public void setIdBalanza(long idBalanza) {
        mIdBalanza = idBalanza;
    }

    public double getPrecioUnitario() { return mPrecioUnitario; }

    public void setPrecioUnitario(double precioUnitario) {
        mPrecioUnitario = precioUnitario;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String descripcion) {
        mDescripcion = descripcion;
    }

    public double getPrecio() {
        return mPrecio;
    }

    public void setPrecio(double precio) {
        mPrecio = precio;
    }

    public long getCantidad() {
        return mCantidad;
    }

    public void setCantidad(long cantidad) {
        mCantidad = cantidad;
    }

}
