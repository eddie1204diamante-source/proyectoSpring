// src/main/java/com/proyecto/spring/config/UserDetailsServiceImpl.java
package com.proyecto.spring.config;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.repository.usuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        
        Usuario usuario = usuarioRepository.findByPersonaDocumento(documento)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + documento));

        return new UsuarioDetailsCustom(usuario);
    }

    // ESTA ES LA VERSIÓN QUE SÍ FUNCIONA CON TU SecurityConfig ACTUAL
    public static class UsuarioDetailsCustom implements UserDetails {
        
        private final Usuario usuario;

        public UsuarioDetailsCustom(Usuario usuario) {
            this.usuario = usuario;
        }

        // IMPORTANTE: Aquí devolvemos el rol_id como String ("1", "2", "3")
        // ¡¡EXACTAMENTE como espera tu SecurityConfig!!
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            String rolId = String.valueOf(usuario.getRolId());
            return List.of(new SimpleGrantedAuthority(rolId));
        }

        @Override public String getPassword() { return usuario.getContrasena(); }
        @Override public String getUsername() { return usuario.getPersona().getDocumento(); }

        @Override public boolean isAccountNonExpired() { return true; }
        @Override public boolean isAccountNonLocked() { return true; }
        @Override public boolean isCredentialsNonExpired() { return true; }
        @Override public boolean isEnabled() { return true; }

        // ¡¡MÉTODO QUE USAS EN TUS CONTROLADORES!!
        public Usuario getUsuario() {
            return this.usuario;
        }
    }
}