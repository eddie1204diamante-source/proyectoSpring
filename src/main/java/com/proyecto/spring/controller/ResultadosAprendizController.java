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
@RequestMapping("/aprendiz")
@RequiredArgsConstructor
public class ResultadosAprendizController {
    
    private final EvaluacionEstresService evaluacionService;

    /**
     * Muestra la página de resultados del aprendiz
     */
    @GetMapping("/resultados")
    public String mostrarMisResultados(Model model,
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        var usuario = userDetails.getUsuario();
        model.addAttribute("nombreAprendiz", usuario.getPersona().getNombreCompleto());
        return "aprendiz/resultados-aprendiz";
    }

    /**
     * Endpoint REST que devuelve todos los datos para las gráficas en formato JSON
     */
    @GetMapping("/resultados/datos")
    @ResponseBody
    public Map<String, Object> obtenerDatosGraficas(
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        var usuario = userDetails.getUsuario();
        Long idUsuario = usuario.getIdUsuario().longValue();
        
        LocalDate hoy = LocalDate.now();
        LocalDate hace12meses = hoy.minusMonths(12);
        
        // Obtener mis datos
        List<EvaluacionEstres> evaluaciones = evaluacionService.getMisEvaluaciones(idUsuario);
        List<Cita> citas = evaluacionService.getMisCitas(idUsuario, hace12meses, hoy);
        
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
        
        return response;
    }

    // ========================================================================
    // MÉTODOS AUXILIARES PARA GENERAR DATOS DE GRÁFICAS
    // ========================================================================

    private Map<String, Object> generarDatosCitasPorMes(List<Cita> citas) {
        Map<String, Object> datos = new HashMap<>();
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
        return datos;
    }

    private Map<String, Long> generarDatosMotivos(List<Cita> citas) {
        Map<String, Long> datos = new HashMap<>();
        datos.put("ansiedad", citas.stream()
                .filter(c -> "ANSIEDAD".equalsIgnoreCase(c.getMotivoClasificado()))
                .count());
        datos.put("estres", citas.stream()
                .filter(c -> "ESTRES".equalsIgnoreCase(c.getMotivoClasificado()))
                .count());
        datos.put("otro", citas.stream()
                .filter(c -> c.getMotivoClasificado() == null ||
                        (!"ANSIEDAD".equalsIgnoreCase(c.getMotivoClasificado()) &&
                         !"ESTRES".equalsIgnoreCase(c.getMotivoClasificado())))
                .count());
        return datos;
    }

    private Map<String, Long> generarDatosNivelEstres(List<EvaluacionEstres> evaluaciones) {
        Map<String, Long> datos = new HashMap<>();
        datos.put("bajo", evaluaciones.stream()
                .filter(e -> e.getPuntuacion() <= 33)
                .count());
        datos.put("medio", evaluaciones.stream()
                .filter(e -> e.getPuntuacion() > 33 && e.getPuntuacion() <= 66)
                .count());
        datos.put("alto", evaluaciones.stream()
                .filter(e -> e.getPuntuacion() > 66)
                .count());
        return datos;
    }

    private Map<String, Object> generarDatosHorarios(List<Cita> citas) {
        Map<String, Object> datos = new HashMap<>();
        
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
}