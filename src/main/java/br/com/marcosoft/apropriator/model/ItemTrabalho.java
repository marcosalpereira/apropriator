package br.com.marcosoft.apropriator.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ItemTrabalho extends BaseModel {
    private final int id;

    private final String descricao;

    public ItemTrabalho(int id, String descricao) {
        super();
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return id + ";" + descricao;
    }



    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ItemTrabalho) {
            final ItemTrabalho that = (ItemTrabalho) obj;
            return new EqualsBuilder().append(this.getId(), that.getId()).isEquals();
        }

        return false;
    }

    /**{@inheritDoc}*/
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 7).toHashCode();
    }

}
