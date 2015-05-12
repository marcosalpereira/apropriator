package br.com.marcosoft.apropriator.po;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import br.com.marcosoft.apropriator.ProgressInfo;
import br.com.marcosoft.apropriator.ProgressInfo.TipoTempo;
import br.com.marcosoft.apropriator.model.DaySummary;
import br.com.marcosoft.apropriator.model.TaskWeeklySummary;
import br.com.marcosoft.apropriator.util.Util;

public class RastreamentoHorasPage extends PageObject {

    public RastreamentoHorasPage() {
        for (int i=0; i<10; i++) {
            if (isOnRastreamentoHorasPage()) {
                return;
            }
            sleep(1000);
        }
        throw new IllegalStateException("Não foi para a página de rastreamento de horas!");
    }

    public static boolean isOnRastreamentoHorasPage() {
        return isDisplayed(By.id("Timesheet_next_button"));
    }

    /**
     * Apropriate task.
     * @param progressInfo progressInfo
     * @param summaryApropriando task daily summary
     */
    public void apropriate(ProgressInfo progressInfo, final TaskWeeklySummary summaryApropriando) {
        final Date data = summaryApropriando.getDataInicio();

        progressInfo.setResumoApropriando(summaryApropriando);

        irParaSemana(data);
        criarLinhaTempoPadrao();

        final TaskWeeklySummary summaryAntes = lerValoresAtuais();
        final TaskWeeklySummary summaryDepois = summaryAntes.somar(summaryApropriando);

        progressInfo.setTempo(TipoTempo.ANTES, summaryAntes);
        progressInfo.setTempo(TipoTempo.DEPOIS, summaryDepois);

        digitarMinutos(summaryDepois);
        salvarAlteracoes();

        irParaSemana(data);
        verificarApropriacoes(summaryDepois);
    }

    private void digitarMinutos(final TaskWeeklySummary summaryDepois) {
        for (final DaySummary daySummary : summaryDepois.getDaysSummary()) {
            if (daySummary.getSum() > 0) {
                digitarMinutos(daySummary.getDay(), daySummary.getHoras());
                sleep(300);
            }
        }
    }

    private void verificarApropriacoes(TaskWeeklySummary esperado) {
        final TaskWeeklySummary reais = lerValoresAtuais();

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

    private TaskWeeklySummary lerValoresAtuais() {
    	final TaskWeeklySummary valoresAtuais = new TaskWeeklySummary();
    	final List<DaySummary> daysSummary = valoresAtuais.getDaysSummary();
    	for (int diaSemana = Calendar.SUNDAY; diaSemana<=Calendar.SATURDAY; diaSemana++) {
            final WebElement element = getWebElementHoras(diaSemana);
            final String preValue = element.getAttribute("prevalue").replace(',', '.');
            final double valorAtual = Util.parseDouble(preValue, 0);
            final int minutos = (int) Math.round(valorAtual * 60);
			daysSummary.add(new DaySummary(diaSemana, minutos));
    	}
		return valoresAtuais;
	}

    private void digitarMinutos(int indiceDiaSemana, double horaDecimal) {
        final WebElement element = getWebElementHoras(indiceDiaSemana);
        element.clear();
        element.sendKeys(Util.formatMinutesDecimal(horaDecimal));
        element.sendKeys(Keys.RETURN);
    }

	private WebElement getWebElementHoras(int indiceDiaSemana) {
		final String horaXpath = String.format(
            "//*[@id='Timesheet_Table']/div[1]/table[1]/tbody/tr[1]/td[%d]/span/input",
            indiceDiaSemana + 2);
        final WebElement element = getWebDriver().findElement(By.xpath(horaXpath));
		return element;
	}

    private void irParaSemana(Date data) {
        final Calendar dataApropriacao = Util.toCalendar(data);
        for (;;) {
            final int dataNaSemana = isDataDentroSemanaAtual(dataApropriacao);
            if (dataNaSemana == 0) {
                break;
            }
            if (dataNaSemana < 0) {
                irParaSemanaAnterior();
            } else {
                irParaSemanaSeguinte();
            }
            sleep(300);
        }

    }

    private void irParaSemanaSeguinte() {
        System.out.println("Semana seguinte");
       click(By.xpath("//*[@id='Timesheet_next_button']/a"));
    }

    private void irParaSemanaAnterior() {
        final WebElement element = getWebDriver().findElement(
            By.xpath("//*[@id='Timesheet_previous_button']/a"));
        element.click();
    }

    private int isDataDentroSemanaAtual(Calendar dataApropriacao) {
        final String text = getWebDriver().findElement(
            By.xpath("//*[@id='Timesheet_weekTextBox']/div[1]/div[1]/div[1]/div[3]/input[2]"))
            .getAttribute("value");

        final Calendar dataInicialPagina = Util.parseCalendar(Util.YYYY_MM_DD_FORMAT, text);

        final Calendar dataFinalPagina = Util.addDay(dataInicialPagina, 6);

        if (dataApropriacao.compareTo(dataInicialPagina) == 0) {
            return 0;
        }
        if (dataApropriacao.compareTo(dataInicialPagina) < 0) {
            return -1;
        }
        if (dataApropriacao.compareTo(dataFinalPagina) <= 0) {
            return 0;
        }
        return 1;
    }

    private void criarLinhaTempoPadrao() {
        final WebElement element = getWebDriver().findElement(
            By.id("Timesheet_Table"));
        final String text = element.getText();
        if (!text.contains("Nenhuma entrada de Horário")) {
            return;
        }
        click(By.xpath("//span[@id='Timecode_addButton']/span[2]/a/span[2]"));
    }

}
