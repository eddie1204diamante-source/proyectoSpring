package com.proyecto.spring.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.spring.Entity.Cita;
import com.proyecto.spring.Entity.EstadoCita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    
    // ====================================================================
    // MÉTODOS PARA APRENDIZ - CORREGIDOS CON @Query EXPLÍCITOS
    // ====================================================================
    
    @Query("SELECT c FROM Cita c " +
           "JOIN c.aprendiz a " +
           "JOIN a.usuario u " +
           "WHERE u.idUsuario = :idUsuario")
    List<Cita> findByAprendizIdUsuario(@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT c FROM Cita c " +
           "JOIN c.aprendiz a " +
           "JOIN a.usuario u " +
           "WHERE u.idUsuario = :idUsuario " +
           "AND c.estado = :estado")
    List<Cita> findByAprendizIdUsuarioAndEstado(
            @Param("idUsuario") Long idUsuario, 
            @Param("estado") EstadoCita estado);
    
    @Query("SELECT c FROM Cita c " +
           "JOIN c.aprendiz a " +
           "JOIN a.usuario u " +
           "WHERE u.idUsuario = :idUsuario " +
           "AND c.fechaCita BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY c.fechaCita ASC, c.horaCita ASC")
    List<Cita> findByAprendizIdUsuarioAndFechaCitaBetween(
            @Param("idUsuario") Long idUsuario,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
    
    // ====================================================================
    // MÉTODOS PARA ORIENTADOR
    // ====================================================================
    
    @Query("SELECT c FROM Cita c " +
           "WHERE c.orientador.idOrientador = :idOrientador " +
           "ORDER BY c.fechaCita DESC, c.horaCita DESC")
    List<Cita> findByOrientadorIdOrientador(@Param("idOrientador") Long idOrientador);
    
    @Query("SELECT c FROM Cita c " +
           "WHERE c.orientador.idOrientador = :idOrientador " +
           "AND c.estado = :estado " +
           "ORDER BY c.fechaCita DESC, c.horaCita DESC")
    List<Cita> findByOrientadorIdOrientadorAndEstado(
            @Param("idOrientador") Long idOrientador, 
            @Param("estado") EstadoCita estado);
    
    @Query("SELECT c FROM Cita c " +
           "WHERE c.orientador.idOrientador = :idOrientador " +
           "AND c.fechaCita = :fechaCita " +
           "AND c.estado = :estado")
    List<Cita> findByOrientadorIdOrientadorAndFechaCitaAndEstado(
            @Param("idOrientador") Long idOrientador,
            @Param("fechaCita") LocalDate fechaCita,
            @Param("estado") EstadoCita estado);
    
    @Query("SELECT c FROM Cita c " +
           "WHERE c.orientador.idOrientador = :idOrientador " +
           "AND c.fechaCita BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY c.fechaCita ASC, c.horaCita ASC")
    List<Cita> findByOrientadorIdOrientadorAndFechaCitaBetween(
            @Param("idOrientador") Long idOrientador,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
    
    // ====================================================================
    // MÉTODOS GENERALES
    // ====================================================================
    
    @Query("SELECT c FROM Cita c " +
           "WHERE c.fechaCita BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY c.fechaCita ASC")
    List<Cita> findByFechaCitaBetween(
            @Param("fechaInicio") LocalDate fechaInicio, 
            @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
           "FROM Cita c " +
           "WHERE c.orientador.idOrientador = :idOrientador " +
           "AND c.fechaCita = :fechaCita " +
           "AND c.horaCita = :horaCita " +
           "AND c.estado <> :estadoCancelada")
    boolean existsByOrientadorIdOrientadorAndFechaCitaAndHoraCitaAndEstadoNot(
            @Param("idOrientador") Long idOrientador,
            @Param("fechaCita") LocalDate fechaCita,
            @Param("horaCita") LocalTime horaCita,
            @Param("estadoCancelada") EstadoCita estadoCancelada);
    
    // ====================================================================
    // MÉTODOS ADICIONALES PARA DEBUG
    // ====================================================================
    
    // Contar citas de un aprendiz
    @Query("SELECT COUNT(c) FROM Cita c " +
           "JOIN c.aprendiz a " +
           "JOIN a.usuario u " +
           "WHERE u.idUsuario = :idUsuario")
    long countByAprendizIdUsuario(@Param("idUsuario") Long idUsuario);
    
    // Contar citas de un orientador
    @Query("SELECT COUNT(c) FROM Cita c " +
           "WHERE c.orientador.idOrientador = :idOrientador")
    long countByOrientadorIdOrientador(@Param("idOrientador") Long idOrientador);
}