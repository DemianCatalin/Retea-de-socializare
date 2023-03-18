package com.example.reteadesocializare.graph;

import com.example.reteadesocializare.utils.PairEntity;
import com.example.reteadesocializare.exceptions.DuplicatedElementException;

import java.util.Collection;

public interface Graph<Nod, Muchie extends PairEntity<Nod, Nod>> {
    void addNode(Nod nod) throws DuplicatedElementException;

    void addEdge(Muchie muchie) throws DuplicatedElementException;

    Collection<Nod> getNodes();
}