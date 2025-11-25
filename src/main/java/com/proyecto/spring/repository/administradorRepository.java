// src/main/java/com/proyecto/spring/repository/administradorRepository.java
package com.proyecto.spring.repository;

import com.proyecto.spring.Entity.administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface administradorRepository extends JpaRepository<administrador, Integer> {
}