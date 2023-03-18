package com.example.reteadesocializare.repo;

import com.example.reteadesocializare.domain.User;
import com.example.reteadesocializare.domain.validation.Validator;
import com.example.reteadesocializare.exceptions.DuplicatedElementException;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class UserDBRepo implements Repository<Long, User> {
    private final Validator<User> validator;
    private final String url;
    private final String username;
    private final String password;

    public UserDBRepo(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findByID(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        }
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Useri\" WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nume = resultSet.getString("nume");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    return new User(id, nume, password, email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findByPredicate(Predicate<User> predicate) throws IllegalArgumentException {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicatul nu poate fi null!");
        }
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Useri\"")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String nume = resultSet.getString("nume");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    User user = new User(id, nume, password, email);
                    if (predicate.test(user)) {
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Useri\""); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                User user = new User(id, nume, password, email);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) throws IllegalArgumentException, DuplicatedElementException {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Useri\" VALUES (?, ?, ?, ?)")) {
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getPassword());
            statement.setString(4, entity.getEmail());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new DuplicatedElementException("Userul exista deja!");
            }
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User remove(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        }
        User user = findByID(id);
        if (user == null) {
            return null;
        }
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Useri\" WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User remove(Predicate<User> predicate) throws IllegalArgumentException {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicatul nu poate fi null!");
        }
        User user = findByPredicate(predicate);
        if (user == null) {
            return null;
        }
        return remove(user.getId());
    }

    @Override
    public User update(Long id, User entity) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id-ul nu poate fi null!");
        }
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("UPDATE \"Useri\" SET nume = ?, password = ?, email = ? WHERE id = ?")) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getEmail());
            statement.setLong(4, id);
            int rowschanged = statement.executeUpdate();
            return (rowschanged == 0) ? null : entity;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Useri\"")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}