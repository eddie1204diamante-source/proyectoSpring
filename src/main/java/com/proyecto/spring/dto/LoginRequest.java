// src/main/java/com/proyecto/spring/dto/LoginRequest.java
package com.proyecto.spring.dto;

public class LoginRequest {
    private String documento;
    private String contrasena;

    // Constructor vacío (Jackson lo necesita)
    public LoginRequest() {}

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}