package com.proyectofinal.smartvendingmachine.utils;

/**
 * Created by franc on 8/10/2016.
 */
public class Api {
    //http://192.168.0.11/BackOffice/Api
    //http://smartvendingdev.somee.com/
    public static final String UrlRoot = "http://smartvendingdev.somee.com/BackOffice/Api";
    public static final String UrlSubmitCompra = Api.UrlRoot + "/Compra/Compra/";
    public static final String UrlGetHistorialCompra = Api.UrlRoot + "/Compra/HistorialCompras";
    public static final String UrlPermitirAcceso = Api.UrlRoot + "/Usuario/PermitirAcceso";
    public static final String UrlGetSaldo = Api.UrlRoot + "/Usuario/GetSaldo";


}
