package com.proyecto.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class index {

    @GetMapping("/pagina-principal")
    public String indexString() {
        return "index";
    }

}
