package br.com.marcosoft.apropriator.model;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import br.com.marcosoft.apropriator.util.Util;

/**
 * Sumario da atividade.
 */
public class TaskSummary extends BaseModel {

    private final Task task;

    private int sum;

    public TaskSummary(Task task) {
        this.task = task;
    }

    public TaskSummary(Task task, int sum) {
        super();
        this.task = task;
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public Task getTask() {
        return task;
    }

    public void add(int duracao) {
        sum += duracao;
    }

    public String getComentario() {
        return task.getComentario();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TaskSummary) {
            final TaskSummary that = (TaskSummary) obj;
            return new EqualsBuilder().append(this.task, that.task)
                .append(this.sum, that.sum).isEquals();
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 3625).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append(task).append(sum)
            .toString();
    }

	public Collection<Integer> getIdsAtividades() {
		final Collection<Integer> ids = new LinkedHashSet<Integer>();
		final int posTraco = getComentario().indexOf('-');
		if (posTraco > 0) {
			final String sids = getComentario().substring(0, posTraco);
			for (final String sid : sids.split(",", -1)) {
				final int id = Util.parseInt(sid, 0);
				if (id > 0) {
					ids.add(id);
				}
			}
		}
		return ids;
	}
}
