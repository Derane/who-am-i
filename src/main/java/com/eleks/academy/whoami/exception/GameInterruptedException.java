package com.eleks.academy.whoami.exception;

public class GameInterruptedException extends RuntimeException {
    public GameInterruptedException() {
        super();
    }

    public GameInterruptedException(String message) {
        super(message);
    }

    public GameInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
