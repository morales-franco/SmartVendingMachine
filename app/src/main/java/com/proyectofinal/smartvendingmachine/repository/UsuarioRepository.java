package com.proyectofinal.smartvendingmachine.repository;

import com.proyectofinal.smartvendingmachine.models.Usuario;

/**
 * Created by franc on 6/6/2016.
 */
public class UsuarioRepository {

    public Usuario Get(){
        Usuario currentUser = new Usuario();

        currentUser.setEmail("test@gmail.com");
        currentUser.setFirstName("Test");
        currentUser.setLastName("Testeo");
        currentUser.setPassword("123");
        currentUser.setUserID("123ASD!312"); //Hash
        currentUser.setUserName("UserTest");

        return  currentUser;
    }
}
