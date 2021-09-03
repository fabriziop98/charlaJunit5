package com.fabrizio.junit5.repositories;

import com.fabrizio.junit5.entities.Transporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransporteRepository extends JpaRepository<Transporte, Integer> {

    @Query("Select count(t) from Transporte t where t.id = :id and t.disponible is true")
    public int transporteDisponible(Integer transporteId);
}