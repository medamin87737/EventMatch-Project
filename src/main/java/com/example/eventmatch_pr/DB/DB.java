package com.example.eventmatch_pr.DB;

import java.sql.*;

public class DB {



        public Connection conn;
        public PreparedStatement pstmt;

        public DB() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation", "root", ""); // ajuste l'URL/MDP si nécessaire
            } catch (Exception e) {
                System.out.println("Erreur de connexion à la base");
                e.printStackTrace();
            }
        }

        public void initPrepar(String sql) throws SQLException {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        }

        public PreparedStatement getPtsm(String sql) {
            return pstmt;
        }
    }


