package com.proyecto.spring.Entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_usuario")
    private Integer idUsuario;

    // Clave Foránea a Persona (OneToOne)
    @OneToOne
    @JoinColumn(name = "Id_persona", nullable = false)
    private persona persona;

    @Column(name = "correo", length = 50, unique = true) // correo esta pero solo para usarlo en futuros modulos
    private String correo;

    @Column(name = "contrasena", length = 100) // Contraseña movida de Persona a Usuario
    private String contrasena; 

    @Column(name = "rol_id", length = 2, nullable= false)
    private int rol_id;

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

    public String getUsername() {
        return correo;
    }

    public int getRol_id() {
    return rol_id;
    }
    
    public void setRol_id(int rol_id) {
        this.rol_id = rol_id;
    }

    public void setUsername(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}