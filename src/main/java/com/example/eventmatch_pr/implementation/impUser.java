package com.example.eventmatch_pr.implementation;

import com.example.eventmatch_pr.DB.DB;
import com.example.eventmatch_pr.entity.Role;
import com.example.eventmatch_pr.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class impUser implements IUser <User> {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/user";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static DB db = new DB();

    @Override
    public User addUser(User user) {
        String sqlUser = "INSERT INTO user (nom, prenom, email, password, Role, tel) VALUES (?,?,?,?,?,?)";
        try {
            // Ajouter l'utilisateur à la table `user`
            db.initPrepar(sqlUser);
            PreparedStatement pstmtUser = db.getPtsm(sqlUser);

            pstmtUser.setString(1, user.getName());
            pstmtUser.setString(2, user.getPrenom());
            pstmtUser.setString(3, user.getEmail());
            pstmtUser.setString(4, user.getPassword());
            pstmtUser.setString(5, user.getRole().toString());
            pstmtUser.setInt(6, user.getPhone());
            pstmtUser.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    @Override
    public User editUser(User user) {
        return null;
    }

    @Override
    public boolean deleteUser(int id) {
        return false;
    }

    @Override
    public List<User> displayUser() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, nom, prenom, email, password, Role, tel FROM user";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("Role")),
                        resultSet.getInt("tel")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }


    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM user";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.fromString(resultSet.getString("role")),
                        resultSet.getInt("tel")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Nombre d'utilisateurs récupérés : " + users.size());
        return users;
    }


    @Override
    public User getUserById(int id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM user WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.fromString(resultSet.getString("role")),
                        resultSet.getInt("tel")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public User getConnection(String email, String password) {
        User user = null;
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

        try {
            PreparedStatement pstmt = db.getPtsm(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));


            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL dans getConnection: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    public User findUserById(int id) {
        String query = "SELECT id, nom, prenom, email, password, Role, tel FROM user WHERE id = ?";
        User user = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf((resultSet.getString("Role"))),
                        resultSet.getInt("tel") // Correspond à tel dans la BD
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }
        return user;
    }

    public static Role fromString(String role) {
        try {
            // Conversion en majuscule et remplacement des espaces par des underscores
            return Role.valueOf(role.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            System.err.println("Rôle inconnu : " + role);
            return null; // Ou une valeur par défaut, comme Role.ADMIN
        }


    }


}