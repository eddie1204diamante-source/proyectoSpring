package com.proyecto.spring.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "bienestar")
public class bienestar{

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id_bienestar;

@OneToMany
@JoinColumn(name = "Id_persona", nullable = false) 
private persona persona;

@OneToOne
@JoinColumn(name = "Id_estudiante", nullable = false)
private aprendiz aprendiz;

}
