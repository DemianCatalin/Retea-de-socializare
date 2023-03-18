package com.example.reteadesocializare.service;

import com.example.reteadesocializare.domain.Friendship;
import com.example.reteadesocializare.domain.FriendshipState;
import com.example.reteadesocializare.domain.User;
import com.example.reteadesocializare.exceptions.DuplicatedElementException;
import com.example.reteadesocializare.exceptions.NotExistentException;
import com.example.reteadesocializare.graph.GraphArgorithms;
import com.example.reteadesocializare.graph.Graf;
import com.example.reteadesocializare.repo.Repository;
import com.example.reteadesocializare.utils.Pair;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceFriendship {
    protected Repository<Long, User> repoUser;
    protected Repository<Long, Friendship> repoPrietenii;
    protected Long idGenerator;

    public ServiceFriendship(Repository<Long, Friendship> repoPrietenii, Repository<Long, User> repoUser) {
        this.repoPrietenii = repoPrietenii;
        this.repoUser = repoUser;
        idGenerator = 1L;
        repoPrietenii.findAll().stream().mapToLong(Friendship::getId).max().ifPresent(id -> idGenerator = id + 1);
    }

    public void add(Long id1, Long id2, LocalDateTime friendsFrom, FriendshipState prietenieState) {
        if (repoUser.findByID(id1) == null || repoUser.findByID(id2) == null)
            throw new NotExistentException("Unul dintre useri nu exista!");
        Friendship prietenie = new Friendship(idGenerator, id1, id2, friendsFrom, prietenieState);
        if (repoPrietenii.findByPredicate(pr -> pr.equals(prietenie)) != null)
            throw new DuplicatedElementException("Prietenia exista deja!");
        repoPrietenii.save(prietenie);
        idGenerator++;
    }

    public void remove(Long id) {
        repoPrietenii.remove(id);
    }

    public void update(Long id, Long id1, Long id2, LocalDateTime friendsFrom, FriendshipState prietenieState) {
        Friendship oldPrietenie = repoPrietenii.findByID(id);
        if (oldPrietenie == null)
            throw new NotExistentException("Prietenia nu exista!");
        if(friendsFrom == null)
            friendsFrom = oldPrietenie.getFriendsFrom();
        Friendship newPrietenie = new Friendship(id, id1, id2, friendsFrom, prietenieState);
        repoPrietenii.update(id, newPrietenie);
    }

    public Friendship findOne(Long id) throws NotExistentException {
        Friendship prietenie = repoPrietenii.findByID(id);
        if(prietenie == null)
            throw new NotExistentException("Prietenia nu exista!");
        return prietenie;
    }

    public Collection<Friendship> findAll() {
        return repoPrietenii.findAll();
    }

    private Graf<Long, Friendship> getGraf() {
        Graf<Long, Friendship> graf = new Graf<>();
        repoUser.findAll().stream().map(User::getId).forEach(graf::addNode);
        List<Friendship> friendships = new ArrayList<>(repoPrietenii.findAll());
        friendships.forEach(graf::addEdge);
        return graf;
    }

    public Integer getNumberOfCommunities(){
        List<Graf<Long, Friendship>> comunitati = getGraf().components();
        return comunitati.size();
    }

    public Pair<Set<User>, Integer> getTheMostSociableCommunity(){
        Pair<Graf<Long, Friendship>, Integer> community;
        Graf<Long, Friendship> graf = getGraf();
        community = GraphArgorithms.componentWithLongestPath(graf);
        Set<User> users = community.first().getNodes().stream().map(repoUser::findByID).collect(Collectors.toSet());
        return new Pair<>(users, community.second());
    }
}
