package com.example.eventmatch_pr.entity;

import com.example.eventmatch_pr.DB.DB;
import com.example.eventmatch_pr.ReservationSalleService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalleService {

    public List<ReservationSalleService.Salle> getAllSalles() {
        List<ReservationSalleService.Salle> salles = new ArrayList<>();
        String query = "SELECT * FROM salle";

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                salles.add(new ReservationSalleService.Salle(
                        rs.getString("nom_salle"),
                        rs.getInt("capacite"),
                        rs.getString("type_salle"),
                        rs.getBoolean("disponible"),
                        rs.getString("description"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getString("adresse"),
                        rs.getString("ville"),
                        rs.getDouble("prix_par_heure"),
                        rs.getString("equipements"),
                        rs.getBoolean("est_disponible")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salles;
    }


    public void addSalle(ReservationSalleService.Salle salle) {
        String sql = "INSERT INTO salle(nom_salle, capacite, type_salle, disponible, description, created_at, updated_at, adresse, ville, prix_par_heure, equipements, est_disponible) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, salle.getNom_salle());
            pstmt.setInt(2, salle.getCapacite());
            pstmt.setString(3, salle.getType_salle());
            pstmt.setBoolean(4, salle.isDisponible());
            pstmt.setString(5, salle.getDescription());
            pstmt.setObject(6, salle.getCreated_at());
            pstmt.setObject(7, salle.getUpdated_at());
            pstmt.setString(8, salle.getAdresse());
            pstmt.setString(9, salle.getVille());
            pstmt.setDouble(10, salle.getPrix_par_heure());
            pstmt.setString(11, salle.getEquipements());
            pstmt.setBoolean(12, salle.isEst_disponible());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateSalle(ReservationSalleService.Salle salle) {

            // Mise à jour d'une salle
            String query = "UPDATE salle SET nom_salle = ?, capacite = ?, type_salle = ?, disponible = ?, description = ?, updated_at = ?, adresse = ?, ville = ?, prix_par_heure = ?, equipements = ?, est_disponible = ? WHERE id_salle = ?";

                try(
            Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query))

            {

                stmt.setString(1, salle.getNom_salle());
                stmt.setInt(2, salle.getCapacite());
                stmt.setString(3, salle.getType_salle());
                stmt.setBoolean(4, salle.isDisponible());
                stmt.setString(5, salle.getDescription());
                stmt.setObject(6, salle.getUpdated_at());
                stmt.setString(7, salle.getAdresse());
                stmt.setString(8, salle.getVille());
                stmt.setDouble(9, salle.getPrix_par_heure());
                stmt.setString(10, salle.getEquipements());
                stmt.setBoolean(11, salle.isDisponible());
                stmt.setInt(12, salle.getId_salle()); // ID de la salle à mettre à jour

                stmt.executeUpdate();
            } catch(
            SQLException e)

            {
                e.printStackTrace();
            }



    }



}