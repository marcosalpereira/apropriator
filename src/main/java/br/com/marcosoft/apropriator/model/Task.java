package br.com.marcosoft.apropriator.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.com.marcosoft.apropriator.util.Util;

/**
 * An Task.
 */
public class Task extends BaseModel {

    private final String contexto;

    private final ItemTrabalho itemTrabalho;

    private final String comentario;

	private final List<Integer> itensRecuperarTitulo;

    public Task(String contexto, ItemTrabalho itemTrabalho, String comentario) {
        super();
        this.contexto = contexto;
        this.itemTrabalho = itemTrabalho;
        this.comentario = comentario;
        this.itensRecuperarTitulo = selecionarItensRecuperarTitulo(comentario);
    }

    private List<Integer> selecionarItensRecuperarTitulo(String comentario) {
    	final List<Integer> itens = new ArrayList<Integer>();
		for (final String itemQuery : comentario.split(",", -1)) {
			if (itemQuery.startsWith("?")) {
				int endIndex = itemQuery.indexOf('-');
				if (endIndex == -1) endIndex = itemQuery.length();
				final Integer item =
						Util.parseInt(itemQuery.substring(1, endIndex), 0);
				if (item > 0) {
					itens.add(item);
				}
			}
		}
		return itens;
	}

    public List<Integer> getItensRecuperarTitulo() {
		return itensRecuperarTitulo;
	}

	public String getContexto() {
        return contexto;
    }

    public ItemTrabalho getItemTrabalho() {
        return itemTrabalho;
    }

    public String getComentario() {
        return comentario;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Task) {
            final Task that = (Task) obj;
            return new EqualsBuilder().append(this.contexto, that.contexto)
                .append(this.itemTrabalho, that.itemTrabalho)
                .append(this.comentario, that.comentario)
                .isEquals();
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 5).append(this.contexto).toHashCode();
    }

    @Override
    public String toString() {
        return this.contexto + " " + this.itemTrabalho + " " + this.comentario;
    }

}
