package com.proyecto.spring.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long idCita;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private aprendiz aprendiz;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orientador", nullable = false)
    private psicologica orientador;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @Column(name = "hora_cita", nullable = false)
    private LocalTime horaCita;

    @Column(name = "motivo_original", length = 255, nullable = false)
    private String motivoOriginal;

    @Column(name = "motivo_clasificado", length = 100)
    private String motivoClasificado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getIdCita() { return idCita; }
    public void setIdCita(Long idCita) { this.idCita = idCita; }

    public aprendiz getAprendiz() { return aprendiz; }
    public void setAprendiz(aprendiz aprendiz) { this.aprendiz = aprendiz; }

    public psicologica getOrientador() { return orientador; }
    public void setOrientador(psicologica orientador) { this.orientador = orientador; }

    public LocalDate getFechaCita() { return fechaCita; }
    public void setFechaCita(LocalDate fechaCita) { this.fechaCita = fechaCita; }

    public LocalTime getHoraCita() { return horaCita; }
    public void setHoraCita(LocalTime horaCita) { this.horaCita = horaCita; }

    public String getMotivoOriginal() { return motivoOriginal; }
    public void setMotivoOriginal(String motivoOriginal) { this.motivoOriginal = motivoOriginal; }

    public String getMotivoClasificado() { return motivoClasificado; }
    public void setMotivoClasificado(String motivoClasificado) { this.motivoClasificado = motivoClasificado; }

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}