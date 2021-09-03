package com.fabrizio.junit5.services;

import java.util.Date;

import com.fabrizio.junit5.dto.RegistraEnvioDTO;
import com.fabrizio.junit5.entities.Cliente;
import com.fabrizio.junit5.entities.Envio;
import com.fabrizio.junit5.entities.Transporte;
import com.fabrizio.junit5.error.GlobalException;
import com.fabrizio.junit5.repositories.ClienteRepository;
import com.fabrizio.junit5.repositories.EnvioRepository;
import com.fabrizio.junit5.repositories.TransporteRepository;
import com.fabrizio.junit5.services.responses.ServiceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnvioService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private TransporteRepository transporteRepository;

    public ServiceResponse registrarEnvio(RegistraEnvioDTO registraEnvioDTO){
        ServiceResponse response = new ServiceResponse();
        try {
            validarCamposRegistraEnvioDTO(registraEnvioDTO);
            if(transporteRepository.transporteDisponible(registraEnvioDTO.getTransporteId()) > 0){
                Cliente cliente = clienteRepository.findById(registraEnvioDTO.getClienteId()).orElseThrow(() -> new GlobalException("El cliente solicitado no existe"));
                Transporte transporte = transporteRepository.findById(registraEnvioDTO.getTransporteId()).orElseThrow(() -> new GlobalException("El transporte solicitado no existe"));
                Envio envio = new Envio(); 
                envio.setAlta(new Date());
                envio.setCliente(cliente);
                envio.setCompletado(false);
                envio.setDireccionOrigen(registraEnvioDTO.getOrigen());
                envio.setDireccionDestino(registraEnvioDTO.getDestino());
                envio.setTransporte(transporte);
                envio = envioRepository.save(envio);
                response.setData(envio.getId());
                response.setCode(201);
                response.setStatus(ServiceResponse.STATUS_SUCCESS);
                response.setMessage("Envío creado con éxito");
            } else {
                throw new GlobalException("El transporte solicitado no esta disponible");
            }
        } catch (GlobalException e) {
            response.setCode(400);
            response.setMessage(e.getMessage());
            response.setStatus(ServiceResponse.STATUS_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(500);
            response.setMessage("Ocurrió un error inesperado");
            response.setStatus(ServiceResponse.STATUS_ERROR);
        }
        return response;
    }

    private void validarCamposRegistraEnvioDTO(RegistraEnvioDTO registraEnvioDTO) throws GlobalException{
        if(registraEnvioDTO.getClienteId() == null) throw new GlobalException("El id del cliente no puede ser nulo");
        if(registraEnvioDTO.getTransporteId() == null) throw new GlobalException("El id del transporte no puede ser nulo");
        if((registraEnvioDTO.getOrigen() == null || registraEnvioDTO.getOrigen().isEmpty()) || (registraEnvioDTO.getDestino() == null || registraEnvioDTO.getDestino().isEmpty()))
            throw new GlobalException("El origen y el destino no pueden ser nulos");
    }
    
}