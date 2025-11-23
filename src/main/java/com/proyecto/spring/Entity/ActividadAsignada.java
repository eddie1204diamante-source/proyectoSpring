package com.proyecto.spring.Entity;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "actividad_asignada")
public class ActividadAsignada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsignada;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private aprendiz aprendiz;

    @ManyToOne
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;

    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion;

    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    
    @Column(name = "url_actividad", length = 300)
    private String urlActividad;


    private String estado;
    // getters y setters...
    public Integer getIdAsignada() {
        return idAsignada;
    }

    public void setIdAsignada(Integer idAsignada) {
        this.idAsignada = idAsignada;
    }

    public aprendiz getAprendiz() {
        return aprendiz;
    }

    public void setAprendiz(aprendiz aprendiz) {
        this.aprendiz = aprendiz;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
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

    private String observacion;
    public String getUrlActividad() {
        return urlActividad;
    }

    public void setUrlActividad(String urlActividad) {
        this.urlActividad = urlActividad;
    }


}
