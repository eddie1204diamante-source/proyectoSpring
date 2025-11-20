package com.proyecto.spring.Entity;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "recursos")
public class recursos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_recurso")
    private Integer idRecurso;

    @Column(name = "nombre_recurso", length = 100)
    private String nombreRecurso;

    @Column(name = "tipo_recurso", length = 100)
    private String tipoRecurso;

    @Column(name = "objetivo_recurso", length = 100)
    private String objetivoRecurso;

    @Column(name = "cargo_recurso", length = 60)
    private String cargoRecurso; // Mapea a 'cargo_recurso' en el diagrama

    @Column(name = "descripcion_recurso", length = 400)
    private String descripcionRecurso;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion_recurso")
    private Date fechaCreacionRecurso;

    @ManyToOne
    @JoinColumn(name = "Id_bienestar") 
    private bienestar bienestar; 

    public Integer getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Integer idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getNombreRecurso() {
        return nombreRecurso;
    }

    public void setNombreRecurso(String nombreRecurso) {
        this.nombreRecurso = nombreRecurso;
    }

    public String getTipoRecurso() {
        return tipoRecurso;
    }

    public void setTipoRecurso(String tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public Date getFechaCreacionRecurso() {
        return fechaCreacionRecurso;
    }

    public String getObjetivoRecurso() {
        return objetivoRecurso;
    }

    public void setObjetivoRecurso(String objetivoRecurso) {
        this.objetivoRecurso = objetivoRecurso;
    }

    public String getCargoRecurso() {
        return cargoRecurso;
    }

    public void setCargoRecurso(String cargoRecurso) {
        this.cargoRecurso = cargoRecurso;
    }

    public String getDescripcionRecurso() {
        return descripcionRecurso;
    }

    public void setDescripcionRecurso(String descripcionRecurso) {
        this.descripcionRecurso = descripcionRecurso;
    }

    public void setFechaCreacionRecurso(Date fechaCreacionRecurso) {
        this.fechaCreacionRecurso = fechaCreacionRecurso;
    }

    public bienestar getBienestar() { 
        return bienestar;
    }

    public void setBienestar(bienestar bienestar) {
        this.bienestar = bienestar;
    }
}