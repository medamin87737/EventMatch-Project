package com.example.eventmatch_pr;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ModifierSalleController {

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
    private Salle salleToEdit;

    public void setSalleController(SalleController salleController) {
        this.salleController = salleController;
    }

    public void setSalleToEdit(Salle salle) {
        this.salleToEdit = salle;
        populateFields();
    }

    private void populateFields() {
        nomField.setText(salleToEdit.getNom_salle());
        capaciteField.setText(String.valueOf(salleToEdit.getCapacite()));
        typeField.setText(salleToEdit.getType_salle());
        disponibleCheck.setSelected(salleToEdit.isDisponible());
        descriptionArea.setText(salleToEdit.getDescription());
        adresseField.setText(salleToEdit.getAdresse());
        villeField.setText(salleToEdit.getVille());
        prixField.setText(String.valueOf(salleToEdit.getPrix_par_heure()));
        equipementsArea.setText(salleToEdit.getEquipements());
        estDisponibleCheck.setSelected(salleToEdit.isEst_disponible());
    }

    @FXML
    private void handleModifier() {
        try {
            salleToEdit.setNom_salle(nomField.getText());
            salleToEdit.setCapacite(Integer.parseInt(capaciteField.getText()));
            salleToEdit.setType_salle(typeField.getText());
            salleToEdit.setDisponible(disponibleCheck.isSelected());
            salleToEdit.setDescription(descriptionArea.getText());
            salleToEdit.setAdresse(adresseField.getText());
            salleToEdit.setVille(villeField.getText());
            salleToEdit.setPrix_par_heure(Double.parseDouble(prixField.getText()));
            salleToEdit.setEquipements(equipementsArea.getText());
            salleToEdit.setEst_disponible(estDisponibleCheck.isSelected());

            salleController.updateSalle(salleToEdit);

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