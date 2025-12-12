package com.proyecto.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.spring.Entity.administrador;

@Repository
public interface administradorRepository extends JpaRepository<administrador, Integer> {

    // ✅ Cambiado: id_persona → Id_persona (como está en la entidad)
    @Query("SELECT a FROM administrador a WHERE a.persona.Id_persona = :idPersona")
    administrador findByPersonaId(@Param("idPersona") Integer idPersona);

}