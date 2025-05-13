package com.example.eventmatch_pr;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;

public class AjouterSalleController {

    @FXML private TextField nomField;
    @FXML private TextField capaciteField;
    @FXML private TextField typeField;
    @FXML private CheckBox disponibleCheck;
    @FXML private TextArea descriptionArea;
    @FXML private TextField adresseField;
    @FXML private TextField villeField;
    @FXML private TextField prixField;
    @FXML private TextArea equipementsArea;
    @FXML private CheckBox estDisponibleCheck;

    private SalleController salleController;

    public void setSalleController(SalleController salleController) {
        this.salleController = salleController;
    }

    @FXML
    private void handleAjouter() {
        try {
            String nom = nomField.getText();
            int capacite = Integer.parseInt(capaciteField.getText());
            String type = typeField.getText();
            boolean disponible = disponibleCheck.isSelected();
            String description = descriptionArea.getText();
            String adresse = adresseField.getText();
            String ville = villeField.getText();
            double prix = Double.parseDouble(prixField.getText());
            String equipements = equipementsArea.getText();
            boolean estDisponible = estDisponibleCheck.isSelected();

            Salle newSalle = new Salle(nom, capacite, type, disponible, description,
                    LocalDateTime.now(), LocalDateTime.now(),
                    adresse, ville, prix, equipements, estDisponible);

            salleController.addSalle(newSalle);

            // Fermer la fenêtre
            nomField.getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Veuillez entrer des valeurs valides pour la capacité et le prix", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAnnuler() {
        nomField.getScene().getWindow().hide();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}