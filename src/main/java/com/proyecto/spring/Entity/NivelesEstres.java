package com.proyecto.spring.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "niveles_estres")
public class NivelesEstres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estres") // El MR usa 'id_estres' como PK
    private Integer idEstres;

    // El MR tenía múltiples columnas (cronico, agudo). Lo normalizamos a una sola.
    @Column(name = "tipo_estres", length = 300)
    private String tipoEstres;

    public Integer getIdEstres() {
        return idEstres;
    }

    public void setIdEstres(Integer idEstres) {
        this.idEstres = idEstres;
    }

    public String getTipoEstres() {
        return tipoEstres;
    }

    public void setTipoEstres(String tipoEstres) {
        this.tipoEstres = tipoEstres;
    } 

    // ... Getters y Setters ...
}