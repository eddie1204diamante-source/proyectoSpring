    // src/main/java/com/proyecto/spring/controller/ResultadosOrientadorController.java
    package com.proyecto.spring.controller;

    import java.time.LocalDate;

    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.ResponseBody;

    import com.proyecto.spring.Entity.psicologica;
    import com.proyecto.spring.config.UserDetailsServiceImpl;
    import com.proyecto.spring.services.EvaluacionEstresService;

    import lombok.RequiredArgsConstructor;

    @Controller
    @RequestMapping("/orientador")
    @RequiredArgsConstructor
    public class ResultadosOrientadorController {

        private final EvaluacionEstresService evaluacionService;

        @GetMapping("/resultados")
        public String mostrarResultadosOrientador(Model model,
                @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) throws Exception {

            var usuario = userDetails.getUsuario(); // ← ¡TU USUARIO COMPLETO!

            psicologica orientador = usuario.getPersona().getPsicologica();
            if (orientador == null) {
                model.addAttribute("error", "No tienes perfil de orientador asignado.");
                return "error";
            }

            Long idOrientador = orientador.getIdOrientador();
            LocalDate hoy = LocalDate.now();
            LocalDate hace12meses = hoy.minusMonths(12);

            var evaluaciones = evaluacionService.getEvaluacionesOrientador(idOrientador);
            var citas = evaluacionService.getCitasAtendidasOrientador(idOrientador, hace12meses, hoy);

            model.addAttribute("graficaCitasMes", evaluacionService.generarGraficaCitasMes(citas));
            model.addAttribute("graficaMotivos", evaluacionService.generarGraficaMotivos(citas));
            model.addAttribute("graficaNivelEstres", evaluacionService.generarGraficaNivelEstres(evaluaciones));
            model.addAttribute("graficaHora", evaluacionService.generarGraficaHora(citas));
            model.addAttribute("graficaDia", evaluacionService.generarGraficaDiaSemana(citas));
            model.addAttribute("evaluaciones", evaluaciones);

            return "orientador/resultados-orientador";
        }

        @PostMapping("/evaluacion/guardar")
        @ResponseBody
        public String guardarEvaluacion(@RequestParam Long idCita,
                                        @RequestParam Integer puntuacion,
                                        @RequestParam(required = false) String observaciones) {
            evaluacionService.guardarEvaluacion(idCita, puntuacion, observaciones);
            return "OK";
        }
    }