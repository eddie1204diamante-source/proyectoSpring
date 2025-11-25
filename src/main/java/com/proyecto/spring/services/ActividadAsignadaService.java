package com.proyecto.spring.services;

import com.proyecto.spring.Entity.ActividadAsignada;
import com.proyecto.spring.dto.ActividadAsignadaRequest;
import com.proyecto.spring.repository.ActividadAsignadaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadAsignadaService {

    @Autowired
    private ActividadAsignadaRepository repo;

    // Crear actividad asignada
    public ActividadAsignada crearAsignada(ActividadAsignadaRequest req) {

        ActividadAsignada nueva = new ActividadAsignada();
        
        nueva.setIdEstudiante(req.getIdEstudiante());
        nueva.setTitulo(req.getTitulo());
        nueva.setDescripcion(req.getDescripcion());
        nueva.setFechaAsignacion(req.getFechaAsignacion());
        nueva.setFechaEntrega(req.getFechaEntrega());
        nueva.setUrlActividad(req.getUrlActividad());
        nueva.setEstado(req.getEstado());
        nueva.setObservacion(req.getObservacion());

        return repo.save(nueva);
    }

    // Listar todas
    public List<ActividadAsignada> listarTodas() {
        return repo.findAll();
    }

    // Listar por ID de estudiante
    public List<ActividadAsignada> listarPorEstudiante(Integer idEstudiante) {
        return repo.findByIdEstudiante(idEstudiante);
    }

    // Buscar por ID asignada
    public ActividadAsignada buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    // Editar actividad asignada
    public ActividadAsignada editar(Integer id, ActividadAsignada datos) {
        ActividadAsignada existente = buscarPorId(id);

        if (existente == null) {
            throw new RuntimeException("No existe la actividad asignada con ID " + id);
        }

        existente.setIdEstudiante(datos.getIdEstudiante());
        existente.setTitulo(datos.getTitulo());
        existente.setDescripcion(datos.getDescripcion());
        existente.setFechaAsignacion(datos.getFechaAsignacion());
        existente.setFechaEntrega(datos.getFechaEntrega());
        existente.setUrlActividad(datos.getUrlActividad());
        existente.setEstado(datos.getEstado());
        existente.setObservacion(datos.getObservacion());

        return repo.save(existente);
    }

    // Eliminar actividad asignada
    public void eliminar(Integer id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("No existe la actividad asignada para eliminar");
        }
        repo.deleteById(id);
    }
}
