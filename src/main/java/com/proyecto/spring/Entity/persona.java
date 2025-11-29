package com.proyecto.spring.Entity;

import jakarta.persistence.*;

import java.util.Set;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "persona")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_persona")
    private int Id_persona;

    @Column(name = "p_nombre", length = 16)
    private String p_nombre;

    @Column(name = "s_nombre", length = 16)
    private String s_nombre;

    @Column(name = "p_apellido", length = 16)
    private String p_apellido;

    @Column(name = "s_apellido", length = 16)
    private String s_apellido;

    @Min(value = 10)
    @Max(value = 10)
    @Column(name = "documento", length = 10, unique = true)
    private String documento;

    @Column(name = "edad")
    private int edad;

    @Column(name = "contrasena", length = 100)
    private String contrasena; // Asumiendo que "contraseña" se mapea a "contrasena" en Java

    // Método útil para frontend
    public String getNombreCompleto() {
        return (p_nombre + " " + 
                (s_nombre != null ? s_nombre + " " : "") +
                p_apellido + " " + 
                (s_apellido != null ? s_apellido : "")).trim();
    }

    // Relaciones
    @OneToOne(mappedBy = "persona")
    private Usuario Usuario;

    @OneToOne(mappedBy = "persona")
    private psicologica psicologica;

    @OneToOne(mappedBy = "persona")
    private administrador administrador;

    @OneToMany(mappedBy = "persona")
    private Set<acudiente> acudientes;

    @OneToMany(mappedBy = "persona")
    private Set<ocupaciones> ocupaciones;

    public int getId_persona() {
        return Id_persona;
    }

    public void setId_persona(int id_persona) {
        Id_persona = id_persona;
    }
    
    public String getPNombre() {  // Cambiar de getP_nombre() a getPNombre()
        return p_nombre;
    }
    
    public void setPNombre(String p_nombre) {  // Cambiar de setP_nombre() a setPNombre()
        this.p_nombre = p_nombre;
    }
    
    public String getSNombre() {  // Cambiar de getS_nombre() a getSNombre()
        return s_nombre;
    }
    
    public void setSNombre(String s_nombre) {  // Cambiar de setS_nombre() a setSNombre()
        this.s_nombre = s_nombre;
    }
    
    public String getPApellido() {  // Cambiar de getP_apellido() a getPApellido()
        return p_apellido;
    }
    
    public void setPApellido(String p_apellido) {  // Cambiar de setP_apellido() a setPApellido()
        this.p_apellido = p_apellido;
    }
    
    public String getSApellido() {  // Cambiar de getS_apellido() a getSApellido()
        return s_apellido;
    }
    
    public void setSApellido(String s_apellido) {  // Cambiar de setS_apellido() a setSApellido()
        this.s_apellido = s_apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(Usuario Usuario) {
        this.Usuario = Usuario;
    }


    public psicologica getPsicologica() {
        return psicologica;
    }

    public void setPsicologica(psicologica psicologica) {
        this.psicologica = psicologica;
    }

    public administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(administrador administrador) {
        this.administrador = administrador;
    }

    public Set<acudiente> getAcudientes() {
        return acudientes;
    }

    public void setAcudientes(Set<acudiente> acudientes) {
        this.acudientes = acudientes;
    }

    public Set<ocupaciones> getOcupaciones() {
        return ocupaciones;
    }

    public void setOcupaciones(Set<ocupaciones> ocupaciones) {
        this.ocupaciones = ocupaciones;
    }


}