package br.com.marcosoft.apropriator.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An Task.
 */
public class Task extends BaseModel {

    private String contexto;

    private ItemTrabalho itemTrabalho;

    private String comentario;

    public Task(String contexto, ItemTrabalho itemTrabalho, String comentario) {
        super();
        this.contexto = contexto;
        this.itemTrabalho = itemTrabalho;
        this.comentario = comentario;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public void setItemTrabalho(ItemTrabalho itemTrabalho) {
        this.itemTrabalho = itemTrabalho;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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
                .append(this.comentario, that.comentario).isEquals();
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
