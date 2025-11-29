package com.proyecto.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ==================== ESTUDIANTE / APRENDIZ ====================
    @GetMapping("/estudiante/dashboard")
    public String dashboardEstudiante() {
        return "estudiante/dashboard";
    }

    @GetMapping("/gestionCita")
    public String gestionarCitaEstudiante() {
        return "estudiante/gestionarCita";
    }

    @GetMapping("/actividades")
    public String actividadesEstudiante() {
        return "estudiante/consultarProceso";
    }

    @GetMapping("/estudiante/resultado")
    public String resultadoEstudiante() {
        return "estudiante/resultado";
    }

    // ==================== ORIENTADOR ====================
    @GetMapping("/orientador/actividades")
    public String actividadesOrientador() {
        return "orientador/actividades";
    }

    // RUTA CORREGIDA: ahora coincide con el nombre del archivo HTML
    @GetMapping("/orientador/gestionarCita")
    public String gestionarCitaOrientador() {
        return "orientador/gestionarCita";
    }

    @GetMapping("/orientador/dashboard")
    public String dashboardOrientador() {
        return "orientador/dashboard";
    }

    // /orientador/resultados lo maneja ResultadosOrientadorController → lo dejamos fuera
}