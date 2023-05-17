package ru.kpfu.itis.exceptions;

import java.io.Serializable;

public class ServerError extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;

    public ServerError(String message) {
        this.message = System.currentTimeMillis() + " : " + message;
    }

    public ServerError() {
        this("Book is not available now!");
    }

    public String getMessage() {
        return message;
    }
}
