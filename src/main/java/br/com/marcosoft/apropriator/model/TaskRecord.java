package br.com.marcosoft.apropriator.model;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.com.marcosoft.apropriator.util.Util;

/**
 * An Activity.
 */
public class TaskRecord extends BaseModel {

    private int numeroLinha;

    private boolean registrado;

    private boolean apropriado;

    private Date data;

    private Task task;

    private String horaInicio;

    private String horaTermino;

    public enum OpcaoFinalizacao { NENHUM, TAREFA, ATIVIDADE, ATIVIDADE_TAREFA };

    private OpcaoFinalizacao finalizar;

    private int duracao;

    private Calendar calendar;

	private String novoComentario;

	public void setNovoComentario(String novoComentario) {
		this.novoComentario = novoComentario;
	}

	public String getNovoComentario() {
		return novoComentario;
	}

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getNumeroLinha() {
        return numeroLinha;
    }

    public void setApropriado(boolean apropriado) {
        this.apropriado = apropriado;
    }

    public boolean isApropriado() {
        return apropriado;
    }

    public void setNumeroLinha(int numeroLinha) {
        this.numeroLinha = numeroLinha;
    }

    public boolean isRegistrado() {
        return registrado;
    }

    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }

    public Date getData() {
        return data;
    }

    public int getSemana() {
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public int getDiaSemana() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    public void setData(Date data) {
        calendar = Calendar.getInstance();
        calendar.setTime(data);
        this.data = data;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public OpcaoFinalizacao isFinalizar() {
        return finalizar;
    }

    public void setFinalizar(OpcaoFinalizacao finalizar) {
		this.finalizar = finalizar;
	}

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getMinutoInicio() {
        return converterMinutos(horaInicio);
    }

    public int getMinutoTermino() {
        return converterMinutos(horaTermino);
    }

    private int converterMinutos(String horaMinuto) {
        final String[] split = horaMinuto.split("\\.");
        final int min = Util.parseInt(split[0], 0) * 60
            + Util.parseInt(split[1], 0);
        return min;
    }

    public boolean overlaps(TaskRecord other) {
        if (!this.data.equals(other.data)
            || this.isRegistrado()
            || other.isRegistrado()) {
            return false;
        }
        return _overlaps(other) || other._overlaps(this);
    }

    private boolean _overlaps(TaskRecord other) {
        if (this.getMinutoInicio() >= other.getMinutoInicio()
            && this.getMinutoInicio() < other.getMinutoTermino()) {
            return true;
        }
        if (this.getMinutoTermino() > other.getMinutoInicio()
            && this.getMinutoTermino() <= other.getMinutoTermino()) {
            return true;
        }

        return false;
    }



    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TaskRecord) {
            final TaskRecord that = (TaskRecord) obj;
            return new EqualsBuilder()
                .append(this.numeroLinha, that.numeroLinha)
                .isEquals();
        }

        return false;
    }

    @Override
    public String toString() {
        return "" + numeroLinha;
    }

    /**{@inheritDoc}*/
    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 5261)
            .append(this.data)
            .toHashCode();
    }

    public OpcaoFinalizacao getFinalizar() {
		return finalizar;
	}

	public boolean isRecuperarTituloItemTrabalho() {
		return !task.getItensRecuperarTitulo().isEmpty();
	}

}
