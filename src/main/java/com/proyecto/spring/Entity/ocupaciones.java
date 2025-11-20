package com.proyecto.spring.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ocupaciones")
public class ocupaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_ocupacion")
    private int Id_ocupacion;

    @Column(name = "ocupacion", length = 100)
    private String ocupacion;

    @ManyToOne
    @JoinColumn(name = "Id_persona")
    private persona persona;

    public int getId_ocupacion() {
        return Id_ocupacion;
    }

    public void setId_ocupacion(int id_ocupacion) {
        Id_ocupacion = id_ocupacion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public persona getPersona() {
        return persona;
    }

    public void setPersona(persona persona) {
        this.persona = persona;
    }

    // GETTERS & SETTERS (Omitidos)
    // ...
    
}