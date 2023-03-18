package com.example.reteadesocializare.exceptions;

public class NotExistentException extends RepositoryException {
    public NotExistentException(String message) {
        super(message);
    }
}
