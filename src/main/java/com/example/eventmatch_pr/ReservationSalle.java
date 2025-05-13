package com.example.eventmatch_pr;

import java.time.LocalDateTime;

public class ReservationSalle {
    private int id_reservation;
    private int id_salle;
    private int id_utilisateur;
    private LocalDateTime date_debut;
    private LocalDateTime date_fin;
    private String statut;
    private String motif;
    private String statut_paiement;
    private LocalDateTime date_reservation;
    private LocalDateTime updated_at;
    private int nombre_participants;
    private String statut_reservation;
    private String commentaires;

    public ReservationSalle(int id_reservation, int id_salle, int id_utilisateur,
                            LocalDateTime date_debut, LocalDateTime date_fin,
                            String statut, String motif, String statut_paiement,
                            LocalDateTime date_reservation, LocalDateTime updated_at,
                            int nombre_participants, String statut_reservation, String commentaires) {
        this.id_reservation = id_reservation;
        this.id_salle = id_salle;
        this.id_utilisateur = id_utilisateur;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.statut = statut;
        this.motif = motif;
        this.statut_paiement = statut_paiement;
        this.date_reservation = date_reservation;
        this.updated_at = updated_at;
        this.nombre_participants = nombre_participants;
        this.statut_reservation = statut_reservation;
        this.commentaires = commentaires;
    }

    public ReservationSalle() {

    }

    // Getters & Setters ici

    // Getters & Setters
    public int getId_reservation() { return id_reservation; }
    public void setId_reservation(int id_reservation) { this.id_reservation = id_reservation; }

    public int getId_salle() { return id_salle; }
    public void setId_salle(int id_salle) { this.id_salle = id_salle; }

    public int getId_utilisateur() { return id_utilisateur; }
    public void setId_utilisateur(int id_utilisateur) { this.id_utilisateur = id_utilisateur; }

    public LocalDateTime getDate_debut() { return date_debut; }
    public void setDate_debut(LocalDateTime date_debut) { this.date_debut = date_debut; }

    public LocalDateTime getDate_fin() { return date_fin; }
    public void setDate_fin(LocalDateTime date_fin) { this.date_fin = date_fin; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getStatut_paiement() { return statut_paiement; }
    public void setStatut_paiement(String statut_paiement) { this.statut_paiement = statut_paiement; }

    public LocalDateTime getDate_reservation() { return date_reservation; }
    public void setDate_reservation(LocalDateTime date_reservation) { this.date_reservation = date_reservation; }

    public LocalDateTime getUpdated_at() { return updated_at; }
    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }

    public int getNombre_participants() { return nombre_participants; }
    public void setNombre_participants(int nombre_participants) { this.nombre_participants = nombre_participants; }

    public String getStatut_reservation() { return statut_reservation; }
    public void setStatut_reservation(String statut_reservation) { this.statut_reservation = statut_reservation; }

    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
}