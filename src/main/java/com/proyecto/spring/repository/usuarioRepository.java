package com.proyecto.spring.repository;

import com.proyecto.spring.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface usuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByCorreo(String correo);

    // Buscar usuario por correo
    Optional<Usuario> findByCorreo(String correo);

    

    Optional<Usuario> findByPersonaDocumento(String documento);


     List<Usuario> findByRolId(int RolId);

     // ========================================================================
    // MÉTODOS PARA ESTADÍSTICAS DEL DASHBOARD
    // ========================================================================
    
    /**
     * Contar usuarios por rol
     * @param rolId ID del rol (1=Admin, 2=Aprendiz, 3=Orientador)
     * @return Cantidad de usuarios con ese rol
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rolId = :rolId")
    long countByRolId(@Param("rolId") int rolId);
    
    /**
     * Contar total de usuarios en el sistema
     * @return Total de usuarios
     */
    @Query("SELECT COUNT(u) FROM Usuario u")
    long countTotalUsuarios();


}
