package com.example.eventmatch_pr;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.event.ActionEvent;


import javafx.scene.Parent;
import javafx.util.Callback;

public class ReservationSalleController {
    @FXML private TableView<ReservationSalle> tableReservations;
    @FXML private TableColumn<ReservationSalle, String> colSalle;
    @FXML private TableColumn<ReservationSalle, LocalDateTime> colDateDebut;
    @FXML private TableColumn<ReservationSalle, LocalDateTime> colDateFin;
    @FXML private TableColumn<ReservationSalle, String> colStatut;
    @FXML private TableColumn<ReservationSalle, Integer> colParticipants;
    @FXML private TableColumn<ReservationSalle, String> colStatutPaiement;

    @FXML private ComboBox<String> cbSearchAttribute;
    @FXML private TextField txtSearch;

    private ObservableList<ReservationSalle> reservationList = FXCollections.observableArrayList();
    private ObservableList<Salle> salleList = FXCollections.observableArrayList();

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void initialize() {
        connectToDatabase();
        setupTableColumns();
        loadReservations();
        loadSearchAttributes();
        loadSalles();
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données");
        }
    }
    private void setupTableColumns() {
        // Formatteur de date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Colonne ID


        // Colonne Utilisateur
        colSalle.setCellValueFactory(cellData -> {
            // 1. Récupérer l'ID de la salle de la réservation actuelle
            int idSalleReservation = cellData.getValue().getId_salle();

            // 2. Trouver la salle correspondante dans la liste
            String nomSalle = salleList.stream()
                    .filter(salle -> salle.getId_salle() == idSalleReservation)  // Filtrer par ID
                    .findFirst()  // Prendre la première correspondance
                    .map(Salle::getNom_salle)  // Extraire le nom
                    .orElse("Inconnu");  // Valeur par défaut si non trouvé

            // 3. Retourner la propriété avec le nom seulement
            return new SimpleStringProperty(nomSalle);
        });

        // Colonne Date Début
        colDateDebut.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDate_debut()));

        // Colonne Date Fin
        colDateFin.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDate_fin()));

        // Colonnes simples
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colParticipants.setCellValueFactory(new PropertyValueFactory<>("nombre_participants"));
        colStatutPaiement.setCellValueFactory(new PropertyValueFactory<>("statut_paiement"));

        // Configuration du rendu des dates
        Callback<TableColumn<ReservationSalle, LocalDateTime>, TableCell<ReservationSalle, LocalDateTime>> cellFactory =
                column -> new TableCell<>() {
                    @Override
                    protected void updateItem(LocalDateTime date, boolean empty) {
                        super.updateItem(date, empty);
                        if (empty || date == null) {
                            setText(null);
                        } else {
                            setText(formatter.format(date));
                        }
                    }
                };

        colDateDebut.setCellFactory(cellFactory);
        colDateFin.setCellFactory(cellFactory);
    }
    private void loadReservations() {
        reservationList.clear();
        try {
            String query = "SELECT * FROM reservationsalle";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ReservationSalle reservation = new ReservationSalle(
                        resultSet.getInt("id_reservation"),
                        resultSet.getInt("id_salle"),
                        resultSet.getInt("id_utilisateur"),
                        resultSet.getTimestamp("date_debut").toLocalDateTime(),
                        resultSet.getTimestamp("date_fin").toLocalDateTime(),
                        resultSet.getString("statut"),
                        resultSet.getString("motif"),
                        resultSet.getString("statut_paiement"),
                        resultSet.getTimestamp("date_reservation").toLocalDateTime(),
                        resultSet.getTimestamp("updated_at").toLocalDateTime(),
                        resultSet.getInt("nombre_participants"),
                        resultSet.getString("statut_reservation"),
                        resultSet.getString("commentaires")
                );
                reservationList.add(reservation);
            }
            tableReservations.setItems(reservationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSalles() {
        salleList.clear();
        try {
            String query = "SELECT * FROM salle";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Salle salle = new Salle(
                        resultSet.getInt("id_salle"),
                        resultSet.getString("nom_salle"),
                        resultSet.getInt("capacite"),
                        resultSet.getString("type_salle"),
                        resultSet.getBoolean("disponible"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        resultSet.getTimestamp("updated_at").toLocalDateTime(),
                        resultSet.getString("adresse"),
                        resultSet.getString("ville"),
                        resultSet.getDouble("prix_par_heure")
                );
                salleList.add(salle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSearchAttributes() {
        ObservableList<String> attributes = FXCollections.observableArrayList(
                "ID", "Salle", "Utilisateur", "Date Début", "Date Fin",
                "Statut", "Participants", "Statut Paiement"
        );
        cbSearchAttribute.setItems(attributes);
        cbSearchAttribute.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleAddReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/eventmatch_pr/AddReservation.fxml"));
            Parent root = loader.load();

            AddReservationController controller = loader.getController();
            controller.setSalleList(salleList);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter une réservation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadReservations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateReservation(ActionEvent event) {
        ReservationSalle selectedReservation = tableReservations.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une réservation à modifier");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/eventmatch_pr/EditReservation.fxml"));
            Parent root = loader.load();

            EditReservationController controller = loader.getController(); // Correction ici
            controller.setReservation(selectedReservation);
            controller.setSalleList(salleList);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier la réservation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadReservations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDeleteReservation(ActionEvent event) {
        ReservationSalle selectedReservation = tableReservations.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une réservation à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la réservation");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String query = "DELETE FROM reservationsalle WHERE id_reservation = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, selectedReservation.getId_reservation());
                preparedStatement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation supprimée avec succès");
                loadReservations();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression de la réservation");
            }
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        tableReservations.getSelectionModel().clearSelection();
        txtSearch.clear();
        cbSearchAttribute.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            loadReservations();
            return;
        }

        String selectedAttribute = cbSearchAttribute.getSelectionModel().getSelectedItem();
        if (selectedAttribute == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun attribut sélectionné", "Veuillez sélectionner un attribut pour la recherche");
            return;
        }

        reservationList.clear();
        try {
            String query = buildSearchQuery(selectedAttribute);
            preparedStatement = connection.prepareStatement(query);

            if (selectedAttribute.equals("Salle")) {
                // Recherche par nom de salle
                preparedStatement.setString(1, "%" + searchText + "%");
            } else {
                // Pour les autres attributs
                preparedStatement.setString(1, "%" + searchText + "%");
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ReservationSalle reservation = new ReservationSalle(
                        resultSet.getInt("id_reservation"),
                        resultSet.getInt("id_salle"),
                        resultSet.getInt("id_utilisateur"),
                        resultSet.getTimestamp("date_debut").toLocalDateTime(),
                        resultSet.getTimestamp("date_fin").toLocalDateTime(),
                        resultSet.getString("statut"),
                        resultSet.getString("motif"),
                        resultSet.getString("statut_paiement"),
                        resultSet.getTimestamp("date_reservation").toLocalDateTime(),
                        resultSet.getTimestamp("updated_at").toLocalDateTime(),
                        resultSet.getInt("nombre_participants"),
                        resultSet.getString("statut_reservation"),
                        resultSet.getString("commentaires")
                );
                reservationList.add(reservation);
            }

            if (reservationList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Aucun résultat", "Aucune réservation trouvée avec ces critères");
            }

            tableReservations.setItems(reservationList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de recherche", "Une erreur s'est produite lors de la recherche");
        }
    }

    private String buildSearchQuery(String attribute) {
        switch (attribute) {
            case "ID":
                return "SELECT r.* FROM reservationsalle r WHERE r.id_reservation LIKE ?";
            case "Salle":
                return "SELECT r.* FROM reservationsalle r JOIN salle s ON r.id_salle = s.id_salle WHERE s.nom_salle LIKE ?";
            case "Utilisateur":
                return "SELECT r.* FROM reservationsalle r WHERE r.id_utilisateur LIKE ?";
            case "Date Début":
                return "SELECT r.* FROM reservationsalle r WHERE DATE_FORMAT(r.date_debut, '%d/%m/%Y %H:%i') LIKE ?";
            case "Date Fin":
                return "SELECT r.* FROM reservationsalle r WHERE DATE_FORMAT(r.date_fin, '%d/%m/%Y %H:%i') LIKE ?";
            case "Statut":
                return "SELECT r.* FROM reservationsalle r WHERE r.statut LIKE ?";
            case "Participants":
                return "SELECT r.* FROM reservationsalle r WHERE r.nombre_participants LIKE ?";
            case "Statut Paiement":
                return "SELECT r.* FROM reservationsalle r WHERE r.statut_paiement LIKE ?";
            default:
                return "SELECT r.* FROM reservationsalle r";
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refreshData() {
        loadReservations();
    }


}