// src/main/java/com/proyecto/spring/services/authService.java
package com.proyecto.spring.services;

<<<<<<< HEAD
import com.proyecto.spring.Entity.*;
import com.proyecto.spring.Entity.administrador;
import com.proyecto.spring.Entity.aprendiz;
import com.proyecto.spring.Entity.psicologica;
import com.proyecto.spring.repository.*;
=======
import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.Entity.Aprendiz;
import com.proyecto.spring.Entity.persona;
import com.proyecto.spring.repository.personaRepository;
import com.proyecto.spring.repository.usuarioRepository;
import com.proyecto.spring.repository.aprendizRepository;
>>>>>>> 41314187b2acd41cab3eac745c9aed83b1c8bf31
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
<<<<<<< HEAD
    private psicologicaRepository psicologicaRepository;

    @Autowired
    private administradorRepository administradorRepository;

    @Autowired
=======
>>>>>>> 41314187b2acd41cab3eac745c9aed83b1c8bf31
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
<<<<<<< HEAD
        persona.setEdad(0); // Puedes pedirlo después en un formulario

        persona = personaRepository.save(persona); // Ya tiene ID generado
=======
        persona.setEdad(0);
        persona.setContrasena(passwordEncoder.encode(contrasena));

        persona = personaRepository.save(persona);
>>>>>>> 41314187b2acd41cab3eac745c9aed83b1c8bf31

        // 2. Determinar rol
        int rolId = determinarRol(correo);

        // 3. Crear y guardar usuario
        Usuario usuario = new Usuario();
        usuario.setPersona(persona);
        usuario.setCorreo(correo.toLowerCase().trim());
        usuario.setContrasena(passwordEncoder.encode(contrasena));
<<<<<<< HEAD
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
=======
        int rol = determinarRol(correo);
        usuario.setRolId(rol);

        usuario = usuarioRepository.save(usuario);

        // ---------------------------------------
        //  🔥 SI ES APRENDIZ, CREAR REGISTRO EN TABLA aprendiz
        // ---------------------------------------
        if (rol == 2) {
            Aprendiz ap = new Aprendiz();
            ap.setUsuario(usuario);         // relación con usuario
            ap.setTipo_problema(null);
            ap.setTrastorno(null);
            ap.setId_trastorno_int(null);

            aprendizRepository.save(ap);
>>>>>>> 41314187b2acd41cab3eac745c9aed83b1c8bf31
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
