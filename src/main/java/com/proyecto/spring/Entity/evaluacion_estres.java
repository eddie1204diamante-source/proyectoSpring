package com.proyecto.spring.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "evaluacion_estres")
public class evaluacion_estres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_evaluacion_estres")
    private int Id_evaluacion_estres;

    @Column(name = "duracion", length = 100)
    private String duracion;

    @Column(name = "valoracion")
    private int valoracion;

    @Column(name = "dificultad")
    private int dificultad;

    @Column(name = "objetivo", length = 100)
    private String objetivo;

    @Column(name = "estudiante_Id_estudiante_int", length = 1)
    private String estudiante_Id_estudiante_int;
    @ManyToOne
    @JoinColumn(name = "Id_estudiante")
    private Aprendiz aprendiz;

    //@ManyToOne
    //@JoinColumn(name = "Id_niveles_estres")
    //private niveles_estres niveles_estres;

  
    @ManyToOne 
    @JoinColumn(name = "Id_escala")
    private Escalas escala; 

    // --- GETTERS & SETTERS ---

    public int getId_evaluacion_estres() {
        return Id_evaluacion_estres;
    }

    public void setId_evaluacion_estres(int id_evaluacion_estres) {
        Id_evaluacion_estres = id_evaluacion_estres;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getEstudiante_Id_estudiante_int() {
        return estudiante_Id_estudiante_int;
    }

    public void setEstudiante_Id_estudiante_int(String estudiante_Id_estudiante_int) {
        this.estudiante_Id_estudiante_int = estudiante_Id_estudiante_int;
    }

    public Aprendiz getAprendiz() {
        return aprendiz;
    }

    public void setAprendiz(Aprendiz aprendiz) {
        this.aprendiz = aprendiz;
    }

    //public niveles_estres getNiveles_estres() {
    //    return niveles_estres;
    //}

    //public void setNiveles_estres(niveles_estres niveles_estres) {
    //    this.niveles_estres = niveles_estres;
    //}
    
    // GETTERS & SETTERS para la relación 'escala'
    public Escalas getEscala() { 
        return escala;
    }

    public void setEscala(Escalas escala) { 
        this.escala = escala;
    }
}