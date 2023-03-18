package com.example.reteadesocializare.ui.console;

import com.example.reteadesocializare.controller.Controller;
import com.example.reteadesocializare.domain.User;
import com.example.reteadesocializare.domain.UserFields;
import com.example.reteadesocializare.domain.UserDetails;
import com.example.reteadesocializare.utils.Utils;

import java.util.Scanner;

public class UIUseri extends AbstractUI {

    public UIUseri(Controller controller, Scanner scanner) {
        super(controller, scanner);
    }

    @Override
    public void execute(String[] args) {
        if(args.length == 1){
            System.out.println("Invalid subcommand! Try one of the following:");
            System.out.println("add, remove, update, find, findall");
            return;
        }
        switch (args[1]) {
            case "add" -> addUser();
            case "remove" -> removeUser();
            case "update" -> updateUser();
            case "find" -> findUser();
            case "findall" -> findAll();
            default -> {
                System.out.println("Invalid command! Try one of the following:");
                System.out.println("add, remove, update, find, findall");
            }
        }
    }

    private UserDetails readDetails(){
        UserDetails details = new UserDetails();
        System.out.print("Introduceti numele user-ului: ");
        details.put(UserFields.Nume, scanner.nextLine());
        System.out.print("Introduceti emailul user-ului: ");
        details.put(UserFields.Email, scanner.nextLine());
        System.out.print("Introduceti parola user-ului: ");
        details.put(UserFields.Password, scanner.nextLine());
        return details;
    }

    private void findAll() {
        if(controller.getServiceUser().findAll().isEmpty()){
            System.err.println("Nu exista useri!");
            return;
        }
        Utils.tryExecute(() -> controller.getServiceUser().findAll().forEach(System.out::println));
    }

    private void findUser() {
        Utils.tryExecute(() -> {
            Long id = readId(null);
            User user = controller.getServiceUser().findOne(id);
            System.out.println(user);
        });
    }

    private void updateUser() {
        Utils.tryExecute(() -> {
            Long id = readId("Introduceti id-ul utilizatorului pe care doriti sa il actualizati:");
            UserDetails details = readDetails();
            controller.getServiceUser().update(id, details);
        });
    }

    private void removeUser() {
        Utils.tryExecute(() -> {
            Long id = readId("Introduceti id-ul user-ului pe care doriti sa il stergeti: ");
            controller.getServiceUser().remove(id);
        });
    }

    private void addUser() {
        UserDetails details = readDetails();
        Utils.tryExecute(() -> controller.getServiceUser().add(details));
    }
}
