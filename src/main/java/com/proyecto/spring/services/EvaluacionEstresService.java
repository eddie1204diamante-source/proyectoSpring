// src/main/java/com/proyecto/spring/services/EvaluacionEstresService.java
package com.proyecto.spring.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.spring.Entity.Cita;
import com.proyecto.spring.Entity.EvaluacionEstres;
import com.proyecto.spring.repository.CitaRepository;
import com.proyecto.spring.repository.EvaluacionEstresRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluacionEstresService {
    
    private static final Logger log = LoggerFactory.getLogger(EvaluacionEstresService.class);
    
    private final EvaluacionEstresRepository evaluacionRepo;
    private final CitaRepository citaRepo;
    private final ChartService chartService;

    // ========================================================================
    // MÉTODOS PARA ORIENTADOR
    // ========================================================================
    
    /**
     * Obtener todas las evaluaciones realizadas por un orientador
     */
    public List<EvaluacionEstres> getEvaluacionesOrientador(Long idOrientador) {
        try {
            log.info("🔍 Buscando evaluaciones para orientador ID: {}", idOrientador);
            List<EvaluacionEstres> evaluaciones = evaluacionRepo.findByOrientadorId(idOrientador);
            log.info("✅ Encontradas {} evaluaciones", evaluaciones.size());
            return evaluaciones;
        } catch (Exception e) {
            log.error("❌ Error al obtener evaluaciones del orientador {}: {}", idOrientador, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Obtener todas las citas atendidas por un orientador en un rango de fechas
     */
    public List<Cita> getCitasAtendidasOrientador(Long idOrientador, LocalDate desde, LocalDate hasta) {
        try {
            log.info("🔍 Buscando citas para orientador ID: {} desde {} hasta {}", idOrientador, desde, hasta);
            List<Cita> citas = citaRepo.findByOrientadorIdOrientadorAndFechaCitaBetween(idOrientador, desde, hasta);
            log.info("✅ Encontradas {} citas", citas.size());
            return citas;
        } catch (Exception e) {
            log.error("❌ Error al obtener citas del orientador {}: {}", idOrientador, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // ========================================================================
    // MÉTODOS PARA APRENDIZ
    // ========================================================================
    
    /**
     * Obtener todas las evaluaciones de un aprendiz
     */
    public List<EvaluacionEstres> getMisEvaluaciones(Long idUsuarioAprendiz) {
        try {
            log.info("🔍 Buscando evaluaciones para aprendiz (usuario ID): {}", idUsuarioAprendiz);
            
            // Validación
            if (idUsuarioAprendiz == null || idUsuarioAprendiz <= 0) {
                log.error("❌ ID de usuario inválido: {}", idUsuarioAprendiz);
                return new ArrayList<>();
            }
            
            List<EvaluacionEstres> evaluaciones = evaluacionRepo.findByAprendizIdUsuario(idUsuarioAprendiz);
            log.info("✅ Encontradas {} evaluaciones para aprendiz", evaluaciones.size());
            
            // Debug: mostrar detalles de la primera evaluación si existe
            if (!evaluaciones.isEmpty()) {
                EvaluacionEstres primera = evaluaciones.get(0);
                log.debug("📋 Primera evaluación: ID={}, Puntuación={}, Nivel={}", 
                    primera.getId(), 
                    primera.getPuntuacion(), 
                    primera.getNivelDetectado());
            }
            
            return evaluaciones;
            
        } catch (Exception e) {
            log.error("❌ ERROR CRÍTICO al obtener evaluaciones del aprendiz {}: {}", 
                idUsuarioAprendiz, e.getMessage(), e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtener todas las citas de un aprendiz en un rango de fechas
     */
    public List<Cita> getMisCitas(Long idUsuarioAprendiz, LocalDate desde, LocalDate hasta) {
        try {
            log.info("🔍 Buscando citas para aprendiz (usuario ID): {} desde {} hasta {}", 
                idUsuarioAprendiz, desde, hasta);
            
            // Validación
            if (idUsuarioAprendiz == null || idUsuarioAprendiz <= 0) {
                log.error("❌ ID de usuario inválido: {}", idUsuarioAprendiz);
                return new ArrayList<>();
            }
            
            if (desde == null || hasta == null) {
                log.error("❌ Fechas inválidas: desde={}, hasta={}", desde, hasta);
                return new ArrayList<>();
            }
            
            List<Cita> citas = citaRepo.findByAprendizIdUsuarioAndFechaCitaBetween(
                idUsuarioAprendiz, desde, hasta);
            
            log.info("✅ Encontradas {} citas para aprendiz", citas.size());
            
            // Debug: mostrar detalles de la primera cita si existe
            if (!citas.isEmpty()) {
                Cita primera = citas.get(0);
                log.debug("📋 Primera cita: ID={}, Fecha={}, Motivo={}", 
                    primera.getIdCita(), 
                    primera.getFechaCita(), 
                    primera.getMotivoClasificado());
            }
            
            return citas;
            
        } catch (Exception e) {
            log.error("❌ ERROR CRÍTICO al obtener citas del aprendiz {}: {}", 
                idUsuarioAprendiz, e.getMessage(), e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ========================================================================
    // GUARDAR Y VALIDAR EVALUACIÓN
    // ========================================================================
    
    /**
     * Guardar una nueva evaluación de estrés (solo una vez por cita)
     */
    @Transactional
    public EvaluacionEstres guardarEvaluacion(Long idCita, Integer puntuacion, String observaciones) {
        try {
            log.info("💾 Guardando evaluación: idCita={}, puntuacion={}", idCita, puntuacion);
            
            Cita cita = citaRepo.findById(idCita)
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + idCita));
            
            if (evaluacionRepo.existsByCitaIdCita(idCita)) {
                throw new RuntimeException("Esta cita ya tiene evaluación");
            }
            
            EvaluacionEstres eval = EvaluacionEstres.builder()
                    .cita(cita)
                    .puntuacion(puntuacion)
                    .observaciones(observaciones)
                    .build();
            
            EvaluacionEstres guardada = evaluacionRepo.save(eval);
            log.info("✅ Evaluación guardada exitosamente con ID: {}", guardada.getId());
            
            return guardada;
            
        } catch (Exception e) {
            log.error("❌ Error al guardar evaluación: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar evaluación: " + e.getMessage(), e);
        }
    }

    /**
     * Verificar si una cita ya tiene evaluación
     */
    public boolean existeEvaluacion(Long idCita) {
        try {
            return evaluacionRepo.existsByCitaIdCita(idCita);
        } catch (Exception e) {
            log.error("❌ Error al verificar evaluación para cita {}: {}", idCita, e.getMessage());
            return false;
        }
    }

    // ========================================================================
    // GRÁFICAS (LEGACY - Mantener por compatibilidad con código existente)
    // ========================================================================
    
    public String generarGraficaCitasMes(List<Cita> citas) throws Exception {
        return chartService.generarGraficaBarrasCitasPorMes(citas);
    }

    public String generarGraficaMotivos(List<Cita> citas) throws Exception {
        return chartService.generarGraficaMotivos(citas);
    }

    public String generarGraficaNivelEstres(List<EvaluacionEstres> evaluaciones) throws Exception {
        return chartService.generarGraficaNivelEstres(evaluaciones);
    }

    public String generarGraficaHora(List<Cita> citas) throws Exception {
        return chartService.generarGraficaHoraMasFrecuente(citas);
    }

    public String generarGraficaDiaSemana(List<Cita> citas) throws Exception {
        return chartService.generarGraficaDiaSemana(citas);
    }
}