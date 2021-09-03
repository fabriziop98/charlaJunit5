package com.fabrizio.junit5.services;

import java.util.Date;

import com.fabrizio.junit5.entities.Cliente;
import com.fabrizio.junit5.entities.Transporte;

public class Datos {

    public static final Cliente getCliente(){
        Cliente cliente = new Cliente();
        cliente.setNombre("nombre");
        cliente.setApellido("apellido");
        cliente.setDocumento(12345);
        cliente.setId(1);
        cliente.setAlta(new Date());
        return cliente;
    }

    public static final Transporte getTransporte(){
        Transporte transporte = new Transporte();
        transporte.setAlta(new Date());
        transporte.setDisponible(true);
        transporte.setId(1);
        transporte.setMarca("marca");
        transporte.setModelo("modelo");
        return transporte;
    }
    
}