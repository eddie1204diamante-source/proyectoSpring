// src/main/java/com/proyecto/spring/repository/EvaluacionEstresRepository.java
package com.proyecto.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.spring.Entity.EvaluacionEstres;

@Repository
public interface EvaluacionEstresRepository extends JpaRepository<EvaluacionEstres, Long> {

    // Para el ORIENTADOR: todas las evaluaciones de sus citas
    @Query("SELECT e FROM EvaluacionEstres e WHERE e.cita.orientador.idOrientador = :idOrientador")
    List<EvaluacionEstres> findByOrientadorId(@Param("idOrientador") Long idOrientador);

    // Para el APRENDIZ: solo sus evaluaciones
    @Query("SELECT e FROM EvaluacionEstres e WHERE e.cita.aprendiz.usuario.idUsuario = :idUsuario")
    List<EvaluacionEstres> findByAprendizIdUsuario(@Param("idUsuario") Long idUsuario);

    // Verificar si una cita ya tiene evaluación
    boolean existsByCitaIdCita(Long idCita);

    // Obtener evaluación por cita
    Optional<EvaluacionEstres> findByCitaIdCita(Long idCita);
}