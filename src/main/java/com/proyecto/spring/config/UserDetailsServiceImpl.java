// src/main/java/com/proyecto/spring/config/UserDetailsServiceImpl.java
package com.proyecto.spring.config;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByPersonaDocumento(documento)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String rol = switch (usuario.getRolId()) {
            case 1 -> "ADMIN";
            case 2 -> "ESTUDIANTE";
            case 3 -> "ORIENTADOR";
            default -> "ESTUDIANTE";
        };

        return new User(
                usuario.getPersona().getDocumento(),
                usuario.getContrasena(),
                List.of(new SimpleGrantedAuthority("ROLE_" + rol))
        );
    }
}