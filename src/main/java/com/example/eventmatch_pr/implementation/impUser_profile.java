package com.example.eventmatch_pr.implementation;

import com.example.eventmatch_pr.DB.DB;
import com.example.eventmatch_pr.entity.User;
import com.example.eventmatch_pr.entity.UserSession;
import com.example.eventmatch_pr.entity.User_Profile;
import javafx.fxml.Initializable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;


public class impUser_profile implements IUser<User_Profile> {

    DB db = new DB();
    User user = new User();
    User_Profile profile = new User_Profile();
    private Date parseDate(LocalDate date) {
        return date != null ? Date.valueOf(date) : null;
    }


    @Override
    public User_Profile addUser(User_Profile user) {
        return null;
    }




    @Override
    public User_Profile editUser(User_Profile user) {
        return null;
    }

    @Override
    public boolean deleteUser(int id) {
        return false;
    }

    @Override
    public List<User_Profile> displayUser() {
        return List.of();
    }

    @Override
    public User getConnection(String email, String password) {
        return null;
    }
}
