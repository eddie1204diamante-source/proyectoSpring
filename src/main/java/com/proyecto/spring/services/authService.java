// src/main/java/com/proyecto/spring/services/authService.java
package com.proyecto.spring.services;

import com.proyecto.spring.Entity.*;
import com.proyecto.spring.Entity.administrador;
import com.proyecto.spring.Entity.aprendiz;
import com.proyecto.spring.Entity.psicologica;
import com.proyecto.spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class authService {

    @Autowired
    private personaRepository personaRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private aprendizRepository aprendizRepository;

    @Autowired
    private psicologicaRepository psicologicaRepository;

    @Autowired
    private administradorRepository administradorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Determinar rol según dominio del correo
    private int determinarRol(String correo) {
        String email = correo.toLowerCase().trim();
        if (email.endsWith("@administradormindwell2025.com")) {
            return 1; // Administrador
        } else if (email.endsWith("@sena.edu.co")) {
            return 3; // Orientador / Psicologica
        } else if (email.endsWith("@gmail.com") || email.endsWith("@soy.sena.edu.co")) {
            return 2; // Aprendiz
        }
        return 2; // Por defecto aprendiz
    }

    // MÉTODO DE REGISTRO COMPLETO Y FUNCIONAL
    public Usuario registrar(String nombres, String apellidos, String documento,
                             String correo, String contrasena) throws Exception {

        // Validaciones
        if (personaRepository.existsByDocumento(documento)) {
            throw new Exception("El documento ya está registrado");
        }
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new Exception("El correo ya está en uso");
        }

        // Separar nombres y apellidos
        String[] partesNombres = nombres.trim().split("\\s+");
        String p_nombre = partesNombres[0];
        String s_nombre = partesNombres.length > 1 ? partesNombres[1] : null;

        String[] partesApellidos = apellidos.trim().split("\\s+");
        String p_apellido = partesApellidos[0];
        String s_apellido = partesApellidos.length > 1 ? partesApellidos[1] : null;

        // 1. Crear y guardar persona
        persona persona = new persona();
        persona.setP_nombre(p_nombre);
        persona.setS_nombre(s_nombre);
        persona.setP_apellido(p_apellido);
        persona.setS_apellido(s_apellido);
        persona.setDocumento(documento);
        persona.setEdad(0); // Puedes pedirlo después en un formulario

        persona = personaRepository.save(persona); // Ya tiene ID generado

        // 2. Determinar rol
        int rolId = determinarRol(correo);

        // 3. Crear y guardar usuario
        Usuario usuario = new Usuario();
        usuario.setPersona(persona);
        usuario.setCorreo(correo.toLowerCase().trim());
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setRol_id(rolId);

        usuario = usuarioRepository.save(usuario); // Ya tiene Id_usuario

        // 4. CREAR REGISTRO EN LA TABLA ESPECÍFICA SEGÚN ROL
        switch (rolId) {
            case 1: // ADMINISTRADOR
                administrador admin = new administrador();
                admin.setPersona(persona);
                // Si el ID es el mismo que persona (PK compartida), puedes asignarlo así:
                // admin.setId_administrador(persona.getId_persona());
                administradorRepository.save(admin);
                break;

            case 3: // ORIENTADOR (Psicologica)
                psicologica orientador = new psicologica();
                orientador.setPersona(persona);
                orientador.setSociedad("SENA");
                orientador.setFechaContratacion(new Date());
                // Si idOrientador es autoincremental → no toques nada
                // Si comparte ID con persona → descomenta la línea:
                // orientador.setIdOrientador(persona.getId_persona().longValue());
                psicologicaRepository.save(orientador);
                break;

            case 2: // APRENDIZ (por defecto)
            default:
                aprendiz aprendiz = new aprendiz();
                aprendiz.setUsuario(usuario); // RELACIÓN OBLIGATORIA
                // Los demás campos pueden quedar null por ahora
                aprendiz.setTipo_problema(null);
                aprendiz.setTrastorno(null);
                aprendiz.setId_trastorno_int(null);
                aprendizRepository.save(aprendiz);
                break;
        }

        return usuario;
    }

    // LOGIN (sin cambios, funciona perfecto)
    public Usuario login(String documento, String contrasena) throws Exception {
        persona persona = personaRepository.findByDocumento(documento)
                .orElseThrow(() -> new Exception("Documento no encontrado"));

        Usuario usuario = usuarioRepository.findByPersonaDocumento(documento)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new Exception("Contraseña incorrecta");
        }

        return usuario;
    }
}