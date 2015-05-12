package br.com.marcosoft.apropriator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Soma da atividades dia-a-dia.
 */
public class TaskDailySummary extends BaseModel {

    private Date data;

    private List<Task> tasks = new ArrayList<Task>();

    private int sum;

    private boolean apropriado;

    public Date getData() {
        return this.data;
    }

    public void setData(final Date data) {
        this.data = data;
    }

    public Task getFirstTask() {
        return this.tasks.get(0);
    }

    public void setTasks(final List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getSum() {
        return this.sum;
    }

    public void setSum(final int duracao) {
        this.sum = duracao;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TaskDailySummary) {
            final TaskDailySummary that = (TaskDailySummary) obj;
            return new EqualsBuilder()
                .append(this.getData(), that.getData())
                .append(this.getFirstTask(), that.getFirstTask())
                .isEquals();
        }

        return false;
    }

    /**{@inheritDoc}*/
    @Override
    public int hashCode() {
        return new HashCodeBuilder(123, 1235)
            .append(this.getData())
            .append(this.getFirstTask())
            .toHashCode();
    }

    @Override
    public String toString() {
        return this.tasks + "," + this.data + "," + this.sum;
    }

    public void setApropriado(final boolean apropriado) {
        this.apropriado = apropriado;
    }

    public boolean isApropriado() {
        return this.apropriado;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

}
