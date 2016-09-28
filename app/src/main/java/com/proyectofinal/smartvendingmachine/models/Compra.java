package com.proyectofinal.smartvendingmachine.models;

import java.util.ArrayList;
import java.util.Date;

public class Compra {
    private String mUserId;
    private long mExhibidorId;
    private String mFechaCompra;
    private long mMonto;
    private ArrayList<Item> mItems;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public long getExhibidorId() {
        return mExhibidorId;
    }

    public void setExhibidorId(long exhibidorId) {
        mExhibidorId = exhibidorId;
    }

    public String getFechaCompra() {
        return mFechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        mFechaCompra = fechaCompra;
    }

    public long getMonto() {
        return mMonto;
    }

    public void setMonto(long monto) {
        mMonto = monto;
    }

    public ArrayList<Item> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Item> items) {
        mItems = items;
    }

}