package com.example.eventmatch_pr.implementation;

import com.example.eventmatch_pr.DB.DB;
import com.example.eventmatch_pr.entity.Role;
import com.example.eventmatch_pr.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class impUser implements IUser <User>{

    DB db = new DB();
    User user = new User();
    @Override
    public User add(User user) {
        String sqlUser = "INSERT INTO user (nom, prenom, email, password,Role, tel) VALUES (?,?,?,?,?,?)";
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

            // Récupérer l'ID de l'utilisateur inséré


            // Ajouter une entrée correspondante dans la table `job`

        }  catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    @Override
    public User edit(User user) {
        String sql = "UPDATE user SET nom = ?, prenom = ?, email = ?, password = ?, Role = ?, tel = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            PreparedStatement pstmt = db.getPtsm(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPrenom());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());

            pstmt.setString(5, user.getRole().toString());
            pstmt.setInt(6, user.getPhone());
            pstmt.setInt(7, user.getId());
            pstmt.executeUpdate();
            System.out.println("success");

        } catch (SQLException e) {
            System.out.println("edit non existant");

            throw new RuntimeException(e);
        }
        System.out.println("edit nsucess");

        return user;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM user WHERE id=?";
        try {
            db.initPrepar(sql);
            PreparedStatement pstmt = db.getPtsm(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        }
        catch (Exception e) {
            e.printStackTrace();
            return false;

        }



        return true;
    }

    @Override
    public List<User> display() {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>(); // Liste pour stocker les utilisateurs
        try {
            db.initPrepar(sql);
            PreparedStatement pstmt = db.getPtsm(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Role role = Role.fromString(rs.getString("Role"));
                int phone = rs.getInt("tel");
                if (role == null) {
                    continue; // Ignorer les utilisateurs avec un rôle invalide
                }



                // Crée un utilisateur à partir des données récupérées
                User user = new User("name",prenom,email,password,role,phone);
                users.add(user); // Ajoute l'utilisateur à la liste
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users; // Retourne la liste des utilisateurs
    }



}
