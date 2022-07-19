package com.fabrizio.junit5.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fabrizio.junit5.dto.RegistraEnvioDTO;
import com.fabrizio.junit5.services.EnvioService;
import com.fabrizio.junit5.services.responses.ServiceResponse;
import com.google.gson.Gson;

public class EnvioRestTest {

    MockMvc mockMvc;

    @Mock
    private EnvioService envioService;

    @InjectMocks
    private EnvioRestController envioRestController;


    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(envioRestController).build();
    }

    @Test
    @DisplayName("Registrar envío OK")  
    void test1() throws Exception{

        when(envioService.registrarEnvio(any(RegistraEnvioDTO.class))).thenReturn(new ServiceResponse(ServiceResponse.STATUS_SUCCESS, 1, 201, "Envío creado con éxito"));

        MvcResult result = mockMvc.perform(post("/envio")
                    .content("{\"clienteId\": 1,\"transporteId\": 1,\"origen\": \"origen\",\"destino\":\"destino\"}")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated()).andReturn();
        
        String jsonResponse = result.getResponse().getContentAsString();
        assertNotNull(jsonResponse);
        ServiceResponse response = new Gson().fromJson(jsonResponse, ServiceResponse.class);
        assertNotNull(response);
        assertEquals(201, response.getCode());
        assertEquals(ServiceResponse.STATUS_SUCCESS, response.getStatus());
        
    }

    @Test
    @DisplayName("Registrar envío Falla")  
    void test2() throws Exception{

        when(envioService.registrarEnvio(any(RegistraEnvioDTO.class))).thenReturn(new ServiceResponse(ServiceResponse.STATUS_ERROR, null, 400, "Campos inválidos"));

        MvcResult result = mockMvc.perform(post("/envio")
                    .content("{\"clienteId\": 1,\"origen\": \"origen\",\"destino\":\"destino\"}")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest()).andReturn();
        
        String jsonResponse = result.getResponse().getContentAsString();
        assertNotNull(jsonResponse);
        ServiceResponse response = new Gson().fromJson(jsonResponse, ServiceResponse.class);
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(ServiceResponse.STATUS_ERROR, response.getStatus());
        
    }


}
