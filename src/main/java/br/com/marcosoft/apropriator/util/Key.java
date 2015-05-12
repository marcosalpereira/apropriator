package br.com.marcosoft.apropriator.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Objeto que agrupa varias chaves.
 */
public class Key {

    /**
     * Objetos que compoem a chave.
     */
    private final List<Object> chaves;

    /**
     * Construtor.
     * @param chaves objetos que compoem a chave
     */
    public Key(Object... chaves) {
        this.chaves = convertToList(chaves);
    }

    /**
     * Converte o array de objetos (que podem conter cada elemento uma lista)
     * para uma unica lista com todos estes elementos.
     * @param valores valores
     * @return array de objetos
     */
    private List<Object> convertToList(Object[] valores) {
        final List<Object> list = new ArrayList<Object>();
        for (final Object valor : valores) {
            if (valor == null) {
                continue;
            } else if (valor instanceof Collection<?>) {
                list.addAll((Collection<?>) valor);
            } else if (valor.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(valor); i++) {
                    list.add(Array.get(valor, i));
                }
            } else {
                list.add(valor);
            }
        }
        return list;
    }

    public List<Object> getChaves() {
        return chaves;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Key) {
            final Key that = (Key) obj;
            return new EqualsBuilder().append(this.chaves, that.chaves).isEquals();
        }

        return false;
    }

    /**{@inheritDoc}*/
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1212197, 21119917).append(chaves).toHashCode();
    }

    @Override
    public String toString() {
        return chaves.toString();
    }
}
