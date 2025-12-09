    package com.proyecto.spring.Entity;
    import java.util.Set;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Entity
    @Table(name = "aprendiz")

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class aprendiz {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "idEstudiante")
        private Integer idEstudiante;  // ✅ PK independiente

        @OneToOne
        @JoinColumn(name = "Id_usuario_int", referencedColumnName = "Id_usuario", nullable = false, unique = true)
        private Usuario usuario;  // ✅ FK normal, sin @MapsId


        @Column(name = "tipo_problema", length = 100)
        private String tipo_problema;

        @Column(name = "trastorno", length = 10)
        private String trastorno;

        @Column(name = "Id_trastorno_int", length = 1)
        private String Id_trastorno_int;

        @OneToMany(mappedBy = "aprendiz")
        private Set<grupo_asignado> grupos_asignados;

        

        public int getId_estudiante() {
            return idEstudiante;
        }

        public void setId_estudiante(int id_estudiante) {
            idEstudiante = id_estudiante;

        }

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
