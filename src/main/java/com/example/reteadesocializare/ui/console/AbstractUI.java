package com.example.reteadesocializare.ui.console;

import com.example.reteadesocializare.controller.Controller;

import java.util.Scanner;

public abstract class AbstractUI {
    protected final Scanner scanner;
    protected final Controller controller;

    public AbstractUI(Controller controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    protected Long readId(String msg) {
        if(msg == null) msg = "Introduceti id-ul";
        System.out.print(msg + " ");
        return scanner.nextLong();
    }

    public abstract void execute(String[] args);
}
