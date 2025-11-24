package com.proyecto.spring.services;

import com.proyecto.spring.Entity.Actividad;
import com.proyecto.spring.repository.ActividadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;

    public ActividadService(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    public Actividad crearActividad(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    public List<Actividad> listarActividades() {
        return actividadRepository.findAll();
    }

    public Actividad obtenerActividad(Integer id) {
        return actividadRepository.findById(id).orElse(null);
    }

    public Actividad actualizarActividad(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    public void eliminarActividad(Integer id) {
        actividadRepository.deleteById(id);
    }
}
