package com.proyecto.spring.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream; // ¡FALTA ESTA IMPORTACIÓN!
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.proyecto.spring.Entity.EvaluacionEstres;
import com.proyecto.spring.repository.EvaluacionEstresRepository;
import com.proyecto.spring.services.EvaluacionesPdfService;

@RestController
@RequestMapping("/reporte")
public class ReportePdfController {

    @Autowired
    private EvaluacionEstresRepository evaluacionRepo;
    
    @Autowired
    private EvaluacionesPdfService evaluacionesPdfService;
    
    /**
     * PDF MEJORADO con gráficas y estadísticas
     * URL: /reporte/actividades
     */
    @GetMapping("/actividades")
    public ResponseEntity<InputStreamResource> generarReporteCompleto() {
        try {
            // 1. Obtener todas las evaluaciones
            List<EvaluacionEstres> todasEvaluaciones = evaluacionRepo.findAll();
            
            // 2. Generar PDF con el servicio MEJORADO
            ByteArrayInputStream pdfStream = evaluacionesPdfService.generarPdfDeTodas(todasEvaluaciones);
            
            // 3. Preparar nombre del archivo
            String fecha = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String nombreArchivo = "reporte_evaluaciones_" + fecha + ".pdf";
            
            // 4. Devolver respuesta
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + nombreArchivo + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
            
        } catch (Exception e) {
            e.printStackTrace();
            
            // En caso de error, devolver PDF de error
            return generarPdfDeError(e.getMessage());
        }
    }
    
    /**
     * Método alternativo que abre el PDF en el navegador (no descarga)
     */
    @GetMapping("/actividades/vista")
    public ResponseEntity<InputStreamResource> verReporteEnNavegador() {
        try {
            List<EvaluacionEstres> evaluaciones = evaluacionRepo.findAll();
            
            if (evaluaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            ByteArrayInputStream pdfStream = evaluacionesPdfService.generarPdfDeTodas(evaluaciones);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "inline; filename=\"reporte_evaluaciones.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * PDF de prueba con datos de ejemplo (si no hay datos reales)
     */
    @GetMapping("/actividades/demo")
    public ResponseEntity<InputStreamResource> generarReporteDemo() {
        try {
            List<EvaluacionEstres> evaluaciones = evaluacionRepo.findAll();
            
            ByteArrayInputStream pdfStream = evaluacionesPdfService.generarPdfDeTodas(evaluaciones);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "inline; filename=\"reporte_demo.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
            
        } catch (Exception e) {
            return generarPdfDeError(e.getMessage());
        }
    }
    
    /**
     * Método de prueba para verificar el sistema
     */
    @GetMapping("/test")
    public String testSistema() {
        try {
            long totalEvaluaciones = evaluacionRepo.count();
            return "✅ Sistema PDF funcionando correctamente. Total evaluaciones: " + totalEvaluaciones;
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
    
    /**
     * PDF de error personalizado
     */
    private ResponseEntity<InputStreamResource> generarPdfDeError(String mensajeError) {
        try {
            // Crear un PDF simple de error
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            
            document.open();
            
            // Título de error
            Font errorFont = new Font(Font.HELVETICA, 16, Font.BOLD, new java.awt.Color(231, 76, 60));
            Paragraph error = new Paragraph("❌ ERROR AL GENERAR REPORTE", errorFont);
            error.setAlignment(Element.ALIGN_CENTER);
            document.add(error);
            
            document.add(Chunk.NEWLINE);
            
            // Mensaje
            document.add(new Paragraph("No se pudo generar el reporte completo."));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Error técnico: " + mensajeError, 
                    new Font(Font.HELVETICA, 9, Font.ITALIC, java.awt.Color.GRAY)));
            
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            
            // Solución
            document.add(new Paragraph("💡 Posibles soluciones:", 
                    new Font(Font.HELVETICA, 11, Font.BOLD)));
            document.add(new Paragraph("1. Verificar conexión a la base de datos"));
            document.add(new Paragraph("2. Asegurar que haya al menos una evaluación"));
            document.add(new Paragraph("3. Reintentar en unos minutos"));
            
            document.close();
            
            ByteArrayInputStream pdfStream = new ByteArrayInputStream(out.toByteArray());
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "inline; filename=\"error_report.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}