package br.com.marcosoft.apropriator;

import br.com.marcosoft.apropriator.model.Task;
import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.po.VisaoGeralPage;


public class FinalizadorTarefa extends BaseSeleniumControler {

	public FinalizadorTarefa(AppContext appContext) {
		super(appContext);
	}

	public boolean finalizarTarefa(final TaskSummary summary) {
		try {
			final Task task = summary.getTask();
			final VisaoGeralPage visaoGeralPage = gotoVisaoGeralPage(
					task.getContexto(), task.getItemTrabalho().getId());
			visaoGeralPage.finalizarTarefa(summary);
			return true;

		} catch (final RuntimeException e) {
			final OpcoesRecuperacaoAposErro opcao = stopAfterException(e);
			if (opcao == OpcoesRecuperacaoAposErro.TENTAR_NOVAMENTE) {
				return finalizarTarefa(summary);

			} else if (opcao == OpcoesRecuperacaoAposErro.PROXIMA) {
				return true;

			} else {
				return false;

			}
		}
	}


}
