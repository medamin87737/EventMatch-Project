package com.example.eventmatch_pr;

import java.time.LocalDate;

public class Participant {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDate dateInscription;
    private String statut;
    private String badgeId;
    private String activite;

    public Participant(int id, String nom, String prenom, String email, String telephone,
                      LocalDate dateInscription, String statut, String badgeId, String activite) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.dateInscription = dateInscription;
        this.statut = statut;
        this.badgeId = badgeId;
        this.activite = activite;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public LocalDate getDateInscription() { return dateInscription; }
    public String getStatut() { return statut; }
    public String getBadgeId() { return badgeId; }
    public String getActivite() { return activite; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setDateInscription(LocalDate dateInscription) { this.dateInscription = dateInscription; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setBadgeId(String badgeId) { this.badgeId = badgeId; }
    public void setActivite(String activite) { this.activite = activite; }
} 