package com.proyecto.spring.controller;

import com.proyecto.spring.Entity.ActividadAsignada;
import com.proyecto.spring.dto.ActividadAsignadaRequest;
import com.proyecto.spring.services.ActividadAsignadaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividad-asignada")
@CrossOrigin(origins = "*")
public class ActividadAsignadaController {

    @Autowired
    private ActividadAsignadaService service;

    // Crear actividad asignada
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody ActividadAsignadaRequest req) {
        try {
            return ResponseEntity.ok(service.crearAsignada(req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear: " + e.getMessage());
        }
    }

    // Listar todas
    @GetMapping("/listar")
    public ResponseEntity<List<ActividadAsignada>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // Listar por id de estudiante
    @GetMapping("/estudiante/{id}")
    public ResponseEntity<?> porEstudiante(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(service.listarPorEstudiante(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id) {
        ActividadAsignada act = service.buscarPorId(id);
        if (act == null) {
            return ResponseEntity.status(404).body("No encontrado");
        }
        return ResponseEntity.ok(act);
    }

    // Editar asignada
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @RequestBody ActividadAsignada datos) {
        try {
            return ResponseEntity.ok(service.editar(id, datos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al editar: " + e.getMessage());
        }
    }

    // Eliminar asignada
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            service.eliminar(id);
            return ResponseEntity.ok("Eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar: " + e.getMessage());
        }
    }
}
