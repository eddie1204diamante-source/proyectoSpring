package com.proyecto.spring.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_rol")
    private int Id_rol;

    @Column(name = "rol", length = 11)
    private String rol;

    @ManyToOne
    @JoinColumn(name = "Id_usuario")
    private Usuario Usuario;

    public int getId_rol() {
        return Id_rol;
    }

    public void setId_rol(int id_rol) {
        Id_rol = id_rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.Usuario = usuario;
    }
}