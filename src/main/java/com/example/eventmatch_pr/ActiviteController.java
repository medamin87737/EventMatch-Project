package com.example.eventmatch_pr;

import com.example.eventmatch_pr.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class ActiviteController {
    @FXML private TableView<Activite> activiteTable;
    @FXML private TableColumn<Activite, String> nomColumn;
    @FXML private TableColumn<Activite, String> descriptionColumn;
    @FXML private TableColumn<Activite, String> dateColumn;
    @FXML private TableColumn<Activite, String> heureDebutColumn;
    @FXML private TableColumn<Activite, String> heureFinColumn;
    @FXML private TableColumn<Activite, String> lieuColumn;
    @FXML private TableColumn<Activite, String> responsableColumn;
    @FXML private TableColumn<Activite, String> typeColumn;
    @FXML private TableColumn<Activite, String> statutColumn;
    @FXML private TableColumn<Activite, Integer> placesColumn;
    
    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker dateField;
    @FXML private TextField heureDebutField;
    @FXML private TextField heureFinField;
    @FXML private TextField lieuField;
    @FXML private TextField responsableField;
    @FXML private ComboBox<String> typeField;
    @FXML private ComboBox<String> statutField;
    @FXML private TextField placesField;
    
    @FXML private ComboBox<String> searchTypeField;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    private final ObservableList<Activite> activiteList = FXCollections.observableArrayList();
    private Connection connection;

    @FXML
    public void initialize() {
        try {
            // Connexion à la base de données
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/projet",
                "root",
                ""
            );

            // Initialiser les colonnes
            setupTable();
            
            // Initialiser les ComboBox
            loadComboBoxes();
            
            // Charger les données
            loadData();
            
            // Configurer la recherche après avoir chargé les données
            setupSearch();
            
            // Mettre à jour le statut
            updateStatus("Prêt");
        } catch (SQLException e) {
            showError("Erreur de connexion à la base de données: " + e.getMessage());
        }
    }

    private void setupTable() {
        // Configurer les colonnes avec les bonnes propriétés
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDate();
            return new SimpleStringProperty(date != null ? cellData.getValue().getDateFormatted() : "");
        });
        heureDebutColumn.setCellValueFactory(cellData -> {
            LocalTime heure = cellData.getValue().getHeureDebut();
            return new SimpleStringProperty(heure != null ? cellData.getValue().getHeureDebutFormatted() : "");
        });
        heureFinColumn.setCellValueFactory(cellData -> {
            LocalTime heure = cellData.getValue().getHeureFin();
            return new SimpleStringProperty(heure != null ? cellData.getValue().getHeureFinFormatted() : "");
        });
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        responsableColumn.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        placesColumn.setCellValueFactory(new PropertyValueFactory<>("places"));

        // Configurer la sélection de la table
        activiteTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Ajouter un écouteur pour la sélection
        activiteTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    fillFormWithSelectedItem(newValue);
                }
            }
        );
    }

    private void fillFormWithSelectedItem(Activite activite) {
        nomField.setText(activite.getNom());
        descriptionField.setText(activite.getDescription());
        dateField.setValue(activite.getDate());
        heureDebutField.setText(activite.getHeureDebut() != null ? activite.getHeureDebut().toString() : "");
        heureFinField.setText(activite.getHeureFin() != null ? activite.getHeureFin().toString() : "");
        lieuField.setText(activite.getLieu());
        responsableField.setText(activite.getResponsable());
        typeField.setValue(activite.getType());
        statutField.setValue(activite.getStatut());
        placesField.setText(String.valueOf(activite.getPlaces()));
    }

    private void loadComboBoxes() {
        // Types d'activités
        typeField.getItems().addAll(
            "Karaoke",
            "Pause musicale",
            "Formation",
            "Chorégraphie",
            "Jeux",
            "Autre"
        );

        // Statuts
        statutField.getItems().addAll(
            "À venir",
            "En cours",
            "Terminée"
        );

        // Types de recherche
        searchTypeField.getItems().addAll(
            "Tous les champs",
            "Nom",
            "Description",
            "Lieu",
            "Responsable",
            "Type",
            "Statut"
        );
        searchTypeField.setValue("Tous les champs");
    }

    private void setupSearch() {
        // Créer une liste filtrée basée sur la liste principale
        FilteredList<Activite> filteredData = new FilteredList<>(activiteList, b -> true);

        // Configurer les écouteurs pour la recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter(filteredData);
        });

        searchTypeField.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter(filteredData);
        });

        // Créer une liste triée basée sur la liste filtrée
        SortedList<Activite> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(activiteTable.comparatorProperty());

        // Mettre à jour le tableau avec la liste triée
        activiteTable.setItems(sortedData);
    }

    private void updateFilter(FilteredList<Activite> filteredData) {
        String searchTerm = searchField.getText();
        String searchType = searchTypeField.getValue();

        filteredData.setPredicate(activite -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return true;
            }

            String lowerCaseSearchTerm = searchTerm.toLowerCase();

            switch (searchType) {
                case "Nom":
                    return activite.getNom() != null && activite.getNom().toLowerCase().contains(lowerCaseSearchTerm);
                case "Description":
                    return activite.getDescription() != null && activite.getDescription().toLowerCase().contains(lowerCaseSearchTerm);
                case "Lieu":
                    return activite.getLieu() != null && activite.getLieu().toLowerCase().contains(lowerCaseSearchTerm);
                case "Responsable":
                    return activite.getResponsable() != null && activite.getResponsable().toLowerCase().contains(lowerCaseSearchTerm);
                case "Type":
                    return activite.getType() != null && activite.getType().toLowerCase().contains(lowerCaseSearchTerm);
                case "Statut":
                    return activite.getStatut() != null && activite.getStatut().toLowerCase().contains(lowerCaseSearchTerm);
                default:
                    return searchInAllFields(activite, lowerCaseSearchTerm);
            }
        });

        // Mettre à jour le statut
        if (searchTerm.isEmpty()) {
            updateStatus("Prêt");
        } else {
            updateStatus("Recherche active: " + searchTerm);
        }
    }

    private boolean searchInAllFields(Activite activite, String searchTerm) {
        return (activite.getNom() != null && activite.getNom().toLowerCase().contains(searchTerm)) ||
               (activite.getDescription() != null && activite.getDescription().toLowerCase().contains(searchTerm)) ||
               (activite.getLieu() != null && activite.getLieu().toLowerCase().contains(searchTerm)) ||
               (activite.getResponsable() != null && activite.getResponsable().toLowerCase().contains(searchTerm)) ||
               (activite.getType() != null && activite.getType().toLowerCase().contains(searchTerm)) ||
               (activite.getStatut() != null && activite.getStatut().toLowerCase().contains(searchTerm));
    }

    private void loadData() {
        try {
            activiteList.clear();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM activite");

            while (resultSet.next()) {
                Activite activite = new Activite();
                activite.setId(resultSet.getInt("id"));
                activite.setNom(resultSet.getString("nom"));
                activite.setDescription(resultSet.getString("description"));
                Date sqlDate = resultSet.getDate("date");
                activite.setDate(sqlDate != null ? sqlDate.toLocalDate() : null);
                Time sqlHeureDebut = resultSet.getTime("heure_debut");
                activite.setHeureDebut(sqlHeureDebut != null ? sqlHeureDebut.toLocalTime() : null);
                Time sqlHeureFin = resultSet.getTime("heure_fin");
                activite.setHeureFin(sqlHeureFin != null ? sqlHeureFin.toLocalTime() : null);
                activite.setLieu(resultSet.getString("lieu"));
                activite.setResponsable(resultSet.getString("responsable"));
                activite.setType(resultSet.getString("type"));
                activite.setStatut(resultSet.getString("statut"));
                activite.setPlaces(resultSet.getInt("places"));
                activiteList.add(activite);
            }

            // Mettre à jour le tableau avec les données
            activiteTable.setItems(activiteList);
            updateStatus("Données chargées avec succès");
        } catch (SQLException e) {
            showError("Erreur lors du chargement des données: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        if (validateForm()) {
            try {
                // Créer une nouvelle activité
                Activite activite = new Activite();
                activite.setNom(nomField.getText());
                activite.setDescription(descriptionField.getText());
                activite.setDate(dateField.getValue());
                activite.setHeureDebut(LocalTime.parse(heureDebutField.getText()));
                activite.setHeureFin(LocalTime.parse(heureFinField.getText()));
                activite.setLieu(lieuField.getText());
                activite.setResponsable(responsableField.getText());
                activite.setType(typeField.getValue());
                activite.setStatut(statutField.getValue());
                activite.setPlaces(Integer.parseInt(placesField.getText()));

                // Insérer dans la base de données
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO activite (nom, description, date, heure_debut, heure_fin, lieu, responsable, type, statut, places) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );

                statement.setString(1, activite.getNom());
                statement.setString(2, activite.getDescription());
                statement.setDate(3, Date.valueOf(activite.getDate()));
                statement.setTime(4, Time.valueOf(activite.getHeureDebut()));
                statement.setTime(5, Time.valueOf(activite.getHeureFin()));
                statement.setString(6, activite.getLieu());
                statement.setString(7, activite.getResponsable());
                statement.setString(8, activite.getType());
                statement.setString(9, activite.getStatut());
                statement.setInt(10, activite.getPlaces());

                statement.executeUpdate();

                // Récupérer l'ID généré
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    activite.setId(generatedKeys.getInt(1));
                }

                // Ajouter à la liste
                activiteList.add(activite);

                // Nettoyer le formulaire
                clearForm();

                updateStatus("Activité ajoutée avec succès");
            } catch (SQLException e) {
                showError("Erreur lors de l'ajout de l'activité: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleModifier() {
        Activite selectedActivite = activiteTable.getSelectionModel().getSelectedItem();
        if (selectedActivite == null) {
            showError("Veuillez sélectionner une activité à modifier");
            return;
        }

        if (validateForm()) {
            try {
                // Mettre à jour l'activité
                selectedActivite.setNom(nomField.getText());
                selectedActivite.setDescription(descriptionField.getText());
                selectedActivite.setDate(dateField.getValue());
                selectedActivite.setHeureDebut(LocalTime.parse(heureDebutField.getText()));
                selectedActivite.setHeureFin(LocalTime.parse(heureFinField.getText()));
                selectedActivite.setLieu(lieuField.getText());
                selectedActivite.setResponsable(responsableField.getText());
                selectedActivite.setType(typeField.getValue());
                selectedActivite.setStatut(statutField.getValue());
                selectedActivite.setPlaces(Integer.parseInt(placesField.getText()));

                // Mettre à jour dans la base de données
                PreparedStatement statement = connection.prepareStatement(
                    "UPDATE activite SET nom = ?, description = ?, date = ?, heure_debut = ?, heure_fin = ?, " +
                    "lieu = ?, responsable = ?, type = ?, statut = ?, places = ? WHERE id = ?"
                );

                statement.setString(1, selectedActivite.getNom());
                statement.setString(2, selectedActivite.getDescription());
                statement.setDate(3, Date.valueOf(selectedActivite.getDate()));
                statement.setTime(4, Time.valueOf(selectedActivite.getHeureDebut()));
                statement.setTime(5, Time.valueOf(selectedActivite.getHeureFin()));
                statement.setString(6, selectedActivite.getLieu());
                statement.setString(7, selectedActivite.getResponsable());
                statement.setString(8, selectedActivite.getType());
                statement.setString(9, selectedActivite.getStatut());
                statement.setInt(10, selectedActivite.getPlaces());
                statement.setInt(11, selectedActivite.getId());

                statement.executeUpdate();

                // Mettre à jour la table
                activiteTable.refresh();

                // Nettoyer le formulaire
                clearForm();

                updateStatus("Activité modifiée avec succès");
            } catch (SQLException e) {
                showError("Erreur lors de la modification de l'activité: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSupprimer() {
        Activite selectedActivite = activiteTable.getSelectionModel().getSelectedItem();
        if (selectedActivite == null) {
            showError("Veuillez sélectionner une activité à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'activité");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette activité ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer de la base de données
                PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM activite WHERE id = ?"
                );
                statement.setInt(1, selectedActivite.getId());
                statement.executeUpdate();

                // Supprimer de la liste
                activiteList.remove(selectedActivite);

                // Nettoyer le formulaire
                clearForm();

                updateStatus("Activité supprimée avec succès");
            } catch (SQLException e) {
                showError("Erreur lors de la suppression de l'activité: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleActualiser() {
        loadData();
    }

    @FXML
    private void handleRechercher() {
        updateFilter(new FilteredList<>(activiteList, b -> true));
    }

    @FXML
    private void handleReinitialiser() {
        clearForm();
        searchField.clear();
        searchTypeField.setValue("Tous les champs");
    }

    private boolean validateForm() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage += "Le nom est requis!\n";
        }
        if (dateField.getValue() == null) {
            errorMessage += "La date est requise!\n";
        }
        if (heureDebutField.getText() == null || heureDebutField.getText().trim().isEmpty()) {
            errorMessage += "L'heure de début est requise!\n";
        } else {
            try {
                LocalTime.parse(heureDebutField.getText());
            } catch (Exception e) {
                errorMessage += "L'heure de début doit être au format HH:mm !\n";
            }
        }
        if (heureFinField.getText() == null || heureFinField.getText().trim().isEmpty()) {
            errorMessage += "L'heure de fin est requise!\n";
        } else {
            try {
                LocalTime.parse(heureFinField.getText());
            } catch (Exception e) {
                errorMessage += "L'heure de fin doit être au format HH:mm !\n";
            }
        }
        // Contrôle heure de début < heure de fin
        if (heureDebutField.getText() != null && !heureDebutField.getText().trim().isEmpty() &&
            heureFinField.getText() != null && !heureFinField.getText().trim().isEmpty()) {
            try {
                LocalTime debut = LocalTime.parse(heureDebutField.getText());
                LocalTime fin = LocalTime.parse(heureFinField.getText());
                if (!fin.isAfter(debut)) {
                    errorMessage += "L'heure de fin doit être après l'heure de début !\n";
                }
            } catch (Exception e) {
                // Erreurs de format déjà gérées plus haut
            }
        }
        if (lieuField.getText() == null || lieuField.getText().trim().isEmpty()) {
            errorMessage += "Le lieu est requis!\n";
        }
        if (responsableField.getText() == null || responsableField.getText().trim().isEmpty()) {
            errorMessage += "Le responsable est requis!\n";
        }
        if (typeField.getValue() == null) {
            errorMessage += "Le type est requis!\n";
        }
        if (statutField.getValue() == null) {
            errorMessage += "Le statut est requis!\n";
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

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showError(errorMessage);
            return false;
        }
    }

    private void clearForm() {
        nomField.clear();
        descriptionField.clear();
        dateField.setValue(null);
        heureDebutField.clear();
        heureFinField.clear();
        lieuField.clear();
        responsableField.clear();
        typeField.setValue(null);
        statutField.setValue(null);
        placesField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
} 