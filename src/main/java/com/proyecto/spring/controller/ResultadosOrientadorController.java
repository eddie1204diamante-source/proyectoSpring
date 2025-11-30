package com.proyecto.spring.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proyecto.spring.Entity.aprendiz;
import com.proyecto.spring.Entity.Cita;
import com.proyecto.spring.Entity.EvaluacionEstres;
import com.proyecto.spring.Entity.psicologica;
import com.proyecto.spring.config.UserDetailsServiceImpl;
import com.proyecto.spring.repository.CitaRepository;
import com.proyecto.spring.services.EvaluacionEstresService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/orientador")
@RequiredArgsConstructor
public class ResultadosOrientadorController {
   
    private static final Logger log = LoggerFactory.getLogger(ResultadosOrientadorController.class);
    
    private final EvaluacionEstresService evaluacionService;
    private final CitaRepository citaRepository;

    @GetMapping("/resultados")
    public String mostrarResultadosOrientador(Model model,
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
       
        var usuario = userDetails.getUsuario();
        psicologica orientador = usuario.getPersona().getPsicologica();
       
        if (orientador == null) {
            model.addAttribute("error", "No tienes perfil de orientador asignado.");
            return "error";
        }
       
        log.info("🟢 Orientador {} accediendo a resultados", orientador.getIdOrientador());
        model.addAttribute("nombreOrientador", usuario.getPersona().getNombreCompleto());
        return "orientador/resultados";
    }

    @GetMapping("/resultados/datos")
    @ResponseBody
    public Map<String, Object> obtenerDatosGraficas(
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails,
            @RequestParam(required = false) Long idEstudiante) {
       
        var usuario = userDetails.getUsuario();
        psicologica orientador = usuario.getPersona().getPsicologica();
        Long idOrientador = orientador.getIdOrientador();
       
        log.info("📊 Cargando datos para orientador ID: {}", idOrientador);
        
        // Incluir citas desde hace 12 meses hasta 1 mes en el futuro
        LocalDate hasta = LocalDate.now().plusMonths(1);
        LocalDate desde = LocalDate.now().minusMonths(12);
        
        log.info("📅 Rango de fechas: {} hasta {}", desde, hasta);
       
        // Obtener datos filtrados
        List<EvaluacionEstres> evaluaciones;
        List<Cita> citas;
       
        if (idEstudiante != null) {
            log.info("🔍 Filtrando por estudiante ID: {}", idEstudiante);
            evaluaciones = evaluacionService.getEvaluacionesOrientador(idOrientador).stream()
                    .filter(e -> e.getCita().getAprendiz().getIdEstudiante().equals(idEstudiante))
                    .collect(Collectors.toList());
            citas = evaluacionService.getCitasAtendidasOrientador(idOrientador, desde, hasta).stream()
                    .filter(c -> c.getAprendiz().getIdEstudiante().equals(idEstudiante))
                    .collect(Collectors.toList());
        } else {
            log.info("📋 Cargando todos los datos del orientador");
            evaluaciones = evaluacionService.getEvaluacionesOrientador(idOrientador);
            citas = evaluacionService.getCitasAtendidasOrientador(idOrientador, desde, hasta);
        }
       
        log.info("✅ Datos cargados: {} evaluaciones, {} citas", evaluaciones.size(), citas.size());
        
        // DEBUG: Imprimir primeras citas
        if (!citas.isEmpty()) {
            log.info("📅 Primera cita: ID={}, Fecha={}, Motivo={}", 
                    citas.get(0).getIdCita(),
                    citas.get(0).getFechaCita(),
                    citas.get(0).getMotivoClasificado());
        } else {
            log.warn("⚠️ NO SE ENCONTRARON CITAS para orientador {}", idOrientador);
        }
        
        if (!evaluaciones.isEmpty()) {
            log.info("📝 Primera evaluación: puntuación={}", evaluaciones.get(0).getPuntuacion());
        } else {
            log.warn("⚠️ NO SE ENCONTRARON EVALUACIONES para orientador {}", idOrientador);
        }
        
        Map<String, Object> response = new HashMap<>();
       
        // Generar datos para gráficas
        response.put("citasPorMes", generarDatosCitasPorMes(citas));
        response.put("motivosClasificados", generarDatosMotivos(citas));
        response.put("nivelesEstres", generarDatosNivelEstres(evaluaciones));
        response.put("citasPorHora", generarDatosHorarios(citas));
        response.put("citasPorDia", generarDatosDiasSemana(citas));
        response.put("estadisticas", generarEstadisticasRapidas(citas, evaluaciones));
        response.put("evaluacionesRecientes", generarListaEvaluaciones(evaluaciones));
       
        log.info("📤 Enviando respuesta al frontend");
        return response;
    }

    @GetMapping("/citas-sin-evaluar")
    @ResponseBody
    public List<Map<String, Object>> obtenerCitasSinEvaluar(
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
       
        try {
            var usuario = userDetails.getUsuario();
            psicologica orientador = usuario.getPersona().getPsicologica();
            Long idOrientador = orientador.getIdOrientador();
           
            log.info("🔎 Buscando citas sin evaluar para orientador ID: {}", idOrientador);
           
            // Buscar citas de los últimos 12 meses hasta 1 mes en el futuro
            LocalDate desde = LocalDate.now().minusMonths(12);
            LocalDate hasta = LocalDate.now().plusMonths(1);
            
            List<Cita> todasCitas = citaRepository.findByOrientadorIdOrientadorAndFechaCitaBetween(
                    idOrientador, desde, hasta);
           
            log.info("📋 Total de citas encontradas: {}", todasCitas.size());
            
            List<Map<String, Object>> citasSinEvaluar = new ArrayList<>();
            
            for (Cita cita : todasCitas) {
                boolean tieneEvaluacion = evaluacionService.existeEvaluacion(cita.getIdCita());
                log.debug("Cita ID={}, tieneEvaluacion={}", cita.getIdCita(), tieneEvaluacion);
                
                if (!tieneEvaluacion) {
                    try {
                        Map<String, Object> citaMap = new HashMap<>();
                        citaMap.put("idCita", cita.getIdCita());
                        citaMap.put("aprendizNombre", 
                                cita.getAprendiz().getUsuario().getPersona().getNombreCompleto());
                        citaMap.put("fechaCita", cita.getFechaCita().toString());
                        citaMap.put("horaCita", cita.getHoraCita().toString());
                        citasSinEvaluar.add(citaMap);
                    } catch (Exception e) {
                        log.error("❌ Error al procesar cita ID {}: {}", cita.getIdCita(), e.getMessage());
                    }
                }
            }
            
            log.info("✅ Citas sin evaluar encontradas: {}", citasSinEvaluar.size());
            return citasSinEvaluar;
            
        } catch (Exception e) {
            log.error("❌ Error general al obtener citas sin evaluar: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/estudiantes")
    @ResponseBody
    public List<Map<String, Object>> obtenerEstudiantes(
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
       
        try {
            var usuario = userDetails.getUsuario();
            psicologica orientador = usuario.getPersona().getPsicologica();
            Long idOrientador = orientador.getIdOrientador();
           
            log.info("👥 Obteniendo estudiantes del orientador ID: {}", idOrientador);
           
            List<Cita> citas = citaRepository.findByOrientadorIdOrientador(idOrientador);
            
            log.info("📋 Total de citas: {}", citas.size());
            
            // Usar Map para evitar duplicados por ID
            Map<Integer, aprendiz> estudiantesUnicos = new HashMap<>();
            
            for (Cita cita : citas) {
                aprendiz aprendiz = cita.getAprendiz();
                if (aprendiz != null && !estudiantesUnicos.containsKey(aprendiz.getIdEstudiante())) {
                    estudiantesUnicos.put(aprendiz.getIdEstudiante(), aprendiz);
                }
            }
            
            List<Map<String, Object>> resultado = new ArrayList<>();
            
            for (aprendiz aprendiz : estudiantesUnicos.values()) {
                try {
                    Map<String, Object> estudianteMap = new HashMap<>();
                    estudianteMap.put("id", aprendiz.getIdEstudiante());
                    estudianteMap.put("nombre", 
                            aprendiz.getUsuario().getPersona().getNombreCompleto());
                    resultado.add(estudianteMap);
                } catch (Exception e) {
                    log.error("❌ Error al procesar estudiante ID {}: {}", 
                            aprendiz.getIdEstudiante(), e.getMessage());
                }
            }
            
            log.info("✅ Estudiantes únicos encontrados: {}", resultado.size());
            return resultado;
            
        } catch (Exception e) {
            log.error("❌ Error al obtener estudiantes: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @PostMapping("/evaluacion/guardar")
    @ResponseBody
    public Map<String, Object> guardarEvaluacion(
            @RequestParam Long idCita,
            @RequestParam Integer puntuacion,
            @RequestParam(required = false) String observaciones) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("💾 Guardando evaluación para cita ID: {}, puntuación: {}", idCita, puntuacion);
            
            // Validaciones
            if (idCita == null || idCita <= 0) {
                throw new RuntimeException("ID de cita inválido");
            }
            
            if (puntuacion == null || puntuacion < 0 || puntuacion > 99) {
                throw new RuntimeException("La puntuación debe estar entre 0 y 99");
            }
            
            EvaluacionEstres evaluacion = evaluacionService.guardarEvaluacion(
                    idCita, puntuacion, observaciones);
            
            log.info("✅ Evaluación guardada exitosamente con ID: {}", evaluacion.getId());
            
            response.put("success", true);
            response.put("message", "Evaluación guardada correctamente");
            response.put("evaluacionId", evaluacion.getId());
            
        } catch (RuntimeException e) {
            log.error("❌ Error al guardar evaluación: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            
        } catch (Exception e) {
            log.error("❌ Error inesperado al guardar evaluación: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error inesperado: " + e.getMessage());
        }
        
        return response;
    }

    // ========================================================================
    // MÉTODOS AUXILIARES PARA GENERAR DATOS DE GRÁFICAS
    // ========================================================================

    private Map<String, Object> generarDatosCitasPorMes(List<Cita> citas) {
        Map<String, Object> datos = new HashMap<>();
        
        if (citas.isEmpty()) {
            datos.put("meses", new ArrayList<>());
            datos.put("cantidades", new ArrayList<>());
            log.warn("⚠️ No hay citas para generar gráfica de citas por mes");
            return datos;
        }
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yyyy", new Locale("es", "ES"));
       
        Map<String, Long> citasPorMes = citas.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getFechaCita().withDayOfMonth(1).format(fmt),
                        Collectors.counting()
                ));
       
        List<String> meses = citasPorMes.keySet().stream()
                .sorted()
                .collect(Collectors.toList());
       
        List<Long> cantidades = meses.stream()
                .map(citasPorMes::get)
                .collect(Collectors.toList());
       
        datos.put("meses", meses);
        datos.put("cantidades", cantidades);
        
        log.debug("📊 Citas por mes: {} meses con datos", meses.size());
        return datos;
    }

    private Map<String, Long> generarDatosMotivos(List<Cita> citas) {
        Map<String, Long> datos = new HashMap<>();
        
        long ansiedad = citas.stream()
                .filter(c -> "ANSIEDAD".equalsIgnoreCase(c.getMotivoClasificado()))
                .count();
        long estres = citas.stream()
                .filter(c -> "ESTRES".equalsIgnoreCase(c.getMotivoClasificado()))
                .count();
        long otro = citas.stream()
                .filter(c -> c.getMotivoClasificado() == null ||
                        (!"ANSIEDAD".equalsIgnoreCase(c.getMotivoClasificado()) &&
                         !"ESTRES".equalsIgnoreCase(c.getMotivoClasificado())))
                .count();
        
        datos.put("ansiedad", ansiedad);
        datos.put("estres", estres);
        datos.put("otro", otro);
        
        log.debug("📊 Motivos: ansiedad={}, estrés={}, otro={}", ansiedad, estres, otro);
        return datos;
    }

    private Map<String, Long> generarDatosNivelEstres(List<EvaluacionEstres> evaluaciones) {
        Map<String, Long> datos = new HashMap<>();
        
        long bajo = evaluaciones.stream()
                .filter(e -> e.getPuntuacion() <= 33)
                .count();
        long medio = evaluaciones.stream()
                .filter(e -> e.getPuntuacion() > 33 && e.getPuntuacion() <= 66)
                .count();
        long alto = evaluaciones.stream()
                .filter(e -> e.getPuntuacion() > 66)
                .count();
        
        datos.put("bajo", bajo);
        datos.put("medio", medio);
        datos.put("alto", alto);
        
        log.debug("📊 Niveles estrés: bajo={}, medio={}, alto={}", bajo, medio, alto);
        return datos;
    }

    private Map<String, Object> generarDatosHorarios(List<Cita> citas) {
        Map<String, Object> datos = new HashMap<>();
        
        if (citas.isEmpty()) {
            datos.put("horas", new ArrayList<>());
            datos.put("cantidades", new ArrayList<>());
            return datos;
        }
       
        Map<Integer, Long> citasPorHora = citas.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getHoraCita().getHour(),
                        Collectors.counting()
                ));
       
        List<String> horas = citasPorHora.keySet().stream()
                .sorted()
                .map(h -> h + ":00")
                .collect(Collectors.toList());
       
        List<Long> cantidades = citasPorHora.keySet().stream()
                .sorted()
                .map(citasPorHora::get)
                .collect(Collectors.toList());
       
        datos.put("horas", horas);
        datos.put("cantidades", cantidades);
        return datos;
    }

    private Map<String, Object> generarDatosDiasSemana(List<Cita> citas) {
        Map<String, Object> datos = new HashMap<>();
       
        Map<Integer, Long> citasPorDia = citas.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getFechaCita().getDayOfWeek().getValue(),
                        Collectors.counting()
                ));
       
        List<Long> cantidades = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            cantidades.add(citasPorDia.getOrDefault(i, 0L));
        }
       
        datos.put("cantidades", cantidades);
        return datos;
    }

    private Map<String, Object> generarEstadisticasRapidas(List<Cita> citas, List<EvaluacionEstres> evaluaciones) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCitas", citas.size());
        stats.put("totalEvaluaciones", evaluaciones.size());
       
        if (!evaluaciones.isEmpty()) {
            double promedio = evaluaciones.stream()
                    .mapToInt(EvaluacionEstres::getPuntuacion)
                    .average()
                    .orElse(0);
            stats.put("promedioEstres", String.format("%.1f", promedio));
        } else {
            stats.put("promedioEstres", "N/A");
        }
       
        // Citas del mes actual
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1);
        long citasEsteMes = citas.stream()
                .filter(c -> !c.getFechaCita().isBefore(inicioMes) && !c.getFechaCita().isAfter(finMes))
                .count();
        stats.put("citasEsteMes", citasEsteMes);
       
        return stats;
    }

    private List<Map<String, Object>> generarListaEvaluaciones(List<EvaluacionEstres> evaluaciones) {
        return evaluaciones.stream()
                .sorted(Comparator.comparing(EvaluacionEstres::getCreatedAt).reversed())
                .limit(10)
                .map(e -> {
                    Map<String, Object> evalMap = new HashMap<>();
                    evalMap.put("fecha", e.getCreatedAt().toLocalDate().toString());
                    evalMap.put("estudiante", e.getCita().getAprendiz().getUsuario().getPersona().getNombreCompleto());
                    evalMap.put("puntuacion", e.getPuntuacion());
                    evalMap.put("nivel", e.getNivelDetectado());
                    evalMap.put("observaciones", e.getObservaciones());
                    return evalMap;
                })
                .collect(Collectors.toList());
    }
}