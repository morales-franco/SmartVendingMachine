package com.proyectofinal.smartvendingmachine.repository;

import com.proyectofinal.smartvendingmachine.models.Credito;

import java.util.Date;

/**
 * Created by franc on 7/6/2016.
 */
public class CreditoRepository {

    public Credito Get(){
        Credito credito = new Credito();
        Date fechaActual = new Date();

        credito.setLastDeposito(fechaActual);
        credito.setSaldo(1500.0);

        return credito;
    }

}
