package com.example.eventmatch_pr;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Promo {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty nomProduit = new SimpleStringProperty();

    private final IntegerProperty reduction = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dateDebut = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateFin = new SimpleObjectProperty<>();

    // Constructeurs
    public Promo() {}

    public Promo(int id, String code, int reduction, LocalDate dateDebut, LocalDate dateFin) {
        setId(id);
        setCode(code);
        setReduction(reduction);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
    }
    public Promo(String code, String nomProduit, int reduction, LocalDate dateDebut, LocalDate dateFin) {
        setCode(code);
        setNomProduit(nomProduit);
        setReduction(reduction);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
    }

    public Promo(String code, int reduction, LocalDate dateDebut, LocalDate dateFin) {
        setCode(code);
        setReduction(reduction);

        setDateDebut(dateDebut);
        setDateFin(dateFin);



    }

    public Promo(String code, String nomProduit, Object o, int reduction, LocalDate dateDebut, LocalDate dateFin) {
        setCode(code);
        setNomProduit(nomProduit);
        setReduction(reduction);
        setDateDebut(dateDebut);
        setDateFin(dateFin);

    }

    public Promo(int id, String code, String nomProduit, int reduction, LocalDate dateDebut, LocalDate dateFin) {

        setId(id);
        setCode(code);
        setNomProduit(nomProduit);
        setReduction(reduction);
        setDateDebut(dateDebut);
        setDateFin(dateFin);

    }


    // Getters et Setters pour les propriétés
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getCode() { return code.get(); }
    public void setCode(String value) { code.set(value); }
    public StringProperty codeProperty() { return code; }

    public String getNomProduit() { return nomProduit.get(); }
    public void setNomProduit(String value) { nomProduit.set(value); }
    public StringProperty nomProduitProperty() { return nomProduit; }

    public int getReduction() { return reduction.get(); }
    public void setReduction(int value) { reduction.set(value); }
    public IntegerProperty reductionProperty() { return reduction; }

    public LocalDate getDateDebut() { return dateDebut.get(); }
    public void setDateDebut(LocalDate value) { dateDebut.set(value); }
    public ObjectProperty<LocalDate> dateDebutProperty() { return dateDebut; }

    public LocalDate getDateFin() { return dateFin.get(); }
    public void setDateFin(LocalDate value) { dateFin.set(value); }
    public ObjectProperty<LocalDate> dateFinProperty() { return dateFin; }
}