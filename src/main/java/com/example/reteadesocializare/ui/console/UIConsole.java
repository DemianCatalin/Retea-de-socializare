package com.example.reteadesocializare.ui.console;

import com.example.reteadesocializare.controller.Controller;
import com.example.reteadesocializare.ui.UI;

import java.util.Scanner;

public class UIConsole implements UI {

    private final UIPrietenii uiPrietenii;
    private final UIUseri uiUseri;
    private final Scanner scanner;

    public UIConsole(Controller controller){
        scanner = new Scanner(System.in);
        uiPrietenii = new UIPrietenii(controller, scanner);
        uiUseri = new UIUseri(controller, scanner);
    }

    public void run() {
        while (true){
            String[] args = scanner.nextLine().split(" ");
            execute(args);
        }
    }

    private void execute(String[] args) {
        switch (args[0]) {
            case "prietenii" -> uiPrietenii.execute(args);
            case "useri" -> uiUseri.execute(args);
            default -> System.out.println("prietenii, useri");
        }
    }
}
