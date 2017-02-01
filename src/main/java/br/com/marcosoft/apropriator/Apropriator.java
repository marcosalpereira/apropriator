package br.com.marcosoft.apropriator;


import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.marcosoft.lib.App;
import org.marcosoft.lib.ApplicationProperties;
import org.marcosoft.lib.SoftwareUpdate;
import org.marcosoft.lib.Version;

import br.com.marcosoft.apropriator.Apropriador.RetornoApropriacao;
import br.com.marcosoft.apropriator.model.ApropriationFile;
import br.com.marcosoft.apropriator.model.ApropriationFile.Config;
import br.com.marcosoft.apropriator.model.TaskRecord;
import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.model.TaskWeeklySummary;
import br.com.marcosoft.apropriator.model.TasksHandler;
import br.com.marcosoft.apropriator.selenium.SeleniumSupport;
import br.com.marcosoft.apropriator.util.Util;

/**
 * Apropriar SGI.
 */
public class Apropriator extends App {

	private static final Version VERSION_1_5 = new Version("1.5");
	private static final Version VERSION_1_11 = new Version("1.11");

	private final Version appVersion;
	private Version macrosVersion;

	private final ProgressInfo progressInfo;

	private final Apropriador apropriador;

	private final FinalizadorAtividade finalizadorAtividade;
	private final FinalizadorTarefa finalizadorTarefa;

	private final List<String> errosApropriacao = new ArrayList<String>();

    public static void main(final String[] args) {
    	new Apropriator(args).main();
    }

	private void main() {
	    final Arguments arguments = Arguments.parse(getArgs());
		try {
		    SoftwareUpdate.update(this, progressInfo);
	        doItForMePlease(arguments.getCsvFile());
        } catch (final Throwable e) {
        	e.printStackTrace();
        	if (macrosNaoMostramMensagemErro()) {
        		showInfoMessage(e.getMessage());
        	}
            gravarArquivoRetornoErro(e.getMessage(), arguments.getCsvFile());
        } finally {
        	progressInfo.dispose();
        }
	}

	private boolean macrosNaoMostramMensagemErro() {
		return macrosVersion == null || macrosVersion.lt(VERSION_1_11);
	}

	private static void showInfoMessage(final String conteudo) {
		final Component c = new JScrollPane(new JTextArea(conteudo, 10, 70));
		JOptionPane.showMessageDialog(null, c, "Apropriator", JOptionPane.INFORMATION_MESSAGE);
	}

    private Apropriator(String[] args) {
        super(args, "ALM Apropriator");
    	appContext = new AppContext();

    	appVersion = new Version(getAppVersion());

    	progressInfo = new ProgressInfo();
    	appContext.setProgressInfo(progressInfo);
        appContext.setApplicationProperties(new ApplicationProperties("sgiApropriator"));

        recuperadorTitulos = new RecuperadorTitulos(appContext);
        apropriador = new Apropriador(appContext);
        finalizadorAtividade = new FinalizadorAtividade(appContext);
        finalizadorTarefa = new FinalizadorTarefa(appContext);

	}

    private String getAppVersion() {
		final InputStream inputStream =
				getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
		String ret = null;
		if (inputStream != null) {
			try {
				final Attributes attributes = new Manifest(inputStream).getMainAttributes();
				ret = (String) attributes.get(Attributes.Name.IMPLEMENTATION_VERSION);
			} catch (final IOException e) {
			}
		}
		if (ret == null) {
			ret = "?";
		}
		return ret;
    }

    private ApropriationFile apropriationFile;

	private final RecuperadorTitulos recuperadorTitulos;

	private final AppContext appContext;

    public void doItForMePlease(final File inputFile) throws ApropriationException {
        parseFile(inputFile);
        verificarCompatibilidade();
        if (registrosApropriacoesIntegros()) {
        	tratarArquivoApropriacao();
        } else {
        	gravarArquivoRetornoErro("Registros inconsistentes", inputFile);
        }
    }

    private void parseFile(final File inputFile) throws ApropriationException {
        final ApropriationFileParser apropriationFileParser = new ApropriationFileParser(inputFile);
        try {
            apropriationFile = apropriationFileParser.parse();
            appContext.setApropriationFile(apropriationFile);
			macrosVersion = new Version(apropriationFile.getConfig().getMacrosVersion());
        } catch (final IOException e) {
            throw new ApropriationException(e);
        }
    }

    /**
     * Verificar a compatibilidade desta implementacao com a versao do arquivo de integracao.
     * @param strVersion versao que esta no arquivo de integracao
     * @throws ApropriationException
     */
    private void verificarCompatibilidade() throws ApropriationException {
        if (macrosVersion == null) {
            return;
        }
        if (macrosVersion.lt(VERSION_1_5)) {
            throw new ApropriationException("Não sei tratar arquivos na versão:" + macrosVersion);
        }
    }

    private void gravarArquivoRetornoErro(String erro, File inputFile) {
        String exportFolder = inputFile.getParent();
        if (exportFolder == null) exportFolder = ".";

        final String fileName = exportFolder + File.separator + "sgi.ret";
        final PrintWriter out;
        try {
            out = new PrintWriter(fileName, "UTF-8");
        } catch (final IOException e) {
            showInfoMessage("Nao consegui gravar arquivo retorno!\n" + e.getMessage());
            return;
        }
        out.println("err|" + erro);
        out.close();
    }

    private void gravarArquivoRetornoApropriacao(List<TaskWeeklySummary> tasksWeeklySummary,
    		List<TaskRecord> trTitulosRecuperados) {
        final String exportFolder = getConfig().getPlanilhaDir();

        final String fileName = exportFolder + File.separator + "sgi.ret";
        PrintWriter out;
        try {
            out = new PrintWriter(fileName, "UTF-8");
        } catch (final IOException e) {
            showInfoMessage("Nao consegui gravar arquivo retorno!\n" + e.getMessage());
            return;
        }

        //Marcar como registrado
        for (final TaskWeeklySummary summary : tasksWeeklySummary) {
            if (summary.isApropriado()) {
                for (final TaskRecord taskRecord : summary.getTaskRecords()) {
                    out.println(String.format("mcr|%s", taskRecord.getNumeroLinha()));
                }
            }
        }
        //Recuperar titulos
        final int colunaComentario = 5;
        for (final TaskRecord taskRecord : trTitulosRecuperados) {
			out.println(String.format(
				"set|%d|%d|%s",
				colunaComentario,
				taskRecord.getNumeroLinha(),
				taskRecord.getNovoComentario()));
		}

        //Modificar lista de validacao da coluna finalizar
        final int colunaFinalizar = 2;
        out.println(String.format("alv|%d|Atividade", colunaFinalizar));
        out.println(String.format("alv|%d|Tarefa", colunaFinalizar));
        out.println(String.format("alv|%d|Atividade/Tarefa", colunaFinalizar));

        //Dump erros
        for (final String erro : errosApropriacao) {
        	out.println("err|" + erro);
        }

        out.close();

    }

    private boolean registrosApropriacoesIntegros() {
        if (this.apropriationFile.isCaptureInfo()) {
            return true;
        }
        final Config config = getConfig();
        final int minimoMinutosApropriacaoDia = config.getMinimoMinutosApropriacaoDia();
        final int maximoMinutosApropriacaoDia = config.getMaximoMinutosApropriacaoDia();

        final TasksHandler tasksHandler = this.apropriationFile.getTasksHandler();
        final List<TaskRecord> sobrepostas = tasksHandler.getRegistrosComSobreposicao();
        if (!sobrepostas.isEmpty()) {
            mostrarSobrePosicoes(sobrepostas);
            return false;
        }

        final Map<Date, Integer> overflow = tasksHandler.getDiasComInconsistencia(
            minimoMinutosApropriacaoDia, maximoMinutosApropriacaoDia);
        if (!overflow.isEmpty()) {
            if (!continuarMesmoComOverflow(overflow)) {
                return false;
            }
        }

        return true;
    }

    private boolean continuarMesmoComOverflow(Map<Date, Integer> overflow) {
        final StringBuilder sb = new StringBuilder();

        for (final Entry<Date, Integer> entry : overflow.entrySet()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(
                String.format(
                    "Na data %s o total registrado foi %d (%s)",
                        Util.DD_MM_YY_FORMAT.format(entry.getKey()), entry.getValue(), Util.formatMinutes(entry.getValue())));
        }

        final Config config = getConfig();
        final int minimoMinutosApropriacaoDia = config.getMinimoMinutosApropriacaoDia();
        final int maximoMinutosApropriacaoDia = config.getMaximoMinutosApropriacaoDia();

        final String message = String.format(
            "As apropriações estão fora dos limites aceitáveis.\n%s\n\nVariáveis na aba de configuração da planilha\n\t%s=%d\n\t%s=%d",
            sb.toString(),
            config.getChaveMinimoMinutosApropriacaoDia(),
            minimoMinutosApropriacaoDia,
            config.getChaveMaximoMinutosApropriacaoDia(),
            maximoMinutosApropriacaoDia);
        final JTextArea textArea = new JTextArea(10, 50);
        textArea.setText(message);
        textArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(textArea);

        final Object[] options = {"Confirma", "Cancela"};
        final int n = JOptionPane.showOptionDialog(null,
            scrollPane,
            "Confirme para continuar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        return n == 0;
    }

    private boolean mostrarSobrePosicoes(final List<TaskRecord> sobrepostas) {
        final StringBuilder sb = new StringBuilder();

        for (final TaskRecord taskRecord : sobrepostas) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(
                String.format(
                    "- Linha[%03d]: Em '%s %s - %s para %s-%s'",
                    taskRecord.getNumeroLinha() + 1,
                    Util.DD_MM_YYYY_FORMAT.format(taskRecord.getData()),
                    taskRecord.getHoraInicio(),
                    taskRecord.getHoraTermino(),
                    taskRecord.getTask().getItemTrabalho(),
                    taskRecord.getTask().getComentario()
                    ));
        }
        final JTextArea textArea = new JTextArea(10, 50);
        textArea.setText(sb.toString());
        textArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(textArea);

        final Object[] options = {"OK"};
        JOptionPane.showOptionDialog(null,
            scrollPane,
            "Sobreposição de Hora detectado",
            JOptionPane.OK_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[0]);

        return true;
    }

    private void tratarArquivoApropriacao() {
    	progressInfo.setTitle(montarTitulo());

    	progressInfo.setInfoMessage("Iniciando apropriações...");

    	SeleniumSupport.initSelenium(appContext.getConfig());

    	final List<TaskWeeklySummary> twsApropriadas = apropriate();

    	final List<TaskRecord> trTitulosRecuperados =
    			recuperadorTitulos.recuperar();

    	gravarArquivoRetornoApropriacao(twsApropriadas, trTitulosRecuperados);

    	verificarFinalizarAtividade();
		verificarFinalizarTarefa();

    	progressInfo.dispose();

    	SeleniumSupport.stopSelenium();
    }

	private List<TaskWeeklySummary> apropriate() {
		final List<TaskWeeklySummary> twsApropriadas = new ArrayList<TaskWeeklySummary>();
		if (!this.apropriationFile.isCaptureInfo()) {
	        final TasksHandler tasksHandler = this.apropriationFile.getTasksHandler();
			final List<TaskWeeklySummary> weeklySummary = tasksHandler.getWeeklySummary();
			for (final TaskWeeklySummary summary : weeklySummary) {
				final RetornoApropriacao retornoApropriacao = apropriador.apropriar(summary, null, null);
				if (retornoApropriacao.deuErro()) {
					errosApropriacao.add(retornoApropriacao.getMensagemErro());
				}
	    		if (retornoApropriacao.isPararProcesso()) {
	    			break;
	    		}
	    		twsApropriadas.add(summary);
	    	}
		}
		return twsApropriadas;
	}

    private String montarTitulo() {
        return "Apropriator v" + appVersion.get() + " - Macros" + macrosVersion;
    }

    private void verificarFinalizarAtividade() {
		final TasksHandler tasksHandler = this.apropriationFile.getTasksHandler();
        final Collection<TaskSummary> summaryFinishedTasks = tasksHandler.getResumoAtividadesFinalizadas();
        for (final TaskSummary summary : summaryFinishedTasks) {
        	progressInfo.setInfoFinalizando(summary);
        	if (!finalizadorAtividade.finalizarAtividade(summary)) {
        		break;
        	}
        }
	}

	private void verificarFinalizarTarefa() {
		final TasksHandler tasksHandler = this.apropriationFile.getTasksHandler();
		final Collection<TaskSummary> summaryFinishedTasks = tasksHandler.getResumoTarefasFinalizadas();
		for (final TaskSummary summary : summaryFinishedTasks) {
			progressInfo.setInfoFinalizando(summary);
			if (!finalizadorTarefa.finalizarTarefa(summary)) {
				break;
			}
		}
	}

	private Config getConfig() {
		return appContext.getConfig();
	}



}
