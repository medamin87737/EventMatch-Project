package com.example.eventmatch_pr;
import com.example.eventmatch_pr.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.eventmatch_pr.*;
import com.example.eventmatch_pr.DB.DB;

public class ActiviteService {
    private static final String TABLE_NAME = "activite";
    private static final String[] COLUMNS = {
            "id", "nom", "description", "date", "heure_debut", "heure_fin", 
            "lieu", "responsable", "type", "statut", "places"
    };

    public List<Activite> findAll() throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String sql = String.format("SELECT %s FROM %s ORDER BY date ASC, heure_debut ASC",
                String.join(", ", COLUMNS), TABLE_NAME);

        Connection conn = null;
        try {
            conn = DB.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    activites.add(createActiviteFromResultSet(rs));
                }
            }
        } finally {

        }
        return activites;
    }

    public Optional<Activite> findById(int id) throws SQLException {
        String sql = String.format("SELECT %s FROM %s WHERE id = ?",
                String.join(", ", COLUMNS), TABLE_NAME);

        Connection conn = null;
        try {
            conn = DB.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(createActiviteFromResultSet(rs));
                    }
                }
            }
        } finally {
        }
        return Optional.empty();
    }

    public List<Activite> findByDate(LocalDate date) throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String sql = String.format("SELECT %s FROM %s WHERE date = ? ORDER BY heure_debut ASC",
                String.join(", ", COLUMNS), TABLE_NAME);

        Connection conn = null;
        try {
            conn = DB.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, Date.valueOf(date));
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        activites.add(createActiviteFromResultSet(rs));
                    }
                }
            }
        } finally {
        }
        return activites;
    }

    public List<Activite> findByType(String type) throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String sql = String.format("SELECT %s FROM %s WHERE type = ? ORDER BY date ASC, heure_debut ASC",
                String.join(", ", COLUMNS), TABLE_NAME);

        Connection conn = null;
        try {
            conn = DB.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, type);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        activites.add(createActiviteFromResultSet(rs));
                    }
                }
            }
        } finally {
        }
        return activites;
    }

    public List<Activite> findByStatut(String statut) throws SQLException {
        List<Activite> activites = new ArrayList<>();
        String sql = String.format("SELECT %s FROM %s WHERE statut = ? ORDER BY date ASC, heure_debut ASC",
                String.join(", ", COLUMNS), TABLE_NAME);

        Connection conn = null;
        try {
            conn = DB.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, statut);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        activites.add(createActiviteFromResultSet(rs));
                    }
                }
            }
        } finally {
        }
        return activites;
    }

    public Activite create(Activite activite) throws SQLException {
        validateActivite(activite);
        String sql = String.format(
                "INSERT INTO %s (nom, description, date, heure_debut, heure_fin, lieu, responsable, type, statut, places) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                TABLE_NAME
        );

        Connection conn = null;
        try {
            conn = DB.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, activite.getNom());
                pstmt.setString(2, activite.getDescription());
                pstmt.setDate(3, Date.valueOf(activite.getDate()));
                pstmt.setTime(4, Time.valueOf(activite.getHeureDebut()));
                pstmt.setTime(5, Time.valueOf(activite.getHeureFin()));
                pstmt.setString(6, activite.getLieu());
                pstmt.setString(7, activite.getResponsable());
                pstmt.setString(8, activite.getType());
                pstmt.setString(9, activite.getStatut());
                pstmt.setInt(10, activite.getPlaces());
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        activite.setId(rs.getInt(1));
                    }
                }
            }
        } finally {
        }
        return activite;
    }

    public void update(Activite activite) throws SQLException {
        validateActivite(activite);
        String sql = String.format(
                "UPDATE %s SET nom=?, description=?, date=?, heure_debut=?, heure_fin=?, " +
                "lieu=?, responsable=?, type=?, statut=?, places=? WHERE id=?",
                TABLE_NAME
        );

        Connection conn = null;
        try {
            conn = DB.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                setActiviteParameters(pstmt, activite);
                pstmt.setInt(11, activite.getId());
                pstmt.executeUpdate();
            }
        } finally {
        }
    }

    public void delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE id=?", TABLE_NAME);

        Connection conn = null;
        try {
            conn = DB.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } finally {
        }
    }

    private Activite createActiviteFromResultSet(ResultSet rs) throws SQLException {
        Activite activite = new Activite();
        activite.setId(rs.getInt("id"));
        activite.setNom(rs.getString("nom"));
        activite.setDescription(rs.getString("description"));
        Date sqlDate = rs.getDate("date");
        if (sqlDate != null) {
            activite.setDate(sqlDate.toLocalDate());
        } else {
            activite.setDate(null);
        }
        Time sqlHeureDebut = rs.getTime("heure_debut");
        if (sqlHeureDebut != null) {
            activite.setHeureDebut(sqlHeureDebut.toLocalTime());
        } else {
            activite.setHeureDebut(null);
        }
        Time sqlHeureFin = rs.getTime("heure_fin");
        if (sqlHeureFin != null) {
            activite.setHeureFin(sqlHeureFin.toLocalTime());
        } else {
            activite.setHeureFin(null);
        }
        activite.setLieu(rs.getString("lieu"));
        activite.setResponsable(rs.getString("responsable"));
        activite.setType(rs.getString("type"));
        activite.setStatut(rs.getString("statut"));
        activite.setPlaces(rs.getInt("places"));
        return activite;
    }

    private void setActiviteParameters(PreparedStatement pstmt, Activite activite) throws SQLException {
        pstmt.setString(1, activite.getNom());
        pstmt.setString(2, activite.getDescription());
        pstmt.setDate(3, Date.valueOf(activite.getDate()));
        pstmt.setTime(4, Time.valueOf(activite.getHeureDebut()));
        pstmt.setTime(5, Time.valueOf(activite.getHeureFin()));
        pstmt.setString(6, activite.getLieu());
        pstmt.setString(7, activite.getResponsable());
        pstmt.setString(8, activite.getType());
        pstmt.setString(9, activite.getStatut());
        pstmt.setInt(10, activite.getPlaces());
    }

    private void validateActivite(Activite activite) throws SQLException {
        if (!activite.isValide()) {
            throw new SQLException("L'activité n'est pas valide");
        }
    }
}