package com.proyecto.spring.repository;

<<<<<<< HEAD
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
=======
import com.proyecto.spring.Entity.Aprendiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface aprendizRepository extends JpaRepository<Aprendiz, Integer> {

}
>>>>>>> 41314187b2acd41cab3eac745c9aed83b1c8bf31
