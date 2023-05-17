package ru.kpfu.itis.exceptions;

import java.io.Serializable;

public class DefaultError extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;

    public DefaultError(String message) {
        this.message = System.currentTimeMillis() + " : " + message;
    }

    public DefaultError() {
        this("Oops.. try again later!");
    }

    public String getMessage() {
        return message;
    }
}
