package br.com.marcosoft.apropriator.po;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.util.Util;

public class VisaoGeralPage extends PageObject {

    public VisaoGeralPage() {
    	final String xpath = "//*[@id='com_ibm_team_workitem_web_ui_internal_view_editor_WorkItemEditor_0']/div/div[2]/a[1]";
		if (FindElement.waitForElement(By.xpath(xpath), FindElement.isEnabled("aria-selected", "true")) == null) {
			throw new IllegalStateException("Não foi para a página de visão geral!");
		}
    }

    public void incluirComentarioFinalizacaoTarefa(final TaskSummary summary) {

        final String comentario = String.format("Finalizando %s, tempo gasto %s",
		summary.getComentario(), Util.formatMinutes(summary.getSum()));

        final WebElement editor = FindElement.findElement(
        		By.className("RichTextEditorWidget"),
        		FindElement.isEnabled("data-placeholder", "Incluir um comentário..."));
        final String ariaLabel = editor.getAttribute("aria-label");
        final String editorName = ariaLabel.split(",").clone()[1].trim();
        final String js = String.format("CKEDITOR.instances['%s'].setData('%s')", editorName, comentario);
        executeScript(js);
        sleep(2000);
        salvarAlteracoes();
    }

	public void finalizarTarefa(TaskSummary summary) {
    	boolean encontrou = false;
    	final Select select = getStatus();
    	final List<WebElement> options = select.getOptions();
    	for (final WebElement option : options) {
			if ("Finalizar".equalsIgnoreCase(option.getText())) {
				option.click();
				encontrou = true;
			}
		}
    	if (encontrou) {
    		salvarAlteracoes();
    	} else {
    		incluirComentarioFinalizacaoTarefa(summary);
    	}
    }



}
