package com.proyecto.spring.Entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "psicologo")
public class psicologo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_psicolgo;

    @Column(name = "especializacion", length = 20, nullable = false)
    private String especializacion;

    @Column(name = "fecha_contratacion", length = 10, nullable = false)
    private Date fecha_contratacion;

    @OneToOne
    @JoinColumn(name = "Id_persona", nullable = false)
    private persona persona;

    public int getId_psicolgo() {
        return id_psicolgo;
    }

    public void setId_psicolgo(int id_psicolgo) {
        this.id_psicolgo = id_psicolgo;
    }

    public String getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(String especializacion) {
        this.especializacion = especializacion;
    }

    public Date getFecha_contratacion() {
        return fecha_contratacion;
    }

    public void setFecha_contratacion(Date fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }

    public persona getPersona() {
        return persona;
    }

    public void setPersona(persona persona) {
        this.persona = persona;
    }
    
}


