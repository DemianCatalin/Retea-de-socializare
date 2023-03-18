package com.example.reteadesocializare.repo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepoMsg {
    private final String url;
    private final String username;
    private final String password;

    public RepoMsg(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void save(Long from, Long to, String text, String name) {
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Mesaje\" VALUES (?, ?, ?, ?)")) {
            statement.setLong(1, from);
            statement.setLong(2, to);
            statement.setString(3, text);
            statement.setString(4, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> findAll(Long id) {
        List<String> rez = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Mesaje\" WHERE id_from = ? OR id_to = ? ORDER BY id")) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String text = resultSet.getString("text");
                    String name = resultSet.getString("name");
                    rez.add("<" + name + ">:" + text + "\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rez;
    }

    public String findLast(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Mesaje\" WHERE id_from = ? OR id_to = ? ORDER BY id DESC")) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String text = resultSet.getString("text");
                    String name = resultSet.getString("name");
                    return "<" + name + ">:" + text + "\n";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
