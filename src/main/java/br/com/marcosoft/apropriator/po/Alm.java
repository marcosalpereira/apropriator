package br.com.marcosoft.apropriator.po;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.model.TaskWeeklySummary;

public class Alm extends PageObject {

    public RastreamentoHorasPage gotoApropriationPage(TaskWeeklySummary summary) throws NotLoggedInException {
        if (LoginPageAlm.isOnLoginPage()) {
            throw new NotLoggedInException();
        }

        final String url = montarUrlRastreamentoHorasAlm(summary.getContexto(), summary.getItemTrabalho().getId());
        final WebDriver driver = getWebDriver();
        tratarProblemaNaoReconhecimentoCamposPagina(driver);
        driver.get(url);
        return new RastreamentoHorasPage();
    }

	private void tratarProblemaNaoReconhecimentoCamposPagina(
			final WebDriver driver) {
		driver.get("about:blank");
        try {
        	driver.switchTo().alert().accept();
        } catch (final NoAlertPresentException e) {

        }
        sleep(1000);
	}

    public VisaoGeralPage gotoApropriationPageVisaoGeral(TaskSummary summary) {
        final String url = montarUrlVisaoGeralAlm(summary.getTask().getContexto(), summary.getTask().getItemTrabalho().getId());
        final WebDriver driver = getWebDriver();
        tratarProblemaNaoReconhecimentoCamposPagina(driver);
        driver.get(url);
        return new VisaoGeralPage();
    }
}


