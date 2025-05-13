package com.example.eventmatch_pr;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Activite {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> heureDebut = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> heureFin = new SimpleObjectProperty<>();
    private final StringProperty lieu = new SimpleStringProperty();
    private final StringProperty responsable = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final IntegerProperty places = new SimpleIntegerProperty();
    private final IntegerProperty inscrits = new SimpleIntegerProperty();
    private final StringProperty statut = new SimpleStringProperty();

    // Constructeurs
    public Activite() {
        this.inscrits.set(0);
        this.statut.set("En attente");
    }

    public Activite(String nom, String description, LocalDate date, LocalTime heureDebut, LocalTime heureFin,
                   String lieu, String responsable, String type, int places) {
        setNom(nom);
        setDescription(description);
        setDate(date);
        setHeureDebut(heureDebut);
        setHeureFin(heureFin);
        setLieu(lieu);
        setResponsable(responsable);
        setType(type);
        setPlaces(places);
        this.inscrits.set(0);
        updateStatut();
    }

    // Getters et Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public StringProperty nomProperty() { return nom; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    public LocalDate getDate() { return date.get(); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

    public LocalTime getHeureDebut() { return heureDebut.get(); }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut.set(heureDebut); }
    public ObjectProperty<LocalTime> heureDebutProperty() { return heureDebut; }

    public LocalTime getHeureFin() { return heureFin.get(); }
    public void setHeureFin(LocalTime heureFin) { this.heureFin.set(heureFin); }
    public ObjectProperty<LocalTime> heureFinProperty() { return heureFin; }

    public String getLieu() { return lieu.get(); }
    public void setLieu(String lieu) { this.lieu.set(lieu); }
    public StringProperty lieuProperty() { return lieu; }

    public String getResponsable() { return responsable.get(); }
    public void setResponsable(String responsable) { this.responsable.set(responsable); }
    public StringProperty responsableProperty() { return responsable; }

    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }
    public StringProperty typeProperty() { return type; }

    public int getPlaces() { return places.get(); }
    public void setPlaces(int places) { this.places.set(places); }
    public IntegerProperty placesProperty() { return places; }

    public int getInscrits() { return inscrits.get(); }
    public void setInscrits(int inscrits) { this.inscrits.set(inscrits); }
    public IntegerProperty inscritsProperty() { return inscrits; }

    public String getStatut() { return statut.get(); }
    public void setStatut(String statut) { this.statut.set(statut); }
    public StringProperty statutProperty() { return statut; }

    public void updateStatut() {
        if (getInscrits() >= getPlaces()) {
            setStatut("Complet");
        } else if (getInscrits() > 0) {
            setStatut("En cours");
        } else {
            setStatut("En attente");
        }
    }

    // Méthodes utilitaires
    public String getDateFormatted() {
        return formatDate(getDate());
    }

    public String getHeureDebutFormatted() {
        return formatTime(getHeureDebut());
    }

    public String getHeureFinFormatted() {
        return formatTime(getHeureFin());
    }

    private String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formatTime(LocalTime time) {
        if (time == null) return "";
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public boolean isValide() {
        return getNom() != null && !getNom().isEmpty() &&
               getType() != null && !getType().isEmpty() &&
               getDate() != null && 
               getHeureDebut() != null && 
               getHeureFin() != null &&
               !getHeureFin().isBefore(getHeureDebut());
    }

    @Override
    public String toString() {
        return String.format("Activité [%d] %s (%s de %s à %s) - %s",
                getId(), getNom(), getDateFormatted(), 
                getHeureDebutFormatted(), getHeureFinFormatted(), getStatut());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activite activite = (Activite) o;
        return getId() == activite.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String nom;
        private String description;
        private LocalDate date;
        private LocalTime heureDebut;
        private LocalTime heureFin;
        private String lieu;
        private String responsable;
        private String type;
        private int places;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder heureDebut(LocalTime heureDebut) {
            this.heureDebut = heureDebut;
            return this;
        }

        public Builder heureFin(LocalTime heureFin) {
            this.heureFin = heureFin;
            return this;
        }

        public Builder lieu(String lieu) {
            this.lieu = lieu;
            return this;
        }

        public Builder responsable(String responsable) {
            this.responsable = responsable;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder places(int places) {
            this.places = places;
            return this;
        }

        public Activite build() {
            return new Activite(nom, description, date, heureDebut, heureFin, lieu, responsable, type, places);
        }
    }
}