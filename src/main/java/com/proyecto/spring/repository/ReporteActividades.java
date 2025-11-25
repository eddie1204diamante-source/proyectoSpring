package com.proyecto.spring.repository;

import com.proyecto.spring.Entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteActividades extends JpaRepository<Actividad, Long> {
}
