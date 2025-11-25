package com.proyecto.spring.dto;

import com.proyecto.spring.Entity.Usuario;

public class UsuarioSelectDTO {
    private Integer idEstudiante;
    private String nombre;
    private String apellido;

    public UsuarioSelectDTO(Usuario u) {
        this.idEstudiante = u.getIdUsuario();
        if (u.getPersona() != null) {
            this.nombre = u.getPersona().getP_nombre();
            this.apellido = u.getPersona().getP_apellido();
        } else {
            this.nombre = "Desconocido";
            this.apellido = "";
        }
    }

    public Integer getIdEstudiante() { return idEstudiante; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
}
