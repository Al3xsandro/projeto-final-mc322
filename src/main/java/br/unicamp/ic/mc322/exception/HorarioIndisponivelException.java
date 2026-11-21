package br.unicamp.ic.mc322.exception;

public class HorarioIndisponivelException extends RuntimeException {

    public HorarioIndisponivelException(String message) {
        super(message);
    }

    public HorarioIndisponivelException(String message, Throwable cause) {
        super(message, cause);
    }
}
