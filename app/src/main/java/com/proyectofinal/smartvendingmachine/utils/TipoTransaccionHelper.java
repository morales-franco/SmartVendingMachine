package com.proyectofinal.smartvendingmachine.utils;

import com.proyectofinal.smartvendingmachine.models.Transaccion;

/**
 * Created by franc on 30/10/2016.
 */

public class TipoTransaccionHelper {
    public static final int COMPRA = 1;
    public static final int DEVOLUCION = 2;
    public static final int ERROR_PRODUCTO_BAL_EQUIVOCADA = 3;

    public static Transaccion GetTipo(int transaccionID){
        Transaccion result = new Transaccion();
        result.setTransaccionID(transaccionID);

        switch (transaccionID){
            case COMPRA:
                result.setEsError(false);
                break;
            case DEVOLUCION:
                result.setEsError(false);
                break;
            case ERROR_PRODUCTO_BAL_EQUIVOCADA:
                result.setEsError(true);
                result.setMensaje("Debe devolver el producto a la balanza correcta");
                break;
            default:
                break;
        }

        return  result;
    }

}
