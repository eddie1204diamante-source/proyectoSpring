// src/main/java/com/proyecto/spring/controller/ResultadosAprendizController.java
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proyecto.spring.Entity.Cita;
import com.proyecto.spring.Entity.EvaluacionEstres;
import com.proyecto.spring.config.UserDetailsServiceImpl;
import com.proyecto.spring.services.EvaluacionEstresService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/estudiante")  // ← MANTENER TU RUTA ORIGINAL
@RequiredArgsConstructor
public class ResultadosAprendizController {
    
    private static final Logger log = LoggerFactory.getLogger(ResultadosAprendizController.class);
    
    private final EvaluacionEstresService evaluacionService;

    /**
     * Muestra la página de resultados del aprendiz
     */
    @GetMapping("/resultado")  // ← MANTENER TU RUTA ORIGINAL
    public String mostrarMisResultados(Model model,
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        var usuario = userDetails.getUsuario();
        log.info("🟢 Aprendiz {} accediendo a resultados", usuario.getIdUsuario());
        model.addAttribute("nombreAprendiz", usuario.getPersona().getNombreCompleto());
        return "estudiante/resultado";  // ← MANTENER TU RUTA ORIGINAL
    }

    /**
     * Endpoint REST que devuelve todos los datos para las gráficas en formato JSON
     * ¡IMPORTANTE! Esta ruta debe coincidir con la que busca el JavaScript
     */
    @GetMapping("/resultados/datos")  // ← NUEVA RUTA PARA EL JS
    @ResponseBody
    public Map<String, Object> obtenerDatosGraficas(
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        try {
            var usuario = userDetails.getUsuario();
            Long idUsuario = usuario.getIdUsuario().longValue();
            
            log.info("📊 Cargando datos para aprendiz ID: {}", idUsuario);
            
            LocalDate hoy = LocalDate.now();
            LocalDate hace12meses = hoy.minusMonths(12);
            
            // Obtener mis datos
            List<EvaluacionEstres> evaluaciones = evaluacionService.getMisEvaluaciones(idUsuario);
            List<Cita> citas = evaluacionService.getMisCitas(idUsuario, hace12meses, hoy);
            
            log.info("✅ Datos cargados: {} evaluaciones, {} citas", evaluaciones.size(), citas.size());
            
            // DEBUG: Imprimir datos
            if (!citas.isEmpty()) {
                log.info("📅 Primera cita: ID={}, Fecha={}", 
                        citas.get(0).getIdCita(),
                        citas.get(0).getFechaCita());
            } else {
                log.warn("⚠️ NO SE ENCONTRARON CITAS para aprendiz {}", idUsuario);
            }
            
            if (!evaluaciones.isEmpty()) {
                log.info("📝 Primera evaluación: puntuación={}", evaluaciones.get(0).getPuntuacion());
            } else {
                log.warn("⚠️ NO SE ENCONTRARON EVALUACIONES para aprendiz {}", idUsuario);
            }
            
            Map<String, Object> response = new HashMap<>();
            
            // 1. Datos para gráfica de Mis Citas por Mes
            response.put("citasPorMes", generarDatosCitasPorMes(citas));
            
            // 2. Datos para gráfica de Mis Motivos Clasificados
            response.put("motivosClasificados", generarDatosMotivos(citas));
            
            // 3. Datos para gráfica de Mi Nivel de Estrés
            response.put("nivelesEstres", generarDatosNivelEstres(evaluaciones));
            
            // 4. Datos para gráfica de Mis Horarios
            response.put("citasPorHora", generarDatosHorarios(citas));
            
            // 5. Datos para gráfica de Mis Días de la Semana
            response.put("citasPorDia", generarDatosDiasSemana(citas));
            
            // 6. Mis Estadísticas rápidas
            response.put("estadisticas", generarEstadisticasRapidas(citas, evaluaciones));
            
            // 7. Lista de mis evaluaciones recientes
            response.put("evaluacionesRecientes", generarListaEvaluaciones(evaluaciones, citas));
            
            log.info("📤 Enviando respuesta al frontend");
            return response;
            
        } catch (Exception e) {
            log.error("❌ Error al cargar datos del aprendiz: {}", e.getMessage(), e);
            // Retornar estructura vacía en caso de error
            Map<String, Object> response = new HashMap<>();
            response.put("citasPorMes", crearEstructuraVacia());
            response.put("motivosClasificados", crearMotivosVacios());
            response.put("nivelesEstres", crearNivelesVacios());
            response.put("citasPorHora", crearEstructuraVacia());
            response.put("citasPorDia", crearDiasVacios());
            response.put("estadisticas", crearEstadisticasVacias());
            response.put("evaluacionesRecientes", new ArrayList<>());
            return response;
        }
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
        
        // Agrupar por mes
        Map<String, Long> citasPorMes = citas.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getFechaCita().withDayOfMonth(1).format(fmt),
                        Collectors.counting()
                ));
        
        // Ordenar por fecha
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
        
        // Promedio de estrés
        if (!evaluaciones.isEmpty()) {
            double promedio = evaluaciones.stream()
                    .mapToInt(EvaluacionEstres::getPuntuacion)
                    .average()
                    .orElse(0);
            stats.put("promedioEstres", String.format("%.1f", promedio));
        } else {
            stats.put("promedioEstres", "N/A");
        }
        
        // Citas este mes
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        long citasEsteMes = citas.stream()
                .filter(c -> !c.getFechaCita().isBefore(inicioMes))
                .count();
        stats.put("citasEsteMes", citasEsteMes);
        
        return stats;
    }

    private List<Map<String, Object>> generarListaEvaluaciones(
            List<EvaluacionEstres> evaluaciones, List<Cita> citas) {
        
        return evaluaciones.stream()
                .sorted(Comparator.comparing(EvaluacionEstres::getCreatedAt).reversed())
                .limit(10)
                .map(e -> {
                    Map<String, Object> evalMap = new HashMap<>();
                    evalMap.put("fechaEvaluacion", e.getCreatedAt().toLocalDate().toString());
                    evalMap.put("fechaCita", e.getCita().getFechaCita().toString());
                    evalMap.put("puntuacion", e.getPuntuacion());
                    evalMap.put("nivel", e.getNivelDetectado());
                    evalMap.put("observaciones", e.getObservaciones());
                    return evalMap;
                })
                .collect(Collectors.toList());
    }

    // ========================================================================
    // MÉTODOS AUXILIARES PARA ESTRUCTURAS VACÍAS (en caso de error)
    // ========================================================================

    private Map<String, Object> crearEstructuraVacia() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("meses", new ArrayList<>());
        datos.put("cantidades", new ArrayList<>());
        datos.put("horas", new ArrayList<>());
        return datos;
    }

    private Map<String, Long> crearMotivosVacios() {
        Map<String, Long> datos = new HashMap<>();
        datos.put("ansiedad", 0L);
        datos.put("estres", 0L);
        datos.put("otro", 0L);
        return datos;
    }

    private Map<String, Long> crearNivelesVacios() {
        Map<String, Long> datos = new HashMap<>();
        datos.put("bajo", 0L);
        datos.put("medio", 0L);
        datos.put("alto", 0L);
        return datos;
    }

    private Map<String, Object> crearDiasVacios() {
        Map<String, Object> datos = new HashMap<>();
        List<Long> cantidades = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            cantidades.add(0L);
        }
        datos.put("cantidades", cantidades);
        return datos;
    }

    private Map<String, Object> crearEstadisticasVacias() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCitas", 0);
        stats.put("totalEvaluaciones", 0);
        stats.put("promedioEstres", "N/A");
        stats.put("citasEsteMes", 0);
        return stats;
    }
}