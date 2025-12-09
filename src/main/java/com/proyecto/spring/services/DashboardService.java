package com.proyecto.spring.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.proyecto.spring.repository.usuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);
    
    private final usuarioRepository usuarioRepository;
    
    /**
     * Obtener estadísticas generales del dashboard
     * @return Map con las estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Total de usuarios registrados
            long totalUsuarios = usuarioRepository.count();
            log.info("📊 Total usuarios: {}", totalUsuarios);
            
            // Total de aprendices (rol_id = 2)
            long totalAprendices = usuarioRepository.countByRolId(2);
            log.info("🎓 Total aprendices: {}", totalAprendices);
            
            // Total de orientadores (rol_id = 3)
            long totalOrientadores = usuarioRepository.countByRolId(3);
            log.info("🧘‍♀️ Total orientadores: {}", totalOrientadores);
            
            stats.put("totalUsuarios", totalUsuarios);
            stats.put("totalAprendices", totalAprendices);
            stats.put("totalOrientadores", totalOrientadores);
            stats.put("success", true);
            
        } catch (Exception e) {
            log.error("❌ Error al obtener estadísticas: {}", e.getMessage(), e);
            stats.put("success", false);
            stats.put("error", e.getMessage());
            stats.put("totalUsuarios", 0);
            stats.put("totalAprendices", 0);
            stats.put("totalOrientadores", 0);
        }
        
        return stats;
    }
    
    /**
     * Obtener información del usuario actual para el mensaje de bienvenida
     * @param idUsuario ID del usuario
     * @return Map con la información del usuario
     */
    public Map<String, Object> obtenerInfoUsuario(Integer idUsuario) {
        Map<String, Object> info = new HashMap<>();
        
        try {
            var usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            String nombreCompleto = usuario.getPersona() != null 
                    ? usuario.getPersona().getNombreCompleto() 
                    : "Usuario";
            
            String tipoUsuario = "Usuario";
            switch (usuario.getRolId()) {
                case 1:
                    tipoUsuario = "Administrador";
                    break;
                case 2:
                    tipoUsuario = "Aprendiz";
                    break;
                case 3:
                    tipoUsuario = "Orientador";
                    break;
            }
            
            info.put("nombreCompleto", nombreCompleto);
            info.put("tipoUsuario", tipoUsuario);
            info.put("rolId", usuario.getRolId());
            info.put("success", true);
            
            log.info("✅ Info usuario cargada: {} - {}", nombreCompleto, tipoUsuario);
            
        } catch (Exception e) {
            log.error("❌ Error al obtener info usuario: {}", e.getMessage(), e);
            info.put("success", false);
            info.put("error", e.getMessage());
            info.put("nombreCompleto", "Usuario");
            info.put("tipoUsuario", "Usuario");
        }
        
        return info;
    }
}