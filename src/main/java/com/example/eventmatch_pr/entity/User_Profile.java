package com.example.eventmatch_pr.entity;

import java.time.LocalDate;

public class User_Profile {

    private String adresse;
    private String ville;
    private String pays;
    private String codePostal;
    private LocalDate dateOfBirth;
    private String bio;
    public User_Profile() {}

    public User_Profile(String adresser, String ville, String pays, String codePostal, LocalDate dateOfBirth, String bio) {
        this.adresse = adresser;
        this.ville = ville;
        this.pays = pays;
        this.codePostal = codePostal;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;

    }

    // Getters et setters
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

}
