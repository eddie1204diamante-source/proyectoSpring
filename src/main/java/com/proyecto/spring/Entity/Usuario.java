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

    @Column(name = "username", length = 50, unique = true) // Campo añadido para login
    private String username;

    @Column(name = "contrasena", length = 100) // Contraseña movida de Persona a Usuario
    private String contrasena; 

    // Relación Many-to-Many con Roles (usando la tabla de unión UsuarioRol)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UsuarioRol> roles;

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
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Set<UsuarioRol> getRoles() {
        return roles;
    }

    public void setRoles(Set<UsuarioRol> roles) {
        this.roles = roles;
    }
    
    // ... Getters y Setters ...
}