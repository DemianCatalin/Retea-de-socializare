package com.example.reteadesocializare.domain;

import com.example.reteadesocializare.utils.PairEntity;
import com.example.reteadesocializare.utils.Utils;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> implements PairEntity<Long, Long> {
    private final Long id_user1;
    private final Long id_user2;
    private final LocalDateTime friendsFrom;
    private final FriendshipState state;

    public Friendship(Long id, Long id1, Long id2, LocalDateTime friendsFrom, FriendshipState state) {
        super(id);
        id_user1 = id1;
        id_user2 = id2;
        this.friendsFrom = friendsFrom;
        this.state = state;
    }

    @Override
    public Long getFirst() {
        return id_user1;
    }

    @Override
    public Long getSecond() {
        return id_user2;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public FriendshipState getState(){
        return state;
    }

    public boolean contains(Long id){
        return id_user1.equals(id) || id_user2.equals(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship prietenie = (Friendship) o;
        return getId().equals(prietenie.getId()) ||
                id_user1.equals(prietenie.id_user1) && id_user2.equals(prietenie.id_user2) ||
                id_user1.equals(prietenie.id_user2) && id_user2.equals(prietenie.id_user1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), id_user1, id_user2);
    }

    @Override
    public String toString() {
        return "id: " + id_user1 + " - id: " + id_user2 + " din \'" + friendsFrom.format(Utils.DATE_TIME_FORMATTER) + '\'';
    }
}
