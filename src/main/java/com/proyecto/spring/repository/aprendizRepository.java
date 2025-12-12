package com.proyecto.spring.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.spring.Entity.aprendiz;

@Repository
public interface aprendizRepository extends JpaRepository<aprendiz, Integer> {
    aprendiz findByUsuario_IdUsuario(Integer idUsuario);

    @Query("SELECT a FROM aprendiz a WHERE a.usuario.idUsuario = :idUsuario")
    aprendiz findByUsuarioId(@Param("idUsuario") Integer idUsuario);


    // Busca el aprendiz por el id_usuario (clave en la sesión)
    Optional<aprendiz> findByUsuarioIdUsuario(Long idUsuario);

    // Versión más corta (la que usamos en el service)
    default Optional<aprendiz> findByIdUsuario(Long idUsuario) {
        return findByUsuarioIdUsuario(idUsuario);
    }

    
}

