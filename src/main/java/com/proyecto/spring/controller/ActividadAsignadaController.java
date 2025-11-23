package com.proyecto.spring.controller;

import com.proyecto.spring.Entity.ActividadAsignada;
import com.proyecto.spring.services.ActividadAsignadaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades-asignadas")
@CrossOrigin(origins = "*")
public class ActividadAsignadaController {

    private final ActividadAsignadaService service;

    public ActividadAsignadaController(ActividadAsignadaService service) {
        this.service = service;
    }

    @PostMapping
    public ActividadAsignada asignar(@RequestBody ActividadAsignada actividadAsignada) {
        return service.asignarActividad(actividadAsignada);
    }

    @GetMapping
    public List<ActividadAsignada> listar() {
        return service.listarAsignadas();
    }

    @GetMapping("/{id}")
    public ActividadAsignada obtener(@PathVariable Integer id) {
        return service.obtenerAsignada(id);
    }

    @PutMapping("/{id}")
    public ActividadAsignada actualizar(@PathVariable Integer id, @RequestBody ActividadAsignada actividadAsignada) {
        actividadAsignada.setIdAsignada(id);
        return service.actualizarAsignada(actividadAsignada);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminarAsignada(id);
    }
}
