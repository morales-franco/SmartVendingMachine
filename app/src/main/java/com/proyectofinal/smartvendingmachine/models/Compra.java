package com.proyectofinal.smartvendingmachine.models;

import java.util.Date;

/**
 * Created by franc on 6/6/2016.
 */
public class Compra {
    private String mFechaCompra;
    private Item mItem;

    public String getFechaCompra() {
        return mFechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        mFechaCompra = fechaCompra;
    }

    public Item getItem() {
        return mItem;
    }

    public void setItem(Item item) {
        mItem = item;
    }

}
