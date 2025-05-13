package com.example.eventmatch_pr.entity;

import javafx.beans.property.*;

import java.time.LocalDate;

public class UserProfile {

    private IntegerProperty profileId;
    private IntegerProperty userId;
    private StringProperty adresse;
    private StringProperty ville;
    private StringProperty pays;
    private StringProperty codePostal;
    private ObjectProperty<LocalDate> dateNaissance;
    private StringProperty bio;

    public UserProfile(int profileId, int userId, String adresse, String ville, String pays, String codePostal, LocalDate dateNaissance, String bio) {
        this.profileId = new SimpleIntegerProperty(profileId);
        this.userId = new SimpleIntegerProperty(userId);
        this.adresse = new SimpleStringProperty(adresse);
        this.ville = new SimpleStringProperty(ville);
        this.pays = new SimpleStringProperty(pays);
        this.codePostal = new SimpleStringProperty(codePostal);
        this.dateNaissance = new SimpleObjectProperty<>(dateNaissance);
        this.bio = new SimpleStringProperty(bio);
    }
    public UserProfile(int userId, String adresse, String ville, String pays, String codePostal, LocalDate dateNaissance, String bio) {
        this.userId = new SimpleIntegerProperty(userId);
        this.adresse = new SimpleStringProperty(adresse);
        this.ville = new SimpleStringProperty(ville);
        this.pays = new SimpleStringProperty(pays);
        this.codePostal = new SimpleStringProperty(codePostal);
        this.dateNaissance = new SimpleObjectProperty<>(dateNaissance);
        this.bio = new SimpleStringProperty(bio);
    }

    public UserProfile(String adresse, String ville, String pays, String codePostal, LocalDate dateNaissance, String bio) {
        this.adresse = new SimpleStringProperty(adresse);

        this.ville = new SimpleStringProperty(ville);
        this.pays = new SimpleStringProperty(pays);
        this.codePostal = new SimpleStringProperty(codePostal);
        this.dateNaissance = new SimpleObjectProperty<>(dateNaissance);
        this.bio = new SimpleStringProperty(bio);

    }

    public UserProfile() {

    }


    // Getters et setters pour les propriétés JavaFX
    public IntegerProperty profileIdProperty() { return profileId; }
    public IntegerProperty userIdProperty() { return userId; }
    public StringProperty adresseProperty() { return adresse; }
    public StringProperty villeProperty() { return ville; }
    public StringProperty paysProperty() { return pays; }
    public StringProperty codePostalProperty() { return codePostal; }
    public ObjectProperty<LocalDate> dateNaissanceProperty() { return dateNaissance; }
    public StringProperty bioProperty() { return bio; }

    public int getProfileId() {
        return profileId.get();
    }

    public void setProfileId(int profileId) {
        this.profileId.set(profileId);
    }

    public int getUserId() {
        return userId.get();
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public String getAdresse() {
        return adresse.get();
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }

    public String getVille() {
        return ville.get();
    }

    public void setVille(String ville) {
        this.ville.set(ville);
    }

    public String getPays() {
        return pays.get();
    }

    public void setPays(String pays) {
        this.pays.set(pays);
    }

    public String getCodePostal() {
        return codePostal.get();
    }

    public void setCodePostal(String codePostal) {
        this.codePostal.set(codePostal);
    }

    public String getBio() {
        return bio.get();
    }

    public void setBio(String bio) {
        this.bio.set(bio);
    }

    public LocalDate getDateNaissance() {
        return dateNaissance.get();
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance.set(dateNaissance);
    }


}
