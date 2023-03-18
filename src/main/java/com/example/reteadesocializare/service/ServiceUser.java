package com.example.reteadesocializare.service;

import com.example.reteadesocializare.domain.Friendship;
import com.example.reteadesocializare.domain.User;
import com.example.reteadesocializare.domain.UserFields;
import com.example.reteadesocializare.domain.UserDetails;
import com.example.reteadesocializare.exceptions.NotExistentException;
import com.example.reteadesocializare.repo.Repository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class ServiceUser {
    protected Repository<Long, User> repoUser;
    protected Repository<Long, Friendship> repoPrietenii;
    protected static Long idGenerator = 0L;

    public ServiceUser(Repository<Long, User> repoUser, Repository<Long, Friendship> repoPrietenii) {
        this.repoUser = repoUser;
        this.repoPrietenii = repoPrietenii;
        idGenerator = 1L;
        repoUser.findAll().stream().mapToLong(User::getId).max().ifPresent(mx -> idGenerator = mx + 1);
    }

    public void add(UserDetails details) {
        User user = new User(idGenerator,
                (String) details.get(UserFields.Nume),
                (String) details.get(UserFields.Email),
                (String) details.get(UserFields.Password));
        repoUser.save(user);
        idGenerator++;
    }

    public void remove(Long id) {
        User user = repoUser.findByID(id);
        List<Friendship> prietenii = new LinkedList<>(repoPrietenii.findAll());
        prietenii.forEach(prietenie -> {
            if (prietenie.contains(user.getId()))
                repoPrietenii.remove(prietenie.getId());
        });
        repoUser.remove(id);
    }

    public void update(Long id, UserDetails details) {
        User newUser = new User(id,
                (String) details.get(UserFields.Nume),
                (String) details.get(UserFields.Email),
                (String) details.get(UserFields.Password));
        repoUser.update(id, newUser);
    }

    public User findOne(Long id) {
        User user = repoUser.findByID(id);
        if(user == null)
            throw new NotExistentException("Userul nu exista!");
        return user;
    }

    public User findOneByPredicate(Predicate<User> predicate) {
        User user = repoUser.findByPredicate(predicate);
        if(user == null)
            throw new NotExistentException("Userul nu exista!");
        return user;
    }

    public Collection<User> findAll() {
        return repoUser.findAll();
    }
}
