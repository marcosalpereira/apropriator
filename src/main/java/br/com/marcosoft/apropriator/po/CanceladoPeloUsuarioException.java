package br.com.marcosoft.apropriator.po;

public class CanceladoPeloUsuarioException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    @Override
    public String getMessage() {
        return "Cancelado pelo usuário";
    }
    
}
