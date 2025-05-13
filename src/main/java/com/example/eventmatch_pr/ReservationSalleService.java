package com.example.eventmatch_pr;

import com.example.eventmatch_pr.DB.DB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationSalleService {
    DB bd = new DB();
    public List<ActiviteFormController.ReservationSalle> getAllReservationsa() {
        List<ActiviteFormController.ReservationSalle> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservationsalle";

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                reservations.add(new ActiviteFormController.ReservationSalle(
                        rs.getInt("id_reservation"),
                        rs.getInt("id_salle"),
                        rs.getInt("id_utilisateur"),
                        rs.getObject("date_debut", LocalDateTime.class),
                        rs.getObject("date_fin", LocalDateTime.class),
                        rs.getString("statut"),
                        rs.getString("motif"),
                        rs.getString("statut_paiement"),
                        rs.getObject("date_reservation", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class),
                        rs.getInt("nombre_participants"),
                        rs.getString("statut_reservation"),
                        rs.getString("commentaires")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public void addReservationa(ActiviteFormController.ReservationSalle reservation) {
        String sql = "INSERT INTO reservationsalle(id_salle, id_utilisateur, date_debut, date_fin, statut, motif, statut_paiement, date_reservation, updated_at, nombre_participants, statut_reservation, commentaires) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservation.getId_salle());
            pstmt.setInt(2, reservation.getId_utilisateur());
            pstmt.setObject(3, reservation.getDate_debut());
            pstmt.setObject(4, reservation.getDate_fin());
            pstmt.setString(5, reservation.getStatut());
            pstmt.setString(6, reservation.getMotif());
            pstmt.setString(7, reservation.getStatut_paiement());
            pstmt.setObject(8, reservation.getDate_reservation());
            pstmt.setObject(9, reservation.getUpdated_at());
            pstmt.setInt(10, reservation.getNombre_participants());
            pstmt.setString(11, reservation.getStatut_reservation());
            pstmt.setString(12, reservation.getCommentaires());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReservationa(ActiviteFormController.ReservationSalle reservation) {
        String sql = "UPDATE reservationsalle SET id_salle=?, id_utilisateur=?, date_debut=?, date_fin=?, statut=?, motif=?, statut_paiement=?, updated_at=?, nombre_participants=?, statut_reservation=?, commentaires=? WHERE id_reservation=?";
        try (Connection conn = DB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservation.getId_salle());
            pstmt.setInt(2, reservation.getId_utilisateur());
            pstmt.setObject(3, reservation.getDate_debut());
            pstmt.setObject(4, reservation.getDate_fin());
            pstmt.setString(5, reservation.getStatut());
            pstmt.setString(6, reservation.getMotif());
            pstmt.setString(7, reservation.getStatut_paiement());
            pstmt.setObject(8, reservation.getUpdated_at());
            pstmt.setInt(9, reservation.getNombre_participants());
            pstmt.setString(10, reservation.getStatut_reservation());
            pstmt.setString(11, reservation.getCommentaires());
            pstmt.setInt(12, reservation.getId_reservation());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReservationa(int id_reservation) {
        String sql = "DELETE FROM reservationsalle WHERE id_reservation = ?";
        try (Connection conn = DB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_reservation);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Salle {
        private int id_salle;
        private String nom_salle;
        private int capacite;
        private String type_salle;
        private boolean disponible;
        private String description;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
        private String adresse;
        private String ville;
        private double prix_par_heure;
        private String equipements;
        private boolean est_disponible;
        public Salle() {

        }
        public Salle( int id_salle, String nom_salle, int capacite, String type_salle,
                      boolean disponible, String description, LocalDateTime created_at,
                      LocalDateTime updated_at, String adresse, String ville,
                      double prix_par_heure, String equipements, boolean est_disponible) {
            this.id_salle = id_salle;
            this.nom_salle = nom_salle;
            this.capacite = capacite;
            this.type_salle = type_salle;
            this.disponible = disponible;
            this.description = description;
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.adresse = adresse;
            this.ville = ville;
            this.prix_par_heure = prix_par_heure;
            this.equipements = equipements;
            this.est_disponible = est_disponible;
        }



        public Salle(String text, int capacite, String text1, boolean selected, String text2, LocalDateTime now, LocalDateTime now1, String text3, String text4, double prixParHeure, String text5, boolean selected1) {
            this.nom_salle = text;
            this.capacite = capacite;
            this.type_salle = text1;
            this.disponible = selected;
            this.description = text2;
            this.created_at = now;
            this.updated_at = now1;
            this.adresse = text3;
            this.ville = text4;
            this.prix_par_heure = prixParHeure;
            this.equipements = text5;
            this.est_disponible = selected1;



        }

        // Getters & Setters ici (générés via IDE ou manuellement)

        // Getters & Setters
        public int getId_salle() { return id_salle; }
        public void setId_salle(int id_salle) { this.id_salle = id_salle; }

        public String getNom_salle() { return nom_salle; }
        public void setNom_salle(String nom_salle) { this.nom_salle = nom_salle; }

        public int getCapacite() { return capacite; }
        public void setCapacite(int capacite) { this.capacite = capacite; }

        public String getType_salle() { return type_salle; }
        public void setType_salle(String type_salle) { this.type_salle = type_salle; }

        public boolean isDisponible() { return disponible; }
        public void setDisponible(boolean disponible) { this.disponible = disponible; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public LocalDateTime getCreated_at() { return created_at; }
        public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

        public LocalDateTime getUpdated_at() { return updated_at; }
        public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }

        public String getAdresse() { return adresse; }
        public void setAdresse(String adresse) { this.adresse = adresse; }

        public String getVille() { return ville; }
        public void setVille(String ville) { this.ville = ville; }

        public double getPrix_par_heure() { return prix_par_heure; }
        public void setPrix_par_heure(double prix_par_heure) { this.prix_par_heure = prix_par_heure; }

        public String getEquipements() { return equipements; }
        public void setEquipements(String equipements) { this.equipements = equipements; }

        public boolean isEst_disponible() { return est_disponible; }
        public void setEst_disponible(boolean est_disponible) { this.est_disponible = est_disponible; }
    }





        public List<ActiviteFormController.ReservationSalle> getAllReservations() {
            List<ActiviteFormController.ReservationSalle> reservations = new ArrayList<>();
            String query = "SELECT * FROM reservationsalle";

            try (Connection conn = DB.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    reservations.add(extractReservationFromResultSet(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return reservations;
        }

        public List<ActiviteFormController.ReservationSalle> searchReservations(String keyword) {
            List<ActiviteFormController.ReservationSalle> results = new ArrayList<>();
            String query = "SELECT * FROM reservationsalle WHERE " +
                    "id_reservation LIKE ? OR " +
                    "id_salle LIKE ? OR " +
                    "id_utilisateur LIKE ? OR " +
                    "statut LIKE ? OR " +
                    "motif LIKE ?";

            try (Connection conn = DB.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                String searchPattern = "%" + keyword + "%";
                for (int i = 1; i <= 5; i++) {
                    pstmt.setString(i, searchPattern);
                }

                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    results.add(extractReservationFromResultSet(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return results;
        }

        public void addReservation(ActiviteFormController.ReservationSalle reservation) {
            String sql = "INSERT INTO reservationsalle(id_salle, id_utilisateur, date_debut, date_fin, statut, motif, " +
                    "statut_paiement, date_reservation, updated_at, nombre_participants, statut_reservation, commentaires) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DB.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                setReservationParameters(pstmt, reservation);
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservation.setId_reservation(generatedKeys.getInt(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void updateReservation(ActiviteFormController.ReservationSalle reservation) {
            String sql = "UPDATE reservationsalle SET id_salle=?, id_utilisateur=?, date_debut=?, date_fin=?, statut=?, " +
                    "motif=?, statut_paiement=?, updated_at=?, nombre_participants=?, statut_reservation=?, commentaires=? " +
                    "WHERE id_reservation=?";

            try (Connection conn = DB.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                setReservationParameters(pstmt, reservation);
                pstmt.setInt(12, reservation.getId_reservation());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteReservation(int id_reservation) {
            String sql = "DELETE FROM reservationsalle WHERE id_reservation = ?";
            try (Connection conn = DB.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id_reservation);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private ActiviteFormController.ReservationSalle extractReservationFromResultSet(ResultSet rs) throws SQLException {
            return new ActiviteFormController.ReservationSalle(
                    rs.getInt("id_reservation"),
                    rs.getInt("id_salle"),
                    rs.getInt("id_utilisateur"),
                    rs.getObject("date_debut", LocalDateTime.class),
                    rs.getObject("date_fin", LocalDateTime.class),
                    rs.getString("statut"),
                    rs.getString("motif"),
                    rs.getString("statut_paiement"),
                    rs.getObject("date_reservation", LocalDateTime.class),
                    rs.getObject("updated_at", LocalDateTime.class),
                    rs.getInt("nombre_participants"),
                    rs.getString("statut_reservation"),
                    rs.getString("commentaires")
            );
        }

        private void setReservationParameters(PreparedStatement pstmt, ActiviteFormController.ReservationSalle reservation) throws SQLException {
            pstmt.setInt(1, reservation.getId_salle());
            pstmt.setInt(2, reservation.getId_utilisateur());
            pstmt.setObject(3, reservation.getDate_debut());
            pstmt.setObject(4, reservation.getDate_fin());
            pstmt.setString(5, reservation.getStatut());
            pstmt.setString(6, reservation.getMotif());
            pstmt.setString(7, reservation.getStatut_paiement());
            pstmt.setObject(8, LocalDateTime.now()); // date_reservation
            pstmt.setObject(9, LocalDateTime.now()); // updated_at
            pstmt.setInt(10, reservation.getNombre_participants());
            pstmt.setString(11, reservation.getStatut_reservation());
            pstmt.setString(12, reservation.getCommentaires());
        }

}