package br.com.marcosoft.apropriator;


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
import javax.swing.UIManager;

import br.com.marcosoft.apropriator.model.ApropriationFile;
import br.com.marcosoft.apropriator.model.ApropriationFile.Config;
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
import br.com.marcosoft.apropriator.util.SoftwareUpdate;
import br.com.marcosoft.apropriator.util.Util;
import br.com.marcosoft.apropriator.util.Version;

/**
 * Apropriar SGI.
 */
public class Apropriator {
	
	private final Version appVersion;

    private static final String CHAVE_SENHA_ALM_APP_PROPERTIES = "alm.password";

    public static void main(final String[] args) {
    	final Apropriator apropriator = new Apropriator();
    	Arguments arguments = Arguments.parse(args);
    	try {
	    	setLookAndFeel();
	        apropriator.checkSoftwareUpdate(arguments.isUpdate(), arguments.getCsvFile().getParent());	        
	        apropriator.doItForMePlease(arguments.getCsvFile());
        } catch (final Throwable e) {
            JOptionPane.showMessageDialog(null, "Um erro inesperado ocorreu!\n" + e.getClass().getName() + ":" + e.getMessage());
            apropriator.gravarArquivoRetornoErro(e, arguments.getCsvFile());
            e.printStackTrace();
        }
    }

	private void checkSoftwareUpdate(boolean update, String targetFolder) {
		if (update) {
			try {
				SoftwareUpdate.update(appVersion, targetFolder);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			System.exit(0);
		}
	}
    
    private Apropriator() {
    	appVersion = new Version(getAppVersion());
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


	private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            e.printStackTrace();
        }
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

    private void iniciarSelenium(Config config, String browserUrl) {
        final WaitWindow waitWindow = new WaitWindow("Iniciando Selenium " + browserUrl);
        try {
            SeleniumSupport.initSelenium(config, browserUrl);
        } finally {
            waitWindow.dispose();
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
            JOptionPane.showMessageDialog(null, "Nao consegui gravar arquivo retorno!\n"
                + e.getMessage());
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
            JOptionPane.showMessageDialog(null, "Nao consegui gravar arquivo retorno!\n"
                + e.getMessage());
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
        final TasksHandler tasksHandler = this.apropriationFile.getTasksHandler();
        final List<TaskWeeklySummary> tasksWeeklySummary = tasksHandler.getWeeklySummary();
        if (registrosApropriacoesIntegros()) {
            apropriateAlm(tasksWeeklySummary);
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

    private void apropriateAlm(List<TaskWeeklySummary> tasksWeeklySummary) {

        if (tasksWeeklySummary.isEmpty()) {
            return ;
        }

        final Config config = apropriationFile.getConfig();
        iniciarSelenium(config, config.getUrlAlm());

        final ProgressInfo progressInfo = new ProgressInfo(montarTitulo());

        for (final TaskWeeklySummary summary : tasksWeeklySummary) {
            if (!apropriateAlm(summary, progressInfo)) {
                break;
            }
        }

        verificarFinalizarTarefas(progressInfo);
        
        progressInfo.dispose();

        SeleniumSupport.stopSelenium();

    }

    private String montarTitulo() {
        return "Apropriator v" + appVersion.get() + " - Macros" + getMacrosVersion();
    }

    private boolean apropriateAlm(TaskWeeklySummary summary, ProgressInfo progressInfo) {
        try {
            final RastreamentoHorasPage apropriationPage = gotoApropriationPage(summary);
            apropriationPage.apropriate(progressInfo, summary);
            summary.setApropriado(true);
            return true;

        } catch (final RuntimeException e) {
            final OpcoesRecuperacaoAposErro opcao = stopAfterException(e);
            if (opcao == OpcoesRecuperacaoAposErro.TENTAR_NOVAMENTE) {
                progressInfo.setInfoMessage("Tentando apropriar novamente mesma atividade!!!");
                return apropriateAlm(summary, progressInfo);

            } else if (opcao == OpcoesRecuperacaoAposErro.PROXIMA) {
                return true;

            } else {
                return false;

            }
        }
    }
    
    private void verificarFinalizarTarefas(ProgressInfo progressInfo) {
        final TasksHandler tasksHandler = this.apropriationFile.getTasksHandler();
        final Collection<TaskSummary> summaryFinishedTasks = tasksHandler.getResumoTarefasFinalizadas();
        for (final TaskSummary summary : summaryFinishedTasks) {
        	progressInfo.setInfoFinalizando(summary);
        	if (!finalizarTarefa(summary)) {
        		break;
        	}
        }
    }

	private boolean finalizarTarefa(final TaskSummary summary) {
		final Alm alm = new Alm();
		try {
			final VisaoGeralPage visaoGeralPage = alm.gotoApropriationPageVisaoGeral(summary);
			visaoGeralPage.incluirComentarioFinalizacaoTarefa(summary);
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

    private RastreamentoHorasPage gotoApropriationPage(TaskWeeklySummary summary) {
        final Alm alm = new Alm();
        try {
            return alm.gotoApropriationPage(summary);
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
