package br.com.marcosoft.apropriator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.com.marcosoft.apropriator.util.Key;

/**
 * Manipulador de tarefas.
 */
public class TasksHandler extends BaseModel {

    private final List<TaskRecord> taskRecords;

    public TasksHandler(List<TaskRecord> taskRecords) {
        this.taskRecords = taskRecords;
    }

    public List<TaskWeeklySummary> getWeeklySummary() {
        final Map<Key, TaskWeeklySummary> map = new LinkedHashMap<Key, TaskWeeklySummary>();

        for (final TaskRecord taskRecord : taskRecords) {
            if (taskRecord.isRegistrado()) {
                continue;
            }
            final Task task = taskRecord.getTask();
            final int semana = taskRecord.getSemana();
            final Key key = new Key(task.getItemTrabalho(), semana);
            TaskWeeklySummary summary = map.get(key);
            if (summary == null) {
                summary = new TaskWeeklySummary(task.getContexto(),
                    task.getItemTrabalho(), taskRecord.getData());
                map.put(key, summary);
            }
            summary.adicionar(taskRecord);
        }

        return new ArrayList<TaskWeeklySummary>(map.values());
    }

    private Map<Date, Integer> getDaySummary(Collection<TaskRecord> tasksRecords) {
        final Map<Date, Integer> map = new HashMap<Date, Integer>();

        for (final TaskRecord task : tasksRecords) {
            final Integer soma = map.get(task.getData());
            if (soma == null) {
                map.put(task.getData(), task.getDuracao());
            } else {
                map.put(task.getData(), soma + task.getDuracao() );
            }
        }

        return map;
    }

    public List<TaskRecord> getRegistrosComSobreposicao() {
        final List<TaskRecord> ret = new ArrayList<TaskRecord>();
        for (int i = 0; i < taskRecords.size(); i++) {
            final TaskRecord taskA = taskRecords.get(i);
            for (int j = i + 1; j < taskRecords.size(); j++) {
                final TaskRecord taskB = taskRecords.get(j);
                if (taskA.overlaps(taskB)) {
                    ret.add(taskA);
                    ret.add(taskB);
                }
            }
        }
        return ret;
    }

    public Map<Date, Integer> getDiasComInconsistencia(int minimoMinutosApropriacaoDia,
        int maximoMinutosApropriacaoDia) {
        final Map<Date, Integer> inconsistencia = new LinkedHashMap<Date, Integer>();
        final Map<Date, Integer> daySummary = getDaySummary(selecionarRegistrosNaoApropriados());
        for (final Entry<Date, Integer> entry : daySummary.entrySet()) {
            final Integer totalDia = entry.getValue();
            if (totalDia < minimoMinutosApropriacaoDia || totalDia > maximoMinutosApropriacaoDia) {
                inconsistencia.put(entry.getKey(), entry.getValue());
            }
        }
        return inconsistencia;
    }

    private Collection<TaskRecord> selecionarRegistrosNaoApropriados() {
        final Collection<TaskRecord> ret = new ArrayList<TaskRecord>();
        for (final TaskRecord task : taskRecords) {
            if (!task.isRegistrado()) {
                ret.add(task);
            }
        }
        return ret;
    }

    public Collection<TaskSummary> getResumoTarefasFinalizadas() {
        final Set<Task> taskFinalizadas = new LinkedHashSet<Task>();
        for (final TaskRecord taskRecord : taskRecords) {
            if (taskRecord.isFinalizar() && !taskRecord.isRegistrado()) {
                taskFinalizadas.add(taskRecord.getTask());
            }
        }

        final Map<Task, TaskSummary> map = new HashMap<Task, TaskSummary>();

        for (Task taskFinalizada : taskFinalizadas) 
        	for (final TaskRecord taskRecord : taskRecords) {
        		if (taskFinalizada.getContexto().equals(taskRecord.getTask().getContexto())
        				&& taskFinalizada.getComentario().equals(taskRecord.getTask().getComentario())) {            
                TaskSummary summary = map.get(taskFinalizada);
                if (summary == null) {
                    summary = new TaskSummary(taskFinalizada);
                    map.put(taskFinalizada, summary);
                }
                summary.add(taskRecord.getDuracao());
            }
        }

        return map.values();
    }

}
