package com.proyecto.spring.repository;

import com.proyecto.spring.Entity.persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface personaRepository extends JpaRepository<persona, Integer> {
    Optional<persona> findByDocumento(String documento);
    boolean existsByDocumento(String documento);
}