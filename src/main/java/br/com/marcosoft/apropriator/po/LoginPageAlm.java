package br.com.marcosoft.apropriator.po;

import org.openqa.selenium.By;

import br.com.marcosoft.apropriator.ReadPasswordWindow;
import br.com.marcosoft.apropriator.model.ApropriationFile.Config;


public class LoginPageAlm extends PageObject {

    private String loginPassword;

    public String getLoginPassword() {
        return loginPassword;
    }

    public static boolean isOnLoginPage() {
        for (int i=0; i<5; i++) {
            if (isDisplayed(By.id("jazz_app_internal_LoginWidget_0_userId"))) {
                return true;
            }
            sleep(500);
        }
        return false;
    }

    public HomePageAlm login(String cpf, String pwd) {
        loginPassword = determinarPassword(pwd);

        type(By.id("jazz_app_internal_LoginWidget_0_userId"), cpf);
        if (cpf != null && loginPassword != null) {
            type(By.id("jazz_app_internal_LoginWidget_0_password"), loginPassword);
            click(By.cssSelector("button[type='submit']"));
            sleep(2000);
        }
        return new HomePageAlm();
    }

    private String determinarPassword(String pwd) {
        final String password;
        if (pwd == null && isSalvarSenha()) {
            final String info = String.format(
                "Essa versão, por default, se oferece para salvar a senha. " +
                "\nSe não quiser que a senha seja salva localmente. Use o botão 'Cancelar' agora e " +
                "\ncoloque na aba 'Config' da planilha o valor 'Não' para opção '%s'" +
                "\nOBS:A senha é salva criptografada.", Config.LOGIN_SALVAR_SENHA);
            final ReadPasswordWindow passwordWindow =
                new ReadPasswordWindow("Informe a senha", info);
            password = passwordWindow.getPassword();
        } else {
            password = pwd;
        }
        return password;
    }

    private boolean isSalvarSenha() {
        final String salvarSenha = System.getProperty(Config.LOGIN_SALVAR_SENHA, "Sim");
        if ("Sim".equalsIgnoreCase(salvarSenha)) {
            return true;
        }
        return false;
    }

}
