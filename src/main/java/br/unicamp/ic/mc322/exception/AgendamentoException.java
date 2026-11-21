package br.unicamp.ic.mc322.exception;

public class AgendamentoException extends RuntimeException {

    public AgendamentoException(String message) {
        super(message);
    }

    public AgendamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}
