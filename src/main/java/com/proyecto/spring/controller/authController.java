package com.proyecto.spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.Entity.administrador;
import com.proyecto.spring.Entity.aprendiz;
import com.proyecto.spring.Entity.psicologica;
import com.proyecto.spring.config.UserDetailsServiceImpl;
import com.proyecto.spring.dto.LoginRequest;
import com.proyecto.spring.repository.administradorRepository;
import com.proyecto.spring.repository.aprendizRepository;
import com.proyecto.spring.repository.psicologicaRepository;
import com.proyecto.spring.services.authService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class authController {

    @Autowired
    private authService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private aprendizRepository aprendizRepository;

    @Autowired
    private psicologicaRepository psicologicaRepository;

    @Autowired
    private administradorRepository administradorRepository;

    // === DTO interno para registro ===
    public static class RegisterRequest {
        public String nombres, apellidos, documento, correo, contrasena;
    }

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
            // Autenticación usando Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getDocumento(),
                    request.getContrasena()
                )
            );

            // Guardamos la autenticación
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Forzamos creación de sesión
            httpRequest.getSession();

            // Obtenemos el usuario desde nuestro UserDetails personalizado
            UserDetailsServiceImpl.UsuarioDetailsCustom userDetails =
                (UserDetailsServiceImpl.UsuarioDetailsCustom) authentication.getPrincipal();

            Usuario usuario = userDetails.getUsuario();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("rol", usuario.getRolId());
            response.put("idUsuario", usuario.getIdUsuario());
            response.put("nombreCompleto", usuario.getPersona().getNombreCompleto());

            // 👇 AGREGAR EL ID ESPECÍFICO SEGÚN EL ROL
            switch (usuario.getRolId()) {
                case 2: // ESTUDIANTE
                    aprendiz aprendiz = aprendizRepository.findByUsuarioId(usuario.getIdUsuario());
                    if (aprendiz != null) {
                        response.put("idEstudiante", aprendiz.getIdEstudiante());
                    }
                    break;

                case 3: // ORIENTADOR
                    psicologica orientador = psicologicaRepository.findByPersonaId(usuario.getPersona().getId_persona());
                    if (orientador != null) {
                        response.put("idOrientador", orientador.getIdOrientador());
                    }
                    break;

                case 1: // ADMINISTRADOR
                    administrador admin = administradorRepository.findByPersonaId(usuario.getPersona().getId_persona());
                    if (admin != null) {
                        response.put("idAdministrador", admin.getId_administrador());
                    }
                    break;
            }

            response.put("redirect", getRedirectUrl(usuario.getRolId()));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "Documento o contraseña incorrectos"));
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