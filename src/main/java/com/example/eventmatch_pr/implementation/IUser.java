package com.example.eventmatch_pr.implementation;

import com.example.eventmatch_pr.User;

import java.util.List;

public interface IUser<T> {
    T addUser(T user);
    T editUser(T user);
    boolean deleteUser(int id);
    List<T> displayUser();
    List<User> getAllUsers();
    User getUserById(int id);

        User getConnection(String email, String password);

    List<User> getAll();
}
