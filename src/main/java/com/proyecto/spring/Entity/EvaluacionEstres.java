// src/main/java/com/proyecto/spring/Entity/EvaluacionEstres.java
package com.proyecto.spring.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evaluacion_estres")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionEstres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_cita", nullable = false, unique = true)
    private Cita cita;

    @Column(nullable = false)
    private Integer puntuacion;

    @Column(length = 1000)
    private String observaciones;

    @Column(name = "nivel_detectado", length = 20)
    private String nivelDetectado;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Se ejecuta antes de guardar
    @PrePersist
    public void calcularNivelYFecha() {
        this.createdAt = LocalDateTime.now();
        if (puntuacion != null) {
            if (puntuacion <= 33) {
                this.nivelDetectado = "BAJO";
            } else if (puntuacion <= 66) {
                this.nivelDetectado = "MEDIO";
            } else {
                this.nivelDetectado = "ALTO";
            }
        }
    }

    // Método para usar en el código viejo (getNivel)
    @Transient
    public String getNivel() {
        if (puntuacion == null) return "SIN_EVALUAR";
        if (puntuacion <= 33) return "BAJO";
        if (puntuacion <= 66) return "MEDIO";
        return "ALTO";
    }
}