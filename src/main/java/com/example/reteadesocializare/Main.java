package com.example.reteadesocializare;

import com.example.reteadesocializare.controller.Controller;
import com.example.reteadesocializare.ui.console.UIConsole;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        UIConsole uiConsole = new UIConsole(controller);
        uiConsole.run();
    }
}