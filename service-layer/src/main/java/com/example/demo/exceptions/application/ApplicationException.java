package com.example.demo.exceptions.application;

import static java.lang.String.format;

public class ApplicationException extends RuntimeException {

    public ApplicationException(long id) {
        super(format("Application with id: %d does not exist!", id));
    }

    public ApplicationException(String message) {
        super(message);
    }
}
