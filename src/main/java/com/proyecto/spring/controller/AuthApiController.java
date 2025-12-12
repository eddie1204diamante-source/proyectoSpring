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
import com.proyecto.spring.Entity.administrador;
import com.proyecto.spring.Entity.aprendiz;
import com.proyecto.spring.Entity.psicologica;
import com.proyecto.spring.repository.administradorRepository;
import com.proyecto.spring.repository.aprendizRepository;
import com.proyecto.spring.repository.psicologicaRepository;
import com.proyecto.spring.repository.usuarioRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthApiController {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private aprendizRepository aprendizRepository;

    @Autowired
    private psicologicaRepository psicologicaRepository;

    @Autowired
    private administradorRepository administradorRepository;

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

        // 👇 AGREGAR EL ID ESPECÍFICO SEGÚN EL ROL
        switch (usuario.getRolId()) {
            case 2: // ESTUDIANTE
                aprendiz aprendiz = aprendizRepository.findByUsuarioId(usuario.getIdUsuario());
                if (aprendiz != null) {
                    res.put("idEstudiante", aprendiz.getIdEstudiante());
                }
                break;

            case 3: // ORIENTADOR
                psicologica orientador = psicologicaRepository.findByPersonaId(usuario.getPersona().getId_persona());
                if (orientador != null) {
                    res.put("idOrientador", orientador.getIdOrientador());
                }
                break;

            case 1: // ADMINISTRADOR
                administrador admin = administradorRepository.findByPersonaId(usuario.getPersona().getId_persona());
                if (admin != null) {
                    res.put("idAdministrador", admin.getId_administrador());
                }
                break;
        }

        return ResponseEntity.ok(res);
    }
}