package com.example.reteadesocializare.domain.validation;

import com.example.reteadesocializare.domain.User;
import com.example.reteadesocializare.exceptions.ValidationException;

public class UserValidator implements Validator<User> {

    private void validateId(User user) throws ValidationException {
        if (user.getId() == null) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        Long zero = 0L;
        if(user.getId().compareTo(zero) < 0) {
            throw new ValidationException("Id-ul nu poate fi negativ!");
        }
    }

    private void validateName(User user) throws ValidationException {
        if (user.getName() == null) {
            throw new ValidationException("Numele nu poate fi null!");
        }
        if (user.getName().equals("")) {
            throw new ValidationException("Numele nu poate fi vid!");
        }
    }

    private void validateEmail(User user) throws ValidationException {
        if (user.getEmail() == null) {
            throw new ValidationException("Email-ul nu poate fi null!");
        }
        if (user.getEmail().equals("")) {
            throw new ValidationException("Email-ul nu poate fi vid!");
        }
    }

    private void validatePassword(User user) throws ValidationException {
        if (user.getPassword() == null) {
            throw new ValidationException("Parola nu poate fi null!");
        }
        if (user.getPassword().equals("")) {
            throw new ValidationException("Parola nu poate fi vida!");
        }
    }

    @Override
    public void validate(User entity) throws ValidationException {
        if(entity == null){
            throw new ValidationException("Entitatea nu poate fi null!");
        }
        validateId(entity);
        validateName(entity);
        validateEmail(entity);
        validatePassword(entity);
    }
}
