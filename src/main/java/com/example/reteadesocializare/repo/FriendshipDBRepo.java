package com.example.reteadesocializare.repo;

import com.example.reteadesocializare.domain.Friendship;
import com.example.reteadesocializare.domain.FriendshipState;
import com.example.reteadesocializare.domain.validation.Validator;
import com.example.reteadesocializare.exceptions.DuplicatedElementException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class FriendshipDBRepo implements Repository<Long, Friendship> {
    protected final Validator<Friendship> validator;
    protected final String url;
    protected final String username;
    protected final String password;


    public FriendshipDBRepo(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    private Friendship extractFriendship(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long id1 = resultSet.getLong("id_user1");
        Long id2 = resultSet.getLong("id_user2");
        LocalDate data = resultSet.getDate("data").toLocalDate();
        LocalTime ora = resultSet.getTime("ora").toLocalTime();
        FriendshipState status = FriendshipState.valueOf(resultSet.getString("status"));
        return new Friendship(id, id1, id2, LocalDateTime.of(data, ora), status);
    }

    @Override
    public Friendship save(Friendship friendship) throws IllegalArgumentException, DuplicatedElementException {
        if (friendship == null)
            throw new IllegalArgumentException("Prietenia nu poate fi null!");
        validator.validate(friendship);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Prietenii\" VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setLong(1, friendship.getId());
            statement.setLong(2, friendship.getFirst());
            statement.setLong(3, friendship.getSecond());
            statement.setDate(4, Date.valueOf(friendship.getFriendsFrom().toLocalDate()));
            statement.setTime(5, Time.valueOf(friendship.getFriendsFrom().toLocalTime()));
            statement.setString(6, friendship.getState().toString());
            statement.executeUpdate();
            return friendship;
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint"))
                throw new DuplicatedElementException("Prietenia exista deja!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship findByID(Long id) throws IllegalArgumentException {
        if (id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Prietenii\" WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractFriendship(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship findByPredicate(Predicate<Friendship> predicate) throws IllegalArgumentException {
        if (predicate == null)
            throw new IllegalArgumentException("Predicate-ul nu poate fi null!");
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Prietenii\"")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    if (predicate.test(extractFriendship(resultSet)))
                        return extractFriendship(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Prietenii\"")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Long id1 = resultSet.getLong("id_user1");
                    Long id2 = resultSet.getLong("id_user2");
                    LocalDate data = resultSet.getDate("data").toLocalDate();
                    LocalTime time = resultSet.getTime("ora").toLocalTime();
                    FriendshipState state = FriendshipState.valueOf(resultSet.getString("status"));
                    friendships.add(new Friendship(id, id1, id2, LocalDateTime.of(data, time), state));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship remove(Long id) throws IllegalArgumentException {
        if (id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        Friendship friendship = findByID(id);
        if (friendship == null)
            return null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Prietenii\" WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
            return friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Friendship remove(Predicate<Friendship> predicate) throws IllegalArgumentException {
        if (predicate == null)
            throw new IllegalArgumentException("Predicate-ul nu poate fi null!");
        Friendship friendship = findByPredicate(predicate);
        if (friendship == null)
            return null;
        return remove(friendship.getId());
    }

    @Override
    public Friendship update(Long id, Friendship entity) throws IllegalArgumentException {
        if (id == null)
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        if (entity == null)
            throw new IllegalArgumentException("Entitatea nu poate fi null!");
        String sql = "UPDATE \"Prietenii\" SET id_user1 = ?, id_user2 = ?, \"data\" = ?, \"ora\" = ?, \"status\" = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, entity.getFirst());
            statement.setLong(2, entity.getSecond());
            statement.setDate(3, Date.valueOf(entity.getFriendsFrom().toLocalDate()));
            statement.setTime(4, Time.valueOf(entity.getFriendsFrom().toLocalTime()));
            statement.setString(5, entity.getState().toString());
            statement.setLong(6, id);
            int changes = statement.executeUpdate();
            if (changes == 0)
                return null;
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Prietenii\"")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}