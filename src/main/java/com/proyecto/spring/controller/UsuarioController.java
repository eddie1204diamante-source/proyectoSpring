package com.proyecto.spring.controller;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.dto.UsuarioSelectDTO;
import com.proyecto.spring.services.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // LISTAR TODOS LOS APRENDICES (rol = 2)
@GetMapping("/rol/{rolId}/select")
public List<UsuarioSelectDTO> listarPorRolSelect(@PathVariable Integer rolId) {
    List<Usuario> usuarios = usuarioService.listarPorRol(rolId);
    return usuarios.stream().map(UsuarioSelectDTO::new).toList();
}



    // OPCIONAL: listar orientadores
    @GetMapping("/orientadores")
    public List<Usuario> listarOrientadores() {
        return usuarioService.listarPorRol(3);
    }
}
