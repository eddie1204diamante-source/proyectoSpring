package com.proyecto.spring.services;

import com.proyecto.spring.Entity.*;
import com.proyecto.spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CitaService {

    @Autowired private CitaRepository citaRepository;
    @Autowired private aprendizRepository aprendizRepository;
    @Autowired private psicologicaRepository psicologicaRepository;

    // Lista de palabras clave para clasificación de motivos
    private static final Map<String, List<String>> PALABRAS_CLAVE = new HashMap<String, List<String>>() {{
        put("Ansiedad", Arrays.asList(
            "ansiedad", "pánico", "ataque", "nervios", "nervioso", "preocupación", 
            "angustia", "miedo", "temor", "inquietud", "taquicardia"
        ));
        put("Estrés académico", Arrays.asList(
            "estrés", "presión", "examen", "parcial", "tarea", "trabajo", 
            "estudio", "académico", "nota", "calificación", "evaluación", 
            "sobrecarga", "agobio"
        ));
        put("Problemas familiares", Arrays.asList(
            "familia", "padres", "madre", "padre", "hermano", "hermana", 
            "familiar", "casa", "hogar", "conflicto familiar", "peleas"
        ));
        put("Duelo", Arrays.asList(
            "duelo", "pérdida", "muerte", "falleció", "murió", "luto", 
            "tristeza profunda", "fallecimiento"
        ));
        put("Depresión", Arrays.asList(
            "depresión", "triste", "tristeza", "melancolía", "sin ganas", 
            "desmotivación", "vacío", "soledad"
        ));
        put("Problemas de pareja", Arrays.asList(
            "pareja", "novio", "novia", "relación", "amor", "ruptura", 
            "separación", "celos"
        ));
        put("Autoestima", Arrays.asList(
            "autoestima", "inseguridad", "valor propio", "confianza", 
            "aceptación", "imagen"
        ));
        put("Bullying", Arrays.asList(
            "bullying", "acoso", "maltrato", "burlas", "intimidación", 
            "agresión", "hostigamiento"
        ));
    }};

    /**
     * Crear nueva cita con todas las validaciones
     */
    @Transactional
    public Cita crearCita(Long idEstudiante, Long idOrientador, LocalDate fecha, LocalTime hora, String motivo) {

        // Validación 1: Fecha no puede ser pasada
        if (fecha.isBefore(LocalDate.now())) {
            throw new RuntimeException("No se permiten citas en fechas pasadas");
        }

        // Validación 2: Fecha máxima 60 días
        LocalDate fechaMaxima = LocalDate.now().plusDays(60);
        if (fecha.isAfter(fechaMaxima)) {
            throw new RuntimeException("No se pueden solicitar citas con más de 60 días de anticipación");
        }

        // Validación 3: Hora válida (06:00 - 18:00, cada 30 minutos)
        if (hora.isBefore(LocalTime.of(6, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            throw new RuntimeException("El horario debe estar entre 06:00 y 18:00");
        }
        if (hora.getMinute() % 30 != 0 || hora.getSecond() != 0) {
            throw new RuntimeException("Solo se permiten horarios cada 30 minutos (ej: 08:00, 08:30)");
        }

        // Validación 4: Verificar disponibilidad del orientador
        boolean ocupado = citaRepository.existsByOrientadorIdOrientadorAndFechaCitaAndHoraCitaAndEstadoNot(
                idOrientador, fecha, hora, EstadoCita.CANCELADA);

        if (ocupado) {
            throw new RuntimeException("El orientador ya tiene una cita en ese horario");
        }

        // Validación 5: Buscar estudiante
        aprendiz aprendiz = aprendizRepository.findByIdUsuario(idEstudiante)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        // Validación 6: Buscar orientador
        psicologica orientador = psicologicaRepository.findById(idOrientador)
                .orElseThrow(() -> new RuntimeException("Orientador no encontrado"));

        // Crear la cita
        Cita cita = new Cita();
        cita.setAprendiz(aprendiz);
        cita.setOrientador(orientador);
        cita.setFechaCita(fecha);
        cita.setHoraCita(hora);
        cita.setMotivoOriginal(motivo);
        cita.setMotivoClasificado(clasificarMotivo(motivo));
        cita.setEstado(EstadoCita.PENDIENTE);

        return citaRepository.save(cita);
    }

    /**
     * Clasificar motivo según palabras clave
     */
    private String clasificarMotivo(String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            return "Otro";
        }

        String textoLower = motivo.toLowerCase();
        Map<String, Integer> coincidencias = new HashMap<>();

        // Contar coincidencias para cada categoría
        for (Map.Entry<String, List<String>> entry : PALABRAS_CLAVE.entrySet()) {
            String categoria = entry.getKey();
            List<String> palabras = entry.getValue();
            
            int contador = 0;
            for (String palabra : palabras) {
                if (textoLower.contains(palabra)) {
                    contador++;
                }
            }
            
            if (contador > 0) {
                coincidencias.put(categoria, contador);
            }
        }

        // Retornar la categoría con más coincidencias
        if (coincidencias.isEmpty()) {
            return "Otro";
        }

        return coincidencias.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    /**
     * Obtener lista de orientadores
     */
    public List<psicologica> getOrientadores() {
        return psicologicaRepository.findAll();
    }
    
    /**
     * Verificar disponibilidad de horario
     */
    public boolean verificarDisponibilidad(Long idOrientador, LocalDate fecha, LocalTime hora) {
        return !citaRepository.existsByOrientadorIdOrientadorAndFechaCitaAndHoraCitaAndEstadoNot(
                idOrientador, fecha, hora, EstadoCita.CANCELADA);
    }

    /**
     * Obtener citas del estudiante
     */
    public List<Cita> getCitasEstudiante(Long idUsuario) {
        return citaRepository.findByAprendizIdUsuario(idUsuario);
    }

    /**
     * Obtener citas del orientador
     */
    public List<Cita> getCitasOrientador(Long idUsuario) {
    // 1. Primero buscamos el psicologica (orientador) usando el id_usuario
    psicologica orientador = psicologicaRepository.findByIdUsuario(idUsuario)
        .orElseThrow(() -> new RuntimeException("Orientador no encontrado con idUsuario: " + idUsuario));

    // 2. Ahora sí buscamos las citas usando el id_orientador real
    return citaRepository.findByOrientadorIdOrientador(orientador.getIdOrientador());
    }

    /**
     * Obtener citas pendientes del orientador
     */
    public List<Cita> getCitasPendientesOrientador(Long idUsuario) {
    psicologica orientador = psicologicaRepository.findByIdUsuario(idUsuario)
        .orElseThrow(() -> new RuntimeException("Orientador no encontrado con idUsuario: " + idUsuario));

    return citaRepository.findByOrientadorIdOrientadorAndEstado(
        orientador.getIdOrientador(), EstadoCita.PENDIENTE);
    }

    /**
     * Aprobar cita
     */
    @Transactional
    public Cita aprobarCita(Long idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Validar que el estado sea PENDIENTE
        if (cita.getEstado() != EstadoCita.PENDIENTE) {
            throw new RuntimeException("Solo se pueden aprobar citas en estado PENDIENTE");
        }

        // Validar que la fecha no haya pasado
        LocalDate hoy = LocalDate.now();
        if (cita.getFechaCita().isBefore(hoy)) {
            throw new RuntimeException("No es posible aprobar citas vencidas");
        }

        cita.setEstado(EstadoCita.APROBADA);
        return citaRepository.save(cita);
    }

    /**
     * Reprogramar cita
     */
    @Transactional
public Cita reprogramarCita(Long idCita, LocalDate nuevaFecha, LocalTime nuevaHora) {
    Cita cita = citaRepository.findById(idCita)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

    // Validar que no esté cancelada o finalizada
    if (cita.getEstado() == EstadoCita.CANCELADA || cita.getEstado() == EstadoCita.FINALIZADA) {
        throw new RuntimeException("No se puede reprogramar una cita cancelada o finalizada");
    }

    // Si la fecha y hora son las mismas, no hacer nada
    if (cita.getFechaCita().equals(nuevaFecha) && cita.getHoraCita().equals(nuevaHora)) {
        throw new RuntimeException("La nueva fecha y hora son iguales a las actuales");
    }

    // Validaciones de fecha y hora
    if (nuevaFecha.isBefore(LocalDate.now())) {
        throw new RuntimeException("No se permiten fechas pasadas");
    }

    LocalDate fechaMaxima = LocalDate.now().plusDays(60);
    if (nuevaFecha.isAfter(fechaMaxima)) {
        throw new RuntimeException("No se pueden solicitar citas con más de 60 días de anticipación");
    }

    if (nuevaHora.isBefore(LocalTime.of(6, 0)) || nuevaHora.isAfter(LocalTime.of(18, 0))) {
        throw new RuntimeException("El horario debe estar entre 06:00 y 18:00");
    }

    if (nuevaHora.getMinute() % 30 != 0) {
        throw new RuntimeException("Solo se permiten horarios cada 30 minutos");
    }

    // Verificar disponibilidad EXCLUYENDO la cita actual
    List<Cita> citasConflicto = citaRepository.findByOrientadorIdOrientadorAndFechaCitaAndHoraCitaAndEstadoNot(
            cita.getOrientador().getIdOrientador(), nuevaFecha, nuevaHora, EstadoCita.CANCELADA);
    
    // Filtrar para excluir la cita actual
    boolean ocupado = citasConflicto.stream()
            .anyMatch(c -> !c.getIdCita().equals(idCita));

    if (ocupado) {
        throw new RuntimeException("El nuevo horario no está disponible");
    }

    // Actualizar cita
    cita.setFechaCita(nuevaFecha);
    cita.setHoraCita(nuevaHora);
    cita.setEstado(EstadoCita.PENDIENTE);

    return citaRepository.save(cita);
}

    /**
     * Cancelar cita
     */
    @Transactional
    public Cita cancelarCita(Long idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new RuntimeException("La cita ya está cancelada");
        }

        cita.setEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }

    /**
     * Finalizar cita (solo orientador)
     */
    @Transactional
    public Cita finalizarCita(Long idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() != EstadoCita.APROBADA) {
            throw new RuntimeException("Solo se pueden finalizar citas aprobadas");
        }

        cita.setEstado(EstadoCita.FINALIZADA);
        return citaRepository.save(cita);
    }

    /**
     * Obtener cita por ID
     */
    public Cita getCitaPorId(Long idCita) {
        return citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }
}