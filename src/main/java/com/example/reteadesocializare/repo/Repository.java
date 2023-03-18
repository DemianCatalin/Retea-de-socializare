package com.example.reteadesocializare.repo;

import com.example.reteadesocializare.domain.Entity;
import com.example.reteadesocializare.domain.Friendship;
import com.example.reteadesocializare.exceptions.DuplicatedElementException;

import java.util.Collection;
import java.util.function.Predicate;

public interface Repository<ID, E extends Entity<ID>> {
    E findByID(ID id) throws IllegalArgumentException;

    E findByPredicate(Predicate<E> predicate) throws IllegalArgumentException;

    Collection<E> findAll();

    E save(E entity) throws IllegalArgumentException, DuplicatedElementException;

    E remove(ID id) throws IllegalArgumentException;

    E update(ID id, E entity) throws IllegalArgumentException;

    void removeAll();
}
