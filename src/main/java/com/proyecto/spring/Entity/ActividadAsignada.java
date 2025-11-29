package com.proyecto.spring.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "actividad_asignada")
public class ActividadAsignada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsignada;

    //  ID del aprendiz 
    @Column(name = "id_estudiante", nullable = false)
    private Integer idEstudiante;
    
    //  Datos independientes de la actividad
    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion;

    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;

    @Column(name = "url_actividad", length = 300)
    private String urlActividad;

    @Column(length = 50)
    private String estado;

    @Column(length = 300)
    private String observacion;

    // 
    // Getters & Setters
    // 

    public Integer getIdAsignada() {
        return idAsignada;
    }

    public void setIdAsignada(Integer idAsignada) {
        this.idAsignada = idAsignada;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getUrlActividad() {
        return urlActividad;
    }

    public void setUrlActividad(String urlActividad) {
        this.urlActividad = urlActividad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
