package com.proyectofinal.smartvendingmachine.business;

import com.proyectofinal.smartvendingmachine.models.Credito;
import com.proyectofinal.smartvendingmachine.repository.CreditoRepository;

/**
 * Created by franc on 7/6/2016.
 */
public class CreditoBusiness {
    private CreditoRepository Repository = new CreditoRepository();

    public Credito Get(){
        return Repository.Get();
    }

}
