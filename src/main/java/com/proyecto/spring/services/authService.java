package com.proyecto.spring.services;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.Entity.persona;
import com.proyecto.spring.repository.personaRepository;
import com.proyecto.spring.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class authService {

    @Autowired
    private personaRepository personaRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Determinar rol según dominio
    private int determinarRol(String correo) {
        String email = correo.toLowerCase().trim();
        if (email.endsWith("@administradormindwell2025.com")) {
            return 1; // Administrador
        } else if (email.endsWith("@sena.edu.co")) {
            return 3; // Orientador
        } else if (email.endsWith("@gmail.com") || email.endsWith("@soy.sena.edu.co")) {
            return 2; // Aprendiz
        }
        return 2; // Por defecto aprendiz
    }

    // Método de registro
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

        // Crear persona
        persona persona = new persona();
        persona.setP_nombre(p_nombre);
        persona.setS_nombre(s_nombre);
        persona.setP_apellido(p_apellido);
        persona.setS_apellido(s_apellido);
        persona.setDocumento(documento);
        persona.setEdad(0); // por ahora null o 0
        persona.setContrasena(passwordEncoder.encode(contrasena)); // opcional, si lo usas aquí también

        persona = personaRepository.save(persona); // Aquí ya tiene ID generado

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setPersona(persona);
        usuario.setCorreo(correo.toLowerCase().trim());
        usuario.setContrasena(passwordEncoder.encode(contrasena));
        usuario.setRol_id(determinarRol(correo));

        return usuarioRepository.save(usuario);
    }

    // Método de login
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