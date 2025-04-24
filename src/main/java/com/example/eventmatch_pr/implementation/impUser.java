package com.example.eventmatch_pr.implementation;

import com.example.eventmatch_pr.DB.DB;
import com.example.eventmatch_pr.entity.Role;
import com.example.eventmatch_pr.entity.User;
import tray.notification.NotificationType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.eventmatch_pr.HelloController.showNotification;

public class impUser implements IUser <User>{

    private DB db = new DB();

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
        return List.of();
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
}
