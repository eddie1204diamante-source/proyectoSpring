package com.proyecto.spring.repository;

import com.proyecto.spring.Entity.psicologica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface psicologicaRepository extends JpaRepository<psicologica, Long> {
    
    // El ID de la tabla psicológica es id_orientador → Long
    // JpaRepository ya tiene findById(Long id)
}