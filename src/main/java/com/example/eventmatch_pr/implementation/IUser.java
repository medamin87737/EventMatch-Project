package com.example.eventmatch_pr.implementation;

import com.example.eventmatch_pr.entity.User;

import java.util.List;

public interface IUser<T> {
    T addUser(T user);
    T editUser(T user);
    boolean deleteUser(int id);
    List<T> displayUser();

        User getConnection(String email, String password);
}
