package com.example.eventmatch_pr;

import javafx.beans.property.*;

import java.time.LocalDate;

public class UserProjet {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();
    private final StringProperty tel = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();
    private final StringProperty ville = new SimpleStringProperty();
    private final StringProperty pays = new SimpleStringProperty();
    private final StringProperty codePostal = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateNaissance = new SimpleObjectProperty<>();
    private final StringProperty bio = new SimpleStringProperty();
    private final StringProperty code = new SimpleStringProperty();



    public UserProjet() {}

    public UserProjet(String nom, String prenom, String email, String password, String role,
                      String tel, String adresse, String ville, String pays,
                      String codePostal, LocalDate dateNaissance, String bio) {
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.email.set(email);
        this.password.set(password);
        this.role.set(role);
        this.tel.set(tel);
        this.adresse.set(adresse);
        this.ville.set(ville);
        this.pays.set(pays);
        this.codePostal.set(codePostal);
        this.dateNaissance.set(dateNaissance);
        this.bio.set(bio);
    }
    public UserProjet(String nom, String prenom, String email, String password, String role,
                      String tel, String adresse, String ville, String pays,
                      String codePostal, LocalDate dateNaissance, String bio,String code) {
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.email.set(email);
        this.password.set(password);
        this.role.set(role);
        this.tel.set(tel);
        this.adresse.set(adresse);
        this.ville.set(ville);
        this.pays.set(pays);
        this.codePostal.set(codePostal);
        this.dateNaissance.set(dateNaissance);
        this.bio.set(bio);
        this.code.set(code);
    }


    public UserProjet(String nom, String prenom, String email, String s, String tel, String s1, String s2, String s3, String s4, LocalDate localDate, String s5) {
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.email.set(email);
        this.password.set(s);
        this.role.set(s);
        this.tel.set(tel);
        this.adresse.set(s1);
        this.ville.set(s2);
        this.pays.set(s3);
        this.codePostal.set(s4);
        this.dateNaissance.set(localDate);
        this.bio.set(s5);


    }

    public UserProjet(String nom, String prenom, String email, String role, String tel, String adresse, String ville, String pays, LocalDate localDate, String bio) {
        this.nom.set(nom);

        this.prenom.set(prenom);
        this.email.set(email);
        this.role.set(role);
        this.tel.set(tel);
        this.adresse.set(adresse);
        this.ville.set(ville);
        this.pays.set(pays);
        this.codePostal.set(codePostal.toString());
        this.dateNaissance.set(localDate);
        this.bio.set(bio);

    }

    // Getters et Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public StringProperty nomProperty() { return nom; }

    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String prenom) { this.prenom.set(prenom); }
    public StringProperty prenomProperty() { return prenom; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public String getPassword() { return password.get(); }
    public void setPassword(String password) { this.password.set(password); }
    public StringProperty passwordProperty() { return password; }

    public String getRole() { return role.get(); }
    public void setRole(String role) { this.role.set(role); }
    public StringProperty roleProperty() { return role; }

    public String getTel() { return tel.get(); }
    public void setTel(String tel) { this.tel.set(tel); }
    public StringProperty telProperty() { return tel; }

    public String getAdresse() { return adresse.get(); }
    public void setAdresse(String adresse) { this.adresse.set(adresse); }
    public StringProperty adresseProperty() { return adresse; }

    public String getVille() { return ville.get(); }
    public void setVille(String ville) { this.ville.set(ville); }
    public StringProperty villeProperty() { return ville; }

    public String getPays() { return pays.get(); }
    public void setPays(String pays) { this.pays.set(pays); }
    public StringProperty paysProperty() { return pays; }

    public String getCodePostal() { return codePostal.get(); }
    public void setCodePostal(String codePostal) { this.codePostal.set(codePostal); }
    public StringProperty codePostalProperty() { return codePostal; }

    public LocalDate getDateNaissance() { return dateNaissance.get(); }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance.set(dateNaissance); }
    public ObjectProperty<LocalDate> dateNaissanceProperty() { return dateNaissance; }

    public String getBio() { return bio.get(); }
    public void setBio(String bio) { this.bio.set(bio); }
    public StringProperty bioProperty() { return bio; }
    public void setCode(String code) { this.code.set(code); }
    public StringProperty getCode() { return code; }
    public StringProperty codeProperty() { return code; }



    // Méthode utilitaire pour masquer le mot de passe
    public String getMaskedPassword() {
        if (password.get() == null || password.get().isEmpty()) {
            return "";
        }
        return "••••••••";
    }

    // Méthode toString pour le débogage
    @Override
    public String toString() {
        return "UserProjet{" +
                "id=" + id.get() +
                ", nom=" + nom.get() +
                ", prenom=" + prenom.get() +
                ", email=" + email.get() +
                ", role=" + role.get() +
                ", ville=" + ville.get() +
                '}';
    }

    // Méthode pour créer un copie de l'objet
    public UserProjet copy() {
        UserProjet copy = new UserProjet();
        copy.setId(this.getId());
        copy.setNom(this.getNom());
        copy.setPrenom(this.getPrenom());
        copy.setEmail(this.getEmail());
        copy.setPassword(this.getPassword());
        copy.setRole(this.getRole());
        copy.setTel(this.getTel());
        copy.setAdresse(this.getAdresse());
        copy.setVille(this.getVille());
        copy.setPays(this.getPays());
        copy.setCodePostal(this.getCodePostal());
        copy.setDateNaissance(this.getDateNaissance());
        copy.setBio(this.getBio());
        return copy;
    }
}