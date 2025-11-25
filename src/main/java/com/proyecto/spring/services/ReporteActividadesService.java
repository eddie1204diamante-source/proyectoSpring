package com.proyecto.spring.services;

import com.proyecto.spring.Entity.Actividad;
import com.proyecto.spring.repository.ActividadRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteActividadesService {

    @Autowired
    private ActividadRepository actividadRepository;

    public byte[] generarReporteActividades() throws Exception {
        List<Actividad> actividades = actividadRepository.findAll();

        InputStream reporteStream = getClass().getResourceAsStream("/reports/reporteActividades.jrxml");
        if (reporteStream == null) {
            throw new RuntimeException("No se encontró el archivo reporteActividades.jrxml");
        }
        
        JasperReport jasperReport = JasperCompileManager.compileReport(reporteStream);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("titulo", "Reporte de Actividades");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(actividades);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
        
    }
}
