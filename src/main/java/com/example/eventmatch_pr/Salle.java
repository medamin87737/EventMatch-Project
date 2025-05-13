package com.example.eventmatch_pr;

import java.time.LocalDateTime;

public class Salle {
    private int id_salle;
    private String nom_salle;
    private int capacite;
    private String type_salle;
    private boolean disponible;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String adresse;
    private String ville;
    private double prix_par_heure;
    private String equipements;
    private boolean est_disponible;
    public Salle(){}
    public Salle(int id_salle, String nom_salle, int capacite, String type_salle,
                 boolean disponible, String description, LocalDateTime created_at,
                 LocalDateTime updated_at, String adresse, String ville,
                 double prix_par_heure, String equipements, boolean est_disponible) {
        this.id_salle = id_salle;
        this.nom_salle = nom_salle;
        this.capacite = capacite;
        this.type_salle = type_salle;
        this.disponible = disponible;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.adresse = adresse;
        this.ville = ville;
        this.prix_par_heure = prix_par_heure;
        this.equipements = equipements;
        this.est_disponible = est_disponible;
    }

    public Salle( String nom_salle, int capacite, String type_salle,
                 boolean disponible, String description, LocalDateTime created_at,
                 LocalDateTime updated_at, String adresse, String ville,
                 double prix_par_heure, String equipements, boolean est_disponible) {
        this.nom_salle = nom_salle;
        this.capacite = capacite;
        this.type_salle = type_salle;
        this.disponible = disponible;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.adresse = adresse;
        this.ville = ville;
        this.prix_par_heure = prix_par_heure;
        this.equipements = equipements;
        this.est_disponible = est_disponible;
    }

    public Salle(int idSalle, String nomSalle, int capacite, String typeSalle, boolean disponible, String description, LocalDateTime createdAt, LocalDateTime updatedAt, String adresse, String ville, double prixParHeure) {
        this.id_salle = idSalle;
        this.nom_salle = nomSalle;
        this.capacite = capacite;
        this.type_salle = typeSalle;
        this.disponible = disponible;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.adresse = adresse;
        this.ville = ville;
        this.prix_par_heure = prixParHeure;
        this.equipements = equipements;
        this.est_disponible = est_disponible;

    }


    // Getters & Setters ici (générés via IDE ou manuellement)

    // Getters & Setters
    public int getId_salle() { return id_salle; }
    public void setId_salle(int id_salle) { this.id_salle = id_salle; }

    public String getNom_salle() { return nom_salle; }
    public void setNom_salle(String nom_salle) { this.nom_salle = nom_salle; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public String getType_salle() { return type_salle; }
    public void setType_salle(String type_salle) { this.type_salle = type_salle; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdated_at() { return updated_at; }
    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public double getPrix_par_heure() { return prix_par_heure; }
    public void setPrix_par_heure(double prix_par_heure) { this.prix_par_heure = prix_par_heure; }

    public String getEquipements() { return equipements; }
    public void setEquipements(String equipements) { this.equipements = equipements; }

    public boolean isEst_disponible() { return est_disponible; }
    public void setEst_disponible(boolean est_disponible) { this.est_disponible = est_disponible; }
}