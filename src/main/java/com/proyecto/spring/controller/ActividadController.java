package com.proyecto.spring.controller;

import com.proyecto.spring.Entity.Actividad;
import com.proyecto.spring.services.ActividadService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
@CrossOrigin(origins = "*")
public class ActividadController {

    private final ActividadService service;

    public ActividadController(ActividadService service) {
        this.service = service;
    }

    @PostMapping
    public Actividad crear(@RequestBody Actividad actividad) {
        return service.crearActividad(actividad);
    }

    @GetMapping
    public List<Actividad> listar() {
        return service.listarActividades();
    }

    @GetMapping("/{id}")
    public Actividad obtener(@PathVariable Integer id) {
        return service.obtenerActividad(id);
    }

    @PutMapping("/{id}")
    public Actividad actualizar(@PathVariable Integer id, @RequestBody Actividad actividad) {
        actividad.setIdActividad(id);
        return service.actualizarActividad(actividad);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminarActividad(id);
    }
}
