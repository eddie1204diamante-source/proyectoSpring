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
    
    // ====================================================================
    // PARA ORIENTADOR: todas las evaluaciones de sus citas
    // ====================================================================
    
    @Query("SELECT e FROM EvaluacionEstres e " +
           "JOIN e.cita c " +
           "JOIN c.orientador o " +
           "WHERE o.idOrientador = :idOrientador " +
           "ORDER BY e.createdAt DESC")
    List<EvaluacionEstres> findByOrientadorId(@Param("idOrientador") Long idOrientador);
    
    // ====================================================================
    // PARA APRENDIZ: solo sus evaluaciones
    // ====================================================================
    
    @Query("SELECT e FROM EvaluacionEstres e " +
           "JOIN e.cita c " +
           "JOIN c.aprendiz a " +
           "JOIN a.usuario u " +
           "WHERE u.idUsuario = :idUsuario " +
           "ORDER BY e.createdAt DESC")
    List<EvaluacionEstres> findByAprendizIdUsuario(@Param("idUsuario") Long idUsuario);
    
    // ====================================================================
    // VERIFICACIONES
    // ====================================================================
    
    // Verificar si una cita ya tiene evaluación
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
           "FROM EvaluacionEstres e " +
           "WHERE e.cita.idCita = :idCita")
    boolean existsByCitaIdCita(@Param("idCita") Long idCita);
    
    // Obtener evaluación por cita
    @Query("SELECT e FROM EvaluacionEstres e " +
           "WHERE e.cita.idCita = :idCita")
    Optional<EvaluacionEstres> findByCitaIdCita(@Param("idCita") Long idCita);
    
    // ====================================================================
    // MÉTODOS ADICIONALES PARA DEBUG Y ESTADÍSTICAS
    // ====================================================================
    
    // Contar evaluaciones de un orientador
    @Query("SELECT COUNT(e) FROM EvaluacionEstres e " +
           "JOIN e.cita c " +
           "WHERE c.orientador.idOrientador = :idOrientador")
    long countByOrientadorId(@Param("idOrientador") Long idOrientador);
    
    // Contar evaluaciones de un aprendiz
    @Query("SELECT COUNT(e) FROM EvaluacionEstres e " +
           "JOIN e.cita c " +
           "JOIN c.aprendiz a " +
           "JOIN a.usuario u " +
           "WHERE u.idUsuario = :idUsuario")
    long countByAprendizIdUsuario(@Param("idUsuario") Long idUsuario);
    
    // Obtener evaluaciones por nivel
    @Query("SELECT e FROM EvaluacionEstres e " +
           "WHERE e.nivelDetectado = :nivel " +
           "ORDER BY e.createdAt DESC")
    List<EvaluacionEstres> findByNivelDetectado(@Param("nivel") String nivel);
}