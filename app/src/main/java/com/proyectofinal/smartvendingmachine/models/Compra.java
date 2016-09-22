package com.proyectofinal.smartvendingmachine.models;

import java.util.Date;

/**
 * Created by franc on 6/6/2016.
 */
public class Compra {
    private long mFechaCompra;
    private double mUsuarioId;
    private Item mItem;

    public long getFechaCompra() {
        return mFechaCompra;
    }

    public void setFechaCompra(long fechaCompra) {
        mFechaCompra = fechaCompra;
    }

    public double getUsuarioId() {
        return mUsuarioId;
    }

    public void setUsuarioId(double usuarioId) {
        mUsuarioId = usuarioId;
    }

    public Item getItem() {
        return mItem;
    }

    public void setItem(Item item) {
        mItem = item;
    }

}
