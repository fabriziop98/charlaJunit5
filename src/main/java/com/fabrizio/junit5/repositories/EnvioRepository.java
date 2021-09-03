package com.fabrizio.junit5.repositories;

import com.fabrizio.junit5.entities.Envio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer>{
    
}