package com.proyecto.spring.Entity;

import jakarta.persistence.*;

import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "persona")
public class persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_persona")
    private int Id_persona;

    @Column(name = "p_nombre", length = 16)
    private String p_nombre;

    @Column(name = "s_nombre", length = 16)
    private String s_nombre;

    @Column(name = "p_apellido", length = 16)
    private String p_apellido;

    @Column(name = "s_apellido", length = 16)
    private String s_apellido;

    @Min(value = 10)
    @Max(value = 10)
    @Column(name = "documento", length = 10, unique = true)
    private String documento;

    @Column(name = "edad")
    private int edad;

    @Column(name = "contrasena", length = 100)
    private String contrasena; // Asumiendo que "contraseña" se mapea a "contrasena" en Java

    // Relaciones
    @OneToOne(mappedBy = "persona")
    private Usuario Usuario;

    @OneToOne(mappedBy = "persona")
    private bienestar bienestar;

    @OneToOne(mappedBy = "persona")
    private psicologica psicologica;

    @OneToOne(mappedBy = "persona")
    private administrador administrador;

    @OneToMany(mappedBy = "persona")
    private Set<acudiente> acudientes;

    @OneToMany(mappedBy = "persona")
    private Set<ocupaciones> ocupaciones;

    public int getId_persona() {
        return Id_persona;
    }

    public void setId_persona(int id_persona) {
        Id_persona = id_persona;
    }

    public String getP_nombre() {
        return p_nombre;
    }

    public void setP_nombre(String p_nombre) {
        this.p_nombre = p_nombre;
    }

    public String getS_nombre() {
        return s_nombre;
    }

    public void setS_nombre(String s_nombre) {
        this.s_nombre = s_nombre;
    }

    public String getP_apellido() {
        return p_apellido;
    }

    public void setP_apellido(String p_apellido) {
        this.p_apellido = p_apellido;
    }

    public String getS_apellido() {
        return s_apellido;
    }

    public void setS_apellido(String s_apellido) {
        this.s_apellido = s_apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(Usuario Usuario) {
        this.Usuario = Usuario;
    }

    public bienestar getBienestar() {
        return bienestar;
    }

    public void setBienestar(bienestar bienestar) {
        this.bienestar = bienestar;
    }

    public psicologica getPsicologica() {
        return psicologica;
    }

    public void setPsicologica(psicologica psicologica) {
        this.psicologica = psicologica;
    }

    public administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(administrador administrador) {
        this.administrador = administrador;
    }

    public Set<acudiente> getAcudientes() {
        return acudientes;
    }

    public void setAcudientes(Set<acudiente> acudientes) {
        this.acudientes = acudientes;
    }

    public Set<ocupaciones> getOcupaciones() {
        return ocupaciones;
    }

    public void setOcupaciones(Set<ocupaciones> ocupaciones) {
        this.ocupaciones = ocupaciones;
    }


}