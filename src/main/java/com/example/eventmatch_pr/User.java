package com.example.eventmatch_pr;

import com.example.eventmatch_pr.entity.Role;

public class User {
    private int id;
    private String name;
    private String prenom;
    private String email;
    private String password;
    private Role role; // Utilisation de l'énumération
    private int phone;


    public User() {

    }


    // Constructeurs
    public User(int id, String name, String prenom, String email, String password, Role role, int phone) {
        this.id = id;
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    public User(String name, String prenom, String email, String password, Role role, int phone) {
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }
    public User(String name,String  prenom, String email, String password, Role role, int phone,int id) {
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.password = password;

        this.role = role;
        this.phone = phone;
        this.id = id;

    }
    public User(String nom, String prenom, String email, int tel) {
        this.name = nom;
        this.prenom = prenom;
        this.email = email;
        this.phone = tel;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNom() {
        return name;
    }
    // Modifiez getTel() pour retourner toujours une String
    public String getTel() {
        return String.valueOf(phone);
    }

// Ou mieux, gardez la cohérence et utilisez toujours getPhone() qui retourne un int
}
// Les autres getters et setters restent inchangés
