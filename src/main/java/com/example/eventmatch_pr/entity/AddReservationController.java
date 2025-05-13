package com.example.eventmatch_pr.entity;

import com.example.eventmatch_pr.ReservationSalleController;
import com.example.eventmatch_pr.ReservationSalle;
import com.example.eventmatch_pr.Salle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddReservationController {
    @FXML private ComboBox<Salle> cbSalle;
    @FXML private TextField txtUserId;
    @FXML private DatePicker dpDateDebut;
    @FXML private ComboBox<String> cbHeureDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private ComboBox<String> cbHeureFin;
    @FXML private TextField txtParticipants;
    @FXML private ComboBox<String> cbStatut;
    @FXML private ComboBox<String> cbStatutPaiement;
    @FXML private TextField txtMotif;
    @FXML private TextArea taCommentaires;

    private ObservableList<Salle> salleList = FXCollections.observableArrayList();
    private ReservationSalleController parentController;

    public void setSalleList(ObservableList<Salle> salleList) {
        this.salleList = salleList;
        cbSalle.setItems(salleList);
    }

    public void setParentController(ReservationSalleController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        // Initialiser les heures (8h-20h)
        ObservableList<String> heures = FXCollections.observableArrayList();
        for (int i = 8; i <= 20; i++) {
            heures.add(String.format("%02d:00", i));
        }
        cbHeureDebut.setItems(heures);
        cbHeureFin.setItems(heures);

        // Initialiser les statuts
        cbStatut.setItems(FXCollections.observableArrayList("en_attente", "valide"));
        cbStatut.getSelectionModel().selectFirst();

        // Initialiser les statuts de paiement
        cbStatutPaiement.setItems(FXCollections.observableArrayList("payé", "non_payé"));
        cbStatutPaiement.getSelectionModel().select(1); // non_payé par défaut
    }

    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }

        try {
            // Créer la réservation
            ReservationSalle reservation = new ReservationSalle();
            reservation.setId_salle(cbSalle.getSelectionModel().getSelectedItem().getId_salle());
            reservation.setId_utilisateur(Integer.parseInt(txtUserId.getText()));

            // Combiner date et heure pour date_debut et date_fin
            LocalDate dateDebut = dpDateDebut.getValue();
            LocalTime heureDebut = LocalTime.parse(cbHeureDebut.getValue());
            reservation.setDate_debut(LocalDateTime.of(dateDebut, heureDebut));

            LocalDate dateFin = dpDateFin.getValue();
            LocalTime heureFin = LocalTime.parse(cbHeureFin.getValue());
            reservation.setDate_fin(LocalDateTime.of(dateFin, heureFin));

            reservation.setNombre_participants(Integer.parseInt(txtParticipants.getText()));
            reservation.setStatut(cbStatut.getValue());
            reservation.setStatut_paiement(cbStatutPaiement.getValue());
            reservation.setMotif(txtMotif.getText());
            reservation.setCommentaires(taCommentaires.getText());
            reservation.setStatut_reservation("en_attente");

            // Enregistrer dans la base de données
            saveReservationToDatabase(reservation);

            // Fermer la fenêtre
            ((Stage) cbSalle.getScene().getWindow()).close();

            // Rafraîchir les données dans le parent
            parentController.refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'enregistrement");
        }
    }

    private boolean validateInputs() {
        if (cbSalle.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez sélectionner une salle");
            return false;
        }

        if (txtUserId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez saisir l'ID utilisateur");
            return false;
        }

        try {
            Integer.parseInt(txtUserId.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", "ID utilisateur doit être un nombre");
            return false;
        }

        if (dpDateDebut.getValue() == null || cbHeureDebut.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez saisir la date et heure de début");
            return false;
        }

        if (dpDateFin.getValue() == null || cbHeureFin.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez saisir la date et heure de fin");
            return false;
        }

        if (txtParticipants.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez saisir le nombre de participants");
            return false;
        }

        try {
            Integer.parseInt(txtParticipants.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Nombre de participants doit être un nombre");
            return false;
        }

        return true;
    }

    private void saveReservationToDatabase(ReservationSalle reservation) {
        String query = "INSERT INTO reservationsalle (id_salle, id_utilisateur, date_debut, date_fin, statut, motif, " +
                "statut_paiement, nombre_participants, statut_reservation, commentaires) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/votre_base_de_donnees", "username", "password");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, reservation.getId_salle());
            statement.setInt(2, reservation.getId_utilisateur());
            statement.setTimestamp(3, Timestamp.valueOf(reservation.getDate_debut()));
            statement.setTimestamp(4, Timestamp.valueOf(reservation.getDate_fin()));
            statement.setString(5, reservation.getStatut());
            statement.setString(6, reservation.getMotif());
            statement.setString(7, reservation.getStatut_paiement());
            statement.setInt(8, reservation.getNombre_participants());
            statement.setString(9, reservation.getStatut_reservation());
            statement.setString(10, reservation.getCommentaires());

            statement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation enregistrée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'enregistrement de la réservation");
        }
    }

    @FXML
    private void handleCancel() {
        ((Stage) cbSalle.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}