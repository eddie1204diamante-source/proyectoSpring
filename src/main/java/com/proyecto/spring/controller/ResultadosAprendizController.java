// src/main/java/com/proyecto/spring/controller/ResultadosAprendizController.java
package com.proyecto.spring.controller;

import java.time.LocalDate;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.spring.config.UserDetailsServiceImpl;
import com.proyecto.spring.services.EvaluacionEstresService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/aprendiz")
@RequiredArgsConstructor
public class ResultadosAprendizController {

    private final EvaluacionEstresService evaluacionService;

    @GetMapping("/resultados")
    public String mostrarMisResultados(Model model,
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) throws Exception {

        var usuario = userDetails.getUsuario(); // ← ¡AQUÍ TIENES TU USUARIO REAL!

        Long idUsuario = usuario.getIdUsuario().longValue();
        LocalDate hoy = LocalDate.now();
        LocalDate hace12meses = hoy.minusMonths(12);

        var evaluaciones = evaluacionService.getMisEvaluaciones(idUsuario);
        var citas = evaluacionService.getMisCitas(idUsuario, hace12meses, hoy);

        model.addAttribute("graficaCitasMes", evaluacionService.generarGraficaCitasMes(citas));
        model.addAttribute("graficaMotivos", evaluacionService.generarGraficaMotivos(citas));
        model.addAttribute("graficaNivelEstres", evaluacionService.generarGraficaNivelEstres(evaluaciones));
        model.addAttribute("graficaHora", evaluacionService.generarGraficaHora(citas));
        model.addAttribute("graficaDia", evaluacionService.generarGraficaDiaSemana(citas));
        model.addAttribute("evaluaciones", evaluaciones);

        return "aprendiz/resultados-aprendiz";
    }
}