package br.com.marcosoft.apropriator;

import java.util.Collection;

import br.com.marcosoft.apropriator.model.Task;
import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.po.VisaoGeralPage;

public class FinalizadorAtividade extends BaseSeleniumControler {

	public FinalizadorAtividade(AppContext appContext) {
		super(appContext);
	}

	public boolean finalizarAtividade(final TaskSummary summary) {
		try {
			final Task task = summary.getTask();
			final Collection<Integer> idsAtividades = summary.getIdsAtividades();
			if (idsAtividades.isEmpty()) {
				final VisaoGeralPage visaoGeralPage = gotoVisaoGeralPage(
						task.getContexto(), task.getItemTrabalho().getId());
				visaoGeralPage.incluirComentarioFinalizacaoTarefa(summary);
			} else {
				for (final Integer id : idsAtividades) {
					final VisaoGeralPage visaoGeralPage = gotoVisaoGeralPage(
							task.getContexto(), id);
					visaoGeralPage.finalizarTarefa(summary);
				}
			}
			return true;

        } catch (final RuntimeException e) {
            final OpcoesRecuperacaoAposErro opcao = stopAfterException(e);
            if (opcao == OpcoesRecuperacaoAposErro.TENTAR_NOVAMENTE) {
                return finalizarAtividade(summary);

            } else if (opcao == OpcoesRecuperacaoAposErro.PROXIMA) {
                return true;

            } else {
                return false;

            }
        }
	}


}
