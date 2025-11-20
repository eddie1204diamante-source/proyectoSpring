package com.proyecto.spring.Entity;

import javax.validation.constraints.*;
import jakarta.persistence.*;

/*
 * Observaciones para esta tabla.
 * 
 * - se agregan campos nuevos, desde nombre hasta dirección.
 * - Se debe actualizar el MLR
 * - Todos las columnas comentadas deben agregarse en MLR
 */

@Entity
@Table(name = "acudiente")
public class acudiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id_acudiente;

    @Column(name = "parentesco", nullable = false)
    private String parentesco;

    @Max(value = 16)
    @Min(value = 3)
    @Column(name = "nombre_acudiente", length = 16, nullable = false)
    private String nombre_acudiente; //El nombre acudiente toca agregarlo en el modelo lógico relacional de BD

    @Max(value = 16)
    @Min(value = 3)
    @Column(name = "apellido_acudiente", length = 16, nullable = false)
    private String apellido_Acudiente; //El nombre acudiente toca agregarlo en el modelo lógico relacional de BD

    @Max(value = 10)
    @Min(value = 3)
    @Column(name = "telefono", length = 16, nullable = false)
    private int telefono; //El nombre acudiente toca agregarlo en el modelo lógico relacional de BD

    @Column(name= "correo_electronico_acudiente", nullable = false)
    private String correo_electronico_acudiente; //El nombre acudiente toca agregarlo en el modelo lógico relacional de BD

    @Max(value = 50)
    @Min(value = 5)
    @Column(name = "direccion", length = 50, nullable = false)
    private int direccion; //El nombre acudiente toca agregarlo en el modelo lógico relacional de BD

    @OneToMany
    @JoinColumn(name = "Id_persona", nullable = false)
    private persona persona; //El nombre acudiente toca agregarlo en el modelo lógico relacional de BD


}