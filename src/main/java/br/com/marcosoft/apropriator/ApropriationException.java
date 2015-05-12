package br.com.marcosoft.apropriator;

/**
 * My Application Exception.
 */
public class ApropriationException extends Exception {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -6881671322212830199L;

    public ApropriationException(String message) {
        super(message);
    }

    public ApropriationException(Throwable cause) {
        super(cause);
    }

}
