package com.proyecto.spring.Entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "psicologica")
public class psicologica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orientador")
    private Long idOrientador;        

    @OneToOne
    @JoinColumn(name = "Id_persona", referencedColumnName = "Id_persona")
    private persona persona;

    @Column(name = "sociedad", length = 40)
    private String sociedad;

    @Column(name = "fecha_contratacion")
    private Date fecha_contratacion;

    @OneToMany(mappedBy = "psicologica")
    private Set<area_trabajo> areas_trabajo;

    // GETTERS & SETTERS
    public Long getIdOrientador() {
        return idOrientador;
    }

    public void setIdOrientador(Long idOrientador) {
        this.idOrientador = idOrientador;
    }

    public persona getPersona() {
        return persona;
    }

    public void setPersona(persona persona) {
        this.persona = persona;
    }

    public String getSociedad() {
        return sociedad;
    }

    public void setSociedad(String sociedad) {
        this.sociedad = sociedad;
    }

    public Date getFechaContratacion() {
        return fecha_contratacion;
    }

    public void setFechaContratacion(Date fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }

    public Set<area_trabajo> getAreasTrabajo() {
        return areas_trabajo;
    }

    public void setAreasTrabajo(Set<area_trabajo> areas_trabajo) {
        this.areas_trabajo = areas_trabajo;
    }
}