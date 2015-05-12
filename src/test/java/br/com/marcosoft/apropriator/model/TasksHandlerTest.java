package br.com.marcosoft.apropriator.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import br.com.marcosoft.apropriator.ApropriationFileParser;
import br.com.marcosoft.apropriator.util.Util;

public class TasksHandlerTest {

    @Test
    public void testGetWeeklySummary() throws IOException {
        final TasksHandler handler = parse("/taskHandler/sgi-integro.csv").getTasksHandler();
        final List<TaskWeeklySummary> esperado = Arrays.asList(
            newTaskWeeklySummary(2, "IMP", "01/04/15", 0, 0, 30, 30, 60),
            newTaskWeeklySummary(1, "GES", "02/04/15", 0, 0,  0, 60, 30),

            newTaskWeeklySummary(1, "GES", "20/08/15", 0, 0,  0, 30, 0),
            newTaskWeeklySummary(2, "IMP", "20/08/15", 0, 0,  0, 30, 0)
            );

        assertEquals(esperado, handler.getWeeklySummary());

    }

    private TaskWeeklySummary newTaskWeeklySummary(int id, String ds, String data, int seg,
        int ter, int qua, int qui, int sex) {
        final TaskWeeklySummary summary =
            new TaskWeeklySummary("ctx", new ItemTrabalho(id, ds), Util.parseDate(Util.DD_MM_YY_FORMAT, data));
        if (seg > 0) summary.getDaySummary(Calendar.MONDAY).add(seg);
        if (ter > 0) summary.getDaySummary(Calendar.TUESDAY).add(ter);
        if (qua > 0) summary.getDaySummary(Calendar.WEDNESDAY).add(qua);
        if (qui > 0) summary.getDaySummary(Calendar.THURSDAY).add(qui);
        if (sex > 0) summary.getDaySummary(Calendar.FRIDAY).add(sex);
        return summary;
    }

    @Test
    public void testGetRegistrosComSobreposicao() throws IOException {
        final TasksHandler handler = parse("/taskHandler/sgi-sobreposicao.csv").getTasksHandler();
        final List<TaskRecord> esperado = Arrays.asList(
            newTaskRecord(1), newTaskRecord(2),
            newTaskRecord(3), newTaskRecord(4),
            newTaskRecord(5), newTaskRecord(6),
            newTaskRecord(7), newTaskRecord(8),
            newTaskRecord(9), newTaskRecord(10),
            newTaskRecord(11), newTaskRecord(12),
            newTaskRecord(13), newTaskRecord(14),
            newTaskRecord(15), newTaskRecord(16),
            newTaskRecord(17), newTaskRecord(18),
            newTaskRecord(19), newTaskRecord(20),
            newTaskRecord(21), newTaskRecord(22)
            );

        assertEquals(esperado, handler.getRegistrosComSobreposicao());
    }

    private TaskRecord newTaskRecord(int linha) {
        final TaskRecord tr = new TaskRecord();
        tr.setNumeroLinha(linha);
        return tr;
    }

    @Test
    public void testGetResumoTarefasFinalizadas() throws IOException {
        final TasksHandler handler = parse("/taskHandler/sgi-finalizar.csv").getTasksHandler();
        final List<TaskSummary> esperado = Arrays.asList(
            new TaskSummary(new Task("ctx", new ItemTrabalho(2, "IMP"), "b"), 150));

        assertEquals(esperado, new ArrayList<TaskSummary>(handler.getResumoTarefasFinalizadas()));
    }

    private ApropriationFile parse(String arquivoCsv) throws IOException {
        final ApropriationFileParser fileParser =
            new ApropriationFileParser(getFile(arquivoCsv));
        return fileParser.parse();
    }

    private File getFile(String filename) {
        final String fullname = this.getClass().getResource(filename).getFile();
        return new File(fullname);
    }

}
