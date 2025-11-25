package com.proyecto.spring.dto;

public class EstudianteDTO {
    private Integer idEstudiante;
    private String nombreCompleto;

    public EstudianteDTO(Integer idEstudiante, String nombreCompleto) {
        this.idEstudiante = idEstudiante;
        this.nombreCompleto = nombreCompleto;
    }

    public Integer getIdEstudiante() { return idEstudiante; }
    public String getNombreCompleto() { return nombreCompleto; }
}
