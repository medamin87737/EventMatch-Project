package com.example.eventmatch_pr;

import com.example.eventmatch_pr.entity.UserSession;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.example.eventmatch_pr.DB.DB;
public class spinController {

    DB db = new DB();


    @FXML private Group wheel;
    @FXML private Label resultLabel;
    @FXML private VBox successBox;
    @FXML private Label promoCodeLabel;
    @FXML private Label promoDetailsLabel;

    private List<Promo> validPromoCodes = new ArrayList<>();
    private Random random = new Random();
    private int connectedUserId;

    public void initialize() {
        connectedUserId = UserSession.getInstance().getUserId();
        loadValidPromoCodes();
        createWheel();
        checkEligibility();
    }

    private void loadValidPromoCodes() {
        try (Connection connection = db.getConnection()) {
            LocalDate today = LocalDate.now();

            String query = "SELECT * FROM code_promo WHERE date_debut <= ? AND date_fin >= ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(today));
            statement.setDate(2, Date.valueOf(today));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Promo code = new Promo(
                        resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("nom_produit"),
                        resultSet.getInt("reduction"),
                        resultSet.getDate("date_debut").toLocalDate(),
                        resultSet.getDate("date_fin").toLocalDate()
                );
                validPromoCodes.add(code);
            }

            if (!validPromoCodes.isEmpty()) {
                validPromoCodes.add(new Promo(0, "", "Pas de chance", 0, today, today));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les codes promo", Alert.AlertType.ERROR);
        }
    }

    private void createWheel() {
        wheel.getChildren().clear();

        if (validPromoCodes.isEmpty()) {
            resultLabel.setText("Aucun code promo disponible pour le moment.");
            return;
        }

        Collections.shuffle(validPromoCodes);

        int segmentCount = validPromoCodes.size();
        double anglePerSegment = 360.0 / segmentCount;
        double radius = 180;

        for (int i = 0; i < segmentCount; i++) {
            Promo code = validPromoCodes.get(i);

            Arc segment = new Arc();
            segment.setCenterX(0);
            segment.setCenterY(0);
            segment.setRadiusX(radius);
            segment.setRadiusY(radius);
            segment.setStartAngle(i * anglePerSegment);
            segment.setLength(anglePerSegment);
            segment.setType(ArcType.ROUND);
            segment.setFill(getSegmentColor(i));

            Text text = new Text(code.getNomProduit());
            text.setFill(javafx.scene.paint.Color.WHITE);
            text.setFont(javafx.scene.text.Font.font("Arial", 12));

            double textAngle = Math.toRadians(i * anglePerSegment + anglePerSegment / 2);
            double textRadius = radius * 0.6;
            text.setX(textRadius * Math.cos(textAngle) - text.getLayoutBounds().getWidth() / 2);
            text.setY(textRadius * Math.sin(textAngle) + text.getLayoutBounds().getHeight() / 4);
            text.setRotate(i * anglePerSegment + anglePerSegment / 2 + 90);

            wheel.getChildren().addAll(segment, text);
        }
    }

    public void back(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user-home.fxml"));
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
    private void handleSpin() {
        if (validPromoCodes.isEmpty()) {
            showAlert("Information", "Aucun code promo disponible pour le moment.", Alert.AlertType.INFORMATION);
            return;
        }

        if (!canUserPlayToday()) {
            showAlert("Information", "Vous avez déjà joué aujourd'hui. Revenez demain!", Alert.AlertType.INFORMATION);
            return;
        }

        Node spinButton = resultLabel.getScene().lookup(".spin-button");

        if (spinButton != null) {
            spinButton.setDisable(true);
        }

        int segmentCount = validPromoCodes.size();
        double anglePerSegment = 360.0 / segmentCount;
        int winningSegment = random.nextInt(segmentCount);
        Promo winningCode = validPromoCodes.get(winningSegment);

        double targetAngle = 360 * 5 + (360 - (winningSegment * anglePerSegment + anglePerSegment / 2));

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(6), wheel);
        rotateTransition.setByAngle(targetAngle);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(false);

        rotateTransition.setOnFinished(event -> {
            if (spinButton != null) {
                spinButton.setDisable(false);
            }
            handleWinningCode(winningCode);
        });

        rotateTransition.play();
    }

    private void handleWinningCode(Promo code) {
        if (code.getId() == 0) {
            resultLabel.setText("Désolé, vous n'avez pas gagné cette fois. Réessayez plus tard!");
            recordSpinResult(null);
        } else {
            promoCodeLabel.setText("Code: " + code.getCode());
            promoDetailsLabel.setText(String.format("%d%% de réduction sur %s (valable jusqu'au %s)",
                    code.getReduction(), code.getNomProduit(), code.getDateFin()));
            successBox.setVisible(true);
            recordSpinResult(code);
        }
    }

    private void recordSpinResult(Promo code) {
        try (Connection connection = db.getConnection()) {
            // Mettre à jour la date du dernier spin
            String updateUserQuery = "UPDATE user SET date_dernier_spin = ? WHERE id = ?";
            PreparedStatement updateUserStmt = connection.prepareStatement(updateUserQuery);
            updateUserStmt.setDate(1, Date.valueOf(LocalDate.now()));
            updateUserStmt.setInt(2, connectedUserId);
            updateUserStmt.executeUpdate();

            // Si un code a été gagné, l'enregistrer
            if (code != null) {
                String insertPromoQuery = "UPDATE user SET code_promo_id = ?, date_utilisation = ? WHERE id = ?";
                PreparedStatement insertPromoStmt = connection.prepareStatement(insertPromoQuery);
                insertPromoStmt.setInt(1, code.getId());
                insertPromoStmt.setDate(2, Date.valueOf(LocalDate.now()));
                insertPromoStmt.setInt(3, connectedUserId);
                insertPromoStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'enregistrer le résultat", Alert.AlertType.ERROR);
        }
    }

    private boolean canUserPlayToday() {
        try (Connection connection = db.getConnection()) {
            String query = "SELECT date_dernier_spin FROM user WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, connectedUserId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Date lastSpinDate = resultSet.getDate("date_dernier_spin");
                if (lastSpinDate != null) {
                    LocalDate lastSpin = lastSpinDate.toLocalDate();
                    showAlert("Information", "Vous avez déjà joué aujourd'hui. Revenez demain!", Alert.AlertType.INFORMATION);

                    return lastSpin.isBefore(LocalDate.now());


                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    private void checkEligibility() {
        if (!canUserPlayToday()) {
            showAlert("Information", "Vous avez déjà joué aujourd'hui. Revenez demain!", Alert.AlertType.INFORMATION);

            resultLabel.setText("Vous avez déjà joué aujourd'hui. Revenez demain!");
            Node spinButton = resultLabel.getScene().lookup(".spin-button");
            if (spinButton != null) {
                spinButton.setDisable(true);
            }
        }
    }

    @FXML
    private void closeSuccessBox() {
        successBox.setVisible(false);
    }

    private javafx.scene.paint.Color getSegmentColor(int index) {
        javafx.scene.paint.Color[] colors = {
                javafx.scene.paint.Color.rgb(231, 76, 60),
                javafx.scene.paint.Color.rgb(241, 196, 15),
                javafx.scene.paint.Color.rgb(46, 204, 113),
                javafx.scene.paint.Color.rgb(52, 152, 219),
                javafx.scene.paint.Color.rgb(155, 89, 182),
                javafx.scene.paint.Color.rgb(26, 188, 156)
        };
        return colors[index % colors.length];
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void viewuser(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user-home.fxml"));

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


}