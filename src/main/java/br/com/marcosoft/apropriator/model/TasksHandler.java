package br.com.marcosoft.apropriator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.com.marcosoft.apropriator.model.TaskRecord.OpcaoFinalizacao;
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

        for (final TaskRecord taskRecord : selecionarRegistrosPendentesApropriacao()) {
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
        final List<TaskRecord> registrosPendentes =
        		selecionarRegistrosPendentesApropriacao();
		for (int i = 0; i < registrosPendentes.size(); i++) {
            final TaskRecord taskA = registrosPendentes.get(i);
            for (int j = i + 1; j < registrosPendentes.size(); j++) {
            	final TaskRecord taskB = registrosPendentes.get(j);
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
        final Map<Date, Integer> daySummary = getDaySummary(selecionarRegistrosComTarefasNaoApropriadas());
        for (final Entry<Date, Integer> entry : daySummary.entrySet()) {
            final Integer totalDia = entry.getValue();
            if (totalDia < minimoMinutosApropriacaoDia || totalDia > maximoMinutosApropriacaoDia) {
                inconsistencia.put(entry.getKey(), entry.getValue());
            }
        }
        return inconsistencia;
    }

    private List<TaskRecord> selecionarRegistrosPendentesApropriacao() {
    	final List<TaskRecord> ret = new ArrayList<TaskRecord>();
    	for (final TaskRecord taskRecord : taskRecords) {
    		if (!taskRecord.isRegistrado() && taskRecord.getDuracao() > 0) {
    			ret.add(taskRecord);
    		}
    	}
    	return ret;
    }

    private Collection<TaskRecord> selecionarRegistrosComTarefasNaoApropriadas() {
        final Set<Task> taskNaoRegistradas = new LinkedHashSet<Task>();
        final List<TaskRecord> pendentes = selecionarRegistrosPendentesApropriacao();
		for (final TaskRecord taskRecord : pendentes) {
			taskNaoRegistradas.add(taskRecord.getTask());
        }

        final Collection<TaskRecord> ret = new ArrayList<TaskRecord>();
        for (final TaskRecord task : pendentes) {
            if (taskNaoRegistradas.contains(task)) {
                ret.add(task);
            }
        }
        return ret;
    }

    public Collection<TaskSummary> getResumoAtividadesFinalizadas() {
    	return getResumoFinalizadas(
    			Arrays.asList(
    					OpcaoFinalizacao.ATIVIDADE, OpcaoFinalizacao.ATIVIDADE_TAREFA));
    }

    public Collection<TaskSummary> getResumoTarefasFinalizadas() {
		return getResumoFinalizadas(Arrays.asList(OpcaoFinalizacao.TAREFA,
				OpcaoFinalizacao.ATIVIDADE_TAREFA));
    }

	private Collection<TaskSummary> getResumoFinalizadas(
			final Collection<OpcaoFinalizacao> opcaoFinalizacao) {
		final Set<Task> taskFinalizadas = new LinkedHashSet<Task>();
        final List<TaskRecord> pendentesApropriacao = selecionarRegistrosPendentesApropriacao();
		for (final TaskRecord taskRecord : pendentesApropriacao) {
			if (opcaoFinalizacao.contains(taskRecord.getFinalizar())) {
                taskFinalizadas.add(taskRecord.getTask());
            }
        }

        final Map<Task, TaskSummary> map = new HashMap<Task, TaskSummary>();

        for (final Task taskFinalizada : taskFinalizadas)
        	for (final TaskRecord taskRecord : pendentesApropriacao) {
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

	public List<TaskRecord> getItensRecuperarTitulo() {
		final List<TaskRecord> ret = new ArrayList<TaskRecord>();
		for (final TaskRecord taskRecord : taskRecords) {
			if (taskRecord.isRecuperarTituloItemTrabalho()) {
				ret.add(taskRecord);
			}
		}
		return ret;
	}

}
