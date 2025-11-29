// ChartService.java
package com.proyecto.spring.services;

import com.proyecto.spring.Entity.Cita;
import com.proyecto.spring.Entity.EvaluacionEstres;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
public class ChartService {

    public String generarGraficaBarrasCitasPorMes(List<Cita> citas) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yyyy");

        citas.stream()
                .map(c -> c.getFechaCita().withDayOfMonth(1))
                .sorted()
                .forEach(fecha -> {
                    String mes = fecha.format(fmt);
                    long count = citas.stream()
                            .filter(c -> c.getFechaCita().withDayOfMonth(1).equals(fecha))
                            .count();
                    dataset.addValue(count, "Citas", mes);
                });

        JFreeChart chart = ChartFactory.createBarChart(
                "Citas por Mes (Últimos 12 meses)",
                "Mes",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        return chartToBase64(chart, 800, 400);
    }

    public String generarGraficaMotivos(List<Cita> citas) throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();
        long ansiedad = citas.stream().filter(c -> "ANSIEDAD".equalsIgnoreCase(c.getMotivoClasificado())).count();
        long estres = citas.stream().filter(c -> "ESTRES".equalsIgnoreCase(c.getMotivoClasificado())).count();
        long otro = citas.stream().filter(c -> c.getMotivoClasificado() == null || 
                (!"ANSIEDAD".equalsIgnoreCase(c.getMotivoClasificado()) && !"ESTRES".equalsIgnoreCase(c.getMotivoClasificado()))).count();

        dataset.setValue("Ansiedad", ansiedad);
        dataset.setValue("Estrés", estres);
        dataset.setValue("Otro", otro);

        JFreeChart chart = ChartFactory.createPieChart(
                "Motivos Clasificados",
                dataset,
                true, true, false
        );

        return chartToBase64(chart, 600, 400);
    }

    public String generarGraficaNivelEstres(List<EvaluacionEstres> evaluaciones) throws Exception {
        DefaultPieDataset dataset = new DefaultPieDataset();

        long bajo = evaluaciones.stream().filter(e -> e.getPuntuacion() <= 33).count();
        long medio = evaluaciones.stream().filter(e -> e.getPuntuacion() > 33 && e.getPuntuacion() <= 66).count();
        long alto = evaluaciones.stream().filter(e -> e.getPuntuacion() > 66).count();

        dataset.setValue("Bajo (0-33)", bajo);
        dataset.setValue("Medio (34-66)", medio);
        dataset.setValue("Alto (67-99)", alto);

        JFreeChart chart = ChartFactory.createPieChart(
                "Nivel de Estrés y Ansiedad (Últimos 12 meses)",
                dataset,
                true, true, false
        );

        return chartToBase64(chart, 700, 400);
    }

    public String generarGraficaHoraMasFrecuente(List<Cita> citas) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        citas.stream()
                .map(c -> c.getHoraCita().getHour())
                .forEach(hora -> {
                    long count = citas.stream().filter(c -> c.getHoraCita().getHour() == hora).count();
                    dataset.addValue(count, "Citas", hora + ":00");
                });

        JFreeChart chart = ChartFactory.createBarChart(
                "Horario más frecuente de citas",
                "Hora",
                "Cantidad",
                dataset
        );

        return chartToBase64(chart, 800, 400);
    }

    public String generarGraficaDiaSemana(List<Cita> citas) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (DayOfWeek day : DayOfWeek.values()) {
            String nombreDia = switch (day) {
                case MONDAY -> "Lunes";
                case TUESDAY -> "Martes";
                case WEDNESDAY -> "Miércoles";
                case THURSDAY -> "Jueves";
                case FRIDAY -> "Viernes";
                case SATURDAY -> "Sábado";
                case SUNDAY -> "Domingo";
            };
            long count = citas.stream()
                    .filter(c -> c.getFechaCita().getDayOfWeek() == day)
                    .count();
            dataset.addValue(count, "Citas", nombreDia);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Citas por día de la semana",
                "Día",
                "Cantidad",
                dataset
        );

        return chartToBase64(chart, 700, 400);
    }

    private String chartToBase64(JFreeChart chart, int width, int height) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, chart, width, height);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}