package com.example.eventmatch_pr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class ParticipantController {
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private DatePicker dateInscriptionField;
    @FXML private ComboBox<String> statutField;
    @FXML private TextField badgeIdField;
    @FXML private ComboBox<String> activiteField;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchTypeField;
    @FXML private TableView<Participant> participantTable;
    @FXML private TableColumn<Participant, Integer> idColumn;
    @FXML private TableColumn<Participant, String> nomColumn;
    @FXML private TableColumn<Participant, String> prenomColumn;
    @FXML private TableColumn<Participant, String> emailColumn;
    @FXML private TableColumn<Participant, String> telephoneColumn;
    @FXML private TableColumn<Participant, LocalDate> dateInscriptionColumn;
    @FXML private TableColumn<Participant, String> statutColumn;
    @FXML private TableColumn<Participant, String> badgeIdColumn;
    @FXML private TableColumn<Participant, String> activiteColumn;
    @FXML private Label statusLabel;

    private ObservableList<Participant> participantList = FXCollections.observableArrayList();
    private Connection connection;

    @FXML
    public void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet", "root", "");
            setupTable();
            loadParticipants();
            setupSearch();
            loadComboBoxes();
            setupEmailValidation();
        } catch (SQLException e) {
            showError("Erreur de connexion à la base de données: " + e.getMessage());
        }
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        dateInscriptionColumn.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        badgeIdColumn.setCellValueFactory(new PropertyValueFactory<>("badgeId"));
        activiteColumn.setCellValueFactory(new PropertyValueFactory<>("activite"));

        participantTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillForm(newSelection);
            }
        });
    }

    private void setupEmailValidation() {
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !isValidEmail(newValue)) {
                emailField.setStyle("-fx-border-color: #FF0000; -fx-border-width: 2px;");
            } else {
                emailField.setStyle("");
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private void loadComboBoxes() {
        statutField.setItems(FXCollections.observableArrayList("confirmé", "en attente", "annulé"));
        searchTypeField.setItems(FXCollections.observableArrayList(
            "Tous les champs",
            "Nom",
            "Prénom",
            "Email",
            "Téléphone",
            "Activité"
        ));
        searchTypeField.setValue("Tous les champs");
        loadActivites();
    }

    private void loadActivites() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nom FROM activite");
            ObservableList<String> activites = FXCollections.observableArrayList();
            while (rs.next()) {
                activites.add(rs.getString("nom"));
            }
            activiteField.setItems(activites);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des activités: " + e.getMessage());
        }
    }

    private void loadParticipants() {
        try {
            participantList.clear();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.*, a.nom as activite_nom FROM participant p LEFT JOIN activite a ON p.id_activite = a.id");
            while (rs.next()) {
                participantList.add(new Participant(
                    rs.getInt("id_participant"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getDate("date_inscription").toLocalDate(),
                    rs.getString("statut"),
                    rs.getString("badge_id"),
                    rs.getString("activite_nom")
                ));
            }
            participantTable.setItems(participantList);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des participants: " + e.getMessage());
        }
    }

    private void setupSearch() {
        FilteredList<Participant> filteredData = new FilteredList<>(participantList, b -> true);
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(participant -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                String searchType = searchTypeField.getValue();
                
                if (searchType == null || searchType.equals("Tous les champs")) {
                    return searchInAllFields(participant, lowerCaseFilter);
                }
                
                switch (searchType) {
                    case "Nom":
                        return participant.getNom() != null && 
                               participant.getNom().toLowerCase().contains(lowerCaseFilter);
                    case "Prénom":
                        return participant.getPrenom() != null && 
                               participant.getPrenom().toLowerCase().contains(lowerCaseFilter);
                    case "Email":
                        return participant.getEmail() != null && 
                               participant.getEmail().toLowerCase().contains(lowerCaseFilter);
                    case "Téléphone":
                        return participant.getTelephone() != null && 
                               participant.getTelephone().contains(lowerCaseFilter);
                    case "Activité":
                        return participant.getActivite() != null && 
                               participant.getActivite().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });

        SortedList<Participant> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(participantTable.comparatorProperty());
        participantTable.setItems(sortedData);
    }

    private boolean searchInAllFields(Participant participant, String searchTerm) {
        return (participant.getNom() != null && participant.getNom().toLowerCase().contains(searchTerm)) ||
               (participant.getPrenom() != null && participant.getPrenom().toLowerCase().contains(searchTerm)) ||
               (participant.getEmail() != null && participant.getEmail().toLowerCase().contains(searchTerm)) ||
               (participant.getTelephone() != null && participant.getTelephone().contains(searchTerm)) ||
               (participant.getActivite() != null && participant.getActivite().toLowerCase().contains(searchTerm));
    }

    @FXML
    private void handleAjouter() {
        if (validateForm()) {
            try {
                PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO participant (nom, prenom, email, telephone, date_inscription, statut, badge_id, id_activite) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT id FROM activite WHERE nom = ?))"
                );
                fillStatement(stmt);
                stmt.executeUpdate();
                clearForm();
                loadParticipants();
                showSuccess("Participant ajouté avec succès");
            } catch (SQLException e) {
                showError("Erreur lors de l'ajout du participant: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleModifier() {
        Participant selected = participantTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Veuillez sélectionner un participant à modifier");
            return;
        }

        if (validateForm()) {
            try {
                PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE participant SET nom = ?, prenom = ?, email = ?, telephone = ?, " +
                    "date_inscription = ?, statut = ?, badge_id = ?, id_activite = " +
                    "(SELECT id FROM activite WHERE nom = ?) WHERE id_participant = ?"
                );
                fillStatement(stmt);
                stmt.setInt(9, selected.getId());
                stmt.executeUpdate();
                clearForm();
                loadParticipants();
                showSuccess("Participant modifié avec succès");
            } catch (SQLException e) {
                showError("Erreur lors de la modification du participant: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        Participant selected = participantTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Veuillez sélectionner un participant à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le participant");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce participant ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM participant WHERE id_participant = ?"
                );
                stmt.setInt(1, selected.getId());
                stmt.executeUpdate();
                clearForm();
                loadParticipants();
                showSuccess("Participant supprimé avec succès");
            } catch (SQLException e) {
                showError("Erreur lors de la suppression du participant: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleReinitialiser() {
        searchField.clear();
        searchTypeField.setValue("Tous les champs");
        statusLabel.setText("Prêt");
        statusLabel.setStyle("-fx-text-fill: white;");
    }

    @FXML
    private void handleRechercher() {
        if (!searchField.getText().isEmpty()) {
            statusLabel.setText("Recherche active");
            statusLabel.setStyle("-fx-text-fill: #2196F3;");
        } else {
            statusLabel.setText("Prêt");
            statusLabel.setStyle("-fx-text-fill: white;");
        }
    }

    private void fillStatement(PreparedStatement stmt) throws SQLException {
        stmt.setString(1, nomField.getText());
        stmt.setString(2, prenomField.getText());
        stmt.setString(3, emailField.getText());
        stmt.setString(4, telephoneField.getText());
        stmt.setDate(5, Date.valueOf(dateInscriptionField.getValue()));
        stmt.setString(6, statutField.getValue());
        stmt.setString(7, badgeIdField.getText());
        stmt.setString(8, activiteField.getValue());
    }

    private void fillForm(Participant participant) {
        nomField.setText(participant.getNom());
        prenomField.setText(participant.getPrenom());
        emailField.setText(participant.getEmail());
        telephoneField.setText(participant.getTelephone());
        dateInscriptionField.setValue(participant.getDateInscription());
        statutField.setValue(participant.getStatut());
        badgeIdField.setText(participant.getBadgeId());
        activiteField.setValue(participant.getActivite());
    }

    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
        dateInscriptionField.setValue(null);
        statutField.setValue(null);
        badgeIdField.clear();
        activiteField.setValue(null);
    }

    private boolean validateForm() {
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
            emailField.getText().isEmpty() || telephoneField.getText().isEmpty() ||
            dateInscriptionField.getValue() == null || statutField.getValue() == null ||
            badgeIdField.getText().isEmpty() || activiteField.getValue() == null) {
            showError("Veuillez remplir tous les champs");
            return false;
        }

        if (!isValidEmail(emailField.getText())) {
            showError("Format d'email invalide. Exemple: exemple@domaine.com");
            return false;
        }

        if (!telephoneField.getText().matches("^[0-9]{8}$")) {
            showError("Le numéro de téléphone doit contenir 8 chiffres");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        statusLabel.setText("Erreur: " + message);
        statusLabel.setStyle("-fx-text-fill: #FF0000;");
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #00FF00;");
    }
} 