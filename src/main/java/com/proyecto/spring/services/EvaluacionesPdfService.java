package com.proyecto.spring.services;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.proyecto.spring.Entity.EvaluacionEstres;

@Service
public class EvaluacionesPdfService {

    private static final Color COLOR_SENA = new Color(0, 102, 0);
    private static final Color COLOR_BAJO = new Color(46, 204, 113);
    private static final Color COLOR_MEDIO = new Color(241, 196, 15);
    private static final Color COLOR_ALTO = new Color(231, 76, 60);
    private static final Color COLOR_FONDO = new Color(248, 249, 250);
    private static final Color COLOR_TEXTO = new Color(52, 73, 94);

    public ByteArrayInputStream generarPdfDeTodas(List<EvaluacionEstres> evaluaciones) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPageEvent(new PdfHeaderFooter());
            document.open();

            // PORTADA
            agregarPortada(document);

            // RESUMEN EJECUTIVO
            agregarResumenEjecutivo(document, evaluaciones);

            if (!evaluaciones.isEmpty()) {
                // GRÁFICAS PRINCIPALES
                agregarGraficasPrincipales(document, evaluaciones);
                
                document.newPage();
                
                // TABLA DETALLADA
                agregarTablaDetallada(document, evaluaciones);
                
                // ESTADÍSTICAS Y ANÁLISIS
                agregarEstadisticasAvanzadas(document, evaluaciones);
                
                // GRÁFICAS ADICIONALES
                if (evaluaciones.size() > 3) {
                    agregarGraficasAdicionales(document, evaluaciones);
                }
                
                // RECOMENDACIONES
                agregarRecomendacionesCompletas(document, evaluaciones);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void agregarPortada(Document document) throws Exception {
        // Logo/título
        try {
            Image logo = Image.getInstance(getClass().getResource("/static/img/logo.png"));
            logo.scaleToFit(180, 90);
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            Paragraph logoTxt = new Paragraph("🧠 MindWell", 
                new Font(Font.HELVETICA, 28, Font.BOLD, COLOR_SENA));
            logoTxt.setAlignment(Element.ALIGN_CENTER);
            document.add(logoTxt);
        }
        
        // Título principal
        Paragraph titulo = new Paragraph("REPORTE DE ANÁLISIS DE ESTRÉS", 
            new Font(Font.HELVETICA, 20, Font.BOLD, COLOR_TEXTO));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingBefore(10);
        document.add(titulo);
        
        // Línea decorativa
        PdfPTable linea = new PdfPTable(1);
        linea.setWidthPercentage(40);
        linea.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celdaLinea = new PdfPCell();
        celdaLinea.setFixedHeight(3);
        celdaLinea.setBackgroundColor(COLOR_SENA);
        celdaLinea.setBorder(Rectangle.NO_BORDER);
        linea.addCell(celdaLinea);
        document.add(linea);
        
        // Información
        Paragraph info = new Paragraph(
            "Sistema de Seguimiento Psicológico\n" +
            "Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY));
        info.setAlignment(Element.ALIGN_CENTER);
        info.setSpacingBefore(20);
        document.add(info);
        
        document.add(Chunk.NEWLINE);
    }

    private void agregarResumenEjecutivo(Document document, List<EvaluacionEstres> evaluaciones) throws Exception {
        Paragraph seccion = new Paragraph("📊 RESUMEN EJECUTIVO", 
            new Font(Font.HELVETICA, 16, Font.BOLD, COLOR_TEXTO));
        seccion.setSpacingBefore(20);
        document.add(seccion);

        if (evaluaciones.isEmpty()) {
            document.add(new Paragraph("No hay evaluaciones registradas en el sistema.", 
                new Font(Font.HELVETICA, 12, Font.ITALIC, Color.GRAY)));
            return;
        }

        PdfPTable metricas = new PdfPTable(5);
        metricas.setWidthPercentage(100);
        metricas.setSpacingBefore(15);

        metricas.addCell(crearTarjetaMetrica(evaluaciones.size() + "", "TOTAL", "📋", COLOR_SENA));
        metricas.addCell(crearTarjetaMetrica(String.format("%.1f", calcularPromedio(evaluaciones)), "PROMEDIO", "📈", COLOR_SENA));
        metricas.addCell(crearTarjetaMetrica(contarPorNivel(evaluaciones, "BAJO") + "", "BAJO", "✅", COLOR_BAJO));
        metricas.addCell(crearTarjetaMetrica(contarPorNivel(evaluaciones, "MEDIO") + "", "MEDIO", "⚠️", COLOR_MEDIO));
        metricas.addCell(crearTarjetaMetrica(contarPorNivel(evaluaciones, "ALTO") + "", "ALTO", "🚨", COLOR_ALTO));

        document.add(metricas);
    }

    private PdfPCell crearTarjetaMetrica(String valor, String titulo, String icono, Color color) {
        Phrase contenido = new Phrase();
        contenido.add(new Chunk(icono + "\n", new Font(Font.HELVETICA, 18, Font.NORMAL, Color.WHITE)));
        contenido.add(new Chunk(valor + "\n", new Font(Font.HELVETICA, 20, Font.BOLD, Color.WHITE)));
        contenido.add(new Chunk(titulo, new Font(Font.HELVETICA, 9, Font.NORMAL, Color.WHITE)));

        PdfPCell celda = new PdfPCell(contenido);
        celda.setBackgroundColor(color);
        celda.setBorderColor(Color.WHITE);
        celda.setBorderWidth(2);
        celda.setPadding(12);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        return celda;
    }

    private void agregarGraficasPrincipales(Document document, List<EvaluacionEstres> evaluaciones) throws Exception {
        Paragraph seccion = new Paragraph("📈 VISUALIZACIÓN DE DATOS", 
            new Font(Font.HELVETICA, 16, Font.BOLD, COLOR_TEXTO));
        seccion.setSpacingBefore(20);
        document.add(seccion);

        // Gráfico 1: Distribución por niveles
        Map<String, Long> distribucion = evaluaciones.stream()
            .collect(Collectors.groupingBy(
                e -> e.getNivelDetectado() != null ? e.getNivelDetectado() : "SIN DATOS",
                Collectors.counting()));

        long total = evaluaciones.size();
        long bajo = distribucion.getOrDefault("BAJO", 0L);
        long medio = distribucion.getOrDefault("MEDIO", 0L);
        long alto = distribucion.getOrDefault("ALTO", 0L);

        document.add(new Paragraph("Distribución por Niveles:", 
            new Font(Font.HELVETICA, 12, Font.BOLD)));
        
        agregarBarraHorizontal(document, "BAJO", bajo, total, COLOR_BAJO);
        agregarBarraHorizontal(document, "MEDIO", medio, total, COLOR_MEDIO);
        agregarBarraHorizontal(document, "ALTO", alto, total, COLOR_ALTO);
        
        document.add(Chunk.NEWLINE);

        // Gráfico 2: Torta porcentual
        document.add(new Paragraph("Distribución Porcentual:", 
            new Font(Font.HELVETICA, 12, Font.BOLD)));
        
        PdfPTable torta = new PdfPTable(2);
        torta.setWidthPercentage(60);
        torta.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        torta.addCell(crearCeldaGrafico("NIVEL", true));
        torta.addCell(crearCeldaGrafico("PORCENTAJE", true));
        
        torta.addCell(crearCeldaGrafico("BAJO", false, COLOR_BAJO));
        torta.addCell(crearCeldaGrafico(String.format("%.1f%%", bajo * 100.0 / total), false, COLOR_BAJO));
        
        torta.addCell(crearCeldaGrafico("MEDIO", false, COLOR_MEDIO));
        torta.addCell(crearCeldaGrafico(String.format("%.1f%%", medio * 100.0 / total), false, COLOR_MEDIO));
        
        torta.addCell(crearCeldaGrafico("ALTO", false, COLOR_ALTO));
        torta.addCell(crearCeldaGrafico(String.format("%.1f%%", alto * 100.0 / total), false, COLOR_ALTO));
        
        document.add(torta);
    }

    private void agregarBarraHorizontal(Document document, String nivel, long cantidad, long total, Color color) throws Exception {
        int barras = total > 0 ? (int) (cantidad * 40 / total) : 0;
        if (barras == 0 && cantidad > 0) barras = 1;
        
        double porcentaje = total > 0 ? (cantidad * 100.0 / total) : 0;
        String barra = "█".repeat(barras);
        String espacios = " ".repeat(40 - barras);
        
        Paragraph p = new Paragraph();
        p.add(new Chunk(String.format("%-6s: ", nivel), new Font(Font.HELVETICA, 10, Font.BOLD, color.darker())));
        p.add(new Chunk(barra + espacios + " ", new Font(Font.HELVETICA, 12, Font.BOLD, color)));
        p.add(new Chunk(String.format("%d (%.1f%%)", cantidad, porcentaje)));
        document.add(p);
    }

    private PdfPCell crearCeldaGrafico(String texto, boolean encabezado) {
        return crearCeldaGrafico(texto, encabezado, encabezado ? COLOR_SENA : Color.WHITE);
    }

    private PdfPCell crearCeldaGrafico(String texto, boolean encabezado, Color fondo) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, 
            new Font(Font.HELVETICA, 10, encabezado ? Font.BOLD : Font.NORMAL,
                    encabezado ? Color.WHITE : Color.BLACK)));
        cell.setBackgroundColor(fondo);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        return cell;
    }

    private void agregarTablaDetallada(Document document, List<EvaluacionEstres> evaluaciones) throws Exception {
        Paragraph seccion = new Paragraph("📋 DETALLE DE EVALUACIONES", 
            new Font(Font.HELVETICA, 16, Font.BOLD, COLOR_TEXTO));
        seccion.setSpacingBefore(10);
        document.add(seccion);

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{0.8f, 2, 1, 1, 1.2f, 3});

        // Encabezados
        String[] headers = {"#", "FECHA Y HORA", "PUNTUACIÓN", "NIVEL", "RIESGO", "OBSERVACIONES"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE)));
            cell.setBackgroundColor(COLOR_SENA);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            tabla.addCell(cell);
        }

        // Datos
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (int i = 0; i < evaluaciones.size(); i++) {
            EvaluacionEstres e = evaluaciones.get(i);
            Color fondo = (i % 2 == 0) ? Color.WHITE : COLOR_FONDO;
            
            tabla.addCell(crearCeldaTabla((i + 1) + "", fondo, Element.ALIGN_CENTER));
            tabla.addCell(crearCeldaTabla(e.getCreatedAt() != null ? e.getCreatedAt().format(fmt) : "N/D", fondo, Element.ALIGN_LEFT));
            tabla.addCell(crearCeldaTabla(e.getPuntuacion() + "/100", fondo, Element.ALIGN_CENTER));
            tabla.addCell(crearCeldaNivel(e.getNivelDetectado(), fondo));
            tabla.addCell(crearCeldaRiesgo(e.getNivelDetectado(), fondo));
            
            String obs = e.getObservaciones();
            if (obs == null || obs.trim().isEmpty()) obs = "—";
            else if (obs.length() > 60) obs = obs.substring(0, 57) + "...";
            tabla.addCell(crearCeldaTabla(obs, fondo, Element.ALIGN_LEFT));
        }

        document.add(tabla);
    }

    private PdfPCell crearCeldaTabla(String texto, Color fondo, int alineacion) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 9)));
        cell.setBackgroundColor(fondo);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alineacion);
        return cell;
    }

    private PdfPCell crearCeldaNivel(String nivel, Color fondo) {
        Color colorFondo = COLOR_SENA;
        Color colorBorde = COLOR_SENA.darker();
        
        if ("BAJO".equals(nivel)) {
            colorFondo = COLOR_BAJO.brighter();
            colorBorde = COLOR_BAJO.darker();
        } else if ("MEDIO".equals(nivel)) {
            colorFondo = COLOR_MEDIO.brighter();
            colorBorde = COLOR_MEDIO.darker();
        } else if ("ALTO".equals(nivel)) {
            colorFondo = COLOR_ALTO.brighter();
            colorBorde = COLOR_ALTO.darker();
        }
        
        PdfPCell cell = new PdfPCell(new Phrase(nivel != null ? nivel : "N/D", 
            new Font(Font.HELVETICA, 9, Font.BOLD)));
        cell.setBackgroundColor(colorFondo);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        cell.setBorderColor(colorBorde);
        cell.setBorderWidth(1.5f);
        return cell;
    }

    private PdfPCell crearCeldaRiesgo(String nivel, Color fondo) {
        String riesgo = "N/A";
        Color color = Color.GRAY;
        
        if ("BAJO".equals(nivel)) {
            riesgo = "MÍNIMO";
            color = COLOR_BAJO;
        } else if ("MEDIO".equals(nivel)) {
            riesgo = "MODERADO";
            color = COLOR_MEDIO;
        } else if ("ALTO".equals(nivel)) {
            riesgo = "ALTO";
            color = COLOR_ALTO;
        }
        
        PdfPCell cell = new PdfPCell(new Phrase(riesgo, 
            new Font(Font.HELVETICA, 9, Font.BOLD, color.darker())));
        cell.setBackgroundColor(fondo);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    private void agregarEstadisticasAvanzadas(Document document, List<EvaluacionEstres> evaluaciones) throws Exception {
        Paragraph seccion = new Paragraph("📊 ESTADÍSTICAS AVANZADAS", 
            new Font(Font.HELVETICA, 16, Font.BOLD, COLOR_TEXTO));
        seccion.setSpacingBefore(20);
        document.add(seccion);

        int max = evaluaciones.stream().mapToInt(EvaluacionEstres::getPuntuacion).max().orElse(0);
        int min = evaluaciones.stream().mapToInt(EvaluacionEstres::getPuntuacion).min().orElse(0);
        double promedio = calcularPromedio(evaluaciones);
        double mediana = calcularMediana(evaluaciones);

        PdfPTable stats = new PdfPTable(2);
        stats.setWidthPercentage(80);
        stats.setHorizontalAlignment(Element.ALIGN_CENTER);
        stats.setSpacingBefore(15);

        agregarFilaEstadistica(stats, "Puntuación más alta:", max + " puntos", COLOR_BAJO);
        agregarFilaEstadistica(stats, "Puntuación más baja:", min + " puntos", COLOR_ALTO);
        agregarFilaEstadistica(stats, "Promedio general:", String.format("%.1f puntos", promedio), COLOR_SENA);
        agregarFilaEstadistica(stats, "Mediana:", String.format("%.1f puntos", mediana), COLOR_MEDIO);
        agregarFilaEstadistica(stats, "Rango total:", (max - min) + " puntos", COLOR_SENA);
        
        double desviacion = calcularDesviacion(evaluaciones, promedio);
        agregarFilaEstadistica(stats, "Desviación estándar:", String.format("%.1f puntos", desviacion), 
            desviacion < 15 ? COLOR_BAJO : desviacion < 30 ? COLOR_MEDIO : COLOR_ALTO);

        document.add(stats);
    }

    private void agregarFilaEstadistica(PdfPTable tabla, String label, String valor, Color color) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, 
            new Font(Font.HELVETICA, 10, Font.BOLD, color.darker())));
        labelCell.setBackgroundColor(color.brighter().brighter());
        labelCell.setPadding(10);
        
        PdfPCell valorCell = new PdfPCell(new Phrase(valor, 
            new Font(Font.HELVETICA, 10, Font.BOLD)));
        valorCell.setBackgroundColor(Color.WHITE);
        valorCell.setPadding(10);
        valorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        tabla.addCell(labelCell);
        tabla.addCell(valorCell);
    }

    private void agregarGraficasAdicionales(Document document, List<EvaluacionEstres> evaluaciones) throws Exception {
        if (evaluaciones.size() < 3) return;
        
        Paragraph seccion = new Paragraph("📈 ANÁLISIS DE TENDENCIAS", 
            new Font(Font.HELVETICA, 16, Font.BOLD, COLOR_TEXTO));
        seccion.setSpacingBefore(20);
        document.add(seccion);

        // Ordenar por fecha
        List<EvaluacionEstres> ordenadas = evaluaciones.stream()
            .sorted((e1, e2) -> e1.getCreatedAt().compareTo(e2.getCreatedAt()))
            .collect(Collectors.toList());

        // Gráfico de línea simple
        document.add(new Paragraph("Evolución temporal:", new Font(Font.HELVETICA, 12, Font.BOLD)));
        
        StringBuilder linea = new StringBuilder();
        for (EvaluacionEstres e : ordenadas) {
            int punt = e.getPuntuacion();
            if (punt <= 33) linea.append("▁");
            else if (punt <= 66) linea.append("▅");
            else linea.append("█");
            linea.append("─");
        }
        
        if (linea.length() > 0) {
            linea.deleteCharAt(linea.length() - 1); // Quitar último guión
            document.add(new Paragraph(linea.toString(), 
                new Font(Font.HELVETICA, 16, Font.BOLD, COLOR_SENA)));
        }

        // Análisis de tendencia
        if (ordenadas.size() >= 2) {
            int primera = ordenadas.get(0).getPuntuacion();
            int ultima = ordenadas.get(ordenadas.size() - 1).getPuntuacion();
            int diferencia = ultima - primera;
            
            String tendencia;
            Color colorTendencia;
            
            if (diferencia < -10) {
                tendencia = "MEJORÍA SIGNIFICATIVA 📉";
                colorTendencia = COLOR_BAJO;
            } else if (diferencia < 0) {
                tendencia = "LEVE MEJORÍA ↘️";
                colorTendencia = COLOR_BAJO.brighter();
            } else if (diferencia == 0) {
                tendencia = "ESTABLE ↔️";
                colorTendencia = COLOR_MEDIO;
            } else if (diferencia <= 10) {
                tendencia = "LEVE AUMENTO ↗️";
                colorTendencia = COLOR_ALTO.brighter();
            } else {
                tendencia = "AUMENTO SIGNIFICATIVO 📈";
                colorTendencia = COLOR_ALTO;
            }
            
            Paragraph analisis = new Paragraph();
            analisis.add(new Chunk("Tendencia general: ", new Font(Font.HELVETICA, 12, Font.BOLD)));
            analisis.add(new Chunk(tendencia, new Font(Font.HELVETICA, 12, Font.BOLD, colorTendencia)));
            analisis.add(new Chunk(" (" + diferencia + " puntos)", new Font(Font.HELVETICA, 10)));
            document.add(analisis);
        }
    }

    private void agregarRecomendacionesCompletas(Document document, List<EvaluacionEstres> evaluaciones) throws Exception {
        Paragraph seccion = new Paragraph("💡 RECOMENDACIONES Y ACCIONES", 
            new Font(Font.HELVETICA, 16, Font.BOLD, COLOR_TEXTO));
        seccion.setSpacingBefore(20);
        document.add(seccion);

        long alto = contarPorNivel(evaluaciones, "ALTO");
        long medio = contarPorNivel(evaluaciones, "MEDIO");
        double promedio = calcularPromedio(evaluaciones);

        // Análisis según datos
        if (alto > 0) {
            document.add(new Paragraph("🚨 PRIORIDAD: Atender " + alto + " caso(s) de ALTO estrés inmediatamente", 
                new Font(Font.HELVETICA, 11, Font.BOLD, COLOR_ALTO)));
        }
        if (medio > 0) {
            document.add(new Paragraph("📢 SUGERENCIA: Seguimiento a " + medio + " caso(s) con estrés moderado", 
                new Font(Font.HELVETICA, 11, Font.BOLD, COLOR_MEDIO)));
        }
        if (promedio > 66) {
            document.add(new Paragraph("⚠️ ALERTA GENERAL: Promedio elevado (" + String.format("%.1f", promedio) + ")", 
                new Font(Font.HELVETICA, 11, Font.BOLD, COLOR_ALTO)));
        }

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Acciones recomendadas:", new Font(Font.HELVETICA, 12, Font.BOLD)));
        
        String[] acciones = {
            "• Implementar talleres de manejo de estrés",
            "• Realizar sesiones de relajación guiada",
            "• Ofrecer acompañamiento psicológico individual",
            "• Revisar cargas académicas de los aprendices",
            "• Crear espacios de escucha activa",
            "• Establecer seguimiento periódico",
            "• Promover actividades de mindfulness",
            "• Capacitar a orientadores en primeros auxilios psicológicos"
        };
        
        for (String accion : acciones) {
            document.add(new Paragraph(accion, new Font(Font.HELVETICA, 10)));
        }
    }

    // ============ MÉTODOS DE CÁLCULO ============
    private double calcularPromedio(List<EvaluacionEstres> evaluaciones) {
        return evaluaciones.stream().mapToInt(EvaluacionEstres::getPuntuacion).average().orElse(0.0);
    }

    private long contarPorNivel(List<EvaluacionEstres> evaluaciones, String nivel) {
        return evaluaciones.stream().filter(e -> nivel.equals(e.getNivelDetectado())).count();
    }

    private double calcularMediana(List<EvaluacionEstres> evaluaciones) {
        List<Integer> puntuaciones = evaluaciones.stream()
            .map(EvaluacionEstres::getPuntuacion)
            .sorted()
            .collect(Collectors.toList());
        
        int n = puntuaciones.size();
        if (n == 0) return 0;
        if (n % 2 == 0) {
            return (puntuaciones.get(n/2 - 1) + puntuaciones.get(n/2)) / 2.0;
        } else {
            return puntuaciones.get(n/2);
        }
    }

    private double calcularDesviacion(List<EvaluacionEstres> evaluaciones, double promedio) {
        double suma = evaluaciones.stream()
            .mapToDouble(e -> Math.pow(e.getPuntuacion() - promedio, 2))
            .sum();
        return Math.sqrt(suma / evaluaciones.size());
    }

    // ============ PIE DE PÁGINA ============
    class PdfHeaderFooter extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfPTable footer = new PdfPTable(3);
                footer.setWidthPercentage(100);
                
                // Fecha
                PdfPCell fecha = new PdfPCell(new Phrase(
                    "Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY)));
                fecha.setBorder(Rectangle.NO_BORDER);
                fecha.setHorizontalAlignment(Element.ALIGN_LEFT);
                
                // Página
                PdfPCell pagina = new PdfPCell(new Phrase(
                    "Página " + writer.getPageNumber(),
                    new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY)));
                pagina.setBorder(Rectangle.NO_BORDER);
                pagina.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                // Sistema
                PdfPCell sistema = new PdfPCell(new Phrase(
                    "MindWell Analytics",
                    new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY)));
                sistema.setBorder(Rectangle.NO_BORDER);
                sistema.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                footer.addCell(fecha);
                footer.addCell(pagina);
                footer.addCell(sistema);
                
                footer.writeSelectedRows(0, -1, 36, 50, writer.getDirectContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}