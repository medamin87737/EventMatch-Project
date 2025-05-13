package com.example.eventmatch_pr;
import com.example.eventmatch_pr.DB.DB;
import com.example.eventmatch_pr.entity.*;
import com.example.eventmatch_pr.entity.Role;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.transformation.FilteredList;
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
public class HelloController {


    @FXML
    private TextField emailtxt;
    @FXML
    private PasswordField passwordtxt;

    private final IUser userDao = new impUser();
    public static String userparams;
    private static ActionEvent event;
    @FXML
    private TextField emailInscrit;


    @FXML
    private Label event1txt;

    @FXML
    private Label event2txt;

    @FXML
    private Label eventtxt;

    @FXML
    private Button loginBtn;


    @FXML
    private Label motivatriontxt;

    @FXML
    private TextField nameInscrit;

    @FXML
    private PasswordField passwordInscrit;


    @FXML
    private TextField phoneInscrit;

    @FXML
    private TextField prenomInscrit;

    @FXML
    private TextField roleInscrit;

    @FXML
    private Label signtxt;

    @FXML
    private AnchorPane slider;

    @FXML
    private Button signuptxt;

    @FXML
    private Hyperlink createtxt;

    @FXML
    private Button Exit;


    @FXML
    private IUser userdao = new impUser();
    User user = new User();


    @FXML
    private ComboBox<Role> comboRole; // Assurez-vous que le type correspond à l'énumération Role


    private FadeTransition createFade(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1), node);
        fade.setFromValue(1.0);
        fade.setToValue(0.3);
        fade.setCycleCount(Animation.INDEFINITE);
        fade.setAutoReverse(true);
        return fade;
    }

    private TranslateTransition createSlide(Node node) {
        TranslateTransition slide = new TranslateTransition(Duration.seconds(1.5), node);
        slide.setByX(10); // Décale vers la droite de 10px
        slide.setCycleCount(Animation.INDEFINITE);
        slide.setAutoReverse(true); // Revenir à la position d'origine
        return slide;
    }


    public class PasswordUtil {

        // Hacher le mot de passe
        public static String hashPassword(String plainPassword) {
            return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        }

        // Vérifier le mot de passe (lors de la connexion par exemple)
        public static boolean checkPassword(String plainPassword, String hashedPassword) {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        }
    }

    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) AS count FROM user WHERE LOWER(email) = LOWER(?)";

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, email.toLowerCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // On retourne true pour bloquer l'inscription en cas d'erreur
            return true;
        }
        return false;
    }



    @FXML
    private Label captchaText;
    @FXML
    private TextField captchaInput;
    private String currentCaptcha;


    private void generateCaptcha() {
        // Génère un CAPTCHA aléatoire de 5 caractères
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder captcha = new StringBuilder();
        Random rnd = new Random();
        while (captcha.length() < 5) {
            int index = (int) (rnd.nextFloat() * chars.length());
            captcha.append(chars.charAt(index));
        }
        currentCaptcha = captcha.toString();
        captchaText.setText(currentCaptcha);
    }

    @FXML
    void refreshCaptcha(ActionEvent event) {
        generateCaptcha();
    }

    @FXML
    void getInscrit(ActionEvent event) {
        // Récupération et nettoyage des données
        String name = nameInscrit.getText().trim();
        String prenom = prenomInscrit.getText().trim();
        String email = emailInscrit.getText().trim().toLowerCase();
        String password = passwordInscrit.getText().trim();
        Role role = comboRole.getValue();
        String phone = phoneInscrit.getText().trim();

        // Validation des champs
        if (name.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || role == null) {
            showNotification("Erreur", "Veuillez saisir tous les champs", NotificationType.ERROR);
            return;
        }
        if (!captchaInput.getText().trim().equalsIgnoreCase(currentCaptcha)) {
            showNotification("Erreur", "CAPTCHA incorrect", NotificationType.ERROR);
            generateCaptcha(); // Régénère un nouveau CAPTCHA
            captchaInput.clear();
            return;
        }


        // Validation de l'email
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
            showNotification("Erreur", "Veuillez entrer une adresse email valide", NotificationType.ERROR);
            return;
        }

        // Vérification de l'unicité de l'email
        if (emailExists(email)) {
            showNotification("Erreur", "Cet email est déjà utilisé", NotificationType.ERROR);
            return;
        }

        // Validation du mot de passe
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            showNotification("Erreur",
                    "Le mot de passe doit contenir:\n" +
                            "- 8 caractères minimum\n" +
                            "- 1 majuscule\n" +
                            "- 1 minuscule\n" +
                            "- 1 chiffre\n" +
                            "- 1 caractère spécial",
                    NotificationType.ERROR);
            return;
        }

        // Validation du numéro de téléphone
        if (!phone.matches("^\\d{8,15}$")) {
            showNotification("Erreur",
                    "Numéro de téléphone invalide\n" +
                            "Doit contenir 8 à 15 chiffres",
                    NotificationType.ERROR);
            return;
        }

        try {
            int phoneNumber = Integer.parseInt(phone);

            // Hachage du mot de passe avant de l'enregistrer
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Création et enregistrement de l'utilisateur
            User user = new User(name, prenom, email, hashedPassword, role, phoneNumber);
            userdao.addUser(user);

            // Génération du code promo
            Promo codePromo = generatePromoCode();
            Promo promo = new Promo(
                    codePromo.getCode(),
                    codePromo.getNomProduit(),
                    codePromo.getReduction(), // 10% de réduction
                    codePromo.getDateDebut(),
                    codePromo.getDateFin()// Valable 3 mois
            );

            // Envoi des notifications
            showPromoCodeAlert(promo.getCode(),promo.getNomProduit(), promo.getReduction(), promo.getDateFin());

            showNotification("Succès", "Inscription réussie!", NotificationType.SUCCESS);
            CloseSlider();
            generateCaptcha();
            captchaInput.clear();
        } catch (NumberFormatException e) {
            showNotification("Erreur", "Le numéro de téléphone doit être valide", NotificationType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("Erreur", "Une erreur inattendue est survenue", NotificationType.ERROR);
        }
    }
    private void showPromoCodeAlert(String promoCode, String nomProduit, int reduction, LocalDate validUntil) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bienvenue sur EventMatch");
        alert.setHeaderText("Votre code promo exclusif");

        alert.getDialogPane().setStyle("-fx-background-color: #f5f7fa;");
        alert.getDialogPane().setPrefWidth(400);

        Label titleLabel = new Label("Merci pour votre inscription!");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");

        Label codeLabel = new Label(promoCode);
        codeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4a6baf; " +
                "-fx-border-color: #cbd5e0; -fx-border-width: 1px; -fx-border-radius: 5px; " +
                "-fx-padding: 10px; -fx-background-color: #ffffff;");

        Label detailsLabel = new Label(String.format(
                "✅ Réduction de %d%%\n" +
                        "🎁 Produit: %s\n" +
                        "⏳ Valable jusqu'au %s",
                reduction,
                nomProduit,
                validUntil.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        );
        detailsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4a5568;");

        Button copyButton = new Button("Copier le code");
        copyButton.setStyle("-fx-background-color: #4a6baf; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 5px;");
        copyButton.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(promoCode);
            clipboard.setContent(content);
        });

        Button qrButton = new Button("Générer QR Code");
        qrButton.setStyle("-fx-background-color: #48bb78; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 5px;");
        qrButton.setOnAction(e -> showQRCode(promoCode, nomProduit, reduction));

        HBox buttonBox = new HBox(15, copyButton, qrButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        VBox content = new VBox(15,
                titleLabel,
                codeLabel,
                detailsLabel,
                buttonBox
        );
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: #f5f7fa;");

        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }

    private void showQRCode(String promoCode, String nomProduit, int reduction) {
        try {
            String qrText = String.format(
                    "Code Promo: %s\n" +
                            "Produit: %s\n" +
                            "Réduction: %d%%",
                    promoCode,
                    nomProduit,
                    reduction
            );

            int width = 300;
            int height = 300;
            BitMatrix bitMatrix = new QRCodeWriter().encode(
                    qrText,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
            );

            WritableImage image = new WritableImage(width, height);
            PixelWriter writer = image.getPixelWriter();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    writer.setArgb(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            Stage qrStage = new Stage();
            ImageView imageView = new ImageView(image);
            VBox root = new VBox(imageView);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));

            Scene scene = new Scene(root);
            qrStage.setScene(scene);
            qrStage.setTitle("QR Code - Votre code promo");
            qrStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("Erreur", "Impossible de générer le QR Code", NotificationType.ERROR);
        }
    }

    // Méthode pour générer un code promo aléatoire

    public Promo generatePromoCode() throws SQLException {
        String query = "SELECT * FROM code_promo WHERE date_fin >= CURRENT_DATE ORDER BY RAND() LIMIT 1";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return new Promo(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("nom_produit"),

                        rs.getInt("reduction"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate()
                );
            }
            return null;
        }
    }





    public void CloseInterface() {
        // Votre logique d'animation de menu
        if (Exit != null) {
            System.exit(0);
        } else {
            OpenSlider();
            CloseSlider();
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------affichage de user ----------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------



        // Références FXML
        @FXML private TableView<UserProjet> userTable;
        @FXML private TableColumn<UserProjet, String> nomColumn;
        @FXML private TableColumn<UserProjet, String> prenomColumn;
        @FXML private TableColumn<UserProjet, String> emailColumn;
        @FXML private TableColumn<UserProjet, String> roleColumn;
        @FXML private TableColumn<UserProjet, String> telColumn;
        @FXML private TableColumn<UserProjet, String> adresseColumn;
        @FXML private TableColumn<UserProjet, String> villeColumn;
        @FXML private TableColumn<UserProjet, String> paysColumn;
        @FXML private TableColumn<UserProjet, String> codePostalColumn;
        @FXML private TableColumn<UserProjet, LocalDate> dateNaissanceColumn;
        @FXML private TableColumn<UserProjet, String> bioColumn;

        @FXML private ComboBox<String> searchType;
        @FXML private TextField searchField;
        @FXML private Button addBtn;
        @FXML private Button editBtn;
        @FXML private Button deleteBtn;
        @FXML private Button refreshBtn;

        private ObservableList<UserProjet> userList = FXCollections.observableArrayList();
        private UserProjet selectedUser = null;


    private void configureColumns() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("Role"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        villeColumn.setCellValueFactory(new PropertyValueFactory<>("ville"));
        paysColumn.setCellValueFactory(new PropertyValueFactory<>("pays"));
        codePostalColumn.setCellValueFactory(new PropertyValueFactory<>("codePostal"));
        dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        bioColumn.setCellValueFactory(new PropertyValueFactory<>("bio"));

    }

    private void configureEditableCells() {
        // Nom
        nomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nomColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setNom(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Prénom
        prenomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        prenomColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setPrenom(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Email
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setEmail(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Rôle
        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        roleColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setRole(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Téléphone
        telColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        telColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setTel(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Adresse
        adresseColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        adresseColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setAdresse(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Ville
        villeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        villeColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setVille(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Pays
        paysColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        paysColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setPays(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Code Postal
        codePostalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        codePostalColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setCodePostal(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Date de Naissance
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateNaissanceColumn.setCellFactory(TextFieldTableCell.forTableColumn(
                new LocalDateStringConverter(formatter, formatter)
        ));
        dateNaissanceColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setDateNaissance(event.getNewValue());
            updateUserInDatabase(user);
        });

        // Bio
        bioColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bioColumn.setOnEditCommit(event -> {
            UserProjet user = event.getRowValue();
            user.setBio(event.getNewValue());
            updateUserInDatabase(user);
        });
    }
    @FXML
    private void configureSearch() {
        // Utilisez la même liste que celle déclarée
        searchType.setItems(searchAttributes);
        searchType.getSelectionModel().selectFirst();

        searchBtn.setOnAction(event -> handleSearch());
    }

        private static final String DB_URL = "jdbc:mysql://localhost:3306/projet";
        private static final String DB_USER = "root"; // Changez-le si nécessaire
        private static final String DB_PASSWORD = ""; // Changez-le si nécessaire


        private Connection connect() throws SQLException {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
    @FXML
    private void loadUsers() {
        userList.clear(); // Utilisez userList au lieu de créer une nouvelle ObservableList
        System.out.println("Tentative de chargement des utilisateurs...");

        String query = "SELECT * FROM user WHERE Role IS NOT NULL AND Role <> 'ADMIN'";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                UserProjet usere = new UserProjet(
                        rs.getString("nom") != null ? rs.getString("nom") : "Inconnu",
                        rs.getString("prenom") != null ? rs.getString("prenom") : "Inconnu",
                        rs.getString("email") != null ? rs.getString("email") : "Inconnu",
                        rs.getString("Role") != null ? rs.getString("Role") : "Non spécifié",
                        rs.getString("tel") != null ? rs.getString("tel") : "Non spécifié",
                        rs.getString("adresse") != null ? rs.getString("adresse") : "Non spécifiée",
                        rs.getString("ville") != null ? rs.getString("ville") : "Non spécifiée",
                        rs.getString("pays") != null ? rs.getString("pays") : "Non spécifié",
                        rs.getString("code_postal") != null ? rs.getString("code_postal") : "Non spécifié",
                        rs.getDate("date_naissance") != null ? rs.getDate("date_naissance").toLocalDate() : null,
                        rs.getString("bio") != null ? rs.getString("bio") : "Non spécifiée"
                );
                userList.add(usere);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les utilisateurs", Alert.AlertType.ERROR);
        }
    }
    private Dialog<UserProjet> createUserDialog(UserProjet user) {
        // Configuration de base de la boîte de dialogue
        Dialog<UserProjet> dialog = new Dialog<>();
        dialog.setTitle(user == null ? "Ajouter un utilisateur" : "Modifier un utilisateur");
        dialog.setHeaderText("Veuillez remplir les informations utilisateur");

        // Boutons de la boîte de dialogue
        ButtonType confirmButtonType = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Création du conteneur principal
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Création des champs du formulaire
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        // ComboBox pour les rôles
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("PRESTATAIRE", "ADMIN", "RESPONSABLE EVENT");
        roleComboBox.setPromptText("Sélectionner un rôle");

        TextField telField = new TextField();
        telField.setPromptText("Téléphone");

        TextField adresseField = new TextField();
        adresseField.setPromptText("Adresse");

        TextField villeField = new TextField();
        villeField.setPromptText("Ville");

        TextField paysField = new TextField();
        paysField.setPromptText("Pays");

        TextField codePostalField = new TextField();
        codePostalField.setPromptText("Code postal");

        DatePicker dateNaissancePicker = new DatePicker();
        dateNaissancePicker.setPromptText("Date de naissance");

        TextArea bioField = new TextArea();
        bioField.setPromptText("Biographie");
        bioField.setWrapText(true);
        bioField.setPrefRowCount(3);

        // Pré-remplissage si modification
        if (user != null) {
            nomField.setText(user.getNom());
            prenomField.setText(user.getPrenom());
            emailField.setText(user.getEmail());
            passwordField.setText(user.getPassword());
            roleComboBox.setValue(user.getRole());
            telField.setText(user.getTel());
            adresseField.setText(user.getAdresse());
            villeField.setText(user.getVille());
            paysField.setText(user.getPays());
            codePostalField.setText(user.getCodePostal());
            dateNaissancePicker.setValue(user.getDateNaissance());
            bioField.setText(user.getBio());
        }

        // Organisation des champs dans la grille
        int row = 0;
        grid.add(new Label("Nom*:"), 0, row);
        grid.add(nomField, 1, row++);

        grid.add(new Label("Prénom*:"), 0, row);
        grid.add(prenomField, 1, row++);

        grid.add(new Label("Email*:"), 0, row);
        grid.add(emailField, 1, row++);

        grid.add(new Label("Mot de passe*:"), 0, row);
        grid.add(passwordField, 1, row++);

        grid.add(new Label("Rôle*:"), 0, row);
        grid.add(roleComboBox, 1, row++);

        grid.add(new Label("Téléphone:"), 0, row);
        grid.add(telField, 1, row++);

        grid.add(new Label("Adresse:"), 0, row);
        grid.add(adresseField, 1, row++);

        grid.add(new Label("Ville:"), 0, row);
        grid.add(villeField, 1, row++);

        grid.add(new Label("Pays:"), 0, row);
        grid.add(paysField, 1, row++);

        grid.add(new Label("Code postal:"), 0, row);
        grid.add(codePostalField, 1, row++);

        grid.add(new Label("Date de naissance:"), 0, row);
        grid.add(dateNaissancePicker, 1, row++);

        grid.add(new Label("Biographie:"), 0, row);
        grid.add(bioField, 1, row);

        // Validation des champs obligatoires
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        // Binding pour activer le bouton seulement quand les champs requis sont remplis
        BooleanBinding isValid = Bindings.createBooleanBinding(() ->
                        !nomField.getText().trim().isEmpty() &&
                                !prenomField.getText().trim().isEmpty() &&
                                !emailField.getText().trim().isEmpty() &&
                                !passwordField.getText().trim().isEmpty() &&
                                roleComboBox.getValue() != null,
                nomField.textProperty(),
                prenomField.textProperty(),
                emailField.textProperty(),
                passwordField.textProperty(),
                roleComboBox.valueProperty()
        );

        confirmButton.disableProperty().bind(isValid.not());

        // Ajout du contenu à la boîte de dialogue
        dialog.getDialogPane().setContent(grid);

        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                UserProjet result = new UserProjet();
                result.setNom(nomField.getText());
                result.setPrenom(prenomField.getText());
                result.setEmail(emailField.getText());
                result.setPassword(passwordField.getText());
                result.setRole(roleComboBox.getValue());
                result.setTel(telField.getText());
                result.setAdresse(adresseField.getText());
                result.setVille(villeField.getText());
                result.setPays(paysField.getText());
                result.setCodePostal(codePostalField.getText());
                result.setDateNaissance(dateNaissancePicker.getValue());
                result.setBio(bioField.getText());

                return result;
            }
            return null;
        });

        return dialog;
    }

        @FXML
        private void handleAddUser() {
            Dialog<UserProjet> dialog = createUserDialog(null);

            dialog.showAndWait().ifPresent(user -> {
                String query = "INSERT INTO user (nom, prenom, email, password, Role, tel, adresse, ville, pays, code_postal, date_naissance, bio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = connect();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, user.getNom());
                    pstmt.setString(2, user.getPrenom());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getMaskedPassword()); // Assurez-vous de sécuriser ce champ
                    pstmt.setString(5, user.getRole());
                    pstmt.setString(6, user.getTel());
                    pstmt.setString(7, user.getAdresse());
                    pstmt.setString(8, user.getVille());
                    pstmt.setString(9, user.getPays());
                    pstmt.setString(10, user.getCodePostal());
                    pstmt.setDate(11, Date.valueOf(user.getDateNaissance()));
                    pstmt.setString(12, user.getBio());

                    pstmt.executeUpdate();
                    loadUsers();
                    showAlert("Succès", "Utilisateur ajouté avec succès.", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Erreur", "Impossible d'ajouter l'utilisateur:\n" + e.getMessage(), Alert.AlertType.ERROR);
                }
            });
        }

    @FXML
    private void handleEditUser() {
        if (selectedUser == null) {
            showAlert("Erreur", "Aucun utilisateur sélectionné pour l'édition.", Alert.AlertType.ERROR);
            return;
        }

        Dialog<UserProjet> dialog = createUserDialog(selectedUser);

        dialog.showAndWait().ifPresent(user -> {
            String query = "UPDATE user SET nom = ?, prenom = ?, email = ?, Role = ?, tel = ?, adresse = ?, ville = ?, pays = ?, code_postal = ?, date_naissance = ?, bio = ? WHERE email = ?";

            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, user.getNom());
                pstmt.setString(2, user.getPrenom());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getRole());
                pstmt.setString(5, user.getTel());
                pstmt.setString(6, user.getAdresse());
                pstmt.setString(7, user.getVille());
                pstmt.setString(8, user.getPays());
                pstmt.setString(9, user.getCodePostal());

                if (user.getDateNaissance() != null) {
                    pstmt.setDate(10, Date.valueOf(user.getDateNaissance()));
                } else {
                    pstmt.setNull(10, java.sql.Types.DATE);
                }

                pstmt.setString(11, user.getBio());
                pstmt.setString(12, selectedUser.getEmail()); // Assurez-vous que l'ID est correctement passé

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    loadUsers(); // Actualisez la TableView
                    showAlert("Succès", "Utilisateur mis à jour avec succès.", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur", "Aucune ligne mise à jour. Vérifiez l'ID.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de modifier l'utilisateur:\n" + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
        private void handleDeleteUser() {
            if (selectedUser == null) return;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer l'utilisateur ?");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer " + selectedUser.getNom() + " ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String query = "DELETE FROM user WHERE email = ?";

                    try (Connection conn = connect();
                         PreparedStatement stmt = conn.prepareStatement(query)) {

                        stmt.setString(1, selectedUser.getEmail()); // ⚠ Utilisez getId()
                        int rowsDeleted = stmt.executeUpdate();

                        if (rowsDeleted > 0) {
                            System.out.println("Utilisateur supprimé !");
                            loadUsers(); // Recharge la table
                        }
                    } catch (SQLException e) {
                        System.err.println("Erreur SQL (delete): " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    @FXML
    private void handleExport() {
        // Créer un FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les données");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Fichiers TXT", "*.txt"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        // Définir un nom de fichier par défaut
        fileChooser.setInitialFileName("utilisateurs_eventmatch_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv");

        // Afficher la boîte de dialogue pour sauvegarder
        Window window = userTable.getScene().getWindow();
        File file = fileChooser.showSaveDialog(window);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // Écrire l'en-tête CSV avec UTF-8 pour supporter les caractères spéciaux
                writer.println("\uFEFF"); // BOM pour UTF-8
                writer.println("Nom,Prénom,Email,Rôle,Téléphone,Adresse,Ville,Pays,Code Postal,Date Naissance,Bio");

                // Écrire les données avec gestion des champs null
                for (UserProjet user : userTable.getItems()) {
                    writer.println(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                            user.getNom() != null ? user.getNom() : "",
                            user.getPrenom() != null ? user.getPrenom() : "",
                            user.getEmail() != null ? user.getEmail() : "",
                            user.getRole() != null ? user.getRole() : "",
                            user.getTel() != null ? user.getTel() : "",
                            user.getAdresse() != null ? user.getAdresse() : "",
                            user.getVille() != null ? user.getVille() : "",
                            user.getPays() != null ? user.getPays() : "",
                            user.getCodePostal() != null ? user.getCodePostal() : "",
                            user.getDateNaissance() != null ?
                                    user.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "",
                            user.getBio() != null ? user.getBio() : ""
                    ));
                }

                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export réussi");
                alert.setHeaderText(null);
                alert.setContentText("Les données ont été exportées avec succès vers:\n" + file.getAbsolutePath());

                // Ajouter un bouton pour ouvrir le dossier de destination
                ButtonType openFolderButton = new ButtonType("Ouvrir le dossier", ButtonBar.ButtonData.OK_DONE);
                alert.getButtonTypes().add(openFolderButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == openFolderButton) {
                    try {
                        Desktop.getDesktop().open(file.getParentFile());
                    } catch (IOException e) {
                        System.err.println("Erreur lors de l'ouverture du dossier: " + e.getMessage());
                    }
                }

            } catch (IOException e) {
                // Gérer les erreurs d'écriture
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'export");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur est survenue lors de l'export:\n" + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private Button searchBtn;

    // Liste des attributs disponibles pour la recherche
    private final ObservableList<String> searchAttributes = FXCollections.observableArrayList(
            "Nom", "Prénom", "Email", "Rôle", "Téléphone", "Adresse", "Ville", "Pays", "Code Postal", "Date Naissance", "Bio"
    );

    private String mapAttributeToColumn(String attribute) {
        switch (attribute) {
            case "Nom": return "nom";
            case "Prénom": return "prenom";
            case "Email": return "email";
            case "Rôle": return "role";
            case "Téléphone": return "tel";
            case "Adresse": return "adresse";
            case "Ville": return "ville";
            case "Pays": return "pays";
            case "Code Postal": return "code_postal";
            case "Date Naissance": return "date_naissance";
            case "Bio": return "bio";
            default: return null;
        }
    }
    @FXML
    private void handleSearch() {
        String selectedField = searchType.getValue();
        String searchText = searchField.getText();

        if (searchText == null || searchText.trim().isEmpty()) {
            loadUsers(); // Recharge les utilisateurs si le champ de recherche est vide
            return;
        }

        String columnName = mapAttributeToColumn(selectedField);
        if (columnName == null) {
            System.out.println("Champ de recherche invalide !");
            return;
        }

        String query = "SELECT * FROM user WHERE " + columnName + " LIKE ? AND Role IS NOT NULL AND Role <> 'ADMIN'";
        ObservableList<UserProjet> filteredUsers = FXCollections.observableArrayList();

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserProjet user = new UserProjet(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("Role"),
                        rs.getString("tel"),
                        rs.getString("adresse"),
                        rs.getString("ville"),
                        rs.getString("pays"),
                        rs.getDate("date_naissance") != null ?
                                rs.getDate("date_naissance").toLocalDate() : null,
                        rs.getString("bio")
                );
                filteredUsers.add(user);

            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
            showAlert("Erreur", "Erreur lors de la recherche : " + e.getMessage(), Alert.AlertType.ERROR);
        }

        userTable.setItems(filteredUsers);
    }
    private void updateUserInDatabase(UserProjet user) {
        String query = "UPDATE user SET nom = ?, prenom = ?, email = ?, Role = ?, tel = ?, adresse = ?, ville = ?, pays = ?, code_postal = ?, date_naissance = ?, bio = ? WHERE email = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getTel());
            stmt.setString(6, user.getAdresse());
            stmt.setString(7, user.getVille());
            stmt.setString(8, user.getPays());
            stmt.setString(9, user.getCodePostal());
            stmt.setDate(10, java.sql.Date.valueOf(user.getDateNaissance()));  // Conversion LocalDate à java.sql.Date
            stmt.setString(11, user.getBio());
            stmt.setString(12, user.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }


// ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------fin delete profile and   user from table ----------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------








        @FXML private Button addBtnpromo;
        @FXML private TableColumn<Promo, String> codeColumn;
        @FXML private TableColumn<Promo, String> nomProduitColumn;
        @FXML private TableColumn<Promo, Integer> reductionColumn;
        @FXML private TableColumn<Promo, LocalDate> dateDebutColumn;
        @FXML private TableColumn<Promo, LocalDate> dateFinColumn;
        @FXML private Button deleteBtnpromo;
        @FXML private Button editBtnpromo;
        @FXML private Button exportBtnpromo;
        @FXML private Button logoutBtn;
        @FXML private Button refreshBtnpromo;
        @FXML private Button searchBtnpromo;
        @FXML private TextField searchFieldpromo;
        @FXML private ComboBox<String> searchTypepromo;
        @FXML private TableView<Promo> userTablepromo;

        private ObservableList<Promo> promolist = FXCollections.observableArrayList();
        private Promo selectpromoa = null;


        private void configureColumnspromo() {
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
            nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
            reductionColumn.setCellValueFactory(new PropertyValueFactory<>("reduction"));
            dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        }

        private void configureEditableCellspromo() {
            // Code
            codeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            codeColumn.setOnEditCommit(event -> {
                Promo promo = event.getRowValue();
                promo.setCode(event.getNewValue());
                updatePromoInDatabasepromo(promo);
            });

            // Nom Produit
            nomProduitColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            nomProduitColumn.setOnEditCommit(event -> {
                Promo promo = event.getRowValue();
                promo.setNomProduit(event.getNewValue());
                updatePromoInDatabasepromo(promo);
            });

            // Réduction
            reductionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            reductionColumn.setOnEditCommit(event -> {
                Promo promo = event.getRowValue();
                promo.setReduction(event.getNewValue());
                updatePromoInDatabasepromo(promo);
            });

            // Date Début
            dateDebutColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                public String toString(LocalDate date) {
                    return (date != null) ? date.format(formatter) : "";
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, formatter);
                    }
                    return null;
                }
            }));
            dateDebutColumn.setOnEditCommit(event -> {
                Promo promo = event.getRowValue();
                promo.setDateDebut(event.getNewValue());
                updatePromoInDatabasepromo(promo);
            });

            // Date Fin
            dateFinColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                public String toString(LocalDate date) {
                    return (date != null) ? date.format(formatter) : "";
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, formatter);
                    }
                    return null;
                }
            }));
            dateFinColumn.setOnEditCommit(event -> {
                Promo promo = event.getRowValue();
                promo.setDateFin(event.getNewValue());
                updatePromoInDatabasepromo(promo);
            });
        }

        private void configureSearchpromo() {
            ObservableList<String> searchAttributes = FXCollections.observableArrayList(
                    "Code", "Nom Produit", "Réduction", "Date Début", "Date Fin"
            );
            searchTypepromo.setItems(searchAttributes);
            searchTypepromo.getSelectionModel().selectFirst();
            searchBtnpromo.setOnAction(event -> handleSearchpromo());
        }

    @FXML
    private void loadUserspromo() {
        ObservableList<Promo> loadedPromos = FXCollections.observableArrayList();
        String query = "SELECT * FROM code_promo";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Promo promo = new Promo(
                        rs.getString("code"),
                        rs.getString("nom_produit"),
                        rs.getInt("reduction"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate()
                );
                loadedPromos.add(promo);
            }

            // Mettre à jour la liste principale
            promolist.setAll(loadedPromos);

        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les promotions", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
        private Dialog<Promo> createPromoDialog(Promo promo) {
            Dialog<Promo> dialog = new Dialog<>();
            dialog.setTitle(promo == null ? "Ajouter une promotion" : "Modifier une promotion");
            dialog.setHeaderText("Veuillez remplir les informations de la promotion");

            ButtonType confirmButtonType = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField codeField = new TextField();
            codeField.setPromptText("Code");

            TextField nomProduitField = new TextField();
            nomProduitField.setPromptText("Nom du produit");

            TextField reductionField = new TextField();
            reductionField.setPromptText("Réduction (%)");

            DatePicker dateDebutPicker = new DatePicker();
            dateDebutPicker.setPromptText("Date de début");

            DatePicker dateFinPicker = new DatePicker();
            dateFinPicker.setPromptText("Date de fin");

            if (promo != null) {
                codeField.setText(promo.getCode());
                nomProduitField.setText(promo.getNomProduit());
                reductionField.setText(String.valueOf(promo.getReduction()));
                dateDebutPicker.setValue(promo.getDateDebut());
                dateFinPicker.setValue(promo.getDateFin());
            }

            grid.add(new Label("Code:"), 0, 0);
            grid.add(codeField, 1, 0);
            grid.add(new Label("Nom Produit:"), 0, 1);
            grid.add(nomProduitField, 1, 1);
            grid.add(new Label("Réduction (%):"), 0, 2);
            grid.add(reductionField, 1, 2);
            grid.add(new Label("Date de début:"), 0, 3);
            grid.add(dateDebutPicker, 1, 3);
            grid.add(new Label("Date de fin:"), 0, 4);
            grid.add(dateFinPicker, 1, 4);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirmButtonType) {
                    try {
                        return new Promo(
                                codeField.getText(),
                                nomProduitField.getText(),
                                Integer.parseInt(reductionField.getText()),
                                dateDebutPicker.getValue(),
                                dateFinPicker.getValue()
                        );
                    } catch (NumberFormatException e) {
                        showAlert("Erreur", "La réduction doit être un nombre entier", Alert.AlertType.ERROR);
                        return null;
                    }
                }
                return null;
            });

            return dialog;
        }

        @FXML
        private void handleAddpromo() {
            Dialog<Promo> dialog = createPromoDialog(null);
            dialog.showAndWait().ifPresent(promo -> {
                String query = "INSERT INTO code_promo (code, nom_produit, reduction, date_debut, date_fin) VALUES (?, ?, ?, ?, ?)";
                try (Connection conn = connect();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, promo.getCode());
                    pstmt.setString(2, promo.getNomProduit());
                    pstmt.setInt(3, promo.getReduction());
                    pstmt.setDate(4, Date.valueOf(promo.getDateDebut()));
                    pstmt.setDate(5, Date.valueOf(promo.getDateFin()));
                    pstmt.executeUpdate();
                    loadUserspromo();
                    showAlert("Succès", "Promotion ajoutée avec succès.", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Erreur", "Impossible d'ajouter la promotion:\n" + e.getMessage(), Alert.AlertType.ERROR);
                }
            });
        }

        @FXML
        private void handleEditpromo() {
            if (selectpromoa == null) {
                showAlert("Erreur", "Aucune promotion sélectionnée pour l'édition.", Alert.AlertType.ERROR);
                return;
            }

            Dialog<Promo> dialog = createPromoDialog(selectpromoa);
            dialog.showAndWait().ifPresent(promo -> {
                String query = "UPDATE code_promo SET code = ?, nom_produit = ?, reduction = ?, date_debut = ?, date_fin = ? WHERE code = ?";
                try (Connection conn = connect();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, promo.getCode());
                    pstmt.setString(2, promo.getNomProduit());
                    pstmt.setInt(3, promo.getReduction());
                    pstmt.setDate(4, Date.valueOf(promo.getDateDebut()));
                    pstmt.setDate(5, Date.valueOf(promo.getDateFin()));
                    pstmt.setString(6, selectpromoa.getCode());
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        loadUserspromo();
                        showAlert("Succès", "Promotion mise à jour avec succès.", Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Erreur", "Aucune ligne mise à jour.", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    showAlert("Erreur", "Impossible de modifier la promotion:\n" + e.getMessage(), Alert.AlertType.ERROR);
                }
            });
        }

        @FXML
        private void handleDeletepromo() {
            if (selectpromoa == null) return;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer le code ?");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer " + selectpromoa.getCode() + " ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String query = "DELETE FROM code_promo WHERE code = ?";
                    try (Connection conn = connect();
                         PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, selectpromoa.getCode());
                        int rowsDeleted = stmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            loadUserspromo();
                        }
                    } catch (SQLException e) {
                        showAlert("Erreur", "Impossible de supprimer la promotion", Alert.AlertType.ERROR);
                    }
                }
            });
        }

        @FXML
        private void handleExportpromo() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exporter les données");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"),
                    new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
            );

            File file = fileChooser.showSaveDialog(userTablepromo.getScene().getWindow());
            if (file != null) {
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.println("Code,Nom Produit,Réduction (%),Date Début,Date Fin");
                    for (Promo promo : userTablepromo.getItems()) {
                        writer.println(String.format("\"%s\",\"%s\",\"%d\",\"%s\",\"%s\"",
                                promo.getCode(),
                                promo.getNomProduit(),
                                promo.getReduction(),
                                promo.getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                promo.getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        ));
                    }
                    showAlert("Export réussi", "Les données ont été exportées avec succès", Alert.AlertType.INFORMATION);
                } catch (IOException e) {
                    showAlert("Erreur d'export", "Une erreur est survenue lors de l'export", Alert.AlertType.ERROR);
                }
            }
        }

        private void updatePromoInDatabasepromo(Promo promo) {
            String query = "UPDATE code_promo SET code = ?, nom_produit = ?, reduction = ?, date_debut = ?, date_fin = ? WHERE code = ?";
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, promo.getCode());
                stmt.setString(2, promo.getNomProduit());
                stmt.setInt(3, promo.getReduction());
                stmt.setDate(4, Date.valueOf(promo.getDateDebut()));
                stmt.setDate(5, Date.valueOf(promo.getDateFin()));
                stmt.setString(6, promo.getCode());
                stmt.executeUpdate();
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de mettre à jour la promotion", Alert.AlertType.ERROR);
            }
        }

        private String mapAttributeToColumnpromo(String attribute) {
            switch (attribute) {
                case "Code": return "code";
                case "Nom Produit": return "nom_produit";
                case "Réduction": return "reduction";
                case "Date Début": return "date_debut";
                case "Date Fin": return "date_fin";
                default: return null;
            }
        }

        @FXML
        private void handleSearchpromo() {
            String selectedField = searchTypepromo.getValue();
            String searchText = searchFieldpromo.getText();

            if (searchText == null || searchText.trim().isEmpty()) {
                loadUserspromo();
                return;
            }

            String columnName = mapAttributeToColumnpromo(selectedField);
            if (columnName == null) return;

            String query = "SELECT * FROM code_promo WHERE " + columnName + " LIKE ?";
            ObservableList<Promo> filteredPromos = FXCollections.observableArrayList();

            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, "%" + searchText + "%");
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Promo promo = new Promo(
                            rs.getString("code"),
                            rs.getString("nom_produit"),
                            rs.getInt("reduction"),
                            rs.getDate("date_debut").toLocalDate(),
                            rs.getDate("date_fin").toLocalDate()
                    );
                    filteredPromos.add(promo);
                }
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la recherche", Alert.AlertType.ERROR);
            }

            userTablepromo.setItems(filteredPromos);
        }








    private void configureDynamicSearch() {
        // Initialisez la FilteredList avec la liste complète des utilisateurs
        FilteredList<UserProjet> filteredUsers = new FilteredList<>(userList, p -> true);

        // Liez la TableView à la FilteredList
        userTable.setItems(filteredUsers);

        // Écouteur pour le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {
                // Si le champ de recherche est vide, affichez tous les utilisateurs
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convertir le texte de recherche en minuscules
                String lowerCaseFilter = newValue.toLowerCase();

                // Récupérer l'attribut sélectionné pour la recherche
                String selectedAttribute = searchType.getValue();

                // Filtrer selon l'attribut sélectionné
                switch (selectedAttribute) {
                    case "Nom":
                        return user.getNom().toLowerCase().contains(lowerCaseFilter);
                    case "Prénom":
                        return user.getPrenom().toLowerCase().contains(lowerCaseFilter);
                    case "Email":
                        return user.getEmail().toLowerCase().contains(lowerCaseFilter);
                    case "Rôle":
                        return user.getRole().toLowerCase().contains(lowerCaseFilter);
                    case "Téléphone":
                        return user.getTel().toLowerCase().contains(lowerCaseFilter);
                    case "Adresse":
                        return user.getAdresse().toLowerCase().contains(lowerCaseFilter);
                    case "Ville":
                        return user.getVille().toLowerCase().contains(lowerCaseFilter);
                    case "Pays":
                        return user.getPays().toLowerCase().contains(lowerCaseFilter);
                    case "Code Postal":
                        return user.getCodePostal().toLowerCase().contains(lowerCaseFilter);
                    case "Date Naissance":
                        return user.getDateNaissance() != null &&
                                user.getDateNaissance().toString().toLowerCase().contains(lowerCaseFilter);
                    case "Bio":
                        return user.getBio().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });
    }
    private void setupDynamicSearchPromo() {
        // Initialiser la FilteredList avec la liste principale
        FilteredList<Promo> filteredPromos = new FilteredList<>(promolist, p -> true);

        // Lier la TableView à la FilteredList
        userTablepromo.setItems(filteredPromos);

        // Configurer les options de recherche
        ObservableList<String> searchOptions = FXCollections.observableArrayList(
                "Code", "Nom Produit", "Réduction", "Date Début", "Date Fin"
        );
        searchTypepromo.setItems(searchOptions);
        searchTypepromo.getSelectionModel().selectFirst();

        // Écouteur pour la recherche dynamique
        searchFieldpromo.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPromos.setPredicate(promo -> {
                // Si le champ est vide ou null, afficher tout
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String selectedAttribute = searchTypepromo.getValue();

                if (selectedAttribute == null) return true;

                try {
                    switch (selectedAttribute) {
                        case "Code":
                            return promo.getCode() != null &&
                                    promo.getCode().toLowerCase().contains(lowerCaseFilter);
                        case "Nom Produit":
                            return promo.getNomProduit() != null &&
                                    promo.getNomProduit().toLowerCase().contains(lowerCaseFilter);
                        case "Réduction":
                            return String.valueOf(promo.getReduction()).contains(lowerCaseFilter);
                        case "Date Début":
                            return promo.getDateDebut() != null &&
                                    promo.getDateDebut().toString().toLowerCase().contains(lowerCaseFilter);
                        case "Date Fin":
                            return promo.getDateFin() != null &&
                                    promo.getDateFin().toString().toLowerCase().contains(lowerCaseFilter);
                        default:
                            return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            });
        });
    }


    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------


    @FXML
    private AnchorPane setslideraccount;
    @FXML
    private AnchorPane fieled;

    @FXML
    private AnchorPane setslider;
    @FXML
    private AnchorPane amin;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private BorderPane stat;
    @FXML
    void initialize() {

        if (stat != null) {
                // Configurer les axes
                CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
                NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();

                xAxis.setLabel("Date d'inscription");
                yAxis.setLabel("Nombre d'utilisateurs");

                // Récupérer les données
                Map<String, Integer> userStats = getUserStats();

                if (userStats.isEmpty()) {
                    showAlert("Aucune donnée", "Aucune donnée d'inscription trouvée.", Alert.AlertType.WARNING);
                    return;
                }

                // Créer la série
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Inscriptions quotidiennes");

                // Ajouter les points de données
                userStats.forEach((date, count) -> {
                    series.getData().add(new XYChart.Data<>(date, count));
                });

                // Ajouter la série au graphique
                lineChart.getData().add(series);

                // Personnalisation
                lineChart.setLegendVisible(true);
                lineChart.setCreateSymbols(true);
                lineChart.setAnimated(true);

            }



        if (amin!=null){
            // Configuration des colonnes
            configureColumnspromo();

            // Rendre les colonnes éditables
            configureEditableCellspromo();

            // Charger les utilisateurs depuis la base de données
            loadUserspromo();
            setupDynamicSearchPromo();
            // Gestion de la sélection
            userTablepromo.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        selectpromoa = newSelection;
                        editBtnpromo.setDisable(newSelection == null);
                        deleteBtnpromo.setDisable(newSelection == null);
                    });


        }
        if (setslideraccount != null) {
            loadUserData();



        }


        if (fieled != null) {
            configureColumns();

            // Rendre les colonnes éditables
            configureEditableCells();

            // Charger les utilisateurs depuis la base de données
            loadUsers();

            // Configurer la recherche dynamique
            configureDynamicSearch();

            // Configurer les options de recherche
            ObservableList<String> searchOptions = FXCollections.observableArrayList(
                    "Nom", "Prénom", "Email", "Rôle", "Téléphone",
                    "Adresse", "Ville", "Pays", "Code Postal",
                    "Date Naissance", "Bio"
            );
            searchType.setItems(searchOptions);
            searchType.getSelectionModel().selectFirst();
            // Gestion de la sélection
            userTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        selectedUser = newSelection;
                        editBtn.setDisable(newSelection == null);
                        deleteBtn.setDisable(newSelection == null);
                    });

        }

            if (setslider != null) {
                configureColumnspromo();
                configureEditableCellspromo();
                configureSearch();
                loadUsers();

                userTable.getSelectionModel().selectedItemProperty().addListener(
                        (obs, oldSelection, newSelection) -> {
                            selectedUser = newSelection;
                            editBtn.setDisable(newSelection == null);
                            deleteBtn.setDisable(newSelection == null);
                        }
                );
            }


//affichage user  terminer
            if (bar != null) {
                bar.setTranslateX(-176);
                MenuClose.setVisible(false);
            }
        if (ProfileBar != null) {
            loadUserProfile();
            ProfileBar.setTranslateX(700);
            ProfileClose.setVisible(false);
            loadUserProfile(); // Charger le profil utilisateur dès l'ouverture

        }

            if (slider == null && ForgetSlider == null) {
                System.err.println("Erreur : Le composant 'slider' n'a pas été injecté.");
            } else {
                ForgetSlider.setTranslateX(500);
                forgettxt.setVisible(true);
                Sendtxt.setVisible(false);

                slider.setTranslateX(-500);
                createtxt.setVisible(true);
                signuptxt.setVisible(false);

                comboRole.getItems().setAll(Role.values());
                generateCaptcha();

                try {
                    if (eventtxt != null) {
                        Node[] nodes = {eventtxt, event1txt, event2txt};

                        for (Node node : nodes) {
                            FadeTransition fade = createFade(node);
                            TranslateTransition slide = createSlide(node);

                            ParallelTransition animation = new ParallelTransition(fade, slide);
                            animation.play();
                        }

                        FadeTransition fade3 = createFade(motivatriontxt);
                        FadeTransition fade4 = createFade(emailtxt);
                        FadeTransition fade5 = createFade(passwordtxt);
                        FadeTransition fade6 = createFade(loginBtn);

                        fade3.play();
                        fade4.play();
                        fade5.play();
                        fade6.play();

                        DropShadow loginShadow = new DropShadow(10, Color.valueOf("#4fa3f1"));

                        DropShadow whiteGlow = new DropShadow();
                        whiteGlow.setColor(Color.rgb(255, 255, 255, 0.7));
                        whiteGlow.setRadius(30);
                        whiteGlow.setSpread(0.6);

                        DropShadow whiteGlow2 = new DropShadow();
                        whiteGlow2.setColor(Color.rgb(233, 223, 123, 0.1));
                        whiteGlow2.setRadius(5);
                        whiteGlow2.setSpread(0.1);
                        whiteGlow2.setOffsetX(19);
                        whiteGlow2.setOffsetY(17);

                        eventtxt.setEffect(whiteGlow);
                        event1txt.setEffect(whiteGlow2);
                        event2txt.setEffect(whiteGlow2);
                        loginBtn.setEffect(loginShadow);
                        signtxt.setEffect(whiteGlow);
                    } else {
                        System.err.println("Warning: eventtxt n'est pas injecté");
                    }


                } catch (Exception e) {
                    System.err.println("Erreur d'initialisation: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            // Configuration des colonnes de tableau

        }


    private Map<String, Integer> getUserStats() {
        Map<String, Integer> stats = new LinkedHashMap<>(); // LinkedHashMap conserve l'ordre

        String url = "jdbc:mysql://localhost:3306/projet";
        String user = "root";
        String password = "";

        // Modifiez la requête pour utiliser date_inscription au lieu de date
        String query = "SELECT DATE(date_inscription) as jour, COUNT(*) as user_count " +
                "FROM user GROUP BY DATE(date_inscription) ORDER BY jour";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {
                Date date = rs.getDate("jour");
                stats.put(sdf.format(date), rs.getInt("user_count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Affichez une alerte en cas d'erreur
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de base de données");
                alert.setHeaderText("Impossible de charger les statistiques");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
        }

        return stats;
    }

    public void OpenSlider() {
        // Animation pour ouvrir le slider
        if (createtxt != null) {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0); // Position finale du slider
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                createtxt.setVisible(false); // Masquer le texte "create"
                signuptxt.setVisible(true); // Afficher le texte "signup"
            });
        }
    }

    public void CloseSlider() {
        // Animation pour fermer le slider
        if (signuptxt != null) {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(-500); // Revenir à la position initiale
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                signuptxt.setVisible(false); // Masquer le texte "signup"
                createtxt.setVisible(true); // Afficher le texte "create"
            });
        }
    }

    private DB db = new DB();

    @FXML
    void getLogin(ActionEvent event) {
        String loginQuery = "SELECT id, role, password FROM user WHERE email = ?";

        String email = emailtxt.getText().trim();
        String password = passwordtxt.getText().trim();

        // Valider les champs d'entrée
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur de connexion", "Veuillez saisir votre email et mot de passe.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Préparer la requête
            db.initPrepar(loginQuery);
            PreparedStatement pstmtUser = db.getPtsm(loginQuery);
            pstmtUser.setString(1, email);

            ResultSet rs = pstmtUser.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String role = rs.getString("role");
                String hashedPassword = rs.getString("password");

                // Vérifier le mot de passe haché
                if (PasswordUtil.checkPassword(password, hashedPassword)) {
                    // Stocker l'ID utilisateur dans la session
                    UserSession.getInstance().setUserId(userId);

                    // Charger l'interface en fonction du rôle
                    if ("admin".equalsIgnoreCase(role)) {
                        loadInterface(event, "admin-home.fxml");
                    } else {
                        loadInterface(event, "account.fxml");
                    }
                } else {
                    // Alerte si le mot de passe est incorrect
                    showAlert("Erreur de connexion", "Email ou mot de passe incorrect.", Alert.AlertType.ERROR);
                }
            } else {
                // Alerte si aucun utilisateur n'est trouvé
                showAlert("Erreur de connexion", "Email ou mot de passe incorrect.", Alert.AlertType.ERROR);
            }

            // Fermer les ressources
            rs.close();
            pstmtUser.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la connexion.", Alert.AlertType.ERROR);
        }
    }

    private void loadInterface(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface.", Alert.AlertType.ERROR);
        }
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showNotification("Erreur", "Veuillez saisir tous les champs", NotificationType.ERROR);
            return false;
        }
        return true;
    }

    // Déclarez cette variable en haut de votre classe

    private void handleLoginResult(User user, String email, ActionEvent event) {

        if (user != null && user.getEmail().equals(email) && user.getPassword().equals(passwordtxt.getText())) {


            showNotification("Connexion réussie", "Bienvenue " + user.getEmail(), NotificationType.SUCCESS);
            redirectToHomeView(event);

        } else {
            showNotification("Erreur", "Identifiants incorrects", NotificationType.ERROR);
        }
    }

// ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------movement entre les pages ----------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Charger la page précédente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page précédente : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Charger la page d'accueil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page d'accueil : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBackprofileHome(ActionEvent event) {
        try {
            // Charger la page précédente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-home.fxml"));
            Parent root = loader.load();

            // Changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page précédente : " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void viewProfileHome(ActionEvent event) {
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


public void viewProfileAccount(ActionEvent event) {
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


    public void viewProfileAccountuser(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("amin.fxml"));
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

    public void viewProfileAccountuserAccount(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("setting.fxml"));
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


    public void viewProfileAccounPassword(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("password.fxml"));
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

    public void viewusers(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user.fxml"));
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
    public void viewcode(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("setting.fxml"));
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





    public void pagepromo(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("promo.fxml"));
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


    public void ghadareservation(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Reservations.fxml"));
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
    public void ghadasalle(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Salle.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir le nouveau contenu
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED); // Utilisation de la fenêtre décorée (avec les bordures et boutons)

            // Permettre le redimensionnement
            stage.setResizable(true); // Assure que la fenêtre peut être redimensionnée

            // Définir la taille minimale de la fenêtre si vous le souhaitez
            stage.setMinWidth(1000); // Largeur minimale
            stage.setMinHeight(1000); // Hauteur minimale

            // Afficher la scène
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void ghadareservationsalle(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReservationSalle.fxml"));
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


    public void Staticbtn(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("statistiques.fxml"));
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

    public void SPIN(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SPIN.fxml"));
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



    public void INTERFACEMAROI(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
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
// ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------fin mouvement entre les pages ----------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------





    private void redirectToHomeView(ActionEvent event) {
        try {
            System.out.println("Tentative de chargement de amin.fxml...");
            URL fxmlLocation = getClass().getResource("/com/example/eventmatch_pr/amin.fxml");
            System.out.println("Chemin résolu : " + fxmlLocation);
            Parent root = FXMLLoader.load(fxmlLocation);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface Principale");
            stage.show();
        } catch (IOException e) {
            showNotification("Erreur", "Erreur lors du chargement de l'interface : " + e.getMessage(), NotificationType.ERROR);
            e.printStackTrace();
        }
    }


    private void handleLoginError(Exception e) {
        showNotification("Erreur", "Erreur de connexion: " + e.getMessage(), NotificationType.ERROR);
        e.printStackTrace();
    }

    public static void showNotification(String title, String message, NotificationType type) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.setAnimationType(type == NotificationType.ERROR ? AnimationType.POPUP : AnimationType.SLIDE);
        tray.showAndDismiss(Duration.seconds(3));
    }


// ------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
    //____________________________________PARTIE PROFILE CONTROLLEUR PAGE DE PROFIL ------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private Button sort;

    @FXML
    private Button Menu;

    @FXML
    private Button MenuClose;

    @FXML
    private AnchorPane bar;

    @FXML
    private Button Profile;

    @FXML
    private AnchorPane ProfileBar;

    @FXML
    private Button ProfileClose;


    public void ClosePage() {
        // Votre logique d'animation de menu
        if (sort != null) {
            System.exit(0);
        } else {

            OpenMenu();
            CloseMenu();
            ProfileOpen();
            ProfileClose();
        }
    }

    public void OpenMenu() {
        // Animation pour ouvrir le menu
        bar.setTranslateX(-176);
        if (Menu != null) {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(bar);
            slide.setToX(0);
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        }
        bar.setTranslateX(-176);
    }

    public void CloseMenu() {
        // Animation pour fermer le menu
        if (MenuClose != null) {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(bar);
            slide.setToX(-176);
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        }
        ;

        // Appeler loadInterface pour charger le fichier FXML de l'interface
    }

    public void ProfileOpen() {
        if (Profile != null) {
            loadUserProfile();

            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), ProfileBar);
            slide.setToX(0); // Animation depuis la droite vers la position visible

            slide.setOnFinished(e -> {
                Profile.setVisible(false);
                ProfileClose.setVisible(true);

            });

            slide.play();
        }
    }

    public void ProfileClose() {
        if (ProfileClose != null) {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), ProfileBar);
            slide.setToX(700); // Retour à droite (caché)

            slide.setOnFinished(e -> {
                Profile.setVisible(true);
                ProfileClose.setVisible(false);
            });

            slide.play();
        }
    }

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
//____________________________________PARTIE Chargement de profil ------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------


// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
//____________________________________ajouter profil ------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------


    @FXML
    private TextField villetxt;
    @FXML
    private TextField biotxt;
    @FXML
    private TextField paystxt;
    @FXML
    private DatePicker pickerdate;
    @FXML
    private TextField postaltxt;
    @FXML
    private TextField addresstxt;




    private void loadUserProfile() {
        int connectedUserId = UserSession.getInstance().getUserId();
        String sqlQuery = "SELECT adresse, ville, pays, code_postal, date_naissance, bio FROM user WHERE id = ?";
        boolean hasData = false;

        try {
            db.initPrepar(sqlQuery);
            PreparedStatement pstmt = db.getPtsm(sqlQuery);
            pstmt.setInt(1, connectedUserId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Vérifier si au moins un champ contient des données
                hasData = rs.getString("adresse") != null ||
                        rs.getString("ville") != null ||
                        rs.getString("pays") != null ||
                        rs.getString("code_postal") != null ||
                        rs.getDate("date_naissance") != null ||
                        rs.getString("bio") != null;

                // Remplir les champs
                addresstxt.setText(rs.getString("adresse") != null ? rs.getString("adresse") : "Remplir le champ");
                villetxt.setText(rs.getString("ville") != null ? rs.getString("ville") : "Remplir le champ");
                paystxt.setText(rs.getString("pays") != null ? rs.getString("pays") : "Remplir le champ");
                postaltxt.setText(rs.getString("code_postal") != null ? rs.getString("code_postal") : "Remplir le champ");
                pickerdate.setValue(rs.getDate("date_naissance") != null ? rs.getDate("date_naissance").toLocalDate() : null);
                biotxt.setText(rs.getString("bio") != null ? rs.getString("bio") : "Remplir le champ");

                // Afficher l'alerte seulement si aucun champ n'est rempli
                if (!hasData) {
                    notifyNoProfileData();
                }
            } else {
                showNotification("Erreur", "Aucune donnée trouvée pour cet utilisateur", NotificationType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showNotification("Erreur", "Erreur lors du chargement du profil", NotificationType.ERROR);
        }
    }


    private void notifyNoProfileData() {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Profil incomplet");
        tray.setMessage("Veuillez remplir les informations de votre profil.");
        tray.setNotificationType(NotificationType.WARNING);
        tray.showAndDismiss(Duration.seconds(5));
    }

    private Date parseDate(LocalDate date) {
        return date != null ? Date.valueOf(date) : null;
    }

    public User_Profile updateProfile(User_Profile user) {
        int userId = UserSession.getInstance().getUserId();
        String sqlUpdateProfile = "UPDATE user SET adresse = ?, ville = ?, pays = ?, code_postal = ?, date_naissance = ?, bio = ? WHERE id = ?";
        try {
            // Initialiser la requête préparée pour la mise à jour
            db.initPrepar(sqlUpdateProfile);
            PreparedStatement pstmtProfile = db.getPtsm(sqlUpdateProfile);

            // Ajouter les valeurs dans la requête préparée
            pstmtProfile.setString(1, user.getAdresse());
            pstmtProfile.setString(2, user.getVille());
            pstmtProfile.setString(3, user.getPays());
            pstmtProfile.setString(4, user.getCodePostal());
            pstmtProfile.setDate(5, parseDate(user.getDateOfBirth()));
            pstmtProfile.setString(6, user.getBio());
            pstmtProfile.setInt(7, userId); // Condition WHERE avec l'ID de l'utilisateur

            // Exécuter la mise à jour
            pstmtProfile.executeUpdate();
            loadUserProfile();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    @FXML
    void AddProfileAccount(ActionEvent event) {
        String adress = addresstxt.getText().trim();
        String ville = villetxt.getText().trim();
        String pays = paystxt.getText().trim();
        String postal = postaltxt.getText().trim();
        LocalDate birthday = pickerdate.getValue();
        String bio = biotxt.getText().trim();

        // Validation des champs
        if (adress.isEmpty() || ville.isEmpty() || pays.isEmpty() || postal.isEmpty() || birthday == null || bio.isEmpty()) {
            showNotification("Erreur", "Veuillez saisir tous les champs", NotificationType.WARNING);
            return;
        }

        try {
            // Création de l'objet utilisateur
            User_Profile user = new User_Profile(adress, ville, pays, postal, birthday, bio);

            // Appel de la méthode d'ajout
            updateProfile(user);
            loadUserProfile();
            showNotification("Sucess", "your Profile Added Successfully", NotificationType.SUCCESS);



        } catch (Exception e) {
            e.printStackTrace();
            showNotification("Erreur", "Une erreur inattendue s'est produite.", NotificationType.ERROR);
        }
    }


// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
//____________________________________modifier profil ------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------



    @FXML
    void EditProfileAccount(ActionEvent event) {
        String adress = addresstxt.getText().trim();
        String ville = villetxt.getText().trim();
        String pays = paystxt.getText().trim();
        String postal = postaltxt.getText().trim();
        LocalDate birthday = pickerdate.getValue();
        String bio = biotxt.getText().trim();

        // Validation des champs
        if (adress.isEmpty() || ville.isEmpty() || pays.isEmpty() || postal.isEmpty() || birthday == null || bio.isEmpty()) {
            showNotification("Erreur", "Veuillez saisir tous les champs", NotificationType.WARNING);
            return;
        }

        try {
            // Création de l'objet utilisateur
            User_Profile user = new User_Profile(adress, ville, pays, postal, birthday, bio);

            // Appel de la méthode de mise à jour
            updateProfile(user);
            loadUserProfile();

            // Notification de succès
            showNotification("Profil mis à jour", "Les informations du profil ont été mises à jour avec succès.", NotificationType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("Erreur", "Une erreur inattendue s'est produite lors de la mise à jour.", NotificationType.ERROR);
        }
    }





// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------supprimer profil ------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------


    public void clearUserProfileAttributes(int userId) {
        String sqlClearAttributes = "UPDATE user SET adresse = NULL, ville = NULL, pays = NULL, code_postal = NULL, date_naissance = NULL, bio = NULL WHERE id = ?";
        try {
            // Initialiser la requête préparée pour "effacer" les attributs
            db.initPrepar(sqlClearAttributes);
            PreparedStatement pstmt = db.getPtsm(sqlClearAttributes);

            // Fournir l'ID de l'utilisateur pour la condition WHERE
            pstmt.setInt(1, userId);

            // Exécuter la requête
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void DeleteProfileAccount(ActionEvent event) {
        try {
            // Confirmer la suppression
            boolean confirmation = showConfirmationDialog("Confirmation", "Êtes-vous sûr de vouloir supprimer votre profil ?");
            if (!confirmation) {
                return; // Annuler l'action si l'utilisateur refuse
            }

            // Appel de la méthode de suppression
             int userId = UserSession.getInstance().getUserId(); // Récupérer l'ID utilisateur
            clearUserProfileAttributes(userId);

            // Notification de succès
            showNotification("Profil supprimé", "Votre profil a été supprimé avec succès.", NotificationType.INFORMATION);

            // Réinitialiser les champs ou rediriger l'utilisateur
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("Erreur", "Une erreur inattendue s'est produite lors de la suppression.", NotificationType.ERROR);
        }
    }

    // Méthode pour afficher une boîte de dialogue de confirmation
    private boolean showConfirmationDialog(String title, String message) {
        // Implémentez ici une boîte de dialogue pour confirmer l'action
        // Retournez true si l'utilisateur confirme, sinon false
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Méthode pour réinitialiser les champs après suppression
    private void clearFields() {
        addresstxt.clear();
        villetxt.clear();
        paystxt.clear();
        postaltxt.clear();
        pickerdate.setValue(null);
        biotxt.clear();
    }







// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------password reset ------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

    @FXML
    private TextField reelempstxt;  // Mot de passe actuel
    @FXML
    private PasswordField nouvmpstxt;

    User userFirstName=new User();
    User userLastName=new User();

    @FXML
    private TextField emailpasstxt; // Champ de texte pour entrer l'email

    @FXML
    void sendPassword(ActionEvent event) {
        String email = emailpasstxt.getText().trim();


        // Validation de l'email
        if (email.isEmpty()) {
            showNotification("Erreur", "Veuillez entrer un email.", NotificationType.WARNING);
            return;
        }

        if (!isValidEmail(email)) {
            showNotification("Erreur", "Format d'email invalide.", NotificationType.WARNING);
            return;
        }

        try {
            // Récupération du mot de passe depuis la base de données
            String password = getPasswordFromDatabase(email);

            if (password == null) {
                showNotification("Erreur", "Aucun compte trouvé avec cet email.", NotificationType.ERROR);
                return;
            }

            // Envoi de l'email
            sendPasswordEmail(email, userFirstName.getName(), userLastName.getPrenom());            showNotification("Succès", "Mot de passe envoyé à votre email.", NotificationType.SUCCESS);

        } catch (SQLException e) {
            showNotification("Erreur", "Erreur de base de données: " + e.getMessage(), NotificationType.ERROR);
            e.printStackTrace();
        } catch (MessagingException e) {
            showNotification("Erreur", "Erreur d'envoi d'email: " + e.getMessage(), NotificationType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        // Expression régulière simple pour validation d'email
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private String getPasswordFromDatabase(String email) throws SQLException {
        String query = "SELECT password FROM user WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/projet",
                "root",
                "");
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getString("password") : null;
            }
        }
    }

    private void sendPasswordEmail(String recipientEmail, String firstName, String lastName) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("med.amin.chniti@gmail.com", "nddt fvzt mohm eans");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("med.amin.chniti@gmail.com", "Équipe EventMatch"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Confirmation de changement de mot de passe - EventMatch");

            // Corps du message en HTML
            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<style>"
                    + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }"
                    + ".header { background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; }"
                    + ".content { padding: 20px; border: 1px solid #ddd; }"
                    + ".footer { margin-top: 20px; font-size: 0.8em; color: #777; text-align: center; }"
                    + ".button { background-color: #4CAF50; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; display: inline-block; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='header'>"
                    + "<h2>EventMatch 3B3 Esprit</h2>"
                    + "</div>"
                    + "<div class='content'>"
                    + "<p>Bonjour " + firstName + " " + lastName + ",</p>"
                    + "<p>Nous vous confirmons que votre mot de passe a été modifié avec succès.</p>"
                    + "<p>Si vous n'êtes pas à l'origine de cette modification, veuillez nous contacter immédiatement.</p>"
                    + "<p>Pour toute question ou assistance, notre équipe est à votre disposition :</p>"
                    + "<ul>"
                    + "<li><strong>Email :</strong> support@eventmatch.tn</li>"
                    + "<li><strong>Téléphone :</strong> +216 70 250 000</li>"
                    + "<li><strong>Adresse :</strong> Campus ESPRIT, Route de l'aéroport, Tunis</li>"
                    + "</ul>"
                    + "<p>Cordialement,</p>"
                    + "<p><strong>L'équipe EventMatch</strong></p>"
                    + "<p>"
                    + "<a href='https://www.eventmatch.tn' class='button'>Visitez notre site</a>"
                    + "</p>"
                    + "</div>"
                    + "<div class='footer'>"
                    + "<p>© 2023 EventMatch 3B3 Esprit. Tous droits réservés.</p>"
                    + "<p>Cet email a été envoyé automatiquement, merci de ne pas y répondre.</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);

        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Erreur d'encodage de l'email", e);
        }
    }




    @FXML
    void changePassword(ActionEvent event) {
        String email = emailpasstxt.getText().trim();
        String currentPassword = reelempstxt.getText().trim();
        String newPassword = nouvmpstxt.getText().trim();

        // Validation des champs
        if (email.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
            showNotification("Erreur", "Tous les champs doivent être remplis", NotificationType.WARNING);
            return;
        }

        // Validation du nouveau mot de passe
        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            showNotification("Erreur",
                    "Le nouveau mot de passe doit contenir:\n" +
                            "- 8 caractères minimum\n" +
                            "- 1 majuscule\n" +
                            "- 1 minuscule\n" +
                            "- 1 chiffre\n" +
                            "- 1 caractère spécial",
                    NotificationType.ERROR);
            return;
        }

        try {
            // 1. Récupérer le mot de passe haché et les infos utilisateur
            Map<String, String> userCredentials = getUserCredentials(email);

            if (userCredentials == null) {
                showNotification("Erreur", "Utilisateur non trouvé", NotificationType.ERROR);
                return;
            }

            String storedHashedPassword = userCredentials.get("password");
            String userNom = userCredentials.get("nom");
            String userPrenom = userCredentials.get("prenom");

            // 2. Vérifier le mot de passe actuel
            if (!BCrypt.checkpw(currentPassword, storedHashedPassword)) {
                showNotification("Erreur", "Mot de passe actuel incorrect", NotificationType.ERROR);
                return;
            }

            // 3. Vérifier si le nouveau mot de passe est différent de l'ancien
            if (BCrypt.checkpw(newPassword, storedHashedPassword)) {
                showNotification("Erreur", "Le nouveau mot de passe doit être différent de l'actuel", NotificationType.ERROR);
                return;
            }

            // 4. Hacher et mettre à jour le mot de passe
            String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            if (updatePasswordInDatabase(email, newHashedPassword)) {
                showNotification("Succès", "Mot de passe changé avec succès", NotificationType.SUCCESS);

                // Envoi de l'email de confirmation
                sendPasswordChangedEmail(email, userNom, userPrenom);
            } else {
                showNotification("Erreur", "Échec de la mise à jour du mot de passe", NotificationType.ERROR);
            }

        } catch (SQLException e) {
            showNotification("Erreur", "Problème de base de données: " + e.getMessage(), NotificationType.ERROR);
            e.printStackTrace();
        } catch (MessagingException e) {
            showNotification("Erreur", "Erreur d'envoi d'email: " + e.getMessage(), NotificationType.ERROR);
            e.printStackTrace();
        } finally {
            ClosePassword_Forget();
        }
    }

    private Map<String, String> getUserCredentials(String email) throws SQLException {
        String query = "SELECT password, nom, prenom FROM user WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, String> credentials = new HashMap<>();
                credentials.put("password", rs.getString("password"));
                credentials.put("nom", rs.getString("nom"));
                credentials.put("prenom", rs.getString("prenom"));
                return credentials;
            }
            return null;
        }
    }

    // Méthode pour récupérer le mot de passe haché
    private String getHashedPasswordFromDatabase(String email) throws SQLException {
        String query = "SELECT password FROM user WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            return rs.next() ? rs.getString("password") : null;
        }
    }

    // Méthode pour mettre à jour le mot de passe (déjà haché)
    private boolean updatePasswordInDatabase(String email, String hashedPassword) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, hashedPassword);
            pst.setString(2, email);
            return pst.executeUpdate() > 0;
        }
    }










    @FXML
    private void sendPasswordcode(ActionEvent event) {
        String email = emailpasstxt.getText().trim();

        if (email.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un email.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erreur", "Format d'email invalide.");
            return;
        }

        try {
            Map<String, String> userInfo = getUserInfoByEmail(email);

            if (userInfo == null) {
                showAlert("Erreur", "Aucun compte trouvé avec cet email.");
                return;
            }

            String verificationCode = generateVerificationCode();
            saveVerificationCode(email, verificationCode);
            sendVerificationEmail(email, userInfo.get("nom"), userInfo.get("prenom"), verificationCode);
            showAlert("Succès", "Code de vérification envoyé à votre email.");

        } catch (SQLException e) {
            showAlert("Erreur DB", "Erreur base de données: " + e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            showAlert("Erreur Email", "Erreur d'envoi d'email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Map<String, String> getUserInfoByEmail(String email) throws SQLException {
        String query = "SELECT nom, prenom FROM user WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("nom", rs.getString("nom"));
                userInfo.put("prenom", rs.getString("prenom"));
                return userInfo;
            }
            return null;
        }
    }



    @FXML
    private void changePasswordcode(ActionEvent event) {
        String email = emailpasstxt.getText().trim();
        String verificationCode = reelempstxt.getText().trim();
        String newPassword = nouvmpstxt.getText().trim();

        if (email.isEmpty() || verificationCode.isEmpty() || newPassword.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires");
            return;
        }

        // Validation du mot de passe
        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            showAlert("Erreur",
                    "Le mot de passe doit contenir:\n" +
                            "- 8 caractères minimum\n" +
                            "- 1 majuscule\n" +
                            "- 1 minuscule\n" +
                            "- 1 chiffre\n" +
                            "- 1 caractère spécial");
            return;
        }

        try {
            // Vérification du code
            String storedCode = getVerificationCodeFromDatabase(email);
            if (storedCode == null || !storedCode.equals(verificationCode)) {
                showAlert("Erreur", "Code de vérification incorrect");
                return;
            }

            // Récupération des infos utilisateur
            Map<String, String> userInfo = getUserInfoByEmail(email);
            if (userInfo == null) {
                showAlert("Erreur", "Utilisateur non trouvé");
                return;
            }

            // Hachage et mise à jour du mot de passe
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            if (updatePasswordInDatabase(email, hashedPassword)) {
                clearVerificationCode(email);
                sendPasswordChangedEmail(email, userInfo.get("nom"), userInfo.get("prenom"));
                showAlert("Succès", "Mot de passe changé avec succès");
            } else {
                showAlert("Erreur", "Échec de la mise à jour");
            }

        } catch (SQLException e) {
            showAlert("Erreur DB", "Erreur base de données: " + e.getMessage());
            e.printStackTrace();
        } catch (MessagingException e) {
            showAlert("Erreur Email", "Erreur d'envoi de confirmation: " + e.getMessage());
            e.printStackTrace();
        }
        resetFields();
    }

        private String generateVerificationCode() {
            return String.valueOf(100000 + new Random().nextInt(900000));
        }

        private void saveVerificationCode(String email, String code) throws SQLException {
            String query = "UPDATE user SET code = ? WHERE email = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, code);
                stmt.setString(2, email);
                stmt.executeUpdate();
            }
        }

        private String getVerificationCodeFromDatabase(String email) throws SQLException {
            String query = "SELECT code FROM user WHERE email = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                return rs.next() ? rs.getString("code") : null;
            }
        }

        private void clearVerificationCode(String email) throws SQLException {
            String query = "UPDATE user SET code = NULL WHERE email = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                stmt.executeUpdate();
            }
        }

        private Connection getConnection() throws SQLException {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/projet",
                    "root",
                    "");
        }
    private void sendVerificationEmail(String to, String firstName, String lastName, String code) throws MessagingException {
        String subject = "Votre code de vérification EventMatch";
        String htmlContent = buildVerificationEmailHtml(firstName, lastName, code);
        sendHtmlEmail(to, subject, htmlContent);
    }

    private void sendPasswordChangedEmail(String to, String firstName, String lastName) throws MessagingException {
        String subject = "Confirmation de changement de mot de passe";
        String htmlContent = buildPasswordChangedEmailHtml(firstName, lastName);
        sendHtmlEmail(to, subject, htmlContent);
    }

    private String buildVerificationEmailHtml(String firstName, String lastName, String code) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #444; max-width: 600px; margin: 0 auto; padding: 0; }"
                + ".container { border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; }"
                + ".header { background-color: #4a6fa5; color: white; padding: 20px; text-align: center; }"
                + ".content { padding: 25px; background-color: #f9f9f9; }"
                + ".code { font-size: 24px; letter-spacing: 3px; color: #4a6fa5; font-weight: bold; margin: 20px 0; text-align: center; }"
                + ".footer { padding: 15px; text-align: center; font-size: 12px; color: #777; background-color: #f0f0f0; }"
                + ".button { display: inline-block; padding: 10px 20px; background-color: #4a6fa5; color: white; text-decoration: none; border-radius: 4px; margin: 10px 0; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h2>EventMatch 3B3 Esprit</h2>"
                + "</div>"
                + "<div class='content'>"
                + "<p>Bonjour " + firstName + " " + lastName + ",</p>"
                + "<p>Voici votre code de vérification pour accéder à votre compte EventMatch :</p>"
                + "<div class='code'>" + code + "</div>"
                + "<p>Ce code est valable pendant <strong>10 minutes</strong>.</p>"
                + "<p>Si vous n'avez pas demandé ce code, veuillez ignorer cet email ou nous contacter.</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>© 2023 EventMatch | Campus ESPRIT, Route de l'aéroport, Tunis</p>"
                + "<p>Contact : support@eventmatch.tn | +216 70 250 000</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    private String buildPasswordChangedEmailHtml(String firstName, String lastName) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #444; max-width: 600px; margin: 0 auto; padding: 0; }"
                + ".container { border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; }"
                + ".header { background-color: #4a6fa5; color: white; padding: 20px; text-align: center; }"
                + ".content { padding: 25px; background-color: #f9f9f9; }"
                + ".footer { padding: 15px; text-align: center; font-size: 12px; color: #777; background-color: #f0f0f0; }"
                + ".button { display: inline-block; padding: 10px 20px; background-color: #4a6fa5; color: white; text-decoration: none; border-radius: 4px; margin: 10px 0; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h2>EventMatch 3B3 Esprit</h2>"
                + "</div>"
                + "<div class='content'>"
                + "<p>Bonjour " + firstName + " " + lastName + ",</p>"
                + "<p>Nous vous confirmons que le mot de passe de votre compte EventMatch a été modifié avec succès.</p>"
                + "<p>Si vous n'êtes pas à l'origine de cette modification, veuillez nous contacter immédiatement.</p>"
                + "<p><a href='mailto:support@eventmatch.tn' class='button'>Contactez le support</a></p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>© 2023 EventMatch | Campus ESPRIT, Route de l'aéroport, Tunis</p>"
                + "<p>Tél: +216 70 250 000 | Email: contact@eventmatch.tn</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("med.amin.chniti@gmail.com", "nddt fvzt mohm eans");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("med.amin.chniti@gmail.com", "Équipe EventMatch"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Erreur d'encodage de l'email", e);
        }
    }

        private void showAlert(String title, String message) {
            // À adapter avec votre système de notification
            System.out.println(title + " - " + message);
        }

        private void resetFields() {
            emailpasstxt.clear();
            reelempstxt.clear();
            nouvmpstxt.clear();
        }






// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------SLIDE MOVE POUR FORGET PASSWORD ------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------



    @FXML
    private Hyperlink forgettxt1;

    @FXML
    private Hyperlink forgettxt;
    @FXML
    private Button Sendtxt;

    @FXML
    private AnchorPane ForgetSlider;


    public void OpenPassword_Forget() {
        // Animation pour ouvrir le slider
        if (forgettxt != null) {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(ForgetSlider);
            slide.setToX(0); // Position finale du slider
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                forgettxt.setVisible(false); // Masquer le texte "create"
                Sendtxt.setVisible(true); // Afficher le texte "signup"
            });
        }

    }
    public void closeHyperPassword_Forget() {
        // Animation pour ouvrir le slider
        if (forgettxt1 != null) {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(ForgetSlider);
            slide.setToX(500); // Revenir à la position initiale
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                Sendtxt.setVisible(true); // Masquer le texte "signup"
                forgettxt1.setVisible(false); // Afficher le texte "create"
            });
        }

    }

    public void ClosePassword_Forget() {
        // Animation pour fermer le slider
        if (Sendtxt != null) {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(ForgetSlider);
            slide.setToX(500); // Revenir à la position initiale
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                Sendtxt.setVisible(true); // Masquer le texte "signup"
                forgettxt.setVisible(false); // Afficher le texte "create"
            });
        }
    }



// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------ADMIN USER TABLE INTERFACE ------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

    @FXML
    private TextField nomSettingTxt;

    @FXML
    private TextField prenomSettingTxt;

    @FXML
    private TextField emailSettingTxt;

    @FXML
    private TextField telSettingTxt;

    public void UpdateProfileSettings(User user) {
        int userId = UserSession.getInstance().getUserId(); // Récupérer l'ID de l'utilisateur
        String sqlUpdate = "UPDATE user SET nom = ?, prenom = ?, email = ?, tel = ? WHERE id = ?";

        try {
            db.initPrepar(sqlUpdate);
            PreparedStatement pstmtUpdate = db.getPtsm(sqlUpdate);

            pstmtUpdate.setString(1, user.getNom());
            pstmtUpdate.setString(2, user.getPrenom());
            pstmtUpdate.setString(3, user.getEmail());
            pstmtUpdate.setString(4, user.getTel());
            pstmtUpdate.setInt(5, userId);

            int rowsAffected = pstmtUpdate.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Profil utilisateur mis à jour avec succès.");
            } else {
                System.out.println("Aucun profil trouvé pour cet utilisateur.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveProfile(ActionEvent event) {
        String nom = nomSettingTxt.getText().trim();
        String prenom = prenomSettingTxt.getText().trim();
        String email = emailSettingTxt.getText().trim();
        String tel = telSettingTxt.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || tel.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Vérifier si le numéro de téléphone est valide
            int phone = Integer.parseInt(tel);

            // Crée un objet User avec les nouvelles données
            User user = new User(nom, prenom, email, phone);

            // Appeler la méthode de mise à jour
            UpdateProfileSettings(user);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir uniquement des chiffres.");
        }
    }

    @FXML
    void cancelEdit(ActionEvent event) {
        nomSettingTxt.clear();
        prenomSettingTxt.clear();
        emailSettingTxt.clear();
        telSettingTxt.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
User se = new User();
    private void loadUserData() {
        int userId = UserSession.getInstance().getUserId(); // Récupérer l'ID de l'utilisateur
        String sqlQuery = "SELECT nom, prenom, email, tel FROM user WHERE id = ?";

        try {
            db.initPrepar(sqlQuery);
            PreparedStatement pstmt = db.getPtsm(sqlQuery);
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Charger les données utilisateur dans les champs
                nomSettingTxt.setText(rs.getString("nom"));
                prenomSettingTxt.setText(rs.getString("prenom"));
                emailSettingTxt.setText(rs.getString("email"));
                telSettingTxt.setText(rs.getString("tel"));
            } else {
                System.out.println("Aucun utilisateur trouvé pour cet ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------admin code promo------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------------------------------


    @FXML
    private TextField codePromoTxt;

    @FXML
    private void verifierPromo() {
        String code = codePromoTxt.getText().trim();
        int userId = UserSession.getInstance().getUserId();
        LocalDate currentDate = LocalDate.now();

        if (code.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un code promo", Alert.AlertType.ERROR);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet", "root", "")) {
            conn.setAutoCommit(false); // Désactive l'autocommit pour la transaction

            // 1. Vérifier si l'utilisateur a déjà utilisé un code
            String checkUserQuery = "SELECT promo, code_promo_id, date_utilisation FROM user WHERE id = ? FOR UPDATE";
            try (PreparedStatement userStmt = conn.prepareStatement(checkUserQuery)) {
                userStmt.setInt(1, userId);
                ResultSet userRs = userStmt.executeQuery();

                if (userRs.next() && userRs.getBoolean("promo")) {
                    int usedCodeId = userRs.getInt("code_promo_id");
                    Date usageDate = userRs.getDate("date_utilisation");

                    String message = String.format("Vous avez déjà utilisé un code promo le %s!",
                            usageDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    showAlert("Déjà utilisé", message, Alert.AlertType.WARNING);
                    conn.rollback();
                    return;
                }
            }

            // 2. Vérifier la validité du code
            String checkCodeQuery = "SELECT id, reduction, nom_produit FROM code_promo WHERE code = ? "
                    + "AND ? BETWEEN date_debut AND date_fin "
                    + "FOR UPDATE";
            int codePromoId = -1;
            double reduction = 0;
            String nomProduit = "";

            try (PreparedStatement codeStmt = conn.prepareStatement(checkCodeQuery)) {
                codeStmt.setString(1, code);
                codeStmt.setDate(2, Date.valueOf(currentDate));
                ResultSet codeRs = codeStmt.executeQuery();

                if (!codeRs.next()) {
                    showAlert("Code invalide", "Code expiré ou non valide", Alert.AlertType.ERROR);
                    conn.rollback();
                    return;
                }

                codePromoId = codeRs.getInt("id");
                reduction = codeRs.getDouble("reduction");
                nomProduit = codeRs.getString("nom_produit");
            }

            // 3. Mettre à jour l'utilisateur
            String updateQuery = "UPDATE user SET promo = TRUE, code_promo_id = ?, date_utilisation = ? WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, codePromoId);
                updateStmt.setDate(2, Date.valueOf(currentDate));
                updateStmt.setInt(3, userId);

                int updated = updateStmt.executeUpdate();

                if (updated == 1) {
                    conn.commit(); // Valide la transaction
                    String successMessage = String.format(
                            "Code promo appliqué avec succès!\n" +
                                    "Produit: %s\n" +
                                    "Réduction: %.2f%%\n" +
                                    "Date d'utilisation: %s\n" +
                                    "Vous ne pourrez pas utiliser d'autre code.",
                            nomProduit, reduction,
                            currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                    showAlert("Succès", successMessage, Alert.AlertType.INFORMATION);
                } else {
                    conn.rollback();
                    showAlert("Erreur", "Échec de la mise à jour", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur BD", "Erreur technique: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }








}
















