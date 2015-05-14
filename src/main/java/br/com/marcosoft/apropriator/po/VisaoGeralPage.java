package br.com.marcosoft.apropriator.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import br.com.marcosoft.apropriator.model.TaskSummary;
import br.com.marcosoft.apropriator.util.Util;

public class VisaoGeralPage extends PageObject {

    public VisaoGeralPage() {
    	String xpath = "//*[@id='com_ibm_team_workitem_web_ui_internal_view_editor_WorkItemEditor_0']/div/div[2]/a[1]";
		if (FindElement.waitForElement(By.xpath(xpath), FindElement.isEnabled("aria-selected", "true")) == null) {
			throw new IllegalStateException("Não foi para a página de visão geral!");			
		}
    }

    public void incluirComentarioFinalizacaoTarefa(final TaskSummary summary) {

        final String comentario = String.format("Finalizando %s, tempo gasto %s",
            summary.getComentario(), Util.formatMinutes(summary.getSum()));
        
        WebElement editor = FindElement.findElement(
        		By.className("RichTextEditorWidget"), 
        		FindElement.isEnabled("data-placeholder", "Incluir um comentário..."));
        String ariaLabel = editor.getAttribute("aria-label");
        String editorName = ariaLabel.split(",").clone()[1].trim();
        String js = String.format("CKEDITOR.instances['%s'].setData('%s')", editorName, comentario);
        executeScript(js);
        sleep(2000);
        salvarAlteracoes();
    }



}
