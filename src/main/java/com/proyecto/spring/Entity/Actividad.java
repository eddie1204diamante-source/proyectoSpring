package com.proyecto.spring.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "actividad")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idActividad;

    @Column(length = 200)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "url_actividad", length = 300)
    private String urlActividad;

    public String getUrlActividad() {
        return urlActividad;
    }

    public void setUrlActividad(String urlActividad) {
        this.urlActividad = urlActividad;
    }

    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    public Integer getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Integer idActividad) {
        this.idActividad = idActividad;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // getters y setters...


    
}
