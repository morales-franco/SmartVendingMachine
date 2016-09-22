//package com.proyectofinal.smartvendingmachine.repository;
//
//import com.proyectofinal.smartvendingmachine.models.Compra;
//import com.proyectofinal.smartvendingmachine.models.CompraItem;
//import com.proyectofinal.smartvendingmachine.models.Item;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by franc on 6/6/2016.
// */
//public class CompraRepository {
//
//    public List<Compra> GetList(){
//        List<Compra> compras = new ArrayList<>();
//        Date fechaActual = new Date();
//
//        Compra compra1 = new Compra();
//        List<CompraItem> compra1_items = new ArrayList<CompraItem>();
//        compra1.setTotal(200.0);
//        compra1.setFechaCompra(fechaActual);
//
//        CompraItem compra1_item1 = new CompraItem();
//        compra1_item1.setCantidad(1);
//        compra1_item1.setPrecio(50.0);
//
//        Item compra1_producto1 = new Item();
//        compra1_producto1.setItemID(1);
//        compra1_producto1.setDescripcion("Coca 750cc");
//
//        compra1_item1.setItem(compra1_producto1);
//
//
//
//        CompraItem compra1_item2 = new CompraItem();
//        compra1_item2.setCantidad(3);
//        compra1_item2.setPrecio(50.0);
//
//        Item compra1_producto2 = new Item();
//        compra1_producto2.setItemID(2);
//        compra1_producto2.setDescripcion("Fanta 750cc");
//
//        compra1_item2.setItem(compra1_producto2);
//
//        compra1_items.add(compra1_item1);
//        compra1_items.add(compra1_item2);
//        compra1.setCompraItems(compra1_items);
//
//
//        Compra compra2 = new Compra();
//        List<CompraItem> compra2_items = new ArrayList<CompraItem>();
//        compra2.setTotal(200.0);
//        compra2.setFechaCompra(fechaActual);
//
//        CompraItem compra2_item1 = new CompraItem();
//        compra2_item1.setCantidad(1);
//        compra2_item1.setPrecio(100.0);
//
//        Item compra2_producto1 = new Item();
//        compra2_producto1.setItemID(3);
//        compra2_producto1.setDescripcion("Pepsi 1l");
//
//        compra2_item1.setItem(compra2_producto1);
//
//        CompraItem compra2_item2 = new CompraItem();
//        compra2_item2.setCantidad(2);
//        compra2_item2.setPrecio(50.0);
//
//        Item compra2_producto2 = new Item();
//        compra2_producto2.setItemID(4);
//        compra2_producto2.setDescripcion("Levite Naranja 750cc");
//
//        compra2_item2.setItem(compra2_producto2);
//
//        compra2_items.add(compra2_item1);
//        compra2_items.add(compra2_item2);
//
//        compra2.setCompraItems(compra2_items);
//
//        compras.add(compra1);
//        compras.add(compra2);
//
//        return  compras;
//    }
//}
