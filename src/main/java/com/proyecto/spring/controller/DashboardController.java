package com.proyecto.spring.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proyecto.spring.config.UserDetailsServiceImpl;
import com.proyecto.spring.services.DashboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    
    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);
    
    private final DashboardService dashboardService;
    
    // ========================================================================
    // DASHBOARD APRENDIZ
    // ========================================================================
    
    @GetMapping("/estudiante/dashboard")
    public String dashboardAprendiz(Model model,
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        try {
            var usuario = userDetails.getUsuario();
            log.info("🎓 Aprendiz {} accediendo al dashboard", usuario.getIdUsuario());
            
            String nombreCompleto = usuario.getPersona() != null 
                    ? usuario.getPersona().getNombreCompleto() 
                    : "Aprendiz";
            
            model.addAttribute("nombreUsuario", nombreCompleto);
            model.addAttribute("tipoUsuario", "Aprendiz");
            
            return "estudiante/dashboard";
            
        } catch (Exception e) {
            log.error("❌ Error al cargar dashboard aprendiz: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el dashboard");
            return "error";
        }
    }
    
    // ========================================================================
    // DASHBOARD ORIENTADOR
    // ========================================================================
    
    @GetMapping("/orientador/dashboard")
    public String dashboardOrientador(Model model,
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        try {
            var usuario = userDetails.getUsuario();
            log.info("🧘‍♀️ Orientador {} accediendo al dashboard", usuario.getIdUsuario());
            
            String nombreCompleto = usuario.getPersona() != null 
                    ? usuario.getPersona().getNombreCompleto() 
                    : "Orientador";
            
            model.addAttribute("nombreUsuario", nombreCompleto);
            model.addAttribute("tipoUsuario", "Orientador");
            
            return "orientador/dashboard";
            
        } catch (Exception e) {
            log.error("❌ Error al cargar dashboard orientador: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el dashboard");
            return "error";
        }
    }
    
    // ========================================================================
    // API: ESTADÍSTICAS DEL DASHBOARD
    // ========================================================================
    
    @GetMapping("/api/dashboard/estadisticas")
    @ResponseBody
    public Map<String, Object> obtenerEstadisticas() {
        log.info("📊 Solicitando estadísticas del dashboard");
        return dashboardService.obtenerEstadisticas();
    }
    
    // ========================================================================
    // API: INFORMACIÓN DEL USUARIO ACTUAL
    // ========================================================================
    
    @GetMapping("/api/dashboard/usuario-actual")
    @ResponseBody
    public Map<String, Object> obtenerUsuarioActual(
            @AuthenticationPrincipal UserDetailsServiceImpl.UsuarioDetailsCustom userDetails) {
        
        try {
            var usuario = userDetails.getUsuario();
            log.info("👤 Solicitando info del usuario ID: {}", usuario.getIdUsuario());
            return dashboardService.obtenerInfoUsuario(usuario.getIdUsuario());
        } catch (Exception e) {
            log.error("❌ Error al obtener usuario actual: {}", e.getMessage(), e);
            return Map.of(
                "success", false,
                "error", e.getMessage(),
                "nombreCompleto", "Usuario",
                "tipoUsuario", "Usuario"
            );
        }
    }
}