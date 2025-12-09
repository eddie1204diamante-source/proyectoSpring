package com.proyecto.spring.services;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.dto.usuarioDTO;
import com.proyecto.spring.repository.usuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

import com.proyecto.spring.dto.usuarioDTO;

@Service
public class AdminUsuarioService {

    private final usuarioRepository repo;

    public AdminUsuarioService(usuarioRepository repo) {
        this.repo = repo;
    }

    public List<usuarioDTO> listar() {
        return repo.findAll().stream().map(u -> new usuarioDTO(
                u.getIdUsuario(),
                u.getPersona() != null ? u.getPersona().getNombreCompleto() : "Sin persona",
                u.getCorreo(),
                u.getRolId(),
                u.getPersona() != null ? u.getPersona().getId_persona() : null
        )).toList();
    }

    public Usuario crear(Usuario u){ return repo.save(u); }

    public Usuario editar(Integer id, Usuario nuevo){
        return repo.findById(id).map(u -> {
            u.setCorreo(nuevo.getCorreo());
            u.setRolId(nuevo.getRolId());
            if(nuevo.getPersona() != null) u.setPersona(nuevo.getPersona());
            if(nuevo.getContrasena() != null) u.setContrasena(nuevo.getContrasena());
            return repo.save(u);
        }).orElseThrow();
    }

    public void eliminar(Integer id){ repo.deleteById(id); }
}
