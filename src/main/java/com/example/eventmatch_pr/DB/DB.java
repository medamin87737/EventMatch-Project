package com.example.eventmatch_pr.DB;


import javafx.scene.control.TableColumn;

import java.sql.*;

public class DB {
    // Database credentials and URL (change these to match your database configuration)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/projet";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public ResultSet executeUpdate;
    private Connection conn;
    public ResultSet resultSet;
    private PreparedStatement ptsm;
// Si vous utilisez ce champ

    public void close() {
        try {
            if (ptsm != null) ptsm.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ResultSet executeQuery() throws SQLException {
        if (ptsm != null) {
            return ptsm.executeQuery();
        }
        throw new SQLException("PreparedStatement not initialized");
    }

    public int executeUpdate() throws SQLException {
        if (ptsm != null) {
            return ptsm.executeUpdate();
        }
        throw new SQLException("PreparedStatement not initialized");
    }



    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        try {
            // Load and register the JDBC driver (optional in newer versions of Java)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found");
        }
    }

    // Example of executing a SELECT query
    public static void getAll() {
        String query = "SELECT * FROM medecin";  // Adjust with actual table name
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");  // Replace with actual column names
                String nom = rs.getString("nom");  // Replace with actual column names
                System.out.println("le nom est ID: " + id + ", nom: " + nom);
                String prenom = rs.getString("prenom");
                System.out.println("le prenom est prenom: " + prenom);
                String tel = rs.getString("tel");
                System.out.println("le tel est tel: " + tel);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Example of executing an INSERT query
    public static void InsertRv(int id, String date) {
        String query = "INSERT INTO rendezvous (id, date) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, date);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " row(s) into rendezvous table.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Example of executing an UPDATE query
    public static void updateRendezvousDate(int id, String newDate) {
        String query = "UPDATE rendezvous SET date = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newDate);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Updated " + rowsAffected + " row(s) in rendezvous table.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Example of executing a DELETE query
    public static void deleteRendezvous(int id) {
        String query = "DELETE FROM rendezvous WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Deleted " + rowsAffected + " row(s) from rendezvous table.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Closing resources (use try-with-resources for automatic closing)
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement initPrepar(String sql) {
        try {
            Connection conn = getConnection();
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement getPtsm(String sql) {
        try {
            return initPrepar(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Connection connection;  // Votre connexion à la DB
    private String sql;            // Votre requête SQL


    public void closeResources() {
    }
}

