package br.com.marcosoft.apropriator.po;

import org.marcosoft.lib.Condition;
import org.marcosoft.lib.WaitWindow;
import org.marcosoft.lib.WebUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import br.com.marcosoft.apropriator.selenium.SeleniumSupport;
import br.com.marcosoft.apropriator.util.Util;

public class PageObject {

    protected WebDriver getWebDriver() {
        return SeleniumSupport.getWebDriver();
    }

    protected static boolean isDisplayed(By by) {
        try {
            return SeleniumSupport.getWebDriver().findElement(by).isDisplayed();
        } catch (final NoSuchElementException e) {
            return false;
        }
    }

    protected void type(By by, String value) {
        final WebElement element = FindElement.waitForElement(by, FindElement.isEnabled());
        element.sendKeys(value);
    }

    protected void click(By by) {
        final WebElement element = FindElement.waitForElement(by, FindElement.isEnabled());
        element.click();
    }

    /**
     * Pausa a execucao durante o numero de milisegundos especificados.
     * @param millis numero de milisegundos
     */
    public static final void sleep(long millis) {
        Util.sleep(millis);
    }

    private String getErrorMessage() {
        final WebElement element = getWebDriver().findElement(By.className("validationMessageAnchor"));
        final String text = element.getAttribute("title");
        return text;
    }

    private boolean existsErrorMessage() {
        return isDisplayed(By.cssSelector("div.validationMessageError"));
    }

    private void clickSaveButton() {
        FindElement.waitForElement(
            By.cssSelector("button.primary-button"),
            FindElement.isEnabled("dojoattachpoint", "saveCmd"))
            .click();
    }

	public Select getStatus() {
		final WebElement webElement = FindElement.findElement(By.className("Select"),
				FindElement.isEnabled("aria-label", "Status"));
		return new Select(webElement);
	}

    public void salvarAlteracoes() {
        clickSaveButton();
        final Condition condition = new Condition() {
            public boolean satisfied() {
                if (FindElement.findElement(
                    By.cssSelector("div.validationMessageError"),
                    FindElement.isDisplayed()) != null) {
                    return true;
                }
                return FindElement.findElement(
                    By.cssSelector("button.primary-button"),
                    FindElement.isDisabled("dojoattachpoint", "saveCmd")) != null;
            }
        };
        WaitWindow.waitForCondition(condition, "Esperando alterações serem salvas...");
        if (existsErrorMessage()) {
            throw new RuntimeException(getErrorMessage());
        }

    }

    protected String montarUrlRastreamentoHorasAlm(String tarefa, int idItemTrabalho) {
        final String url = String.format(
                "https://alm.serpro/ccm/web/projects/%s" +
                "#action=com.ibm.team.workitem.viewWorkItem&id=%s" +
                "&tab=rastreamentodehoras"
                , WebUtils.encode(tarefa)
                , idItemTrabalho);
        return url;
    }

    protected String montarUrlVisaoGeralAlm(String tarefa, int idItemTrabalho) {
        final String url = String.format(
            "https://alm.serpro/ccm/web/projects/%s" +
                "#action=com.ibm.team.workitem.viewWorkItem&id=%s"
                , WebUtils.encode(tarefa)
                , idItemTrabalho);
        return url;
    }

	protected void executeScript(String js) {
		final JavascriptExecutor jsExec = (JavascriptExecutor) getWebDriver();
        jsExec.executeScript(js);
	}


}
