package br.unicamp.ic.mc322.exception;

public class AutenticacaoException extends RuntimeException {

    public AutenticacaoException(String message) {
        super(message);
    }

    public AutenticacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
