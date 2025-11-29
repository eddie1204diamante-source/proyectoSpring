package com.proyecto.spring.repository;


import com.proyecto.spring.Entity.aprendiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface aprendizRepository extends JpaRepository<aprendiz, Integer> {

    // Busca el aprendiz por el id_usuario (clave en la sesión)
    Optional<aprendiz> findByUsuarioIdUsuario(Long idUsuario);

    // Versión más corta (la que usamos en el service)
    default Optional<aprendiz> findByIdUsuario(Long idUsuario) {
        return findByUsuarioIdUsuario(idUsuario);
    }
}

