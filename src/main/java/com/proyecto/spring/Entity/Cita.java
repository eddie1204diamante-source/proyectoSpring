package com.proyecto.spring.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_aprendiz")
    private aprendiz aprendiz;

    @ManyToOne
    @JoinColumn(name = "id_psicologica")
    private psicologica psicologica;

    private LocalDateTime fecha;

    private String motivo;

    private String estado; // Pendiente / Aprobada / Cancelada

    // GETTERS Y SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public aprendiz getAprendiz() { return aprendiz; }
    public void setAprendiz(aprendiz aprendiz) { this.aprendiz = aprendiz; }

    public psicologica getpsicologica() { return psicologica; }
    public void setpsicologica(psicologica psicologica) { this.psicologica = psicologica; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
