package br.com.marcosoft.apropriator;

@SuppressWarnings("serial")
public class ErroInesperado extends RuntimeException {

    public ErroInesperado() {
        super();
    }

    public ErroInesperado(String message) {
        super(message);
    }

}
