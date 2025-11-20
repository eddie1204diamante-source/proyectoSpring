package com.proyecto.spring.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_rol")
public class UsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_usuario_rol")
    private Integer idUsuarioRol;

    @ManyToOne
    @JoinColumn(name = "Id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "Id_rol")
    private roles rol;

    public Integer getIdUsuarioRol() {
        return idUsuarioRol;
    }

    public void setIdUsuarioRol(Integer idUsuarioRol) {
        this.idUsuarioRol = idUsuarioRol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public roles getRol() {
        return rol;
    }

    public void setRol(roles rol) {
        this.rol = rol;
    }

}