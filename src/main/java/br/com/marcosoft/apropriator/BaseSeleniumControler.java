package br.com.marcosoft.apropriator;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import br.com.marcosoft.apropriator.model.ApropriationFile;
import br.com.marcosoft.apropriator.model.ApropriationFile.Config;
import br.com.marcosoft.apropriator.model.TaskWeeklySummary;
import br.com.marcosoft.apropriator.po.Alm;
import br.com.marcosoft.apropriator.po.LoginPageAlm;
import br.com.marcosoft.apropriator.po.NotLoggedInException;
import br.com.marcosoft.apropriator.po.RastreamentoHorasPage;
import br.com.marcosoft.apropriator.po.VisaoGeralPage;
import br.com.marcosoft.apropriator.util.ApplicationProperties;
import br.com.marcosoft.apropriator.util.Cipher;

public abstract class BaseSeleniumControler {
    private boolean lerSenhaSalva = true;
    private static final String CHAVE_SENHA_ALM_APP_PROPERTIES = "alm.password";

	protected final AppContext appContext;

    private final ApplicationProperties applicantionProperties =
    		new ApplicationProperties("sgiApropriator");


    public BaseSeleniumControler (AppContext appContext) {
    	this.appContext = appContext;
    }

    protected void doLogin() {
        final LoginPageAlm loginPage = new LoginPageAlm();
        final String cpf = getConfig().getCpf();
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

    protected Config getConfig() {
		return appContext.getConfig();
	}

    protected ProgressInfo getProgressInfo() {
    	return appContext.getProgressInfo();
    }

	protected ApropriationFile getApropriationFile() {
		return appContext.getApropriationFile();
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

    protected enum OpcoesRecuperacaoAposErro {
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

    protected OpcoesRecuperacaoAposErro stopAfterException(final RuntimeException e) {
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

    protected VisaoGeralPage gotoVisaoGeralPage(String contexto, int id) {
    	final Alm alm = new Alm();
    	try {
    		return alm.gotoVisaoGeralPage(
    				contexto, id);
    	} catch (final NotLoggedInException e) {
    		doLogin();
    		return gotoVisaoGeralPage(contexto, id);
    	}
    }

    protected RastreamentoHorasPage gotoApropriationPage(TaskWeeklySummary summary) {
        final Alm alm = new Alm();
        try {
            return alm.gotoApropriationPage(
            		summary.getContexto(), summary.getItemTrabalho().getId());
        } catch (final NotLoggedInException e) {
            doLogin();
            return gotoApropriationPage(summary);
        }
    }

}
