package com.proyecto.spring.controller;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.services.authService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class authController {

    @Autowired
    private authService authService;

    public static class RegisterRequest {
        public String nombres;
        public String apellidos;
        public String documento;
        public String correo;
        public String contrasena;
    }

    public static class LoginRequest {
        public String documento;
        public String contrasena;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Usuario usuario = authService.registrar(
                request.nombres,
                request.apellidos,
                request.documento,
                request.correo,
                request.contrasena
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Registro exitoso");
            response.put("rol", usuario.getRolId());
            response.put("redirect", getRedirectUrl(usuario.getRolId()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Usuario usuario = authService.login(request.documento, request.contrasena);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("rol", usuario.getRolId());
            response.put("redirect", getRedirectUrl(usuario.getRolId()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    private String getRedirectUrl(int rol) {
        return switch (rol) {
            case 1 -> "/administrador/dashboard";
            case 3 -> "/orientador/dashboard";
            default -> "/estudiante/dashboard";
        };
    }
}