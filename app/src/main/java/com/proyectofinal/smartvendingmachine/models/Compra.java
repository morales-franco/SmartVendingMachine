package com.proyectofinal.smartvendingmachine.models;

import java.util.Date;
import java.util.List;

/**
 * Created by franc on 6/6/2016.
 */
public class Compra {
    private Date mFechaCompra;
    private Double mTotal;
    private List<CompraItem> mCompraItems;


    public Date getFechaCompra() {
        return mFechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        mFechaCompra = fechaCompra;
    }

    public Double getTotal() {
        return mTotal;
    }

    public void setTotal(Double total) {
        mTotal = total;
    }

    public List<CompraItem> getCompraItems() {
        return mCompraItems;
    }

    public void setCompraItems(List<CompraItem> compraItems) {
        mCompraItems = compraItems;
    }



}
