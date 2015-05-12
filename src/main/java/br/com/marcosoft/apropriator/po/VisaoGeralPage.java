package br.com.marcosoft.apropriator.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.util.Util;

public class VisaoGeralPage extends PageObject {

    public VisaoGeralPage() {
        for (int i=0; i<10; i++) {
            if (isDisplayed(By.linkText("Incluir Comentário"))) {
                return;
            }
            sleep(1000);
        }
        throw new IllegalStateException("Não foi para a página de visão geral!");
    }

    public void incluirComentarioFinalizacaoTarefa(final TaskSummary summary) {

        final String comentario = String.format("Finalizando %s, tempo gasto %s",
            summary.getComentario(), Util.formatMinutes(summary.getSum()));
        click(By.linkText("Incluir Comentário"));

        FindElement.findElement(
            By.cssSelector("iframe.dijitEditorIFrame"), FindElement.isDisplayed());

        final WebDriver driver = getWebDriver();
        final WebElement fr = driver.findElement(By.className("dijitEditorIFrame"));
        driver.switchTo().frame(fr);

        final WebElement element = driver.findElement(By.className("RichTextBody"));
        element.sendKeys(comentario);

        sleep(3000);

        driver.switchTo().defaultContent();
        salvarAlteracoes();
    }



}
