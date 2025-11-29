// src/main/java/com/proyecto/spring/services/EvaluacionEstresService.java
package com.proyecto.spring.services;

import java.time.LocalDate;
import java.util.List;

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

    private final EvaluacionEstresRepository evaluacionRepo;
    private final CitaRepository citaRepo;
    private final ChartService chartService;

    // === ORIENTADOR ===
    public List<EvaluacionEstres> getEvaluacionesOrientador(Long idOrientador) {
        return evaluacionRepo.findByOrientadorId(idOrientador);
    }

    public List<Cita> getCitasAtendidasOrientador(Long idOrientador, LocalDate desde, LocalDate hasta) {
        return citaRepo.findByOrientadorIdOrientadorAndFechaCitaBetween(idOrientador, desde, hasta);
    }

    // === APRENDIZ ===
    public List<EvaluacionEstres> getMisEvaluaciones(Long idUsuarioAprendiz) {
        return evaluacionRepo.findByAprendizIdUsuario(idUsuarioAprendiz);
    }

    public List<Cita> getMisCitas(Long idUsuarioAprendiz, LocalDate desde, LocalDate hasta) {
        return citaRepo.findByAprendizIdUsuarioAndFechaCitaBetween(idUsuarioAprendiz, desde, hasta);
    }

    // === GUARDAR EVALUACIÓN (UNA SOLA VEZ) ===
    @Transactional
    public EvaluacionEstres guardarEvaluacion(Long idCita, Integer puntuacion, String observaciones) {
        Cita cita = citaRepo.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (evaluacionRepo.existsByCitaIdCita(idCita)) {
            throw new RuntimeException("Esta cita ya tiene evaluación");
        }

        EvaluacionEstres eval = EvaluacionEstres.builder()
                .cita(cita)
                .puntuacion(puntuacion)
                .observaciones(observaciones)
                .build();

        return evaluacionRepo.save(eval); // @PrePersist calcula nivel y fecha
    }

    // === GRÁFICAS ===
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