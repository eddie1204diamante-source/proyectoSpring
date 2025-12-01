package com.proyecto.spring.dto;

public record usuarioDTO(
        Integer idUsuario,
        String nombreCompleto,
        String correo,
        Integer rolId,
        Integer personaId
) {}
