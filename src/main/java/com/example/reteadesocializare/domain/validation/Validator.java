package com.example.reteadesocializare.domain.validation;
import com.example.reteadesocializare.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}