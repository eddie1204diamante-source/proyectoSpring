package com.proyecto.spring.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "bienestar")
public class bienestar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_bienestar;

    // Error: usabas @OneToMany con un solo objeto
    @OneToOne
    @JoinColumn(name = "Id_persona", nullable = false)
    private persona persona;

    @OneToOne
    @JoinColumn(name = "Id_estudiante", nullable = false)
    private aprendiz aprendiz;

    // Getters y Setters
    public int getId_bienestar() { return id_bienestar; }
    public void setId_bienestar(int id_bienestar) { this.id_bienestar = id_bienestar; }
    public persona getPersona() { return persona; }
    public void setPersona(persona persona) { this.persona = persona; }
    public aprendiz getAprendiz() { return aprendiz; }
    public void setAprendiz(aprendiz aprendiz) { this.aprendiz = aprendiz; }
}
