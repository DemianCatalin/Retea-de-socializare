package com.example.reteadesocializare.controller;

import com.example.reteadesocializare.domain.Friendship;
import com.example.reteadesocializare.domain.User;
import com.example.reteadesocializare.domain.validation.FriendshipValidator;
import com.example.reteadesocializare.domain.validation.UserValidator;
import com.example.reteadesocializare.domain.validation.Validator;
import com.example.reteadesocializare.repo.FriendshipDBRepo;
import com.example.reteadesocializare.repo.RepoMsg;
import com.example.reteadesocializare.repo.Repository;
import com.example.reteadesocializare.repo.UserDBRepo;
import com.example.reteadesocializare.service.ServiceFriendship;
import com.example.reteadesocializare.service.ServiceMessage;
import com.example.reteadesocializare.service.ServiceUser;

public class Controller {
    private final ServiceUser userService;
    private final ServiceFriendship prietenieService;
    private final ServiceMessage messageService;

    public Controller() {
        Validator<User> validatorUser = new UserValidator();
        Validator<Friendship> validatorPrietenie = new FriendshipValidator();

        RepoMsg repoMsg = new RepoMsg("jdbc:postgresql://localhost:5432/postgres", "postgres", "parola");
        Repository<Long, User> repoUser = new UserDBRepo("jdbc:postgresql://localhost:5432/postgres", "postgres", "parola", validatorUser);
        Repository<Long, Friendship> repoPrietenii = new FriendshipDBRepo("jdbc:postgresql://localhost:5432/postgres", "postgres", "parola", validatorPrietenie);

        messageService = new ServiceMessage(repoMsg);
        userService = new ServiceUser(repoUser, repoPrietenii);
        prietenieService = new ServiceFriendship(repoPrietenii, repoUser);
    }

    public ServiceUser getServiceUser() {
        return userService;
    }

    public ServiceFriendship getServiceFriendship() {
        return prietenieService;
    }

    public ServiceMessage getServiceMessage() { return messageService; }
}
