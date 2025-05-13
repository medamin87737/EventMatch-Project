package com.example.eventmatch_pr;

import com.example.eventmatch_pr.entity.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddReservationController {
    // Références aux éléments FXML
    @FXML private ComboBox<Salle> cbSalle;
    @FXML private TextField txtUserId;  // Gardé mais rendu non éditable
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

    // Initialisation
    @FXML
    public void initialize() {
        // Récupération de l'ID utilisateur connecté
        int connectedUserId = UserSession.getInstance().getUserId();
        txtUserId.setText(String.valueOf(connectedUserId));
        txtUserId.setEditable(false);  // Empêche la modification manuelle

        // Configuration des heures (8h-20h)
        ObservableList<String> heures = FXCollections.observableArrayList();
        for (int i = 8; i <= 20; i++) {
            heures.add(String.format("%02d:00", i));
        }
        cbHeureDebut.setItems(heures);
        cbHeureFin.setItems(heures);

        // Configuration des statuts
        cbStatut.setItems(FXCollections.observableArrayList("en_attente", "valide"));
        cbStatut.getSelectionModel().selectFirst();

        // Configuration des statuts de paiement
        cbStatutPaiement.setItems(FXCollections.observableArrayList("payé", "non_payé"));
        cbStatutPaiement.getSelectionModel().select(1); // non_payé par défaut
    }

    // Modifiez la méthode setSalleList dans les deux contrôleurs
    public void setSalleList(ObservableList<Salle> salleList) {
        this.salleList = salleList;

        // Configuration du ComboBox pour afficher seulement le nom
        cbSalle.setItems(salleList);
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
    public void setParentController(ReservationSalleController parentController) {
        this.parentController = parentController;
    }

    // Gestion de l'enregistrement (inchangée)
    @FXML
    private void handleSave() {
        if (!validateInputs()) {
            return;
        }

        try {
            ReservationSalle nouvelleReservation = createReservationFromInput();
            saveReservationToDatabase(nouvelleReservation);
            closeWindow();
            parentController.refreshData();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'enregistrement: " + e.getMessage());
        }
    }

    private ReservationSalle createReservationFromInput() {
        ReservationSalle reservation = new ReservationSalle();
        reservation.setId_salle(cbSalle.getSelectionModel().getSelectedItem().getId_salle());
        reservation.setId_utilisateur(Integer.parseInt(txtUserId.getText()));  // Utilisation de l'ID affiché

        // Combinaison date + heure
        LocalDateTime dateDebut = LocalDateTime.of(
                dpDateDebut.getValue(),
                LocalTime.parse(cbHeureDebut.getValue())
        );
        LocalDateTime dateFin = LocalDateTime.of(
                dpDateFin.getValue(),
                LocalTime.parse(cbHeureFin.getValue())
        );

        reservation.setDate_debut(dateDebut);
        reservation.setDate_fin(dateFin);
        reservation.setNombre_participants(Integer.parseInt(txtParticipants.getText()));
        reservation.setStatut(cbStatut.getValue());
        reservation.setStatut_paiement(cbStatutPaiement.getValue());
        reservation.setMotif(txtMotif.getText());
        reservation.setCommentaires(taCommentaires.getText());
        reservation.setStatut_reservation("en_attente"); // Valeur par défaut

        return reservation;
    }

    // Validation des entrées (simplifiée pour l'ID utilisateur)
    private boolean validateInputs() {
        if (cbSalle.getSelectionModel().isEmpty()) {
            showAlert("Salle manquante", "Veuillez sélectionner une salle");
            return false;
        }

        // Validation que l'ID utilisateur est bien présent (mais normalement toujours valide)
        if (txtUserId.getText().isEmpty()) {
            showAlert("Erreur session", "Aucun utilisateur connecté");
            return false;
        }

        // Reste des validations inchangées
        if (dpDateDebut.getValue() == null || cbHeureDebut.getValue() == null) {
            showAlert("Date début manquante", "Veuillez spécifier date et heure de début");
            return false;
        }

        if (dpDateFin.getValue() == null || cbHeureFin.getValue() == null) {
            showAlert("Date fin manquante", "Veuillez spécifier date et heure de fin");
            return false;
        }

        if (txtParticipants.getText().isEmpty() || !txtParticipants.getText().matches("\\d+")) {
            showAlert("Participants invalides", "Le nombre de participants doit être un nombre");
            return false;
        }

        LocalDateTime debut = LocalDateTime.of(dpDateDebut.getValue(), LocalTime.parse(cbHeureDebut.getValue()));
        LocalDateTime fin = LocalDateTime.of(dpDateFin.getValue(), LocalTime.parse(cbHeureFin.getValue()));

        if (fin.isBefore(debut) || fin.isEqual(debut)) {
            showAlert("Dates invalides", "La date de fin doit être après la date de début");
            return false;
        }

        return true;
    }

    // Interaction avec la base de données (inchangée)
    private void saveReservationToDatabase(ReservationSalle reservation) throws SQLException {
        String query = "INSERT INTO reservationsalle (id_salle, id_utilisateur, date_debut, date_fin, statut, motif, "
                + "statut_paiement, nombre_participants, statut_reservation, commentaires) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservation.getId_salle());
            stmt.setInt(2, reservation.getId_utilisateur());
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getDate_debut()));
            stmt.setTimestamp(4, Timestamp.valueOf(reservation.getDate_fin()));
            stmt.setString(5, reservation.getStatut());
            stmt.setString(6, reservation.getMotif());
            stmt.setString(7, reservation.getStatut_paiement());
            stmt.setInt(8, reservation.getNombre_participants());
            stmt.setString(9, reservation.getStatut_reservation());
            stmt.setString(10, reservation.getCommentaires());

            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation enregistrée avec succès");
        }
    }

    // Gestion de l'annulation (inchangée)
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) cbSalle.getScene().getWindow()).close();
    }

    // Utilitaires (inchangés)
    private void showAlert(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}