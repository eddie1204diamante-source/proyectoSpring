package com.proyecto.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// ¡IMPORTANTE! Solo puede haber UNA anotación @Controller por clase
@Controller
public class HomeController {

    // ==================== PÁGINAS PÚBLICAS ====================
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

    @GetMapping("/gestionCita")  // Esta ruta estaba mal porque no tenía el prefijo /estudiante
    public String gestionarCitaEstudiante() {
        return "estudiante/gestionarCita";
    }

    @GetMapping("/actividades")
    public String actividadesEstudiante() {
        return "estudiante/consultarProceso";  // ¿Estás seguro de que el HTML se llama "consultarProceso.html"?
    }

    // ==================== ORIENTADOR ====================
    @GetMapping("/orientador/dashboard")
    public String dashboardOrientador() {
        return "orientador/dashboard";
    }

    @GetMapping("/orientador/gestionarCita")
    public String gestionarCitaOrientador() {
        return "orientador/gestionarCita";
    }

    @GetMapping("/orientador/actividades")
    public String actividadesOrientador() {
        return "orientador/actividades";
    }

    // ==================== ADMINISTRADOR ====================
    @GetMapping("/administrador/dashboard")
    public String dashboardAdministrador() {
        return "admin/dashboard";
    }
}