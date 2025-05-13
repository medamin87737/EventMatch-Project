package com.example.eventmatch_pr;
import com.example.eventmatch_pr.DB.DB;
import com.example.eventmatch_pr.entity.*;
import com.example.eventmatch_pr.entity.Role;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Window;
import javafx.util.StringConverter;
import org.mindrot.jbcrypt.BCrypt;

import com.example.eventmatch_pr.implementation.IUser;
import com.example.eventmatch_pr.implementation.impUser;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.util.Properties;

import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.scene.input.*;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;


import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.stage.FileChooser;
import java.io.File;
import java.io.PrintWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SalleController {

    @FXML private TableView<Salle> tableSalles;
    @FXML private TableColumn<Salle, String> colNom;
    @FXML private TableColumn<Salle, Integer> colCapacite;
    @FXML private TableColumn<Salle, String> colType;
    @FXML private TableColumn<Salle, Boolean> colDisponible;
    @FXML private TableColumn<Salle, String> colDescription;
    @FXML private TableColumn<Salle, String> colAdresse;
    @FXML private TableColumn<Salle, String> colVille;
    @FXML private TableColumn<Salle, Double> colPrixParHeure;
    @FXML private TableColumn<Salle, String> colEquipements;
    @FXML private TableColumn<Salle, LocalDateTime> colCreatedAt;
    @FXML private TableColumn<Salle, LocalDateTime> colUpdatedAt;
    @FXML private TableColumn<Salle, Boolean> colEstDisponible;
    @FXML private TextField searchField;

    private ObservableList<Salle> salleList = FXCollections.observableArrayList();
    private SalleDAO salleDAO;
    private Connection connection;

    @FXML
    public void initialize() {
        // Initialiser la connexion à la base de données
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/projet",
                    "root",
                    "");
            salleDAO = new SalleDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de connexion", "Impossible de se connecter à la base de données", Alert.AlertType.ERROR);
            return;
        }

        // Configurer les colonnes
        configureTableColumns();

        // Charger les données depuis la base de données
        refreshTable();

        // Configurer la recherche
        setupSearch();
    }

    private void configureTableColumns() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom_salle"));
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type_salle"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colPrixParHeure.setCellValueFactory(new PropertyValueFactory<>("prix_par_heure"));
        colEquipements.setCellValueFactory(new PropertyValueFactory<>("equipements"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updated_at"));
        colEstDisponible.setCellValueFactory(new PropertyValueFactory<>("est_disponible"));
    }

    private void refreshTable() {
        salleList.clear();
        List<Salle> salles = salleDAO.getAllSalles();
        if (salles != null) {
            salleList.addAll(salles);
        }
        tableSalles.setItems(salleList);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                refreshTable();
            } else {
                salleList.clear();
                List<Salle> searchResults = salleDAO.searchSalles(newValue);
                if (searchResults != null) {
                    salleList.addAll(searchResults);
                }
                tableSalles.setItems(salleList);
            }
        });
    }

    @FXML
    private void ajouterSalle() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajouterSalle.fxml"));
            Parent root = loader.load();

            AjouterSalleController controller = loader.getController();
            controller.setSalleController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter une nouvelle salle");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modifierSalle() {
        Salle selectedSalle = tableSalles.getSelectionModel().getSelectedItem();

        if (selectedSalle == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une salle à modifier", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifierSalle.fxml"));
            Parent root = loader.load();

            ModifierSalleController controller = loader.getController();
            controller.setSalleController(this);
            controller.setSalleToEdit(selectedSalle);

            Stage stage = new Stage();
            stage.setTitle("Modifier la salle");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void exporterExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les données");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        fileChooser.setInitialFileName("export_salles.csv");

        File file = fileChooser.showSaveDialog(tableSalles.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // En-tête CSV
                writer.println("Nom,Capacité,Type,Disponible,Description,Adresse,Ville,Prix par heure,Équipements,Créé à,Mis à jour,Est Disponible");

                // Données
                for (Salle salle : tableSalles.getItems()) {
                    writer.println(String.format("\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\",\"%s\",\"%s\"",
                            salle.getNom_salle(),
                            salle.getCapacite(),
                            salle.getType_salle(),
                            salle.isDisponible() ? "Oui" : "Non",
                            salle.getDescription(),
                            salle.getAdresse(),
                            salle.getVille(),
                            salle.getPrix_par_heure(),
                            salle.getEquipements(),
                            salle.getCreated_at().toString(),
                            salle.getUpdated_at().toString(),
                            salle.isEst_disponible() ? "Oui" : "Non"
                    ));
                }

                showAlert("Export réussi", "Les données ont été exportées avec succès", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Erreur d'export", "Une erreur est survenue lors de l'export", Alert.AlertType.ERROR);
            }
        }
    }
    @FXML
    private void supprimerSalle() {
        Salle selectedSalle = tableSalles.getSelectionModel().getSelectedItem();

        if (selectedSalle == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner une salle à supprimer", Alert.AlertType.WARNING);
            return;
        }



        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la salle");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer la salle " + selectedSalle.getNom_salle() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (salleDAO.deleteSalle(selectedSalle.getId_salle())) {
                refreshTable();
                showAlert("Succès", "Salle supprimée avec succès", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Échec de la suppression de la salle", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void viewuser() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("user.fxml"));
            Stage stage = (Stage) tableSalles.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSalle(Salle newSalle) {
        if (salleDAO.addSalle(newSalle)) {
            refreshTable();
            showAlert("Succès", "Salle ajoutée avec succès", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Échec de l'ajout de la salle", Alert.AlertType.ERROR);
        }
    }

    public void updateSalle(Salle updatedSalle) {
        if (salleDAO.updateSalle(updatedSalle)) {
            refreshTable();
            showAlert("Succès", "Salle mise à jour avec succès", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Échec de la mise à jour de la salle", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}