package br.com.marcosoft.apropriator;


/**
 * Ocorre quando os dados estão inconsistentes.
 */
public class InconsistentDataException extends Exception {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -6881671322212830199L;


    public InconsistentDataException(String message) {
        super(message);
    }

}
