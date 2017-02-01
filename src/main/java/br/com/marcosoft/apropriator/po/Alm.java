package br.com.marcosoft.apropriator.po;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

public class Alm extends PageObject {

    public RastreamentoHorasPage gotoApropriationPage(String contexto, int id) throws NotLoggedInException {
        if (LoginPageAlm.isOnLoginPage()) {
            throw new NotLoggedInException();
        }

        final String url = montarUrlRastreamentoHorasAlm(contexto, id);
        final WebDriver driver = getWebDriver();
        tratarProblemaNaoReconhecimentoCamposPagina(driver);
        driver.get(url);
        return new RastreamentoHorasPage();
    }

    private void tratarProblemaNaoReconhecimentoCamposPagina(final WebDriver driver) {
        driver.get("about:blank");
        try {
            driver.switchTo().alert().accept();
        } catch (final NoAlertPresentException e) {

        }
        //sleep(500);
    }

    public VisaoGeralPage gotoVisaoGeralPage(String contexto, int id) throws NotLoggedInException {
        if (LoginPageAlm.isOnLoginPage()) {
            throw new NotLoggedInException();
        }

        final String url = montarUrlVisaoGeralAlm(contexto, id);
        final WebDriver driver = getWebDriver();
        tratarProblemaNaoReconhecimentoCamposPagina(driver);
        driver.get(url);
        return new VisaoGeralPage();
    }
}


