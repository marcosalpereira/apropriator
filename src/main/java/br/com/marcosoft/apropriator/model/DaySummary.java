package br.com.marcosoft.apropriator.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DaySummary {
    private final int day;

    private int sum;

    public DaySummary(int day, int sum) {
        super();
        this.day = day;
        this.sum = sum;
    }

    public int getDay() {
        return day;
    }

    public double getHoras() {
        return sum / 60.0;
    }
    
    public int getSum() {
		return sum;
	}

    public void add(int duracao) {
        sum += duracao;
    }



    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DaySummary) {
            final DaySummary that = (DaySummary) obj;
            return new EqualsBuilder()
                .append(this.day, that.day)
                .append(this.sum, that.sum)
                .isEquals();
        }

        return false;
    }

    /**{@inheritDoc}*/
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 7).toHashCode();
    }

    @Override
    public String toString() {
        return this.day + ":" + this.sum;
    }

}
