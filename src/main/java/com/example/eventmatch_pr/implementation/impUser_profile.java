package com.example.eventmatch_pr.implementation;

import com.example.eventmatch_pr.DB.DB;
import com.example.eventmatch_pr.User;
import com.example.eventmatch_pr.entity.User_Profile;

import java.sql.Date;
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
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User getConnection(String email, String password) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }
}
