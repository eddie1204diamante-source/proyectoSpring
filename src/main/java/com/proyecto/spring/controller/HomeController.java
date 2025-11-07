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

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // -> busca templates/dashboard.html
    }
}
