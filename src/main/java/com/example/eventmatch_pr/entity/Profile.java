package com.example.eventmatch_pr.entity;

import java.time.LocalDate;

public class Profile {

    private int profileId;
    private int userId;
    private String adresse;
    private String ville;
    private String pays;
    private String codePostal;
    private LocalDate dateOfBirth;
    private String bio;

    public Profile(int profileId, int userId, String adresse, String ville, String pays, String codePostal, LocalDate dateOfBirth, String bio) {
        this.profileId = profileId;
        this.userId = userId;
        this.adresse = adresse;
        this.ville = ville;
        this.pays = pays;
        this.codePostal = codePostal;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
    }

    // Getters et setters
    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
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
