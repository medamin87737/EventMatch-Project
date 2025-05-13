package com.example.eventmatch_pr;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EditReservationController {
    @FXML private ComboBox<Salle> cbSalle;
    @FXML private TextField txtUserId; // Conservé mais rendu non éditable
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
    private ReservationSalle currentReservation;

    @FXML
    public void initialize() {
        // Configuration des heures (8h-20h)
        ObservableList<String> heures = FXCollections.observableArrayList();
        for (int i = 8; i <= 20; i++) {
            heures.add(String.format("%02d:00", i));
        }
        cbHeureDebut.setItems(heures);
        cbHeureFin.setItems(heures);

        // Configuration des statuts
        cbStatut.setItems(FXCollections.observableArrayList("en_attente", "valide"));
        cbStatutPaiement.setItems(FXCollections.observableArrayList("payé", "non_payé"));
    }

    public void setReservation(ReservationSalle reservation) {
        this.currentReservation = reservation;
        populateFieldsWithReservationData();
    }

    public void setSalleList(ObservableList<Salle> salleList) {
        this.salleList = salleList;
        cbSalle.setItems(salleList);
        configureSalleComboBox(); // Configurer ici aussi au cas où
    }
    public void setParentController(ReservationSalleController parentController) {
        this.parentController = parentController;
    }
    private void populateFieldsWithReservationData() {
        if (currentReservation != null) {
            // 1. Afficher l'ID utilisateur
            txtUserId.setText(String.valueOf(currentReservation.getId_utilisateur()));
            txtUserId.setEditable(false);

            // 2. Configurer le ComboBox des salles
            configureSalleComboBox();

            // 3. Sélectionner la salle après que la liste soit chargée
            Platform.runLater(() -> {
                Salle salleReservation = salleList.stream()
                        .filter(s -> s.getId_salle() == currentReservation.getId_salle())
                        .findFirst()
                        .orElse(null);

                if (salleReservation != null) {
                    cbSalle.getSelectionModel().select(salleReservation);
                }
            });

            // 4. Configurer les dates
            LocalDateTime dateDebut = currentReservation.getDate_debut();
            dpDateDebut.setValue(dateDebut.toLocalDate());
            cbHeureDebut.setValue(String.format("%02d:00", dateDebut.getHour()));

            LocalDateTime dateFin = currentReservation.getDate_fin();
            dpDateFin.setValue(dateFin.toLocalDate());
            cbHeureFin.setValue(String.format("%02d:00", dateFin.getHour()));

            // 5. Remplir les autres champs
            txtParticipants.setText(String.valueOf(currentReservation.getNombre_participants()));
            cbStatut.setValue(currentReservation.getStatut());
            cbStatutPaiement.setValue(currentReservation.getStatut_paiement());
            txtMotif.setText(currentReservation.getMotif());
            taCommentaires.setText(currentReservation.getCommentaires());
        }
    }

    private void configureSalleComboBox() {
        // Configurer l'affichage des salles
        cbSalle.setCellFactory(param -> new ListCell<Salle>() {
            @Override
            protected void updateItem(Salle salle, boolean empty) {
                super.updateItem(salle, empty);
                setText(empty || salle == null ? null : salle.getNom_salle());
            }
        });

        cbSalle.setConverter(new StringConverter<Salle>() {
            @Override
            public String toString(Salle salle) {
                return salle == null ? null : salle.getNom_salle();
            }

            @Override
            public Salle fromString(String string) {
                return salleList.stream()
                        .filter(s -> s.getNom_salle().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }
    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }

        try {
            // Mettre à jour les champs modifiables
            currentReservation.setId_salle(cbSalle.getSelectionModel().getSelectedItem().getId_salle());

            // L'ID utilisateur reste inchangé (déjà dans currentReservation)

            // Mise à jour des dates
            LocalDate dateDebut = dpDateDebut.getValue();
            LocalTime heureDebut = LocalTime.parse(cbHeureDebut.getValue());
            currentReservation.setDate_debut(LocalDateTime.of(dateDebut, heureDebut));

            LocalDate dateFin = dpDateFin.getValue();
            LocalTime heureFin = LocalTime.parse(cbHeureFin.getValue());
            currentReservation.setDate_fin(LocalDateTime.of(dateFin, heureFin));

            currentReservation.setNombre_participants(Integer.parseInt(txtParticipants.getText()));
            currentReservation.setStatut(cbStatut.getValue());
            currentReservation.setStatut_paiement(cbStatutPaiement.getValue());
            currentReservation.setMotif(txtMotif.getText());
            currentReservation.setCommentaires(taCommentaires.getText());

            updateReservationInDatabase(currentReservation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation mise à jour avec succès");
            closeWindow();
            parentController.refreshData();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (cbSalle.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez sélectionner une salle");
            return false;
        }

        if (dpDateDebut.getValue() == null || cbHeureDebut.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez spécifier date et heure de début");
            return false;
        }

        if (dpDateFin.getValue() == null || cbHeureFin.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez spécifier date et heure de fin");
            return false;
        }

        if (txtParticipants.getText().isEmpty() || !txtParticipants.getText().matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Nombre de participants invalide");
            return false;
        }

        LocalDateTime debut = LocalDateTime.of(dpDateDebut.getValue(), LocalTime.parse(cbHeureDebut.getValue()));
        LocalDateTime fin = LocalDateTime.of(dpDateFin.getValue(), LocalTime.parse(cbHeureFin.getValue()));

        if (fin.isBefore(debut)) {
            showAlert(Alert.AlertType.WARNING, "Validation", "La date de fin doit être après la date de début");
            return false;
        }

        return true;
    }

    private void updateReservationInDatabase(ReservationSalle reservation) throws SQLException {
        String query = "UPDATE reservationsalle SET " +
                "id_salle = ?, " +
                "date_debut = ?, " +
                "date_fin = ?, " +
                "statut = ?, " +
                "motif = ?, " +
                "statut_paiement = ?, " +
                "nombre_participants = ?, " +
                "commentaires = ? " +
                "WHERE id_reservation = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservation.getId_salle());
            stmt.setTimestamp(2, Timestamp.valueOf(reservation.getDate_debut()));
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getDate_fin()));
            stmt.setString(4, reservation.getStatut());
            stmt.setString(5, reservation.getMotif());
            stmt.setString(6, reservation.getStatut_paiement());
            stmt.setInt(7, reservation.getNombre_participants());
            stmt.setString(8, reservation.getCommentaires());
            stmt.setInt(9, reservation.getId_reservation());

            stmt.executeUpdate();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
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