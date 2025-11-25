package com.proyecto.spring.controller;

import com.proyecto.spring.services.ReporteActividadesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReporteController {

    private static final Logger logger = LoggerFactory.getLogger(ReporteController.class);

    private final ReporteActividadesService reporteActividadesService;

    public ReporteController(ReporteActividadesService reporteActividadesService) {
        this.reporteActividadesService = reporteActividadesService;
    }

    @GetMapping("/reporte/actividades")
    public ResponseEntity<byte[]> descargarReporteActividades() {
        try {
            byte[] pdf = reporteActividadesService.generarReporteActividades();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                                  .filename("reporte_actividades.pdf")
                                  .build()
            );

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al generar el reporte de actividades", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
