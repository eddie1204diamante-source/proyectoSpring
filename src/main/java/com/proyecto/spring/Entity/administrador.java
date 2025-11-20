package com.proyecto.spring.Entity;

import jakarta.persistence.*;
@Entity
@Table(name = "administrador")
public class administrador {

    @Id
    @Column(name = "Id_administrador")
    private int Id_administrador; // Asumiendo que es la FK de persona y también PK

    @OneToOne
    @JoinColumn(name = "Id_persona", referencedColumnName = "Id_persona")
    private persona persona;

    public int getId_administrador() {
        return Id_administrador;
    }

    public void setId_administrador(int id_administrador) {
        Id_administrador = id_administrador;
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