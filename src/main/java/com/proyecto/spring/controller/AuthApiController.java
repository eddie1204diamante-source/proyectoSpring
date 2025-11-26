// src/main/java/com/proyecto/spring/controller/AuthApiController.java

package com.proyecto.spring.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.repository.usuarioRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthApiController {

    @Autowired
    private usuarioRepository usuarioRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        String documento = principal.getName();
        Usuario usuario = usuarioRepository.findByPersonaDocumento(documento)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Object> res = new HashMap<>();
        res.put("idUsuario", usuario.getIdUsuario());
        res.put("nombreCompleto", usuario.getPersona().getNombreCompleto());
        res.put("rol", usuario.getRolId());

        return ResponseEntity.ok(res);
    }
}