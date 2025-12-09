package com.proyecto.spring.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.spring.Entity.Cita;
import com.proyecto.spring.Entity.Usuario;
import com.proyecto.spring.Entity.persona;
import com.proyecto.spring.repository.personaRepository;
import com.proyecto.spring.repository.usuarioRepository;
import com.proyecto.spring.services.CitaService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private personaRepository personaRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    // ====================== CREAR CITA ======================
    @PostMapping("/crear")
    public ResponseEntity<?> crearCita(@RequestBody Map<String, Object> payload, HttpSession session) {
        try {
            Long idEstudiante = Long.valueOf(payload.get("idEstudiante").toString());
            Long idOrientador = Long.valueOf(payload.get("idOrientador").toString());
            LocalDate fecha = LocalDate.parse(payload.get("fecha").toString());
            LocalTime hora = LocalTime.parse(payload.get("hora").toString());
            String motivo = payload.get("motivo").toString();

            if (motivo.length() > 255) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El motivo no puede exceder 255 caracteres"));
            }

            Cita cita = citaService.crearCita(idEstudiante, idOrientador, fecha, hora, motivo);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cita creada exitosamente");
            response.put("cita", convertirCitaAMap(cita));
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    // ====================== ORIENTADORES DISPONIBLES ======================
    @GetMapping("/orientadores")
    public ResponseEntity<List<Map<String, Object>>> getOrientadores() {
        try {
            List<Map<String, Object>> orientadores = citaService.getOrientadores().stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", p.getIdOrientador());
                    String nombre = p.getPersona().getPNombre();
                    if (p.getPersona().getSNombre() != null && !p.getPersona().getSNombre().isEmpty()) {
                        nombre += " " + p.getPersona().getSNombre();
                    }
                    nombre += " " + p.getPersona().getPApellido();
                    if (p.getPersona().getSApellido() != null && !p.getPersona().getSApellido().isEmpty()) {
                        nombre += " " + p.getPersona().getSApellido();
                    }
                    map.put("nombreCompleto", nombre.trim());
                    return map;
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(orientadores);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ====================== VERIFICAR DISPONIBILIDAD ======================
    @GetMapping("/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @RequestParam Long orientador_id,
            @RequestParam String fecha,
            @RequestParam String hora) {
        try {
            LocalDate f = LocalDate.parse(fecha);
            LocalTime h = LocalTime.parse(hora);
            boolean disponible = citaService.verificarDisponibilidad(orientador_id, f, h);
            return ResponseEntity.ok(disponible);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(false);
        }
    }

    // ====================== CITAS DEL ESTUDIANTE ======================
    @GetMapping("/estudiante/{idUsuario}")
    public ResponseEntity<List<Map<String, Object>>> getCitasEstudiante(@PathVariable Long idUsuario) {
        try {
            List<Cita> citas = citaService.getCitasEstudiante(idUsuario);
            List<Map<String, Object>> citasMap = citas.stream()
                .map(this::convertirCitaAMap)
                .collect(Collectors.toList());
            return ResponseEntity.ok(citasMap);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ====================== CITAS DEL ORIENTADOR (CORREGIDO) ======================
    @GetMapping("/orientador/{idUsuario}")
    public ResponseEntity<List<Map<String, Object>>> getCitasOrientador(@PathVariable("idUsuario") Long idOrientador) {
        try {
            List<Cita> citas = citaService.getCitasOrientador(idOrientador); // ← idUsuario = id_orientador
            List<Map<String, Object>> citasMap = citas.stream()
                .map(cita -> {
                    Map<String, Object> map = convertirCitaAMap(cita);
                    String nombreEstudiante = obtenerNombreCompleto(
                        cita.getAprendiz().getUsuario().getPersona()
                    );
                    map.put("nombreEstudiante", nombreEstudiante);
                    return map;
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(citasMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // ====================== CITAS PENDIENTES DEL ORIENTADOR (CORREGIDO) ======================
    @GetMapping("/orientador/{idUsuario}/pendientes")
    public ResponseEntity<List<Map<String, Object>>> getCitasPendientesOrientador(
            @PathVariable("idUsuario") Long idOrientador) {
        try {
            List<Cita> citas = citaService.getCitasPendientesOrientador(idOrientador);
            List<Map<String, Object>> citasMap = citas.stream()
                .map(cita -> {
                    Map<String, Object> map = convertirCitaAMap(cita);
                    String nombreEstudiante = obtenerNombreCompleto(
                        cita.getAprendiz().getUsuario().getPersona()
                    );
                    map.put("nombreEstudiante", nombreEstudiante);
                    return map;
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(citasMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // ====================== APROBAR, REPROGRAMAR, CANCELAR, FINALIZAR ======================
    @PutMapping("/{idCita}/aprobar")
    public ResponseEntity<?> aprobarCita(@PathVariable Long idCita) {
        try {
            Cita cita = citaService.aprobarCita(idCita);
            return ResponseEntity.ok(Map.of(
                "message", "Cita aprobada exitosamente",
                "cita", convertirCitaAMap(cita)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{idCita}/reprogramar")
    public ResponseEntity<?> reprogramarCita(
            @PathVariable Long idCita,
            @RequestBody Map<String, Object> payload) {
        try {
            LocalDate nuevaFecha = LocalDate.parse(payload.get("fecha").toString());
            LocalTime nuevaHora = LocalTime.parse(payload.get("hora").toString());
            Cita cita = citaService.reprogramarCita(idCita, nuevaFecha, nuevaHora);
            return ResponseEntity.ok(Map.of(
                "message", "Cita reprogramada exitosamente",
                "cita", convertirCitaAMap(cita)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{idCita}/cancelar")
    public ResponseEntity<?> cancelarCita(@PathVariable Long idCita) {
        try {
            Cita cita = citaService.cancelarCita(idCita);
            return ResponseEntity.ok(Map.of(
                "message", "Cita cancelada exitosamente",
                "cita", convertirCitaAMap(cita)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{idCita}/finalizar")
    public ResponseEntity<?> finalizarCita(@PathVariable Long idCita) {
        try {
            Cita cita = citaService.finalizarCita(idCita);
            return ResponseEntity.ok(Map.of(
                "message", "Cita finalizada exitosamente",
                "cita", convertirCitaAMap(cita)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{idCita}")
public ResponseEntity<?> getDetalleCita(@PathVariable Long idCita) {
    try {
        Cita cita = citaService.getCitaPorId(idCita);
        Map<String, Object> detalle = convertirCitaAMap(cita);
        
        // IMPORTANTE: Agregar información del orientador y estudiante
        detalle.put("idOrientador", cita.getOrientador().getIdOrientador());
        detalle.put("idEstudiante", cita.getAprendiz().getIdEstudiante());
        
        detalle.put("nombreEstudiante", obtenerNombreCompleto(
            cita.getAprendiz().getUsuario().getPersona()
        ));
        detalle.put("nombreOrientador", obtenerNombreCompleto(
            cita.getOrientador().getPersona()
        ));
        
        return ResponseEntity.ok(detalle);
    } catch (RuntimeException e) {
        return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
    }
}
    // ====================== MÉTODOS AUXILIARES ======================
    private Map<String, Object> convertirCitaAMap(Cita cita) {
        Map<String, Object> map = new HashMap<>();
        map.put("idCita", cita.getIdCita());
        map.put("fechaCita", cita.getFechaCita().toString());
        map.put("horaCita", cita.getHoraCita().toString());
        map.put("motivoOriginal", cita.getMotivoOriginal());
        map.put("motivoClasificado", cita.getMotivoClasificado());
        map.put("estado", cita.getEstado().name());
        map.put("createdAt", cita.getCreatedAt() != null ? cita.getCreatedAt().toString() : null);
        map.put("updatedAt", cita.getUpdatedAt() != null ? cita.getUpdatedAt().toString() : null);
        return map;
    }

    private String obtenerNombreCompleto(persona persona) {
        String nombre = persona.getPNombre();
        if (persona.getSNombre() != null && !persona.getSNombre().isEmpty()) {
            nombre += " " + persona.getSNombre();
        }
        nombre += " " + persona.getPApellido();
        if (persona.getSApellido() != null && !persona.getSApellido().isEmpty()) {
            nombre += " " + persona.getSApellido();
        }
        return nombre.trim();
    }

    // ====================== /api/auth/me (MANTENIDO EN EL MISMO CONTROLADOR) ======================
    @GetMapping("/api/auth/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }
        try {
            String documento = principal.getName();
            persona persona = personaRepository.findByDocumento(documento)
                    .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
            Usuario usuario = usuarioRepository.findByPersonaDocumento(documento)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String nombreCompleto = obtenerNombreCompleto(persona);

            Map<String, Object> response = new HashMap<>();
            response.put("idUsuario", usuario.getIdUsuario());
            response.put("nombreCompleto", nombreCompleto);
            response.put("rol", usuario.getRolId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno: " + e.getMessage()));
        }
    }
}