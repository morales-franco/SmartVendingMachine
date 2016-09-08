//package com.proyectofinal.smartvendingmachine.repository;
//
//import com.proyectofinal.smartvendingmachine.models.Compra;
//import com.proyectofinal.smartvendingmachine.models.CompraItem;
//import com.proyectofinal.smartvendingmachine.models.Producto;
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
//        Producto compra1_producto1 = new Producto();
//        compra1_producto1.setProductoID(1);
//        compra1_producto1.setNombre("Coca 750cc");
//
//        compra1_item1.setProducto(compra1_producto1);
//
//
//
//        CompraItem compra1_item2 = new CompraItem();
//        compra1_item2.setCantidad(3);
//        compra1_item2.setPrecio(50.0);
//
//        Producto compra1_producto2 = new Producto();
//        compra1_producto2.setProductoID(2);
//        compra1_producto2.setNombre("Fanta 750cc");
//
//        compra1_item2.setProducto(compra1_producto2);
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
//        Producto compra2_producto1 = new Producto();
//        compra2_producto1.setProductoID(3);
//        compra2_producto1.setNombre("Pepsi 1l");
//
//        compra2_item1.setProducto(compra2_producto1);
//
//        CompraItem compra2_item2 = new CompraItem();
//        compra2_item2.setCantidad(2);
//        compra2_item2.setPrecio(50.0);
//
//        Producto compra2_producto2 = new Producto();
//        compra2_producto2.setProductoID(4);
//        compra2_producto2.setNombre("Levite Naranja 750cc");
//
//        compra2_item2.setProducto(compra2_producto2);
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
