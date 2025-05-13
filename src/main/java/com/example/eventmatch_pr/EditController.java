package com.example.eventmatch_pr;

import com.example.eventmatch_pr.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditController {
    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> heureDebutCombo;
    @FXML private ComboBox<String> heureFinCombo;
    @FXML private TextField lieuField;
    @FXML private TextField responsableField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private ComboBox<String> statutCombo;
    @FXML private Label errorLabel;
    @FXML private Label validationLabel;

    private Activite activite;
    private boolean valide = false;

    @FXML
    public void initialize() {
        // Initialiser les ComboBox d'heures
        for (int i = 0; i < 24; i++) {
            String hour = String.format("%02d:00", i);
            heureDebutCombo.getItems().add(hour);
            heureFinCombo.getItems().add(hour);
        }

        // Initialiser les types d'activités
        typeCombo.getItems().addAll("Karaoke", "Pause musicale", "Formation", "Chorégraphie", "Jeux", "Autre");

        // Initialiser les statuts
        statutCombo.getItems().addAll("À venir", "En cours", "Terminée");

        // Configurer les validateurs
        configurerValidateurs();
    }

    private void configurerValidateurs() {
        // Valider le nom (minimum 3 caractères, maximum 50 caractères, pas de caractères spéciaux)
        nomField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                afficherErreur("Le nom est obligatoire");
            } else if (newValue.trim().length() < 3) {
                afficherErreur("Le nom doit contenir au moins 3 caractères");
            } else if (newValue.trim().length() > 50) {
                afficherErreur("Le nom ne doit pas dépasser 50 caractères");
            } else if (!newValue.matches("^[\\p{L}\\s\\d'-]+$")) {
                afficherErreur("Le nom ne doit contenir que des lettres, chiffres, espaces, tirets et apostrophes");
            } else {
                effacerErreur();
            }
            updateValidationStatus();
        });

        // Valider la description (maximum 500 caractères, pas de caractères spéciaux dangereux)
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.length() > 500) {
                    afficherErreur("La description ne doit pas dépasser 500 caractères");
                } else if (newValue.matches(".*[<>{}].*")) {
                    afficherErreur("La description ne doit pas contenir de caractères spéciaux dangereux (<, >, {, })");
                } else {
                    effacerErreur();
                }
            }
            updateValidationStatus();
        });

        // Valider la date
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isBefore(LocalDate.now())) {
                    afficherErreur("La date ne peut pas être dans le passé");
                } else if (newValue.isAfter(LocalDate.now().plusYears(1))) {
                    afficherErreur("La date ne peut pas être plus d'un an dans le futur");
                } else {
                    validerHeures();
                }
            }
            updateValidationStatus();
        });

        heureDebutCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            validerHeures();
            updateValidationStatus();
        });

        heureFinCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            validerHeures();
            updateValidationStatus();
        });

        // Valider le lieu (minimum 3 caractères, maximum 100 caractères)
        lieuField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                afficherErreur("Le lieu est obligatoire");
            } else if (newValue.trim().length() < 3) {
                afficherErreur("Le lieu doit contenir au moins 3 caractères");
            } else if (newValue.trim().length() > 100) {
                afficherErreur("Le lieu ne doit pas dépasser 100 caractères");
            } else if (!newValue.matches("^[\\p{L}\\s\\d'-.,]+$")) {
                afficherErreur("Le lieu ne doit contenir que des lettres, chiffres, espaces, tirets, apostrophes et points");
            } else {
                effacerErreur();
            }
            updateValidationStatus();
        });

        // Valider le responsable (minimum 3 caractères, maximum 50 caractères)
        responsableField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                afficherErreur("Le responsable est obligatoire");
            } else if (newValue.trim().length() < 3) {
                afficherErreur("Le nom du responsable doit contenir au moins 3 caractères");
            } else if (newValue.trim().length() > 50) {
                afficherErreur("Le nom du responsable ne doit pas dépasser 50 caractères");
            } else if (!newValue.matches("^[\\p{L}\\s'-]+$")) {
                afficherErreur("Le nom du responsable ne doit contenir que des lettres, espaces, tirets et apostrophes");
            } else {
                effacerErreur();
            }
            updateValidationStatus();
        });

        // Valider le type
        typeCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                afficherErreur("Le type est obligatoire");
            } else {
                effacerErreur();
            }
            updateValidationStatus();
        });

        // Valider le statut
        statutCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                afficherErreur("Le statut est obligatoire");
            } else {
                effacerErreur();
            }
            updateValidationStatus();
        });
    }

    private void validerHeures() {
        if (datePicker.getValue() == null || heureDebutCombo.getValue() == null || heureFinCombo.getValue() == null) {
            return;
        }

        try {
            LocalTime debut = LocalTime.parse(heureDebutCombo.getValue());
            LocalTime fin = LocalTime.parse(heureFinCombo.getValue());

            // Vérifier que l'heure de début n'est pas trop tôt (après 6h du matin)
            if (debut.isBefore(LocalTime.of(6, 0))) {
                afficherErreur("L'activité ne peut pas commencer avant 6h du matin");
                return;
            }

            // Vérifier que l'heure de fin n'est pas trop tard (avant 22h)
            if (fin.isAfter(LocalTime.of(22, 0))) {
                afficherErreur("L'activité ne peut pas se terminer après 22h");
                return;
            }

            // Vérifier que l'heure de fin est après l'heure de début
            if (fin.isBefore(debut)) {
                afficherErreur("L'heure de fin doit être postérieure à l'heure de début");
                return;
            }

            // Vérifier que la durée de l'activité n'est pas trop longue (max 24h)
            if (debut.plusHours(24).isBefore(fin)) {
                afficherErreur("La durée de l'activité ne peut pas dépasser 24 heures");
                return;
            }

            // Vérifier que la durée de l'activité n'est pas trop courte (min 30 minutes)
            if (debut.plusMinutes(30).isAfter(fin)) {
                afficherErreur("La durée de l'activité doit être d'au moins 30 minutes");
                return;
            }

            effacerErreur();
        } catch (DateTimeParseException e) {
            afficherErreur("Format d'heure invalide. Utilisez le format HH:00");
        }
    }

    private void afficherErreur(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void effacerErreur() {
        errorLabel.setText("");
    }

    private void updateValidationStatus() {
        valide = isValide();
        validationLabel.setText(valide ? "Formulaire valide" : "Formulaire invalide");
        validationLabel.setTextFill(valide ? Color.GREEN : Color.RED);
    }

    public void setActivite(Activite activite) {
        this.activite = activite;

        // Remplir les champs avec les données de l'activité
        nomField.setText(activite.getNom());
        descriptionField.setText(activite.getDescription());
        datePicker.setValue(activite.getDate());
        heureDebutCombo.setValue(activite.getHeureDebut().format(DateTimeFormatter.ofPattern("HH:00")));
        heureFinCombo.setValue(activite.getHeureFin().format(DateTimeFormatter.ofPattern("HH:00")));
        lieuField.setText(activite.getLieu());
        responsableField.setText(activite.getResponsable());
        typeCombo.setValue(activite.getType());
        statutCombo.setValue(activite.getStatut());
    }

    public Activite getUpdatedActivite() {
        Activite updatedActivite = new Activite();
        updatedActivite.setId(activite.getId());
        updatedActivite.setNom(nomField.getText());
        updatedActivite.setDescription(descriptionField.getText());
        updatedActivite.setDate(datePicker.getValue());
        updatedActivite.setHeureDebut(LocalTime.parse(heureDebutCombo.getValue()));
        updatedActivite.setHeureFin(LocalTime.parse(heureFinCombo.getValue()));
        updatedActivite.setLieu(lieuField.getText());
        updatedActivite.setResponsable(responsableField.getText());
        updatedActivite.setType(typeCombo.getValue());
        updatedActivite.setStatut(statutCombo.getValue());
        return updatedActivite;
    }

    public boolean isValide() {
        return !nomField.getText().trim().isEmpty() &&
                !descriptionField.getText().trim().isEmpty() &&
                datePicker.getValue() != null &&
                heureDebutCombo.getValue() != null &&
                heureFinCombo.getValue() != null &&
                !lieuField.getText().trim().isEmpty() &&
                !responsableField.getText().trim().isEmpty() &&
                typeCombo.getValue() != null &&
                statutCombo.getValue() != null &&
                errorLabel.getText().isEmpty();
    }

    @FXML
    private void handleAnnuler() {
        ((Stage) nomField.getScene().getWindow()).close();
    }

    @FXML
    private void handleValider() {
        if (isValide()) {
            valide = true;
            ((Stage) nomField.getScene().getWindow()).close();
        } else {
            afficherErreur("Veuillez corriger les erreurs avant de valider");
        }
    }
}