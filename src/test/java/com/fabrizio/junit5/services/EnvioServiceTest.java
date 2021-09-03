package com.fabrizio.junit5.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.fabrizio.junit5.dto.RegistraEnvioDTO;
import com.fabrizio.junit5.entities.Envio;
import com.fabrizio.junit5.repositories.ClienteRepository;
import com.fabrizio.junit5.repositories.EnvioRepository;
import com.fabrizio.junit5.repositories.TransporteRepository;
import com.fabrizio.junit5.services.responses.ServiceResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
public class EnvioServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private TransporteRepository transporteRepository;

    @InjectMocks
    private EnvioService envioService;

    @Nested
    @DisplayName("Test registro de envíos")
    class RegistrarEnvioTests{

        private final RegistraEnvioDTO getRegistraEnvioDTO(){
            RegistraEnvioDTO dto = new RegistraEnvioDTO();
            dto.setDestino("destino");
            dto.setOrigen("origen");
            dto.setClienteId(1);
            dto.setTransporteId(1);
            return dto;
        }

        @Test
        @DisplayName("Registro envío sin cliente ID")
        void registrarEnvio(){
            RegistraEnvioDTO dto = new RegistraEnvioDTO();
            dto.setDestino("destino");
            dto.setOrigen("origen");
            dto.setTransporteId(1);

            ServiceResponse response = envioService.registrarEnvio(dto);

            assertNotNull(response);
            assertNull(response.getData());
            assertEquals("El id del cliente no puede ser nulo", response.getMessage());
            assertEquals(ServiceResponse.STATUS_ERROR, response.getStatus());
            assertEquals(400, response.getCode());

            verify(clienteRepository, never()).findById(anyInt());
            verify(transporteRepository, never()).findById(anyInt());
            verify(envioRepository, never()).save(any(Envio.class));
        }

        @Test
        @DisplayName("Registro envío sin transporte ID")
        void registrarEnvio2(){
            RegistraEnvioDTO dto = new RegistraEnvioDTO();
            dto.setDestino("destino");
            dto.setOrigen("origen");
            dto.setClienteId(1);

            ServiceResponse response = envioService.registrarEnvio(dto);

            assertNotNull(response);
            assertNull(response.getData());
            assertEquals("El id del transporte no puede ser nulo", response.getMessage());
            assertEquals(ServiceResponse.STATUS_ERROR, response.getStatus());
            assertEquals(400, response.getCode());

            verify(clienteRepository, never()).findById(anyInt());
            verify(transporteRepository, never()).findById(anyInt());
            verify(transporteRepository, never()).transporteDisponible(anyInt());
            verify(envioRepository, never()).save(any(Envio.class));
        }

        @Test
        @DisplayName("Registro envío sin orígen o destino")
        void registrarEnvio3(){
            RegistraEnvioDTO dto = new RegistraEnvioDTO();
            dto.setDestino("destino");
            dto.setClienteId(1);
            dto.setTransporteId(1);

            ServiceResponse response = envioService.registrarEnvio(dto);

            assertNotNull(response);
            assertNull(response.getData());
            assertEquals("El origen y el destino no pueden ser nulos", response.getMessage());
            assertEquals(ServiceResponse.STATUS_ERROR, response.getStatus());
            assertEquals(400, response.getCode());

            verify(clienteRepository, never()).findById(anyInt());
            verify(transporteRepository, never()).findById(anyInt());
            verify(transporteRepository, never()).transporteDisponible(anyInt());
            verify(envioRepository, never()).save(any(Envio.class));
        }

        @Test
        @DisplayName("Registro envío sin transporte disponible")
        void registrarEnvio4(){
            RegistraEnvioDTO dto = new RegistraEnvioDTO();
            dto.setDestino("destino");
            dto.setOrigen("origen");
            dto.setClienteId(1);
            dto.setTransporteId(1);

            when(transporteRepository.transporteDisponible(anyInt())).thenReturn(0);

            ServiceResponse response = envioService.registrarEnvio(dto);

            assertNotNull(response);
            assertNull(response.getData());
            assertEquals("El transporte solicitado no esta disponible", response.getMessage());
            assertEquals(ServiceResponse.STATUS_ERROR, response.getStatus());
            assertEquals(400, response.getCode());

            verify(clienteRepository, never()).findById(anyInt());
            verify(transporteRepository, never()).findById(anyInt());
            verify(transporteRepository, times(1)).transporteDisponible(anyInt());
            verify(envioRepository, never()).save(any(Envio.class));
        }

        @Test
        @DisplayName("Registro envío sin cliente existente")
        void registrarEnvio5(){
            when(transporteRepository.transporteDisponible(anyInt())).thenReturn(1);

            ServiceResponse response = envioService.registrarEnvio(getRegistraEnvioDTO());

            assertNotNull(response);
            assertNull(response.getData());
            assertEquals("El cliente solicitado no existe", response.getMessage());
            assertEquals(ServiceResponse.STATUS_ERROR, response.getStatus());
            assertEquals(400, response.getCode());

            verify(clienteRepository, times(1)).findById(anyInt());
            verify(transporteRepository, never()).findById(anyInt());
            verify(transporteRepository, times(1)).transporteDisponible(anyInt());
            verify(envioRepository, never()).save(any(Envio.class));
        }

        @Test
        @DisplayName("Registro envío sin transporte existente")
        void registrarEnvio6(){
            when(transporteRepository.transporteDisponible(anyInt())).thenReturn(1);
            when(clienteRepository.findById(anyInt())).thenReturn(Optional.of(Datos.getCliente()));

            ServiceResponse response = envioService.registrarEnvio(getRegistraEnvioDTO());

            assertNotNull(response);
            assertNull(response.getData());
            assertEquals("El transporte solicitado no existe", response.getMessage());
            assertEquals(ServiceResponse.STATUS_ERROR, response.getStatus());
            assertEquals(400, response.getCode());

            verify(clienteRepository, times(1)).findById(anyInt());
            verify(transporteRepository, times(1)).findById(anyInt());
            verify(transporteRepository, times(1)).transporteDisponible(anyInt());
            verify(envioRepository, never()).save(any(Envio.class));
        }

        @Test
        @DisplayName("Registro envío parámetros correctos")
        void registrarEnvio7(){
            when(transporteRepository.transporteDisponible(anyInt())).thenReturn(1);
            when(clienteRepository.findById(anyInt())).thenReturn(Optional.of(Datos.getCliente()));
            when(transporteRepository.findById(anyInt())).thenReturn(Optional.of(Datos.getTransporte()));
            when(envioRepository.save(any(Envio.class))).thenAnswer(new Answer<Envio>(){
                @Override
                public Envio answer(InvocationOnMock invocation) throws Throwable {
                    Envio envio = invocation.getArgument(0);
                    envio.setId(1);
                    return envio;
                } 
            });

            ServiceResponse response = envioService.registrarEnvio(getRegistraEnvioDTO());

            assertNotNull(response);
            assertNotNull(response.getData());
            assertEquals("Envío creado con éxito", response.getMessage());
            assertEquals(ServiceResponse.STATUS_SUCCESS, response.getStatus());
            assertEquals(201, response.getCode());
            assertEquals(1, response.getData());

            verify(clienteRepository, times(1)).findById(anyInt());
            verify(transporteRepository, times(1)).findById(anyInt());
            verify(transporteRepository, times(1)).transporteDisponible(anyInt());
            verify(envioRepository, times(1)).save(any(Envio.class));
        }
    }
    
}