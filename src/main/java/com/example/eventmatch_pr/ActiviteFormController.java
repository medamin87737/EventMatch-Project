package com.example.eventmatch_pr;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ActiviteFormController {
    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> debutHeureSpinner;
    @FXML private Spinner<Integer> debutMinuteSpinner;
    @FXML private Spinner<Integer> finHeureSpinner;
    @FXML private Spinner<Integer> finMinuteSpinner;
    @FXML private TextField lieuField;
    @FXML private TextField responsableField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField placesField;
    @FXML private Label statutLabel;
    @FXML private Label statusLabel;

    private Stage dialogStage;
    private Activite activite;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        // Initialiser les valeurs par défaut
        datePicker.setValue(LocalDate.now());
        
        // Configurer les Spinners
        SpinnerValueFactory<Integer> heureFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8);
        SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        
        debutHeureSpinner.setValueFactory(heureFactory);
        debutMinuteSpinner.setValueFactory(minuteFactory);
        finHeureSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        finMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        
        // Ajouter les types d'activité
        typeComboBox.getItems().addAll(
            "Sport",
            "Culture",
            "Éducation",
            "Loisir",
            "Autre"
        );

        // Ajouter les validateurs
        setupValidators();
    }

    private void setupValidators() {
        // Valider le nom
        nomField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                nomField.setStyle("-fx-border-color: red;");
            } else {
                nomField.setStyle("");
            }
        });

        // Valider les heures
        debutHeureSpinner.valueProperty().addListener((observable, oldValue, newValue) -> validateHeures());
        debutMinuteSpinner.valueProperty().addListener((observable, oldValue, newValue) -> validateHeures());
        finHeureSpinner.valueProperty().addListener((observable, oldValue, newValue) -> validateHeures());
        finMinuteSpinner.valueProperty().addListener((observable, oldValue, newValue) -> validateHeures());

        // Valider les places
        placesField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int places = Integer.parseInt(newValue);
                if (places <= 0) {
                    placesField.setStyle("-fx-border-color: red;");
                } else {
                    placesField.setStyle("");
                }
            } catch (NumberFormatException e) {
                placesField.setStyle("-fx-border-color: red;");
            }
        });
    }

    private void validateHeures() {
        LocalTime debut = LocalTime.of(
            debutHeureSpinner.getValue(),
            debutMinuteSpinner.getValue()
        );
        LocalTime fin = LocalTime.of(
            finHeureSpinner.getValue(),
            finMinuteSpinner.getValue()
        );

        if (fin.isBefore(debut)) {
            finHeureSpinner.setStyle("-fx-border-color: red;");
            finMinuteSpinner.setStyle("-fx-border-color: red;");
            statusLabel.setText("L'heure de fin doit être après l'heure de début");
        } else {
            finHeureSpinner.setStyle("");
            finMinuteSpinner.setStyle("");
            statusLabel.setText("Prêt");
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;

        if (activite != null) {
            nomField.setText(activite.getNom());
            descriptionField.setText(activite.getDescription());
            datePicker.setValue(activite.getDate());
            debutHeureSpinner.getValueFactory().setValue(activite.getHeureDebut().getHour());
            debutMinuteSpinner.getValueFactory().setValue(activite.getHeureDebut().getMinute());
            finHeureSpinner.getValueFactory().setValue(activite.getHeureFin().getHour());
            finMinuteSpinner.getValueFactory().setValue(activite.getHeureFin().getMinute());
            lieuField.setText(activite.getLieu());
            responsableField.setText(activite.getResponsable());
            typeComboBox.setValue(activite.getType());
            placesField.setText(String.valueOf(activite.getPlaces()));
            statutLabel.setText(activite.getStatut());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleEnregistrer() {
        if (isInputValid()) {
            activite.setNom(nomField.getText());
            activite.setDescription(descriptionField.getText());
            activite.setDate(datePicker.getValue());
            activite.setHeureDebut(LocalTime.of(
                debutHeureSpinner.getValue(),
                debutMinuteSpinner.getValue()
            ));
            activite.setHeureFin(LocalTime.of(
                finHeureSpinner.getValue(),
                finMinuteSpinner.getValue()
            ));
            activite.setLieu(lieuField.getText());
            activite.setResponsable(responsableField.getText());
            activite.setType(typeComboBox.getValue());
            activite.setPlaces(Integer.parseInt(placesField.getText()));
            activite.updateStatut();

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleAnnuler() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage += "Le nom est requis!\n";
        }
        if (datePicker.getValue() == null) {
            errorMessage += "La date est requise!\n";
        }
        if (lieuField.getText() == null || lieuField.getText().trim().isEmpty()) {
            errorMessage += "Le lieu est requis!\n";
        }
        if (responsableField.getText() == null || responsableField.getText().trim().isEmpty()) {
            errorMessage += "Le responsable est requis!\n";
        }
        if (typeComboBox.getValue() == null) {
            errorMessage += "Le type est requis!\n";
        }
        if (placesField.getText() == null || placesField.getText().trim().isEmpty()) {
            errorMessage += "Le nombre de places est requis!\n";
        } else {
            try {
                int places = Integer.parseInt(placesField.getText());
                if (places <= 0) {
                    errorMessage += "Le nombre de places doit être positif!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Le nombre de places doit être un nombre valide!\n";
            }
        }

        LocalTime debut = LocalTime.of(
            debutHeureSpinner.getValue(),
            debutMinuteSpinner.getValue()
        );
        LocalTime fin = LocalTime.of(
            finHeureSpinner.getValue(),
            finMinuteSpinner.getValue()
        );
        if (fin.isBefore(debut)) {
            errorMessage += "L'heure de fin doit être après l'heure de début!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            statusLabel.setText(errorMessage);
            return false;
        }
    }

    public static class ReservationSalle {
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

        // Constructeur par défaut
        public ReservationSalle() {
        }

        // Constructeur avec tous les paramètres
        public ReservationSalle(int id_reservation, int id_salle, int id_utilisateur,
                                LocalDateTime date_debut, LocalDateTime date_fin,
                                String statut, String motif, String statut_paiement,
                                LocalDateTime date_reservation, LocalDateTime updated_at,
                                int nombre_participants, String statut_reservation,
                                String commentaires) {
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

        // Getters et Setters
        public int getId_reservation() {
            return id_reservation;
        }

        public void setId_reservation(int id_reservation) {
            this.id_reservation = id_reservation;
        }

        public int getId_salle() {
            return id_salle;
        }

        public void setId_salle(int id_salle) {
            this.id_salle = id_salle;
        }

        public int getId_utilisateur() {
            return id_utilisateur;
        }

        public void setId_utilisateur(int id_utilisateur) {
            this.id_utilisateur = id_utilisateur;
        }

        public LocalDateTime getDate_debut() {
            return date_debut;
        }

        public void setDate_debut(LocalDateTime date_debut) {
            this.date_debut = date_debut;
        }

        public LocalDateTime getDate_fin() {
            return date_fin;
        }

        public void setDate_fin(LocalDateTime date_fin) {
            this.date_fin = date_fin;
        }

        public String getStatut() {
            return statut;
        }

        public void setStatut(String statut) {
            this.statut = statut;
        }

        public String getMotif() {
            return motif;
        }

        public void setMotif(String motif) {
            this.motif = motif;
        }

        public String getStatut_paiement() {
            return statut_paiement;
        }

        public void setStatut_paiement(String statut_paiement) {
            this.statut_paiement = statut_paiement;
        }

        public LocalDateTime getDate_reservation() {
            return date_reservation;
        }

        public void setDate_reservation(LocalDateTime date_reservation) {
            this.date_reservation = date_reservation;
        }

        public LocalDateTime getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(LocalDateTime updated_at) {
            this.updated_at = updated_at;
        }

        public int getNombre_participants() {
            return nombre_participants;
        }

        public void setNombre_participants(int nombre_participants) {
            this.nombre_participants = nombre_participants;
        }

        public String getStatut_reservation() {
            return statut_reservation;
        }

        public void setStatut_reservation(String statut_reservation) {
            this.statut_reservation = statut_reservation;
        }

        public String getCommentaires() {
            return commentaires;
        }

        public void setCommentaires(String commentaires) {
            this.commentaires = commentaires;
        }

        // Méthodes pour l'affichage formaté des dates
        public String getFormattedDateDebut() {
            return formatDateTime(date_debut);
        }

        public String getFormattedDateFin() {
            return formatDateTime(date_fin);
        }

        public String getFormattedDateReservation() {
            return formatDateTime(date_reservation);
        }

        public String getFormattedUpdatedAt() {
            return formatDateTime(updated_at);
        }

        private String formatDateTime(LocalDateTime dateTime) {
            if (dateTime == null) {
                return "";
            }
            return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }

        // Méthode toString pour le débogage
        @Override
        public String toString() {
            return "ReservationSalle{" +
                    "id_reservation=" + id_reservation +
                    ", id_salle=" + id_salle +
                    ", id_utilisateur=" + id_utilisateur +
                    ", date_debut=" + getFormattedDateDebut() +
                    ", date_fin=" + getFormattedDateFin() +
                    ", statut='" + statut + '\'' +
                    ", motif='" + motif + '\'' +
                    ", statut_paiement='" + statut_paiement + '\'' +
                    ", date_reservation=" + getFormattedDateReservation() +
                    ", updated_at=" + getFormattedUpdatedAt() +
                    ", nombre_participants=" + nombre_participants +
                    ", statut_reservation='" + statut_reservation + '\'' +
                    ", commentaires='" + commentaires + '\'' +
                    '}';
        }
    }
}