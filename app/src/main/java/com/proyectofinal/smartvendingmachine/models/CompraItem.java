package com.proyectofinal.smartvendingmachine.models;

/**
 * Created by franc on 6/6/2016.
 */
//todo: va?
public class CompraItem {
    private Item mItem;
    private int mCantidad;
    private double mPrecio;

    public Item getItem() {
        return mItem;
    }

    public void setItem(Item item) {
        mItem = item;
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
