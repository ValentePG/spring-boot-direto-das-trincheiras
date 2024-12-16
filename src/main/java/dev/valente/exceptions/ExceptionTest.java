package dev.valente.exceptions;


public class ExceptionTest extends RuntimeException{
    public ExceptionTest() {
        super();
    }

    public ExceptionTest(String message) {
        super(message);
    }
}
