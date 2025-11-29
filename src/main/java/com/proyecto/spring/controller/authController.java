package com.proyecto.spring.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.dto.LoginRequest;
import com.proyecto.spring.services.authService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")// IMPORTANTE
public class authController {

    @Autowired
    private authService authService;

    // === DTOs internos (más simple que crear clases separadas) ===
    public static class RegisterRequest {
        public String nombres, apellidos, documento, correo, contrasena;
    }

    // Ya no usamos el interno, usamos el del paquete dto
    // public static class LoginRequest { ... }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Usuario usuario = authService.registrar(
                request.nombres, request.apellidos, request.documento,
                request.correo, request.contrasena
            );
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("message", "Registro exitoso");
            res.put("redirect", getRedirectUrl(usuario.getRolId()));
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
    try {
        Usuario usuario = authService.login(request.getDocumento(), request.getContrasena());

        // USAMOS EL rol_id DIRECTO COMO STRING
        String rolId = String.valueOf(usuario.getRolId());

        var auth = new UsernamePasswordAuthenticationToken(
            usuario.getPersona().getDocumento(),
            null,
            List.of(new SimpleGrantedAuthority(rolId))
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        httpRequest.getSession(); // Crea la sesión

        String nombreCompleto = usuario.getPersona().getNombreCompleto();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Login exitoso");
        response.put("rol", usuario.getRolId());
        response.put("idUsuario", usuario.getIdUsuario());
        response.put("nombreCompleto", nombreCompleto);
        response.put("redirect", getRedirectUrl(usuario.getRolId()));

        return ResponseEntity.ok(response);

    } catch (Exception e) {
        return ResponseEntity.status(401)
                .body(Map.of("success", false, "message", e.getMessage()));
    }
}

    private String getRedirectUrl(int rol) {
        return switch (rol) {
            case 1 -> "/administrador/dashboard";
            case 2 -> "/estudiante/dashboard";
            case 3 -> "/orientador/dashboard";
            default -> "/estudiante/dashboard";
        };
    }
}