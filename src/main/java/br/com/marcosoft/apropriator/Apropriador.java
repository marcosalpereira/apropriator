package br.com.marcosoft.apropriator;

import java.util.Date;

import br.com.marcosoft.apropriator.ProgressInfo.TipoTempo;
import br.com.marcosoft.apropriator.model.DaySummary;
import br.com.marcosoft.apropriator.model.TaskWeeklySummary;
import br.com.marcosoft.apropriator.po.RastreamentoHorasPage;
import br.com.marcosoft.apropriator.util.Util;

public class Apropriador extends BaseSeleniumControler {

	public Apropriador(AppContext appContext) {
		super(appContext);
	}

    public boolean apropriar(TaskWeeklySummary summaryApropriando, TaskWeeklySummary summaryAntes, TaskWeeklySummary summaryDepois) {
    	try {
    		final ProgressInfo progressInfo = getProgressInfo();

            final RastreamentoHorasPage apropriationPage = gotoApropriationPage(summaryApropriando);
            progressInfo.setResumoApropriando(summaryApropriando);

            if (apropriationPage.isTarefaAberta()) {
            	apropriationPage.iniciarTarefa();
            }

            final Date dataInicio = summaryApropriando.getDataInicio();

			apropriationPage.irParaSemana(dataInicio);
            apropriationPage.criarLinhaTempoPadrao();

            if (summaryDepois == null) {
            	summaryAntes = apropriationPage.lerValoresLinhaTempo();
            	summaryDepois = summaryAntes.somar(summaryApropriando);
            }

            progressInfo.setTempo(TipoTempo.ANTES, summaryAntes);
            progressInfo.setTempo(TipoTempo.DEPOIS, summaryDepois);

            apropriationPage.digitarMinutos(summaryDepois);
            apropriationPage.salvarAlteracoes();

            apropriationPage.irParaSemana(dataInicio);

            final TaskWeeklySummary valoresLinhaTempoReais = apropriationPage.lerValoresLinhaTempo();
			verificarApropriacoes(summaryDepois, valoresLinhaTempoReais);

            summaryApropriando.setApropriado(true);
            return true;

        } catch (final RuntimeException e) {
            final OpcoesRecuperacaoAposErro opcao = stopAfterException(e);
            if (opcao == OpcoesRecuperacaoAposErro.TENTAR_NOVAMENTE) {
                getProgressInfo().setInfoMessage("Tentando apropriar novamente mesma atividade!!!");
                return apropriar(summaryApropriando, summaryAntes, summaryDepois);

            } else if (opcao == OpcoesRecuperacaoAposErro.PROXIMA) {
                return true;

            } else {
                return false;

            }
        }
    }

    private void verificarApropriacoes(TaskWeeklySummary esperado, final TaskWeeklySummary reais) {

        final StringBuilder erros = new StringBuilder();
        for (final DaySummary daySummaryEsperado : esperado.getDaysSummary()) {
        	final int diaSemana = daySummaryEsperado.getDay();
			final DaySummary daySummaryReal = reais.getDaySummary(diaSemana);
			if (!daySummaryEsperado.equals(daySummaryReal)) {
        		erros.append(
        				String.format(
        						"Dia %s esperado %s encontrado %s\n",
        						Util.nomeDiaSemana(diaSemana),
        						Util.formatMinutesDecimal(daySummaryEsperado.getHoras()),
        						Util.formatMinutesDecimal(daySummaryReal.getHoras())
        				)
        		);
        	}
        }
        if (erros.length() > 0) {
        	throw new IllegalStateException(
        			"Deu algum problema na apropriação:" + erros.toString());
        }
    }


}
