package com.proyecto.spring.repository;

import com.proyecto.spring.Entity.ActividadAsignada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadAsignadaRepository extends JpaRepository<ActividadAsignada, Integer> {
    List<ActividadAsignada> findByIdEstudiante(Integer idEstudiante);
}
