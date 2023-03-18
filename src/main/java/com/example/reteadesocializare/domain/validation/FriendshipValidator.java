package com.example.reteadesocializare.domain.validation;

import com.example.reteadesocializare.domain.Friendship;
import com.example.reteadesocializare.exceptions.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {

    private void validateId(Long id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("Id-ul nu poate fi null!");
        }
        if(id.compareTo(0L) < 0) {
            throw new ValidationException("Id-ul nu poate fi negativ!");
        }
    }

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity == null)
            throw new ValidationException("Prietenia nu poate fi vida!");
        if (entity.getFirst() == null || entity.getSecond() == null)
            throw new ValidationException("Prietenia trebuie sa aiba cel putin un user!");
        if (entity.getFirst().equals(entity.getSecond()))
            throw new ValidationException("Un user nu poate fi prieten cu el insusi!");
        validateId(entity.getFirst());
        validateId(entity.getSecond());
    }
}
