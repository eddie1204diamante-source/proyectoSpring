package com.proyecto.spring.repository;

import com.proyecto.spring.Entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
}
