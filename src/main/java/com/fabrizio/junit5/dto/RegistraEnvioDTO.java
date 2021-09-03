package com.fabrizio.junit5.dto;

import lombok.Data;

@Data
public class RegistraEnvioDTO {

    private Integer clienteId;
    private Integer transporteId;
    private String origen;
    private String destino;
    
    
}