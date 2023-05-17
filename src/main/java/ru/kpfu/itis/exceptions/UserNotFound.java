package ru.kpfu.itis.exceptions;

import java.io.Serializable;

public class UserNotFound extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;

    public UserNotFound(String message) {
        this.message = System.currentTimeMillis() + " : " + message;
    }

    public UserNotFound() {
        this("No user with this login!");
    }

    public String getMessage() {
        return message;
    }
}
