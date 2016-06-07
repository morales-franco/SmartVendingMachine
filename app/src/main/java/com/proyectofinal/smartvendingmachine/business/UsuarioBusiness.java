package com.proyectofinal.smartvendingmachine.business;

import com.proyectofinal.smartvendingmachine.models.Usuario;
import com.proyectofinal.smartvendingmachine.repository.UsuarioRepository;

import java.util.List;

/**
 * Created by franc on 6/6/2016.
 */
public class UsuarioBusiness {
    private UsuarioRepository Repository = new UsuarioRepository();

    public Usuario Get(){
        return Repository.Get();
    }
}
