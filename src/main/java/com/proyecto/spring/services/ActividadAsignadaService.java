package com.proyecto.spring.services;

import com.proyecto.spring.Entity.ActividadAsignada;
import com.proyecto.spring.repository.ActividadAsignadaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadAsignadaService {

    private final ActividadAsignadaRepository repository;

    public ActividadAsignadaService(ActividadAsignadaRepository repository) {
        this.repository = repository;
    }

    public ActividadAsignada asignarActividad(ActividadAsignada actividadAsignada) {
        return repository.save(actividadAsignada);
    }

    public List<ActividadAsignada> listarAsignadas() {
        return repository.findAll();
    }

    public ActividadAsignada obtenerAsignada(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public ActividadAsignada actualizarAsignada(ActividadAsignada actividadAsignada) {
        return repository.save(actividadAsignada);
    }

    public void eliminarAsignada(Integer id) {
        repository.deleteById(id);
    }
}
