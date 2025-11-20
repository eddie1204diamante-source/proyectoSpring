package com.proyecto.spring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.spring.services.authService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(path = "api/v1/auth")
public class authController {

    @Autowired
    private authService aprendizService;

    @PostMapping("path")
    public void saveOrUpdate(@RequestBody aprendiz aprendiz){
        aprendizService.saveOrUpdate(aprendiz);
    }
}
