package com.fabrizio.junit5.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabrizio.junit5.dto.RegistraEnvioDTO;
import com.fabrizio.junit5.services.EnvioService;
import com.fabrizio.junit5.services.responses.ServiceResponse;

@RestController
@RequestMapping("/envio")
public class EnvioRestController {

    @Autowired
    private EnvioService envioService;

    @PostMapping
    public ResponseEntity<?> registrarEnvio(@RequestBody RegistraEnvioDTO dto){
        ServiceResponse response = envioService.registrarEnvio(dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
