package com.example.eventmatch_pr.implementation;

import java.util.List;

public interface IUser<T> {
    T add(T user);
    T edit(T user);
    boolean delete(int id);
    List<T> display();
}
