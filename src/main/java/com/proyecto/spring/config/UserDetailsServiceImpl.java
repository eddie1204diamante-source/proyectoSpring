package com.proyecto.spring.config;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByPersonaDocumento(documento)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // DEVOLVEMOS SOLO EL rol_id COMO STRING: "1", "2" o "3"
        String rolId = String.valueOf(usuario.getRolId());

        return User.withUsername(usuario.getPersona().getDocumento())
                .password(usuario.getContrasena())
                .authorities(new SimpleGrantedAuthority(rolId))
                .build();
    }
}