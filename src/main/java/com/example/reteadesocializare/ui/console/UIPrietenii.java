package com.example.reteadesocializare.ui.console;

import com.example.reteadesocializare.controller.Controller;
import com.example.reteadesocializare.domain.Friendship;
import com.example.reteadesocializare.domain.FriendshipState;
import com.example.reteadesocializare.domain.User;
import com.example.reteadesocializare.exceptions.ParsingException;
import com.example.reteadesocializare.utils.Pair;
import com.example.reteadesocializare.utils.Utils;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Set;

public class UIPrietenii extends AbstractUI {

    private LocalDateTime readDateTime(){
        System.out.println("Do you want to update the moment? [Yes/No]");
        if(scanner.next().matches("^[yY]*"))
        {
            System.out.println("Do you want to use now for the moment of friendship? [Yes/No]");
            if(scanner.next().matches("^[yY]*"))
                return LocalDateTime.now();
            int year, month, day, hour, minute;
            System.out.print("Introduceti anul: ");
            year = scanner.nextInt();
            System.out.print("Introduceti luna: ");
            month = scanner.nextInt();
            System.out.print("Introduceti ziua: ");
            day = scanner.nextInt();
            System.out.print("Introduceti ora: ");
            hour = scanner.nextInt();
            System.out.print("Introduceti minutul: ");
            minute = scanner.nextInt();
            try { return LocalDateTime.of(year, month, day, hour, minute); }
            catch (DateTimeException ex) { throw new ParsingException("Acest moment nu este valid!"); }
        }
        return null;
    }

    public UIPrietenii(Controller controller, Scanner scanner) {
        super(controller, scanner);
    }

    @Override
    public void execute(String[] args) {
        Runnable errorMsg = () -> {
        };
        if(args.length < 2){
            errorMsg.run();
            return;
        }
        switch (args[1]) {
            case "add" -> addFriendship();
            case "remove" -> removeFriendship();
            case "update" -> updateFriendship();
            case "find" -> findFriendship();
            case "findall" -> findAll();
            case "nrcomunitati" -> numberOfComunities();
            case "cmsc" -> cmsc();
            default -> {
                System.out.println("Invalid subcommand! Try one of the following:");
                System.out.println("add, remove, update, find, findall, nrcomunitati, cmsc");
            }
        }
    }

    private void cmsc() {
        Utils.tryExecute(() -> {
            Pair<Set<User>, Integer> com = controller.getServiceFriendship().getTheMostSociableCommunity();
            System.out.println("Cea mai sociabila comunitate are scorul "+com.second()+" si este formata din:");
            com.first().forEach(System.out::println);
        });
    }

    private void numberOfComunities() {
        Utils.tryExecute(() ->
            System.out.println("Numarul de comunitati este: " + controller.getServiceFriendship().getNumberOfCommunities())
        );
    }

    private void findAll() {
        if(!controller.getServiceFriendship().findAll().isEmpty()) {
            System.out.println("Prietenii:");
            Utils.tryExecute(() -> controller.getServiceFriendship().findAll().forEach(prietenie -> {
                User user1 = controller.getServiceUser().findOne(prietenie.getFirst());
                User user2 = controller.getServiceUser().findOne(prietenie.getSecond());
                System.out.println(prietenie.getId()+". " + user1.getName() + " este prieten cu " + user2.getName() + " din '" +prietenie.getFriendsFrom().format(Utils.DATE_TIME_FORMATTER)+'\'');
            }));
        }
        else {
            System.err.println("Nu exista nici o prietenie!");
        }
    }

    private void findFriendship() {
        Utils.tryExecute(() -> {
            Long id = readId("Id-ul prieteniei:");
            Friendship prietenie = controller.getServiceFriendship().findOne(id);
            System.out.println(prietenie);
        });
    }

    private void updateFriendship() {
        Utils.tryExecute(() -> {
            Long id = readId("Id prietenie:");
            Long id1 = readId("Id user 1:");
            Long id2 = readId("Id user 2:");
            LocalDateTime moment = readDateTime();
            controller.getServiceFriendship().update(id, id1, id2, null, FriendshipState.Accepted);
        });
    }

    private void removeFriendship() {
        Utils.tryExecute(() -> {
            Long id = readId(null);
            controller.getServiceFriendship().remove(id);
        });
    }

    private void addFriendship() {
        Utils.tryExecute(() -> {
            Long id1 = readId("Id user 1:");
            Long id2 = readId("Id user 2:");
            controller.getServiceFriendship().add(id1, id2, LocalDateTime.now(), FriendshipState.Accepted);
        });
    }
}
