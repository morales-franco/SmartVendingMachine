package com.proyectofinal.smartvendingmachine.utils;
import com.proyectofinal.smartvendingmachine.models.Usuario;

/**
 * Created by franc on 10/10/2016.
 */

public class ApplicationHelper extends android.app.Application {
    private Usuario currentUser;

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;
    }

    public void updateSaldo(double saldo) {
        this.currentUser.setSaldo(saldo);
    }

}
