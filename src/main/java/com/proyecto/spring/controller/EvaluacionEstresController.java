package com.proyecto.spring.controller;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.spring.Entity.EvaluacionEstres;
import com.proyecto.spring.services.EvaluacionEstresService;
import com.proyecto.spring.services.EvaluacionEstresPdfService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EvaluacionEstresController {

    @Autowired
    private EvaluacionEstresService evaluacionEstresService;

    @Autowired
    private EvaluacionEstresPdfService evaluacionEstresPdfService;

    /**
     * Descargar PDF de una evaluación de estrés
     */
    @GetMapping("/evaluacion/{id}/pdf")
    public ResponseEntity<InputStreamResource> generarPdf(@PathVariable Long id) {

        try {
            log.info("📄 Solicitando PDF para evaluación ID {}", id);

            EvaluacionEstres evaluacion = evaluacionEstresService.getById(id);

            if (evaluacion == null) {
                log.warn("⚠ No se encontró la evaluación con ID {}", id);
                return ResponseEntity.notFound().build();
            }

            ByteArrayInputStream pdfStream = evaluacionEstresPdfService.generarPdf(evaluacion);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=evaluacion_" + id + ".pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));

        } catch (Exception e) {
            log.error("❌ Error generando PDF: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
