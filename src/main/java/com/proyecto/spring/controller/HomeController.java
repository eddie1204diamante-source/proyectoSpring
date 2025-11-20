package com.proyecto.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index"; // → templates/index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // → templates/login.html (tu diseño hermoso)
    }

    // Estas rutas son IMPORTANTES: apuntan directo al archivo en templates/
    @GetMapping("/estudiante/dashboard")
    public String dashboardEstudiante() {
        return "estudiante/dashboard"; // → templates/estudiante/dashboard.html
    }

    @GetMapping("/orientador/dashboard")
    public String dashboardOrientador() {
        return "orientador/dashboard"; // → templates/orientador/dashboard.html
    }

    @GetMapping("/administrador/dashboard")
    public String dashboardAdministrador() {
        return "administrador/dashboard"; // → templates/administrador/dashboard.html
    }

    // Opcional: otras rutas que ya tenías
    @GetMapping("/gestionCita")
    public String gestionCita() {
        return "estudiante/gestionarCita";
    }

    @GetMapping("/orientador-gestionCita")
    public String gestionCitaOrientador() {
        return "orientador/gestionarCita";
    }
}