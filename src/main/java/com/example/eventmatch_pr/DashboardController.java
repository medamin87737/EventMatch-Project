package com.example.eventmatch_pr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.text.DecimalFormat;

public class DashboardController {
    @FXML private Label totalParticipantsLabel;
    @FXML private Label tauxParticipationLabel;
    @FXML private Label statusLabel;
    
    @FXML private TableView<ParticipationStat> statsTable;
    @FXML private TableColumn<ParticipationStat, String> typeActiviteColumn;
    @FXML private TableColumn<ParticipationStat, Integer> totalParticipantsColumn;
    @FXML private TableColumn<ParticipationStat, Integer> placesDisponiblesColumn;
    @FXML private TableColumn<ParticipationStat, String> tauxParticipationColumn;

    private final ObservableList<ParticipationStat> statsList = FXCollections.observableArrayList();
    private Connection connection;

    @FXML
    public void initialize() {
        try {
            // Connexion à la base de données
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/application",
                "root",
                ""
            );

            // Initialiser les colonnes
            setupTable();

            // Charger les données
            loadStats();

            // Mettre à jour le statut
            updateStatus("Données chargées avec succès");
        } catch (SQLException e) {
            showError("Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTable() {
        typeActiviteColumn.setCellValueFactory(new PropertyValueFactory<>("typeActivite"));
        totalParticipantsColumn.setCellValueFactory(new PropertyValueFactory<>("totalParticipants"));
        placesDisponiblesColumn.setCellValueFactory(new PropertyValueFactory<>("placesDisponibles"));
        tauxParticipationColumn.setCellValueFactory(new PropertyValueFactory<>("tauxParticipationFormatted"));
    }

    private void loadStats() {
        try {
            statsList.clear();
            
            // 1. Vérifier le contenu de la table activite
            System.out.println("\n=== Contenu de la table activite ===");
            String checkActiviteQuery = "SELECT id, type, places FROM activite";
            Statement activiteStatement = connection.createStatement();
            ResultSet activiteResult = activiteStatement.executeQuery(checkActiviteQuery);
            while (activiteResult.next()) {
                System.out.println("Activité ID: " + activiteResult.getInt("id") + 
                                 " | Type: " + activiteResult.getString("type") + 
                                 " | Places: " + activiteResult.getInt("places"));
            }

            // 2. Vérifier le contenu de la table participation_activite
            System.out.println("\n=== Contenu de la table participation_activite ===");
            String checkParticipationQuery = "SELECT * FROM participation_activite";
            Statement participationStatement = connection.createStatement();
            ResultSet participationResult = participationStatement.executeQuery(checkParticipationQuery);
            while (participationResult.next()) {
                System.out.println("Participation ID: " + participationResult.getInt("id_participation") + 
                                 " | Activité ID: " + participationResult.getInt("id_activite") + 
                                 " | Participant ID: " + participationResult.getInt("id_participant"));
            }

            // 3. Requête de test pour compter les participants par activité
            System.out.println("\n=== Test de comptage des participants par activité ===");
            String testQuery = "SELECT a.id, a.type, COUNT(pa.id_participant) as count " +
                             "FROM activite a " +
                             "LEFT JOIN participation_activite pa ON a.id = pa.id_activite " +
                             "GROUP BY a.id, a.type";
            Statement testStatement = connection.createStatement();
            ResultSet testResult = testStatement.executeQuery(testQuery);
            while (testResult.next()) {
                System.out.println("Activité ID: " + testResult.getInt("id") + 
                                 " | Type: " + testResult.getString("type") + 
                                 " | Nombre de participants: " + testResult.getInt("count"));
            }

            // 4. Requête principale modifiée
            String query = "SELECT " +
                "a.type as type_activite, " +
                "SUM(a.places) as places_disponibles, " +
                "COUNT(pa.id_participant) as total_participants " +
                "FROM activite a " +
                "LEFT JOIN participation_activite pa ON a.id = pa.id_activite " +
                "GROUP BY a.type " +
                "ORDER BY a.type";

            System.out.println("\n=== Exécution de la requête principale ===");
            System.out.println("Requête : " + query);
            
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int totalParticipants = 0;
            int totalPlaces = 0;
            int rowCount = 0;

            while (resultSet.next()) {
                rowCount++;
                String typeActivite = resultSet.getString("type_activite");
                int placesDisponibles = resultSet.getInt("places_disponibles");
                int participants = resultSet.getInt("total_participants");

                System.out.println("\nType d'activité : " + typeActivite + 
                                 "\nPlaces : " + placesDisponibles + 
                                 "\nParticipants : " + participants);

                totalParticipants += participants;
                totalPlaces += placesDisponibles;

                ParticipationStat stat = new ParticipationStat(
                    typeActivite,
                    participants,
                    placesDisponibles
                );
                statsList.add(stat);
            }

            System.out.println("\n=== Résumé final ===");
            System.out.println("Nombre total de types d'activités trouvés : " + rowCount);
            System.out.println("Total participants : " + totalParticipants);
            System.out.println("Total places : " + totalPlaces);

            // Mettre à jour les statistiques générales
            totalParticipantsLabel.setText(String.valueOf(totalParticipants));
            
            double tauxMoyen = totalPlaces > 0 ? 
                ((double) totalParticipants / totalPlaces) * 100 : 0;
            DecimalFormat df = new DecimalFormat("#.##");
            tauxParticipationLabel.setText(df.format(tauxMoyen) + "%");

            // Mettre à jour le tableau
            statsTable.setItems(statsList);

            if (rowCount == 0) {
                updateStatus("Aucune donnée trouvée");
            } else {
                updateStatus("Données chargées avec succès - " + rowCount + " types d'activités trouvés");
            }

        } catch (SQLException e) {
            showError("Erreur lors du chargement des statistiques: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
        System.out.println("Statut : " + message);
    }

    private void showError(String message) {
        statusLabel.setText("Erreur: " + message);
        System.out.println("Erreur : " + message);
    }

    // Classe interne pour représenter les statistiques de participation
    public static class ParticipationStat {
        private final String typeActivite;
        private final int totalParticipants;
        private final int placesDisponibles;

        public ParticipationStat(String typeActivite, int totalParticipants, int placesDisponibles) {
            this.typeActivite = typeActivite;
            this.totalParticipants = totalParticipants;
            this.placesDisponibles = placesDisponibles;
        }

        public String getTypeActivite() {
            return typeActivite;
        }

        public int getTotalParticipants() {
            return totalParticipants;
        }

        public int getPlacesDisponibles() {
            return placesDisponibles;
        }

        public String getTauxParticipationFormatted() {
            if (placesDisponibles == 0) return "0%";
            double taux = ((double) totalParticipants / placesDisponibles) * 100;
            return String.format("%.2f%%", taux);
        }
    }
}