package com.example.eventmatch_pr;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO {
    private Connection connection;

    public SalleDAO(Connection connection) {
        this.connection = connection;
    }

    // Ajouter une salle
    public boolean addSalle(Salle salle) {
        String sql = "INSERT INTO salle (nom_salle, capacite, type_salle, disponible, description, " +
                "adresse, ville, prix_par_heure, equipements, est_disponible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, salle.getNom_salle());
            statement.setInt(2, salle.getCapacite());
            statement.setString(3, salle.getType_salle());
            statement.setBoolean(4, salle.isDisponible());
            statement.setString(5, salle.getDescription());
            statement.setString(6, salle.getAdresse());
            statement.setString(7, salle.getVille());
            statement.setDouble(8, salle.getPrix_par_heure());
            statement.setString(9, salle.getEquipements());
            statement.setBoolean(10, salle.isEst_disponible());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    salle.setId_salle(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mettre à jour une salle
    public boolean updateSalle(Salle salle) {
        String sql = "UPDATE salle SET nom_salle = ?, capacite = ?, type_salle = ?, " +
                "disponible = ?, description = ?, adresse = ?, ville = ?, " +
                "prix_par_heure = ?, equipements = ?, est_disponible = ? " +
                "WHERE id_salle = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, salle.getNom_salle());
            statement.setInt(2, salle.getCapacite());
            statement.setString(3, salle.getType_salle());
            statement.setBoolean(4, salle.isDisponible());
            statement.setString(5, salle.getDescription());
            statement.setString(6, salle.getAdresse());
            statement.setString(7, salle.getVille());
            statement.setDouble(8, salle.getPrix_par_heure());
            statement.setString(9, salle.getEquipements());
            statement.setBoolean(10, salle.isEst_disponible());
            statement.setInt(11, salle.getId_salle());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer une salle
    public boolean deleteSalle(int id) {
        String sql = "DELETE FROM salle WHERE id_salle = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtenir toutes les salles
    public List<Salle> getAllSalles() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Salle salle = new Salle();
                salle.setId_salle(resultSet.getInt("id_salle"));
                salle.setNom_salle(resultSet.getString("nom_salle"));
                salle.setCapacite(resultSet.getInt("capacite"));
                salle.setType_salle(resultSet.getString("type_salle"));
                salle.setDisponible(resultSet.getBoolean("disponible"));
                salle.setDescription(resultSet.getString("description"));
                salle.setCreated_at(resultSet.getTimestamp("created_at").toLocalDateTime());
                salle.setUpdated_at(resultSet.getTimestamp("updated_at").toLocalDateTime());
                salle.setAdresse(resultSet.getString("adresse"));
                salle.setVille(resultSet.getString("ville"));
                salle.setPrix_par_heure(resultSet.getDouble("prix_par_heure"));
                salle.setEquipements(resultSet.getString("equipements"));
                salle.setEst_disponible(resultSet.getBoolean("est_disponible"));

                salles.add(salle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }

    // Rechercher des salles
    public List<Salle> searchSalles(String keyword) {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle WHERE nom_salle LIKE ? OR type_salle LIKE ? " +
                "OR ville LIKE ? OR adresse LIKE ? OR description LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String likeKeyword = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) {
                statement.setString(i, likeKeyword);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Salle salle = new Salle();
                salle.setId_salle(resultSet.getInt("id_salle"));
                salle.setNom_salle(resultSet.getString("nom_salle"));
                salle.setCapacite(resultSet.getInt("capacite"));
                salle.setType_salle(resultSet.getString("type_salle"));
                salle.setDisponible(resultSet.getBoolean("disponible"));
                salle.setDescription(resultSet.getString("description"));
                salle.setCreated_at(resultSet.getTimestamp("created_at").toLocalDateTime());
                salle.setUpdated_at(resultSet.getTimestamp("updated_at").toLocalDateTime());
                salle.setAdresse(resultSet.getString("adresse"));
                salle.setVille(resultSet.getString("ville"));
                salle.setPrix_par_heure(resultSet.getDouble("prix_par_heure"));
                salle.setEquipements(resultSet.getString("equipements"));
                salle.setEst_disponible(resultSet.getBoolean("est_disponible"));

                salles.add(salle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }
}