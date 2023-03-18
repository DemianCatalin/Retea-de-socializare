package com.example.reteadesocializare.service;

import com.example.reteadesocializare.repo.RepoMsg;
import com.example.reteadesocializare.utils.Observable;
import com.example.reteadesocializare.utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class ServiceMessage implements Observable {
    protected RepoMsg repoMsg;
    private final List<Observer> observers = new ArrayList<>();

    public ServiceMessage(RepoMsg repoMsg) {
        this.repoMsg = repoMsg;
    }

    public void save(Long from, Long to, String text, String name) {
        repoMsg.save(from, to, text, name);
        notifyObservers();
    }

    public List<String> findAll(Long id) {
        return repoMsg.findAll(id);
    }

    public String findLast(Long id) {
        return repoMsg.findLast(id);
    }

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
