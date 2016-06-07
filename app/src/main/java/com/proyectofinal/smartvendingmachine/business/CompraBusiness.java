package com.proyectofinal.smartvendingmachine.business;

import com.proyectofinal.smartvendingmachine.models.Compra;
import com.proyectofinal.smartvendingmachine.repository.CompraRepository;

import java.util.List;

/**
 * Created by franc on 6/6/2016.
 */
public class CompraBusiness {
    private CompraRepository Repository = new CompraRepository();

    public List<Compra> GetList(){
        return Repository.GetList();
    }
}
