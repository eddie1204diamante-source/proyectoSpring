package com.proyecto.spring.controller;

import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.dto.usuarioDTO;
import com.proyecto.spring.services.AdminUsuarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/usuarios")
@CrossOrigin("*")
public class UsuarioAdminController {

    private final AdminUsuarioService service;

    public UsuarioAdminController(AdminUsuarioService service) {
        this.service = service;
    }

    // 📌 GET todos
    @GetMapping
    public List<usuarioDTO> listar(){ return service.listar(); }

    // 📌 POST crear usuario
    @PostMapping
    public Usuario crear(@RequestBody Usuario u){ return service.crear(u); }

    // 📌 PUT editar
    @PutMapping("/{id}")
    public Usuario editar(@PathVariable Integer id, @RequestBody Usuario u){
        return service.editar(id, u);
    }

    // 📌 DELETE eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id){ service.eliminar(id); }
}
