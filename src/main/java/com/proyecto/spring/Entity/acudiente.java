package com.proyecto.spring.Entity;

import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "acudiente")
public class acudiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id_acudiente;

    @Column(name = "parentesco", nullable = false)
    private String parentesco;

    @Size(min = 3, max = 16)
    @Column(name = "nombre_acudiente", length = 16, nullable = false)
    private String nombre_acudiente;

    @Size(min = 3, max = 16)
    @Column(name = "apellido_acudiente", length = 16, nullable = false)
    private String apellido_Acudiente;

    @Column(name = "telefono", length = 16, nullable = false)
    private String telefono; // ← Cambiado a String (teléfono no es int)

    @Column(name = "correo_electronico_acudiente", nullable = false)
    private String correo_electronico_acudiente;

    @Size(min = 5, max = 50)
    @Column(name = "direccion", length = 50, nullable = false)
    private String direccion; // ← Cambiado a String (dirección no es int)

    // Relación correcta: un acudiente pertenece a una persona
    @ManyToOne
    @JoinColumn(name = "Id_persona", nullable = false)
    private persona persona;

    // Getters y Setters
    public int getId_acudiente() { return Id_acudiente; }
    public void setId_acudiente(int id_acudiente) { Id_acudiente = id_acudiente; }
    public String getParentesco() { return parentesco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }
    public String getNombre_acudiente() { return nombre_acudiente; }
    public void setNombre_acudiente(String nombre_acudiente) { this.nombre_acudiente = nombre_acudiente; }
    public String getApellido_Acudiente() { return apellido_Acudiente; }
    public void setApellido_Acudiente(String apellido_Acudiente) { this.apellido_Acudiente = apellido_Acudiente; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo_electronico_acudiente() { return correo_electronico_acudiente; }
    public void setCorreo_electronico_acudiente(String correo_electronico_acudiente) { this.correo_electronico_acudiente = correo_electronico_acudiente; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public persona getPersona() { return persona; }
    public void setPersona(persona persona) { this.persona = persona; }
}