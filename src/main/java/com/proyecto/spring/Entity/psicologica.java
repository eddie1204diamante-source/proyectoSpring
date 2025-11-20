package com.proyecto.spring.Entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "psicologica")
public class psicologica {

    @Id
    @Column(name = "Id_orientador")
    private int Id_orientador; 

    @OneToOne
    @JoinColumn(name = "Id_persona", referencedColumnName = "Id_persona")
    private persona persona;

    @Column(name = "Id_orientador_int", length = 11)
    private String Id_orientador_int;

    @Column(name = "sociedad", length = 40)
    private String sociedad;

    @Column(name = "fecha_contratacion")
    private Date fecha_contratacion; // Asumiendo Date para DATE

    // Relaciones
    @OneToMany(mappedBy = "psicologica")
    private Set<area_trabajo> areas_trabajo;

    public int getId_orientador() {
        return Id_orientador;
    }

    public void setId_orientador(int id_orientador) {
        Id_orientador = id_orientador;
    }

    public persona getPersona() {
        return persona;
    }

    public void setPersona(persona persona) {
        this.persona = persona;
    }

    public String getId_orientador_int() {
        return Id_orientador_int;
    }

    public void setId_orientador_int(String id_orientador_int) {
        Id_orientador_int = id_orientador_int;
    }

    public String getSociedad() {
        return sociedad;
    }

    public void setSociedad(String sociedad) {
        this.sociedad = sociedad;
    }

    public Date getFecha_contratacion() {
        return fecha_contratacion;
    }

    public void setFecha_contratacion(Date fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }

    public Set<area_trabajo> getAreas_trabajo() {
        return areas_trabajo;
    }

    public void setAreas_trabajo(Set<area_trabajo> areas_trabajo) {
        this.areas_trabajo = areas_trabajo;
    }

    // GETTERS & SETTERS (Omitidos)
    // ...
    
}