package com.proyecto.spring.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "area_trabajo")
public class area_trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_area_trabajo")
    private int Id_area_trabajo;

    @Column(name = "area_trabajo", length = 11)
    private String area_trabajo;

    @ManyToOne
    @JoinColumn(name = "Id_orientador")
    private psicologica psicologica;

    public int getId_area_trabajo() {
        return Id_area_trabajo;
    }

    public void setId_area_trabajo(int id_area_trabajo) {
        Id_area_trabajo = id_area_trabajo;
    }

    public String getArea_trabajo() {
        return area_trabajo;
    }

    public void setArea_trabajo(String area_trabajo) {
        this.area_trabajo = area_trabajo;
    }

    public psicologica getPsicologica() {
        return psicologica;
    }

    public void setPsicologica(psicologica psicologica) {
        this.psicologica = psicologica;
    }

    // GETTERS & SETTERS (Omitidos)
    // ...
}