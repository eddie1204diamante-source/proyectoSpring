package com.proyecto.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.spring.Entity.psicologica;

@Repository
public interface psicologicaRepository extends JpaRepository<psicologica, Long> {

    // ✅ Cambiado: id_persona → Id_persona (como está en la entidad)
    @Query("SELECT p FROM psicologica p WHERE p.persona.Id_persona = :idPersona")
    psicologica findByPersonaId(@Param("idPersona") Integer idPersona);

    /**
     * Busca el registro de psicologica (orientador) a partir del id_usuario
     * Esto es necesario porque en la tabla citas guardamos id_orientador (de psicologica),
     * pero en el login/frontend manejamos id_usuario.
     */
    @Query("SELECT p FROM psicologica p " +
           "WHERE p.persona.Usuario.idUsuario = :idUsuario")
    Optional<psicologica> findByIdUsuario(@Param("idUsuario") Long idUsuario);
}