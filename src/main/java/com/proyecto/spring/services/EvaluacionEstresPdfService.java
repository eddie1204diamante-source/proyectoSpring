package com.proyecto.spring.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.proyecto.spring.Entity.EvaluacionEstres;

@Service
public class EvaluacionEstresPdfService {

    public ByteArrayInputStream generarPdf(EvaluacionEstres evaluacion) {

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // TÍTULO
            Font tituloFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph titulo = new Paragraph("Reporte de Evaluación de Estrés", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // INFORMACIÓN
            Font textoFont = new Font(Font.HELVETICA, 12);

            document.add(new Paragraph(
                    "ID Evaluación: " + evaluacion.getId(), textoFont));
            document.add(new Paragraph(
                    "ID Cita: " + evaluacion.getCita().getIdCita(), textoFont));
            document.add(new Paragraph(
                    "Fecha: " + evaluacion.getCreatedAt(), textoFont));
            document.add(new Paragraph(
                    "Puntuación: " + evaluacion.getPuntuacion(), textoFont));
            document.add(new Paragraph(
                    "Nivel Detectado: " + evaluacion.getNivelDetectado(), textoFont));
            document.add(Chunk.NEWLINE);

            // OBSERVACIONES
            Paragraph obsTitle = new Paragraph("Observaciones:", new Font(Font.HELVETICA, 14, Font.BOLD));
            obsTitle.setSpacingAfter(5);
            document.add(obsTitle);

            Paragraph obs = new Paragraph(
                    evaluacion.getObservaciones() != null ? evaluacion.getObservaciones() : "Sin observaciones",
                    textoFont
            );
            obs.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(obs);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
