package com.proyecto.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") 
    public String index() {
        return "index"; // -> busca templates/index.html
    }

    @GetMapping("/login") 
    public String login() {
        return "login"; // -> busca templates/login.html
    }

      @GetMapping("/estudiante/dashboard")
    public String dashboardEstudiante() {
        return "estudiante/dashboard"; 
    }
    @GetMapping("/gestionCita")
    public String gestionarCita() {
        return "estudiante/gestionarCita"; // -> busca templates/gestionarCita.html
    }
    @GetMapping("/actividades")
    public String consultarProceso() {
        return "estudiante/consultarProceso"; // -> busca templates/consultarProceso.html
    }

 @GetMapping("/orientador/actividades")
public String consultarProcesoOrientador() {
    return "orientador/actividades";
}

@GetMapping("/orientador/gestionCita")
public String gestionarCitaOrientador() {
    return "orientador/gestionarCita";
}

@GetMapping("/orientador/dashboard")
public String dashboardOrientador() {
    return "orientador/dashboard";
}

@GetMapping("/orientador/resultados")
public String resultadosdOrientador() {
    return "orientador/resultados";
}
}
