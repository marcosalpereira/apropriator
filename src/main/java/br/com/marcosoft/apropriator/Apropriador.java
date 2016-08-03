package br.com.marcosoft.apropriator;

import java.util.Date;

import br.com.marcosoft.apropriator.ProgressInfo.TipoTempo;
import br.com.marcosoft.apropriator.model.DaySummary;
import br.com.marcosoft.apropriator.model.TaskWeeklySummary;
import br.com.marcosoft.apropriator.po.RastreamentoHorasPage;
import br.com.marcosoft.apropriator.util.Util;

public class Apropriador extends BaseSeleniumControler {

	public static class RetornoApropriacao {
		private final boolean pararProcesso;
		private final String mensagemErro;

		public RetornoApropriacao() {
			this(null);
		}

		public RetornoApropriacao(String mensagemErro) {
			this(mensagemErro, false);
		}

		public RetornoApropriacao(String mensagemErro, boolean pararProcesso) {
			this.mensagemErro = mensagemErro;
			this.pararProcesso = pararProcesso;
		}

		public boolean deuErro() {
			return mensagemErro != null;
		}

		public boolean isPararProcesso() {
			return pararProcesso;
		}

		public String getMensagemErro() {
			return mensagemErro;
		}

	}

	public Apropriador(AppContext appContext) {
		super(appContext);
	}

    public RetornoApropriacao apropriar(TaskWeeklySummary summaryApropriando, TaskWeeklySummary summaryAntes, TaskWeeklySummary summaryDepois) {
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
            return new RetornoApropriacao();

        } catch (final RuntimeException e) {
        	final String mensagem = String.format(
        			"%s ao apropriar %s", e.getMessage(), summaryApropriando.getItemTrabalho());
            if (appContext.getConfig().isContinuarAposErro()) {
				return new RetornoApropriacao(mensagem);
            }
            final OpcoesRecuperacaoAposErro opcao = stopAfterException(e);
            if (opcao == OpcoesRecuperacaoAposErro.TENTAR_NOVAMENTE) {
                getProgressInfo().setInfoMessage("Tentando apropriar novamente mesma atividade!!!");
                return apropriar(summaryApropriando, summaryAntes, summaryDepois);

            } else if (opcao == OpcoesRecuperacaoAposErro.PROXIMA) {
            	return new RetornoApropriacao();

            } else {
            	return new RetornoApropriacao(mensagem, true);

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
        			"Deu algum problema na apropria��o:" + erros.toString());
        }
    }


}
