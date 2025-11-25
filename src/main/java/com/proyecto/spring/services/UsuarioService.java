package com.proyecto.spring.services;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.repository.usuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final usuarioRepository repo;

    public UsuarioService(usuarioRepository repo) {
        this.repo = repo;
    }

    public List<Usuario> listarPorRol(int RolId) {
        return repo.findByRolId(RolId);
    }
}
