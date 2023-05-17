package ru.kpfu.itis.exceptions;

import java.io.Serializable;

public class BookNotFound extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    public BookNotFound(String message) {
        this.message = System.currentTimeMillis() + " : " + message;
    }

    public BookNotFound() {
        this("Book is not available now!");
    }

    public String getMessage() {
        return message;
    }

}

