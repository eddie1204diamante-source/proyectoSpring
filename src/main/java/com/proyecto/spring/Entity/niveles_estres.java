package com.proyecto.spring.Entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/*
 * Hacer en la base de datos una nueva tabla llamada "Escalas" para hacer la medicion de que tan urgente es llevar el proceso.
 * hacer trigger de escalas para que se calcule automaticamente
 */

@Entity
@Table(name = "niveles_Estres")
public class niveles_estres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_niveles_estres;

    @Min(value= 3)
    @Max(value = 20)
    @Column(name = "nombre_estres", length=20, nullable = false)
    private String nombre_estres;

    @OneToMany
    @JoinColumn(name = "Id_escalas", nullable = false)
    private Escalas escalas;

    @Column(name = "descripcion", length=800, nullable= false)
    private String descripcion;
}
