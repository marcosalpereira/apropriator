package br.com.marcosoft.apropriator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.marcosoft.apropriator.model.Task;
import br.com.marcosoft.apropriator.model.TaskRecord;
import br.com.marcosoft.apropriator.model.TasksHandler;
import br.com.marcosoft.apropriator.po.VisaoGeralPage;

public class RecuperadorTitulos extends BaseSeleniumControler {
	final Map<String, String> jaRecuperadas = new HashMap<String, String>();

	public RecuperadorTitulos(AppContext appContext) {
		super(appContext);
	}

	public List<TaskRecord> recuperar() {
		final List<TaskRecord> ret = new ArrayList<TaskRecord>();
        final TasksHandler tasksHandler = getApropriationFile().getTasksHandler();

        for (final TaskRecord taskRecord : tasksHandler.getItensRecuperarTitulo()) {
        	final Task task = taskRecord.getTask();
        	String novosIds = null;
        	String novosTitulos = null;
        	for (final Integer idItem : task.getItensRecuperarTitulo()) {
	        	final String titulo = recuperarTitulo(task.getContexto(), idItem);
	        	if (titulo == null) {
	        		return ret;
	        	}
	        	if (titulo.isEmpty()) {
	        		continue;
	        	}
	        	if (novosIds == null) {
	        		novosIds = String.valueOf(idItem);
	        		novosTitulos =  titulo;
	        	} else {
	        		novosIds += "," + idItem;
	        		novosTitulos += ";/n" + titulo;
	        	}
        	}
        	if (novosIds != null) {
        		final String novoComentario = novosIds + "-" + novosTitulos;
        		taskRecord.setNovoComentario(novoComentario);
        		ret.add(taskRecord);
        	}
		}
		return ret;

	}

	private String recuperarTitulo(String contexto, Integer id) {
		getProgressInfo().setInfoRecuperandoTitulos(contexto, id);
		final String chave = contexto + id;
		final String titulo = jaRecuperadas.get(chave);
		if (titulo != null) {
			return titulo;
		}

		try {
			final VisaoGeralPage visaoGeralPage =
					gotoVisaoGeralPage(contexto, id);
			final String tituloRecuperado = visaoGeralPage.getTituloItemTrabalho();
			jaRecuperadas.put(chave, tituloRecuperado);
			return tituloRecuperado;

        } catch (final RuntimeException e) {
            final OpcoesRecuperacaoAposErro opcao = stopAfterException(e);
            if (opcao == OpcoesRecuperacaoAposErro.TENTAR_NOVAMENTE) {
                return recuperarTitulo(contexto, id);

            } else if (opcao == OpcoesRecuperacaoAposErro.PROXIMA) {
                return "";

            } else {
                return null;

            }
        }
	}

}
