package com.proyectofinal.smartvendingmachine.models;

import java.util.Date;

/**
 * Created by franc on 6/6/2016.
 */
//todo: va?
public class Credito {
    private Date mLastDeposito;
    private double mSaldo;

    public Date getLastDeposito() {
        return mLastDeposito;
    }

    public void setLastDeposito(Date lastDeposito) {
        mLastDeposito = lastDeposito;
    }

    public double getSaldo() {
        return mSaldo;
    }

    public void setSaldo(double saldo) {
        mSaldo = saldo;
    }
}
