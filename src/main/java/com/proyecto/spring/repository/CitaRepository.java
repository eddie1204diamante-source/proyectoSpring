package com.proyecto.spring.repository;

import com.proyecto.spring.Entity.Cita;
import com.proyecto.spring.Entity.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // === MÉTODOS QUE FALLABAN (aprendiz → usuario) → AHORA CON @Query ===
    @Query("SELECT c FROM Cita c WHERE c.aprendiz.usuario.idUsuario = :idUsuario")
    List<Cita> findByAprendizIdUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT c FROM Cita c WHERE c.aprendiz.usuario.idUsuario = :idUsuario AND c.estado = :estado")
    List<Cita> findByAprendizIdUsuarioAndEstado(@Param("idUsuario") Long idUsuario, @Param("estado") EstadoCita estado);

    @Query("SELECT c FROM Cita c WHERE c.aprendiz.usuario.idUsuario = :idUsuario AND c.fechaCita BETWEEN :fechaInicio AND :fechaFin")
    List<Cita> findByAprendizIdUsuarioAndFechaCitaBetween(
            @Param("idUsuario") Long idUsuario,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // === MÉTODOS DEL ORIENTADOR (estos sí funcionan con nombres normales) ===
    List<Cita> findByOrientadorIdOrientador(Long idOrientador);

    List<Cita> findByOrientadorIdOrientadorAndEstado(Long idOrientador, EstadoCita estado);

    List<Cita> findByOrientadorIdOrientadorAndFechaCitaAndEstado(
            Long idOrientador,
            LocalDate fechaCita,
            EstadoCita estado
    );

    List<Cita> findByOrientadorIdOrientadorAndFechaCitaBetween(
            Long idOrientador,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    // === OTROS MÉTODOS QUE SÍ FUNCIONAN TAL CUAL ===
    List<Cita> findByFechaCitaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    boolean existsByOrientadorIdOrientadorAndFechaCitaAndHoraCitaAndEstadoNot(
            Long idOrientador,
            LocalDate fechaCita,
            LocalTime horaCita,
            EstadoCita estadoCancelada
    );
}