package com.proyecto.spring.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "escalas")
public class Escalas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_escala")
    private int Id_escala;

    @Column(name = "nombre_escala", length = 100)
    private String nombre_escala;

    @Column(name = "nivel_urgencia", length = 50)
    private String nivel_urgencia; 

    @Column(name = "puntuacion_minima")
    private int puntuacion_minima;

    @Column(name = "puntuacion_maxima")
    private int puntuacion_maxima;

    @Column(name = "descripcion_criterio", length = 500)
    private String descripcion_criterio;

    // Relación
    //@OneToMany(mappedBy = "escala")
    //private Set<evaluacion_estres> evaluaciones_estres;

    public int getId_escala() {
        return Id_escala;
    }

    public void setId_escala(int id_escala) {
        Id_escala = id_escala;
    }

    public String getNombre_escala() {
        return nombre_escala;
    }

    public void setNombre_escala(String nombre_escala) {
        this.nombre_escala = nombre_escala;
    }

    public String getNivel_urgencia() {
        return nivel_urgencia;
    }

    public void setNivel_urgencia(String nivel_urgencia) {
        this.nivel_urgencia = nivel_urgencia;
    }

    public int getPuntuacion_minima() {
        return puntuacion_minima;
    }

    public void setPuntuacion_minima(int puntuacion_minima) {
        this.puntuacion_minima = puntuacion_minima;
    }

    public int getPuntuacion_maxima() {
        return puntuacion_maxima;
    }

    public void setPuntuacion_maxima(int puntuacion_maxima) {
        this.puntuacion_maxima = puntuacion_maxima;
    }

    public String getDescripcion_criterio() {
        return descripcion_criterio;
    }

    public void setDescripcion_criterio(String descripcion_criterio) {
        this.descripcion_criterio = descripcion_criterio;
    }

    //public Set<evaluacion_estres> getEvaluaciones_estres() {
    //    return evaluaciones_estres;
    //}
    //
    //public void setEvaluaciones_estres(Set<evaluacion_estres> evaluaciones_estres) {
    //    this.evaluaciones_estres = evaluaciones_estres;
    //}
}