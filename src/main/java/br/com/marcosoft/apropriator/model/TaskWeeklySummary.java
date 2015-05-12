package br.com.marcosoft.apropriator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import br.com.marcosoft.apropriator.util.Util;

/**
 * Soma da atividades na semana.
 */
public class TaskWeeklySummary extends BaseModel {
    private String contexto;

    private ItemTrabalho itemTrabalho;

    private Date dataInicio;

    private List<DaySummary> daysSummary = new ArrayList<DaySummary>(7);

    private boolean apropriado;

    private final List<TaskRecord> tasksRecords = new ArrayList<TaskRecord>();

    public TaskWeeklySummary(String contexto, ItemTrabalho itemTrabalho, Date dataInicio) {
        super();
        this.contexto = contexto;
        this.itemTrabalho = itemTrabalho;
        this.dataInicio = dataInicio;
    }

    public TaskWeeklySummary() {
	}


    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public ItemTrabalho getItemTrabalho() {
        return itemTrabalho;
    }

    public void setItemTrabalho(ItemTrabalho itemTrabalho) {
        this.itemTrabalho = itemTrabalho;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public List<DaySummary> getDaysSummary() {
        return daysSummary;
    }

    public void setDaysSummary(List<DaySummary> daysSummary) {
        this.daysSummary = daysSummary;
    }

    public void setApropriado(boolean apropriado) {
        this.apropriado=apropriado;
    }

    public boolean isApropriado() {
        return apropriado;
    }

    public DaySummary getDaySummary(int diaSemana) {
        for (final DaySummary day : this.daysSummary) {
            if (day.getDay() == diaSemana) {
                return day;
            }
        }
        final DaySummary daySummary = new DaySummary(diaSemana, 0);
        this.daysSummary.add(daySummary);
        return daySummary;
    }



    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TaskWeeklySummary) {
            final TaskWeeklySummary that = (TaskWeeklySummary) obj;
            return new EqualsBuilder()
                .append(this.contexto, that.contexto)
                .append(this.itemTrabalho, that.itemTrabalho)
                .append(this.daysSummary, that.daysSummary)
                .isEquals();
        }

        return false;
    }

    /**{@inheritDoc}*/
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 3).append(this.contexto).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append(Util.formatDate(this.dataInicio))
            .append(this.itemTrabalho)
            .append(this.daysSummary)
            .toString();
    }

    public List<TaskRecord> getTaskRecords() {
        return tasksRecords;
    }

    public void adicionar(TaskRecord taskRecord) {
        tasksRecords.add(taskRecord);
        final DaySummary daySummary = getDaySummary(taskRecord.getDiaSemana());
        daySummary.add(taskRecord.getDuracao());
    }

    public TaskWeeklySummary somar(TaskWeeklySummary other) {
        final TaskWeeklySummary ret = new TaskWeeklySummary(contexto, itemTrabalho, dataInicio);
        for (final DaySummary daySummary : this.daysSummary) {
        	ret.getDaySummary(daySummary.getDay()).add(daySummary.getSum());
        }
        for (final DaySummary daySummary : other.daysSummary) {
        	ret.getDaySummary(daySummary.getDay()).add(daySummary.getSum());
        }
        return ret;
    }

}
