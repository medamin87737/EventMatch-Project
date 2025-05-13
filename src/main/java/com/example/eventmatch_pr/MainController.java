package com.example.eventmatch_pr;

import com.example.eventmatch_pr.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class MainController {
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
    @FXML private ComboBox<String> typeFilter;
    @FXML private ComboBox<String> statutFilter;
    @FXML private DatePicker dateFilter;
    @FXML private Label statusLabel;

    private final ObservableList<Activite> activiteList = FXCollections.observableArrayList();
    private final ActiviteService activiteService = new ActiviteService();

    @FXML
    public void initialize() {
        // Initialiser les colonnes
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureDebutColumn.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        heureFinColumn.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        responsableColumn.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Initialiser les filtres
        typeFilter.getItems().addAll("Karaoke", "Pause musicale", "Formation", "Chorégraphie", "Jeux", "Autre");
        statutFilter.getItems().addAll("À venir", "En cours", "Terminée");

        // Configurer la sélection de la table
        activiteTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Charger les données
        loadData();
    }

    private void loadData() {
        try {
            activiteList.clear();
            activiteList.addAll(activiteService.findAll());
            activiteTable.setItems(activiteList);
            updateStatus("Données chargées avec succès");
        } catch (Exception e) {
            showError("Erreur lors du chargement des données: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        Activite nouvelleActivite = new Activite();
        if (showEditDialog(nouvelleActivite)) {
            try {
                Activite activiteAjoutee = activiteService.create(nouvelleActivite);
                if (activiteAjoutee != null) {
                    loadData();
                    updateStatus("Activité ajoutée avec succès");
                } else {
                    showError("Erreur lors de l'ajout de l'activité");
                }
            } catch (Exception e) {
                showError("Erreur lors de l'ajout: " + e.getMessage());
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

        if (showEditDialog(selectedActivite)) {
            try {
                activiteService.update(selectedActivite);
                loadData();
                updateStatus("Activité modifiée avec succès");
            } catch (Exception e) {
                showError("Erreur lors de la modification: " + e.getMessage());
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
                activiteService.delete(selectedActivite.getId());
                loadData();
                updateStatus("Activité supprimée avec succès");
            } catch (Exception e) {
                showError("Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleActualiser() {
        loadData();
    }

    @FXML
    private void handleFiltrer() {
        String type = typeFilter.getValue();
        String statut = statutFilter.getValue();
        LocalDate date = dateFilter.getValue();

        FilteredList<Activite> filteredData = new FilteredList<>(activiteList, b -> true);

        filteredData.setPredicate(activite -> {
            boolean typeMatch = type == null || type.isEmpty() || activite.getType().equals(type);
            boolean statutMatch = statut == null || statut.isEmpty() || activite.getStatut().equals(statut);
            boolean dateMatch = date == null || (activite.getDate() != null && activite.getDate().equals(date));

            return typeMatch && statutMatch && dateMatch;
        });

        SortedList<Activite> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(activiteTable.comparatorProperty());
        activiteTable.setItems(sortedData);
    }

    @FXML
    private void handleReinitialiser() {
        typeFilter.setValue(null);
        statutFilter.setValue(null);
        dateFilter.setValue(null);
        loadData();
    }

    @FXML
    private void handleActivites() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("activite-view.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Gestion des Activités");
            stage.setScene(new Scene(root, 800, 600));
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture de la gestion des activités: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleParticipants() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("participant-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des Participants");
            stage.setScene(new Scene(root, 1000, 700));
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture de la gestion des participants: " + e.getMessage());
        }
    }


    public void logoutin(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir le nouveau contenu
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retour(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir le nouveau contenu
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Dashboard des Inscriptions");
            stage.setScene(new Scene(root, 1000, 700));
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture du dashboard: " + e.getMessage());
        }
    }

    private boolean showEditDialog(Activite activite) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/edit-view.fxml"));
            Parent root = loader.load();
            EditController controller = loader.getController();
            controller.setActivite(activite);

            Stage stage = new Stage();
            stage.setTitle(activite.getId() == 0 ? "Nouvelle Activité" : "Modifier Activité");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.isValide()) {
                Activite updatedActivite = controller.getUpdatedActivite();
                activite.setNom(updatedActivite.getNom());
                activite.setDescription(updatedActivite.getDescription());
                activite.setDate(updatedActivite.getDate());
                activite.setHeureDebut(updatedActivite.getHeureDebut());
                activite.setHeureFin(updatedActivite.getHeureFin());
                activite.setLieu(updatedActivite.getLieu());
                activite.setResponsable(updatedActivite.getResponsable());
                activite.setType(updatedActivite.getType());
                activite.setStatut(updatedActivite.getStatut());
                return true;
            }
            return false;
        } catch (Exception e) {
            showError("Erreur lors de l'ouverture du formulaire: " + e.getMessage());
            return false;
        }
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
