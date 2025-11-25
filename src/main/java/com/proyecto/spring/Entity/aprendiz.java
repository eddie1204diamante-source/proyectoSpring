package com.proyecto.spring.Entity;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aprendiz")
<<<<<<< HEAD
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class aprendiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_estudiante")
    private int idEstudiante;
=======
public class Aprendiz {

    @Id
    @Column(name = "idEstudiante")
    private Integer idEstudiante;  // MISMO ID que el usuario

    @OneToOne
    @MapsId
    @JoinColumn(name = "Id_usuario_int", referencedColumnName = "Id_usuario")
    private Usuario usuario;
>>>>>>> 41314187b2acd41cab3eac745c9aed83b1c8bf31

    @Column(name = "tipo_problema", length = 100)
    private String tipo_problema;

    @Column(name = "trastorno", length = 10)
    private String trastorno;

    @Column(name = "Id_trastorno_int", length = 1)
    private String Id_trastorno_int;

    @OneToMany(mappedBy = "aprendiz")
    private Set<grupo_asignado> grupos_asignados;

<<<<<<< HEAD
    public int getId_estudiante() {
        return idEstudiante;
    }

    public void setId_estudiante(int id_estudiante) {
        idEstudiante = id_estudiante;
=======
    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
>>>>>>> 41314187b2acd41cab3eac745c9aed83b1c8bf31
    }

    public String getTipo_problema() {
        return tipo_problema;
    }

    public void setTipo_problema(String tipo_problema) {
        this.tipo_problema = tipo_problema;
    }

    public String getTrastorno() {
        return trastorno;
    }

    public void setTrastorno(String trastorno) {
        this.trastorno = trastorno;
    }

    public String getId_trastorno_int() {
        return Id_trastorno_int;
    }

    public void setId_trastorno_int(String id_trastorno_int) {
        Id_trastorno_int = id_trastorno_int;
    }

    public Set<grupo_asignado> getGrupos_asignados() {
        return grupos_asignados;
    }

    public void setGrupos_asignados(Set<grupo_asignado> grupos_asignados) {
        this.grupos_asignados = grupos_asignados;
    }
}
