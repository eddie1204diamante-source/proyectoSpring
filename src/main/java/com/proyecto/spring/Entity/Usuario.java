package com.proyecto.spring.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_usuario")
    private Integer idUsuario;

    @OneToOne
    @JoinColumn(name = "Id_persona", nullable = false)
    private persona persona;

    @Column(name = "correo", length = 50, unique = true)
    private String correo;

    @Column(name = "contrasena", length = 100)
    private String contrasena;

    @Column(name = "rol_id", length = 2, nullable = false)
    private int rol_id;

    // === GETTERS Y SETTERS CORRECTOS ===
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public persona getPersona() {
        return persona;
    }

    public void setPersona(persona persona) {
        this.persona = persona;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getRol_id() {
        return rol_id;
    }

    public void setRol_id(int rol_id) {
        this.rol_id = rol_id;
    }
}