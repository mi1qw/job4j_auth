package com.example.rest.Exception;

public class EmptyArgumentException extends RuntimeException {
    EmptyArgumentException(final String message) {
        super(message);
    }
}
