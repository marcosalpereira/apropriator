package br.com.marcosoft.apropriator;


import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import br.com.marcosoft.apropriator.ProgressInfo.TipoTempo;
import br.com.marcosoft.apropriator.model.ApropriationFile;
import br.com.marcosoft.apropriator.model.ApropriationFile.Config;
import br.com.marcosoft.apropriator.model.DaySummary;
import br.com.marcosoft.apropriator.model.Task;
import br.com.marcosoft.apropriator.model.TaskRecord;
import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.model.TaskWeeklySummary;
import br.com.marcosoft.apropriator.model.TasksHandler;
import br.com.marcosoft.apropriator.po.Alm;
import br.com.marcosoft.apropriator.po.LoginPageAlm;
import br.com.marcosoft.apropriator.po.NotLoggedInException;
import br.com.marcosoft.apropriator.po.RastreamentoHorasPage;
import br.com.marcosoft.apropriator.po.VisaoGeralPage;
import br.com.marcosoft.apropriator.selenium.SeleniumSupport;
import br.com.marcosoft.apropriator.util.ApplicationProperties;
import br.com.marcosoft.apropriator.util.Cipher;
import br.com.marcosoft.apropriator.util.Exec;
import br.com.marcosoft.apropriator.util.SoftwareUpdate;
import br.com.marcosoft.apropriator.util.Util;
import br.com.marcosoft.apropriator.util.Version;

/**
 * Apropriar SGI.
 */
public class Apropriator {

	private final Version appVersion;

	private final ProgressInfo progressInfo;

    private static final String CHAVE_SENHA_ALM_APP_PROPERTIES = "alm.password";

    public static void main(final String[] args) {
    	final Apropriator apropriator = new Apropriator();
    	final Arguments arguments = Arguments.parse(args);
    	try {
	        apropriator.handleSoftwareUpdate(arguments);
	        apropriator.doItForMePlease(arguments.getCsvFile());
        } catch (final Throwable e) {
            showInfoMessage("Um erro inesperado ocorreu!" + e.getClass().getName() + "\n" + e.getMessage());
            apropriator.gravarArquivoRetornoErro(e, arguments.getCsvFile());
            e.printStackTrace();
        } finally {
        	apropriator.progressInfo.dispose();
        }
    }

	private void showReleaseNotes() {
		final String releaseNotesKey = "last-version";
		final String lastVersion = this.applicantionProperties.getProperty(releaseNotesKey);
		if (appVersion.get().equals(lastVersion)) {
			return;
		}
		this.applicantionProperties.setProperty(releaseNotesKey, appVersion.get());

		try {
			final InputStream stream = getClass()
					.getClassLoader().getResourceAsStream("releaseNotes.txt");
			final List<?> lines = IOUtils.readLines(stream, "UTF-8");
			final String conteudo = StringUtils.join(lines, '\n');
			showInfoMessage(conteudo);
		} catch (final IOException e) {
		}
	}

	private void showWarnMessage() {
		final String warnMessageKey = "last-version-warn-message";
		final String lastVersion = this.applicantionProperties.getProperty(warnMessageKey);
		if (appVersion.get().equals(lastVersion)) {
			return;
		}
		this.applicantionProperties.setProperty(warnMessageKey, appVersion.get());

		try {
			final String fileName = String.format("mensagemImportante-%s.txt", appVersion.get());
			final InputStream stream = getClass()
					.getClassLoader().getResourceAsStream(fileName);
			if (stream != null) {
				final List<?> lines = IOUtils.readLines(stream, "UTF-8");
				final String conteudo = StringUtils.join(lines, "");
				showWarnMessage(conteudo);
			}
		} catch (final IOException e) {
		}
	}

	private static void showWarnMessage(final String conteudo) {
		final JTextPane textArea = new JTextPane();
		textArea.setContentType("text/html");
		textArea.setText(conteudo);
		textArea.setPreferredSize(new Dimension(650, 300));
		final Component c = new JScrollPane(textArea);
		JOptionPane.showMessageDialog(null, c, "Mensagem Importante", JOptionPane.WARNING_MESSAGE);
	}

	private static void showInfoMessage(final String conteudo) {
		final Component c = new JScrollPane(new JTextArea(conteudo, 10, 70));
		JOptionPane.showMessageDialog(null, c, "Release Notes", JOptionPane.INFORMATION_MESSAGE);
	}

	private void handleSoftwareUpdate(Arguments arguments) {
		final File csvFile = arguments.getCsvFile();
		final String targetFolder = csvFile.getParent();
		progressInfo.setTitle("Atualizator - " + appVersion);
		final Version newVersion = SoftwareUpdate.update(appVersion, targetFolder, progressInfo);
		progressInfo.setTitle("Apropriator - " + appVersion);
		if (newVersion != null) {
			final String jar = String.format("%s%salm-apropriator-%s.jar", targetFolder, File.separator, newVersion);
			Exec.jar(jar, csvFile.getPath());
			System.exit(0);
		} else {
			showReleaseNotes();
			showWarnMessage();
		}
	}

    private Apropriator() {
    	appVersion = new Version(getAppVersion());
    	progressInfo = new ProgressInfo();
	}

    private String getAppVersion() {
        final String ret = "?";
		final InputStream stream = getClass()
				.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
        if (stream != null) {
            final Properties prop = new Properties();
            try {
                prop.load(stream);
                return prop.getProperty("version");
            } catch (final IOException e) {
            }
        }
        return ret;
    }

    private ApropriationFile apropriationFile;

    private final ApplicationProperties applicantionProperties = new ApplicationProperties(
        "sgiApropriator");

    private boolean lerSenhaSalva = true;

    public void doItForMePlease(final File inputFile) throws ApropriationException {
        parseFile(inputFile);
        verificarCompatibilidade();
        apropriate();
    }

    private void parseFile(final File inputFile) throws ApropriationException {
        final ApropriationFileParser apropriationFileParser = new ApropriationFileParser(inputFile);
        try {
            apropriationFile = apropriationFileParser.parse();
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
        final String strVersion = getMacrosVersion();
        if (strVersion == null) {
            return;
        }
        final double version = Double.parseDouble(strVersion);
        if (version < 1.5) {
            throw new ApropriationException("Não sei tratar arquivos na versão:" + strVersion);
        }
    }

    private void gravarArquivoRetornoErro(Throwable erro, File inputFile) {
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
        out.println("err|" + erro.getMessage());
        out.close();
    }

    private void gravarArquivoRetornoApropriacao(List<TaskWeeklySummary> tasksWeeklySummary) {
        final String exportFolder = this.apropriationFile.getConfig().getPlanilhaDir();

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
                for (final TaskRecord task : summary.getTaskRecords()) {
                    out.println(String.format("mcr|%s", task.getNumeroLinha()));
                }
            }
        }

        out.println();

        out.close();

    }

    private void apropriate() {
    	progressInfo.setTitle(montarTitulo());
        final TasksHandler tasksHandler = this.apropriationFile.getTasksHandler();
        final List<TaskWeeklySummary> tasksWeeklySummary = tasksHandler.getWeeklySummary();
        if (!tasksWeeklySummary.isEmpty()) {
        	if (registrosApropriacoesIntegros()) {
        		apropriate(tasksWeeklySummary);
        	}
        }
        gravarArquivoRetornoApropriacao(tasksWeeklySummary);
    }



    private boolean registrosApropriacoesIntegros() {
        final Config config = this.apropriationFile.getConfig();
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
                    "Na data %s o total apropriado é %d",
                        Util.DD_MM_YY_FORMAT.format(entry.getKey()), entry.getValue()));
        }

        final Config config = this.apropriationFile.getConfig();
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

    private void apropriate(List<TaskWeeklySummary> tasksWeeklySummary) {
    	progressInfo.setInfoMessage("Iniciando apropriações...");

    	initSelenium();

    	for (final TaskWeeklySummary summary : tasksWeeklySummary) {
    		if (!apropriate(summary, null, null)) {
    			break;
    		}
    	}

    	verificarFinalizarTarefasAtividades();

    	progressInfo.dispose();

    	SeleniumSupport.stopSelenium();

    }

	private void initSelenium() {
		final Config config = apropriationFile.getConfig();
    	SeleniumSupport.initSelenium(config);
	}

    private boolean apropriate(TaskWeeklySummary summaryApropriando, TaskWeeklySummary summaryAntes, TaskWeeklySummary summaryDepois) {
    	try {
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
                progressInfo.setInfoMessage("Tentando apropriar novamente mesma atividade!!!");
                return apropriate(summaryApropriando, summaryAntes, summaryDepois);

            } else if (opcao == OpcoesRecuperacaoAposErro.PROXIMA) {
                return true;

            } else {
                return false;

            }
        }
    }

    private String montarTitulo() {
        return "Apropriator v" + appVersion.get() + " - Macros" + getMacrosVersion();
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


    private void verificarFinalizarTarefasAtividades() {
        verificarFinalizarAtividade();
        verificarFinalizarTarefa();
    }

	private void verificarFinalizarAtividade() {
		final TasksHandler tasksHandler = this.apropriationFile.getTasksHandler();
        final Collection<TaskSummary> summaryFinishedTasks = tasksHandler.getResumoAtividadesFinalizadas();
        for (final TaskSummary summary : summaryFinishedTasks) {
        	progressInfo.setInfoFinalizando(summary);
        	if (!finalizarAtividade(summary)) {
        		break;
        	}
        }
	}

	private void verificarFinalizarTarefa() {
		final TasksHandler tasksHandler = this.apropriationFile.getTasksHandler();
		final Collection<TaskSummary> summaryFinishedTasks = tasksHandler.getResumoTarefasFinalizadas();
		for (final TaskSummary summary : summaryFinishedTasks) {
			progressInfo.setInfoFinalizando(summary);
			if (!finalizarTarefa(summary)) {
				break;
			}
		}
	}

	private boolean finalizarAtividade(final TaskSummary summary) {
		final Alm alm = new Alm();
		try {
			final Task task = summary.getTask();
			final Collection<Integer> idsAtividades = summary.getIdsAtividades();
			if (idsAtividades.isEmpty()) {
				final VisaoGeralPage visaoGeralPage = alm.gotoApropriationPageVisaoGeral(
						task.getContexto(), task.getItemTrabalho().getId());
				visaoGeralPage.incluirComentarioFinalizacaoTarefa(summary);
			} else {
				for (final Integer id : idsAtividades) {
					final VisaoGeralPage visaoGeralPage = alm.gotoApropriationPageVisaoGeral(
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

	private boolean finalizarTarefa(final TaskSummary summary) {
		final Alm alm = new Alm();
		try {
			final Task task = summary.getTask();
			final VisaoGeralPage visaoGeralPage = alm.gotoApropriationPageVisaoGeral(
					task.getContexto(), task.getItemTrabalho().getId());
			visaoGeralPage.finalizarTarefa(summary);
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

    private RastreamentoHorasPage gotoApropriationPage(TaskWeeklySummary summary) {
        final Alm alm = new Alm();
        try {
            return alm.gotoApropriationPage(
            		summary.getContexto(), summary.getItemTrabalho().getId());
        } catch (final NotLoggedInException e) {
            doLogin();
            return gotoApropriationPage(summary);
        }
    }

    private void doLogin() {
        final LoginPageAlm loginPage = new LoginPageAlm();
        final String cpf = this.apropriationFile.getConfig().getCpf();
        final String pwd;
        if (lerSenhaSalva) {
            pwd = readAlmSavedPassword();
        } else {
            final ReadPasswordWindow passwordWindow = new ReadPasswordWindow(
                "Informe a nova senha",
                "Não consegui me logar usando a senha atualmente salva." +
                "\nTalvez a senha anterior esteja expirada.");
            pwd = passwordWindow.getPassword();
        }
        loginPage.login(cpf, pwd);
        writeAlmSavedPassword(loginPage.getLoginPassword());
        lerSenhaSalva = false;
    }

    private enum OpcoesRecuperacaoAposErro {
        TENTAR_NOVAMENTE, PROXIMA, TERMINAR;

        @Override
        public String toString() {
            switch (this) {
            case TENTAR_NOVAMENTE:
                return "Tentar Novamente";
            case PROXIMA:
                return "Ir para a próxima";
            default:
                return "Terminar";
            }
        }
    }

    private OpcoesRecuperacaoAposErro stopAfterException(final RuntimeException e) {
        e.printStackTrace();
        final String message = "O seguinte erro ocorreu:\n" + e.getMessage() + "!";
        final JTextArea textArea = new JTextArea(10, 60);
        textArea.setText(message);
        textArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(textArea);

        final Object[] options = {"Tentar Novamente",
                            "Ir para próxima",
                            "Terminar"};
        final int n = JOptionPane.showOptionDialog(null,
            scrollPane,
            "Erro na apropriação",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        return OpcoesRecuperacaoAposErro.values()[n];

    }

    private String getMacrosVersion() {
        return this.apropriationFile.getConfig().getMacrosVersion();
    }

    private void writeAlmSavedPassword(String senha) {
        if (senha != null) {
            this.applicantionProperties.setProperty(CHAVE_SENHA_ALM_APP_PROPERTIES,
                Cipher.cript(senha));
        }
    }

    private String readAlmSavedPassword() {
        final String pwdCripto = this.applicantionProperties.getProperty(
            CHAVE_SENHA_ALM_APP_PROPERTIES);
        if (pwdCripto != null) {
            return Cipher.uncript(pwdCripto);
        }
        return null;
    }

}
