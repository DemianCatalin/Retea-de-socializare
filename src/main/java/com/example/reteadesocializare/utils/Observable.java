package com.example.reteadesocializare.utils;

public interface Observable {
    void addObserver(Observer e);
    void notifyObservers();
}
