package com.proyectofinal.smartvendingmachine.models;

public class HistorialCompras {
    private String mConcepto;
    private long mFecha;
    private Producto mProducto;
    private long mPrecio;

    public long getPrecio() {
        return mPrecio;
    }

    public void setPrecio(long precio) {
        mPrecio = precio;
    }

    public String getConcepto() {
        return mConcepto;
    }

    public void setConcepto(String concepto) {
        mConcepto = concepto;
    }

    public long getFecha() {
        return mFecha;
    }

    public void setFecha(long fecha) {
        mFecha = fecha;
    }

    public Producto getProducto() {
        return mProducto;
    }

    public void setProducto(Producto producto) {
        mProducto = producto;
    }



}
