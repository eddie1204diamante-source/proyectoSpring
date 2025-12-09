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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/estudiante")
@RequiredArgsConstructor
public class ResultadosAprendizController {
    
    private static final Logger log = LoggerFactory.getLogger(ResultadosAprendizController.class);
    
    private final EvaluacionEstresService evaluacionService;

    // ========================================================================
    // ENDPOINT DE PRUEBA - TEMPORAL PARA DEBUG
    // ========================================================================
    
    @GetMapping("/resultados/test")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testEndpoint(
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("🧪 TEST: Iniciando prueba de diagnóstico");
            
            // Paso 1: Verificar UserDetails
            if (userDetails == null) {
                response.put("error", "UserDetails es null");
                response.put("paso", 1);
                return ResponseEntity.status(401).body(response);
            }
            log.info("✅ Paso 1: UserDetails OK");
            
            // Paso 2: Verificar Usuario
            var usuario = userDetails.getUsuario();
            if (usuario == null) {
                response.put("error", "Usuario es null");
                response.put("paso", 2);
                return ResponseEntity.status(401).body(response);
            }
            log.info("✅ Paso 2: Usuario OK - ID: {}", usuario.getIdUsuario());
            
            // Paso 3: Obtener ID de usuario
            Long idUsuario = usuario.getIdUsuario().longValue();
            response.put("idUsuario", idUsuario);
            log.info("✅ Paso 3: ID Usuario: {}", idUsuario);
            
            // Paso 4: Verificar fechas
            LocalDate hoy = LocalDate.now();
            LocalDate hace12meses = hoy.minusMonths(12);
            response.put("fechaDesde", hace12meses.toString());
            response.put("fechaHasta", hoy.toString());
            log.info("✅ Paso 4: Fechas - Desde: {} Hasta: {}", hace12meses, hoy);
            
            // Paso 5: Intentar obtener evaluaciones
            try {
                List<EvaluacionEstres> evaluaciones = evaluacionService.getMisEvaluaciones(idUsuario);
                response.put("cantidadEvaluaciones", evaluaciones.size());
                log.info("✅ Paso 5: Evaluaciones obtenidas: {}", evaluaciones.size());
            } catch (Exception e) {
                response.put("errorEvaluaciones", e.getMessage());
                log.error("❌ Paso 5 FALLÓ: {}", e.getMessage(), e);
            }
            
            // Paso 6: Intentar obtener citas
            try {
                List<Cita> citas = evaluacionService.getMisCitas(idUsuario, hace12meses, hoy);
                response.put("cantidadCitas", citas.size());
                log.info("✅ Paso 6: Citas obtenidas: {}", citas.size());
            } catch (Exception e) {
                response.put("errorCitas", e.getMessage());
                log.error("❌ Paso 6 FALLÓ: {}", e.getMessage(), e);
            }
            
            response.put("success", true);
            response.put("mensaje", "Test completado - Revisa los logs para detalles");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("❌ TEST FALLÓ: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("stackTrace", e.getClass().getName());
            return ResponseEntity.status(500).body(response);
        }
    }

    // ========================================================================
    // ENDPOINTS PRINCIPALES
    // ========================================================================

    @GetMapping("/resultado")
    public String mostrarMisResultados(Model model,
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        try {
            var usuario = userDetails.getUsuario();
            log.info("🟢 Aprendiz {} accediendo a resultados", usuario.getIdUsuario());
            
            String nombreCompleto = usuario.getPersona() != null 
                ? usuario.getPersona().getNombreCompleto() 
                : "Usuario";
            
            model.addAttribute("nombreAprendiz", nombreCompleto);
            return "estudiante/resultado";
            
        } catch (Exception e) {
            log.error("❌ Error al cargar página de resultados: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar la página de resultados");
            return "error";
        }
    }

    @GetMapping("/resultados/datos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerDatosGraficas(
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        try {
            log.info("═══════════════════════════════════════════════════════");
            log.info("📊 INICIO: Cargando datos de gráficas para aprendiz");
            log.info("═══════════════════════════════════════════════════════");
            
            // Validación inicial
            if (userDetails == null) {
                log.error("❌ UserDetails es null");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(crearRespuestaError("No autenticado"));
            }
            
            var usuario = userDetails.getUsuario();
            if (usuario == null) {
                log.error("❌ Usuario es null");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(crearRespuestaError("Usuario no encontrado"));
            }
            
            Long idUsuario = usuario.getIdUsuario().longValue();
            log.info("✅ Usuario autenticado - ID: {}", idUsuario);
            
            LocalDate hoy = LocalDate.now();
            LocalDate hace12meses = hoy.minusMonths(12);
            log.info("📅 Rango de fechas: {} hasta {}", hace12meses, hoy);
            
            // Obtener datos con manejo de errores detallado
            log.info("─────────────────────────────────────────────────────");
            log.info("🔍 Obteniendo evaluaciones...");
            List<EvaluacionEstres> evaluaciones = obtenerEvaluacionesSeguro(idUsuario);
            log.info("✅ Evaluaciones obtenidas: {}", evaluaciones.size());
            
            log.info("─────────────────────────────────────────────────────");
            log.info("🔍 Obteniendo citas...");
            List<Cita> citas = obtenerCitasSeguro(idUsuario, hace12meses, hoy);
            log.info("✅ Citas obtenidas: {}", citas.size());
            
            log.info("─────────────────────────────────────────────────────");
            log.info("🎨 Generando estructuras de datos para gráficas...");
            
            Map<String, Object> response = new HashMap<>();
            
            // Generar datos de forma segura
            response.put("citasPorMes", generarDatosCitasPorMes(citas));
            response.put("motivosClasificados", generarDatosMotivos(citas));
            response.put("nivelesEstres", generarDatosNivelEstres(evaluaciones));
            response.put("citasPorHora", generarDatosHorarios(citas));
            response.put("citasPorDia", generarDatosDiasSemana(citas));
            response.put("estadisticas", generarEstadisticasRapidas(citas, evaluaciones));
            response.put("evaluacionesRecientes", generarListaEvaluaciones(evaluaciones, citas));
            response.put("success", true);
            
            log.info("═══════════════════════════════════════════════════════");
            log.info("✅ FIN: Respuesta generada exitosamente");
            log.info("═══════════════════════════════════════════════════════");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("═══════════════════════════════════════════════════════");
            log.error("❌ ERROR CRÍTICO al cargar datos del aprendiz");
            log.error("═══════════════════════════════════════════════════════");
            log.error("Mensaje: {}", e.getMessage());
            log.error("Tipo: {}", e.getClass().getName());
            log.error("StackTrace:", e);
            
            Map<String, Object> errorResponse = crearRespuestaError(
                "Error al procesar los datos: " + e.getMessage()
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
        }
    }

    // ========================================================================
    // MÉTODOS AUXILIARES SEGUROS
    // ========================================================================

    private List<EvaluacionEstres> obtenerEvaluacionesSeguro(Long idUsuario) {
        try {
            log.debug("  → Llamando a evaluacionService.getMisEvaluaciones({})", idUsuario);
            List<EvaluacionEstres> evaluaciones = evaluacionService.getMisEvaluaciones(idUsuario);
            log.debug("  → Resultado: {} evaluaciones", evaluaciones.size());
            return evaluaciones;
        } catch (Exception e) {
            log.error("  ❌ Error al obtener evaluaciones: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<Cita> obtenerCitasSeguro(Long idUsuario, LocalDate desde, LocalDate hasta) {
        try {
            log.debug("  → Llamando a evaluacionService.getMisCitas({}, {}, {})", idUsuario, desde, hasta);
            List<Cita> citas = evaluacionService.getMisCitas(idUsuario, desde, hasta);
            log.debug("  → Resultado: {} citas", citas.size());
            return citas;
        } catch (Exception e) {
            log.error("  ❌ Error al obtener citas: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // ========================================================================
    // MÉTODOS PARA GENERAR DATOS DE GRÁFICAS
    // ========================================================================

    private Map<String, Object> generarDatosCitasPorMes(List<Cita> citas) {
        Map<String, Object> datos = new HashMap<>();
        
        try {
            if (citas == null || citas.isEmpty()) {
                datos.put("meses", new ArrayList<>());
                datos.put("cantidades", new ArrayList<>());
                log.debug("  ⚠️ Sin datos para citas por mes");
                return datos;
            }
            
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yyyy", new Locale("es", "ES"));
            
            List<Cita> citasValidas = citas.stream()
                .filter(c -> c != null && c.getFechaCita() != null)
                .collect(Collectors.toList());
            
            Map<String, Long> citasPorMes = citasValidas.stream()
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
            
            log.debug("  ✓ Citas por mes: {} meses", meses.size());
            
        } catch (Exception e) {
            log.error("  ❌ Error en generarDatosCitasPorMes: {}", e.getMessage(), e);
            datos.put("meses", new ArrayList<>());
            datos.put("cantidades", new ArrayList<>());
        }
        
        return datos;
    }

    private Map<String, Long> generarDatosMotivos(List<Cita> citas) {
        Map<String, Long> datos = new HashMap<>();
        
        try {
            if (citas == null || citas.isEmpty()) {
                datos.put("ansiedad", 0L);
                datos.put("estres", 0L);
                datos.put("otro", 0L);
                return datos;
            }
            
            long ansiedad = citas.stream()
                .filter(c -> c != null && "ANSIEDAD".equalsIgnoreCase(c.getMotivoClasificado()))
                .count();
            
            long estres = citas.stream()
                .filter(c -> c != null && "ESTRES".equalsIgnoreCase(c.getMotivoClasificado()))
                .count();
            
            long otro = citas.stream()
                .filter(c -> c != null && (c.getMotivoClasificado() == null ||
                    (!"ANSIEDAD".equalsIgnoreCase(c.getMotivoClasificado()) &&
                     !"ESTRES".equalsIgnoreCase(c.getMotivoClasificado()))))
                .count();
            
            datos.put("ansiedad", ansiedad);
            datos.put("estres", estres);
            datos.put("otro", otro);
            
            log.debug("  ✓ Motivos: ansiedad={}, estrés={}, otro={}", ansiedad, estres, otro);
            
        } catch (Exception e) {
            log.error("  ❌ Error en generarDatosMotivos: {}", e.getMessage(), e);
            datos.put("ansiedad", 0L);
            datos.put("estres", 0L);
            datos.put("otro", 0L);
        }
        
        return datos;
    }

    private Map<String, Long> generarDatosNivelEstres(List<EvaluacionEstres> evaluaciones) {
        Map<String, Long> datos = new HashMap<>();
        
        try {
            if (evaluaciones == null || evaluaciones.isEmpty()) {
                datos.put("bajo", 0L);
                datos.put("medio", 0L);
                datos.put("alto", 0L);
                return datos;
            }
            
            List<EvaluacionEstres> evaluacionesValidas = evaluaciones.stream()
                .filter(e -> e != null && e.getPuntuacion() != null)
                .collect(Collectors.toList());
            
            long bajo = evaluacionesValidas.stream()
                .filter(e -> e.getPuntuacion() <= 33)
                .count();
            
            long medio = evaluacionesValidas.stream()
                .filter(e -> e.getPuntuacion() > 33 && e.getPuntuacion() <= 66)
                .count();
            
            long alto = evaluacionesValidas.stream()
                .filter(e -> e.getPuntuacion() > 66)
                .count();
            
            datos.put("bajo", bajo);
            datos.put("medio", medio);
            datos.put("alto", alto);
            
            log.debug("  ✓ Niveles: bajo={}, medio={}, alto={}", bajo, medio, alto);
            
        } catch (Exception e) {
            log.error("  ❌ Error en generarDatosNivelEstres: {}", e.getMessage(), e);
            datos.put("bajo", 0L);
            datos.put("medio", 0L);
            datos.put("alto", 0L);
        }
        
        return datos;
    }

    private Map<String, Object> generarDatosHorarios(List<Cita> citas) {
        Map<String, Object> datos = new HashMap<>();
        
        try {
            if (citas == null || citas.isEmpty()) {
                datos.put("horas", new ArrayList<>());
                datos.put("cantidades", new ArrayList<>());
                return datos;
            }
            
            List<Cita> citasConHora = citas.stream()
                .filter(c -> c != null && c.getHoraCita() != null)
                .collect(Collectors.toList());
            
            if (citasConHora.isEmpty()) {
                datos.put("horas", new ArrayList<>());
                datos.put("cantidades", new ArrayList<>());
                return datos;
            }
            
            Map<Integer, Long> citasPorHora = citasConHora.stream()
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
            
        } catch (Exception e) {
            log.error("  ❌ Error en generarDatosHorarios: {}", e.getMessage(), e);
            datos.put("horas", new ArrayList<>());
            datos.put("cantidades", new ArrayList<>());
        }
        
        return datos;
    }

    private Map<String, Object> generarDatosDiasSemana(List<Cita> citas) {
        Map<String, Object> datos = new HashMap<>();
        
        try {
            List<Cita> citasValidas = citas != null ? citas.stream()
                .filter(c -> c != null && c.getFechaCita() != null)
                .collect(Collectors.toList()) : new ArrayList<>();
            
            Map<Integer, Long> citasPorDia = citasValidas.stream()
                .collect(Collectors.groupingBy(
                    c -> c.getFechaCita().getDayOfWeek().getValue(),
                    Collectors.counting()
                ));
            
            List<Long> cantidades = new ArrayList<>();
            for (int i = 1; i <= 7; i++) {
                cantidades.add(citasPorDia.getOrDefault(i, 0L));
            }
            
            datos.put("cantidades", cantidades);
            
        } catch (Exception e) {
            log.error("  ❌ Error en generarDatosDiasSemana: {}", e.getMessage(), e);
            List<Long> cantidades = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                cantidades.add(0L);
            }
            datos.put("cantidades", cantidades);
        }
        
        return datos;
    }

    private Map<String, Object> generarEstadisticasRapidas(
            List<Cita> citas, List<EvaluacionEstres> evaluaciones) {
        
        Map<String, Object> stats = new HashMap<>();
        
        try {
            stats.put("totalCitas", citas != null ? citas.size() : 0);
            stats.put("totalEvaluaciones", evaluaciones != null ? evaluaciones.size() : 0);
            
            if (evaluaciones != null && !evaluaciones.isEmpty()) {
                List<EvaluacionEstres> evaluacionesValidas = evaluaciones.stream()
                    .filter(e -> e != null && e.getPuntuacion() != null)
                    .collect(Collectors.toList());
                
                if (!evaluacionesValidas.isEmpty()) {
                    double promedio = evaluacionesValidas.stream()
                        .mapToInt(EvaluacionEstres::getPuntuacion)
                        .average()
                        .orElse(0);
                    stats.put("promedioEstres", String.format("%.1f", promedio));
                } else {
                    stats.put("promedioEstres", "N/A");
                }
            } else {
                stats.put("promedioEstres", "N/A");
            }
            
            LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
            long citasEsteMes = 0;
            
            if (citas != null) {
                citasEsteMes = citas.stream()
                    .filter(c -> c != null && c.getFechaCita() != null)
                    .filter(c -> !c.getFechaCita().isBefore(inicioMes))
                    .count();
            }
            
            stats.put("citasEsteMes", citasEsteMes);
            
        } catch (Exception e) {
            log.error("  ❌ Error en generarEstadisticasRapidas: {}", e.getMessage(), e);
            stats.put("totalCitas", 0);
            stats.put("totalEvaluaciones", 0);
            stats.put("promedioEstres", "N/A");
            stats.put("citasEsteMes", 0);
        }
        
        return stats;
    }

    private List<Map<String, Object>> generarListaEvaluaciones(
            List<EvaluacionEstres> evaluaciones, List<Cita> citas) {
        
        try {
            if (evaluaciones == null || evaluaciones.isEmpty()) {
                return new ArrayList<>();
            }
            
            return evaluaciones.stream()
                .filter(e -> e != null && e.getCreatedAt() != null && e.getCita() != null)
                .sorted(Comparator.comparing(EvaluacionEstres::getCreatedAt).reversed())
                .limit(10)
                .map(e -> {
                    try {
                        Map<String, Object> evalMap = new HashMap<>();
                        evalMap.put("fechaEvaluacion", 
                            e.getCreatedAt().toLocalDate().toString());
                        evalMap.put("fechaCita", 
                            e.getCita().getFechaCita() != null 
                                ? e.getCita().getFechaCita().toString() 
                                : "N/A");
                        evalMap.put("puntuacion", 
                            e.getPuntuacion() != null ? e.getPuntuacion() : 0);
                        evalMap.put("nivel", 
                            e.getNivelDetectado() != null ? e.getNivelDetectado() : "N/A");
                        evalMap.put("observaciones", 
                            e.getObservaciones() != null ? e.getObservaciones() : "");
                        return evalMap;
                    } catch (Exception ex) {
                        log.error("  ❌ Error al procesar evaluación: {}", ex.getMessage());
                        return null;
                    }
                })
                .filter(map -> map != null)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("  ❌ Error en generarListaEvaluaciones: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // ========================================================================
    // MÉTODOS DE RESPUESTA DE ERROR
    // ========================================================================

    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", mensaje);
        response.put("citasPorMes", crearEstructuraVacia());
        response.put("motivosClasificados", crearMotivosVacios());
        response.put("nivelesEstres", crearNivelesVacios());
        response.put("citasPorHora", crearEstructuraVacia());
        response.put("citasPorDia", crearDiasVacios());
        response.put("estadisticas", crearEstadisticasVacias());
        response.put("evaluacionesRecientes", new ArrayList<>());
        return response;
    }

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