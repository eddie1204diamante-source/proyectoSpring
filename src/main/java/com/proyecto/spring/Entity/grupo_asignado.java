package com.proyecto.spring.Entity;

import jakarta.persistence.*;
// import java.util.Set; // No necesario para esta clase

@Entity
@Table(name = "grupo_asignado")
public class grupo_asignado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_grupo_asignado")
    private int Id_grupo_asignado;

    @ManyToOne
    @JoinColumn(name = "Id_usuario")
    private Usuario usuario; // <-- CAMBIO: 'usuario' a 'Usuario'

    @ManyToOne
    @JoinColumn(name = "Id_estudiante")
    private aprendiz aprendiz;

    // ... (getters y setters de Id_grupo_asignado)

    public Usuario getUsuario() { // <-- CAMBIO: 'usuario' a 'Usuario'
        return usuario;
    }

    public void setUsuario(Usuario usuario) { // <-- CAMBIO: 'usuario' a 'Usuario'
        this.usuario = usuario;
    }
    
    // Y harías lo mismo para aprendiz si estuviera con minúscula
    public aprendiz getAprendiz() {
        return aprendiz;
    }

    public void setAprendiz(aprendiz aprendiz) {
        this.aprendiz = aprendiz;
    }

    public int getId_grupo_asignado() {
        return Id_grupo_asignado;
    }

    public void setId_grupo_asignado(int id_grupo_asignado) {
        Id_grupo_asignado = id_grupo_asignado;
    }
}