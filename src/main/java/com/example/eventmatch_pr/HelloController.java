package com.example.eventmatch_pr;
import com.example.eventmatch_pr.DB.DB;
import com.example.eventmatch_pr.entity.Role;
import com.example.eventmatch_pr.entity.User;
import com.example.eventmatch_pr.entity.UserSession;
import com.example.eventmatch_pr.entity.User_Profile;
import com.example.eventmatch_pr.implementation.IUser;
import com.example.eventmatch_pr.implementation.impUser;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;


import javafx.scene.control.Button;








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
    private TextField passwordInscrit;


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
    private IUser ProfilUser = new impUser();


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


    @FXML
    void getInscrit(ActionEvent event) {
        String name = nameInscrit.getText();
        String prenom = prenomInscrit.getText();
        String email = emailInscrit.getText();
        String password = passwordInscrit.getText();
        Role role = comboRole.getValue(); // Récupère la valeur sélectionnée dans la ComboBox
        String phone = phoneInscrit.getText();

        // Validation des champs
        if (email.isEmpty() || name.isEmpty() || prenom.isEmpty() || password.isEmpty() || phone.isEmpty() || role == null) {
            showNotification("Erreur", "Veuillez saisir tous les champs", NotificationType.ERROR);
        } else {
            try {
                // Création de l'objet utilisateur
                User user = new User(name, prenom, email, password, role, Integer.parseInt(phone));
                userdao.addUser(user); // Appel de la méthode d'ajout
                showNotification("Utilisateur ajouté", "Succès", NotificationType.SUCCESS);
                CloseSlider();

            } catch (NumberFormatException e) {
                showNotification("Erreur", "Le numéro de téléphone doit être un entier valide", NotificationType.ERROR);
            }
        }

        // Fermer le slider après l'ajout
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

    @FXML
    void initialize() {
        if (bar != null) {
            bar.setTranslateX(-176);
            MenuClose.setVisible(false);
        }
        if (ProfileBar != null) {
            ProfileBar.setTranslateX(700);
            ProfileClose.setVisible(false);
        }


        if (slider == null) {

            System.err.println("Erreur : Le composant 'slider' n'a pas été injecté.");
        } else {


            // Positionner le slider initialement hors de l'écran
            slider.setTranslateX(-500); // Initialisation du slider à -500
            createtxt.setVisible(true); // Texte par défaut visible
            signuptxt.setVisible(false); // Masquer l'autre texte
            // Initialiser les rôles dans la ComboBox
            comboRole.getItems().setAll(Role.values()); // Ajoute toutes les valeurs de l'énumération Role


            try {
                // Vérification des éléments avant l'application des effets
                if (eventtxt != null) {
                    // Création de la méthode pour générer une animation fade réutilisable

                    Node[] nodes = {eventtxt, event1txt, event2txt,};

                    for (Node node : nodes) {
                        FadeTransition fade = createFade(node);
                        TranslateTransition slide = createSlide(node);

                        // Lancer les deux ensemble
                        ParallelTransition animation = new ParallelTransition(fade, slide);
                        animation.play();
                    }
// Dans initialize()
                    FadeTransition fade3 = createFade(motivatriontxt);

                    FadeTransition fade4 = createFade(emailtxt);
                    FadeTransition fade5 = createFade(passwordtxt);
                    FadeTransition fade6 = createFade(loginBtn);

// Lancer les animations
                    fade3.play();

                    fade4.play();
                    fade5.play();
                    fade6.play();


                    DropShadow loginShadow = new DropShadow(10, Color.valueOf("#4fa3f1"));
                    DropShadow whiteGlow = new DropShadow();
                    whiteGlow.setColor(Color.rgb(255, 255, 255, 0.7)); // Blanc avec 70% d'opacité
                    whiteGlow.setRadius(30);                           // Taille de la lueur
                    whiteGlow.setSpread(0.6);                          // Intensité (0-1)
                    whiteGlow.setOffsetX(0);                           // Pas de décalage horizontal
                    whiteGlow.setOffsetY(0);

                    DropShadow whiteGlow2 = new DropShadow();
                    whiteGlow2.setColor(Color.rgb(233, 223, 123, 0.1)); // Blanc avec 70% d'opacité
                    whiteGlow2.setRadius(5);                           // Taille de la lueur
                    whiteGlow2.setSpread(0.1);                          // Intensité (0-1)
                    whiteGlow2.setOffsetX(19);                           // Pas de décalage horizontal
                    whiteGlow2.setOffsetY(17);                           // Pas de décalage vertical

                    // Application des effets aux éléments
                    eventtxt.setEffect(whiteGlow);
                    event1txt.setEffect(whiteGlow2);
                    event2txt.setEffect(whiteGlow2);
                    loginBtn.setEffect(loginShadow);
                    signtxt.setEffect(whiteGlow);


                    // Initialisation des autres éléments
                } else {
                    System.err.println("Warning: eventtxt n'est pas injecté");
                }

            } catch (Exception e) {
                System.err.println("Erreur d'initialisation: " + e.getMessage());
                e.printStackTrace();
            }
        }

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

        String loginQuery = "SELECT id FROM user WHERE email = ? AND password = ?";


        String email = emailtxt.getText().trim();
        String password = passwordtxt.getText().trim();


        if (!validateInputs(email, password)) {
            return;
        }

        try {
            User user = userDao.getConnection(email, password);
            handleLoginResult(user, email, event);

            db.initPrepar(loginQuery);
            PreparedStatement pstmtUser = db.getPtsm(loginQuery);

            pstmtUser.setString(1, email);
            pstmtUser.setString(2, password);

            ResultSet rs = pstmtUser.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

                // Stocker l'ID utilisateur (via une méthode ou un gestionnaire de session)
                UserSession.getInstance().setUserId(userId);
            }


        } catch (Exception e) {
            handleLoginError(e);
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

    private static final String DB_URL = "jdbc:mysql://localhost:3306/user";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private void loadUserProfile() {
        int connectedUserId = UserSession.getInstance().getUserId();

        String query = "SELECT * FROM user_profile WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, connectedUserId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Remplir les champs avec les données de la base de données
                addresstxt.setText(rs.getString("adresse"));
                villetxt.setText(rs.getString("ville"));
                paystxt.setText(rs.getString("pays"));
                postaltxt.setText(rs.getString("code_postal"));
                pickerdate.setValue(rs.getDate("date_naissance").toLocalDate());
                biotxt.setText(rs.getString("bio"));
            } else {
                // Notification si aucune donnée n'existe pour le profil
                notifyNoProfileData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void notifyNoProfileData() {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Profil incomplet");
        tray.setMessage("Veuillez remplir les informations de votre profil.");
        tray.setNotificationType(NotificationType.WARNING);
        tray.showAndDismiss(javafx.util.Duration.seconds(5));
    }

    private java.sql.Date parseDate(LocalDate date) {
        return date != null ? java.sql.Date.valueOf(date) : null;
    }

    public User_Profile InsertProfile(User_Profile user) {
        int Userid = UserSession.getInstance().getUserId();
        String sqlprofile = "INSERT INTO user_profile (user_id,adresse,ville,pays,code_postal,date_naissance,bio) VALUES (?,?,?,?,?,?,?)";
        try {
            // Ajouter l'utilisateur à la table `user`
            db.initPrepar(sqlprofile);
            PreparedStatement pstmtProfile = db.getPtsm(sqlprofile);
            pstmtProfile.setInt(1, Userid);
            pstmtProfile.setString(2, user.getAdresse());
            pstmtProfile.setString(3, user.getVille());
            pstmtProfile.setString(4, user.getPays());
            pstmtProfile.setString(5, user.getCodePostal());
            pstmtProfile.setDate(6, parseDate(user.getDateOfBirth()));
            pstmtProfile.setString(7, user.getBio());


            pstmtProfile.executeUpdate();

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
            InsertProfile(user);


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

    public void UpdateProfile(User_Profile user) {
        int userId = UserSession.getInstance().getUserId();
        String sqlUpdate = "UPDATE user_profile SET adresse = ?, ville = ?, pays = ?, code_postal = ?, date_naissance = ?, bio = ? WHERE user_id = ?";

        try {
            db.initPrepar(sqlUpdate);
            PreparedStatement pstmtUpdate = db.getPtsm(sqlUpdate);

            pstmtUpdate.setString(1, user.getAdresse());
            pstmtUpdate.setString(2, user.getVille());
            pstmtUpdate.setString(3, user.getPays());
            pstmtUpdate.setString(4, user.getCodePostal());
            pstmtUpdate.setDate(5, parseDate(user.getDateOfBirth()));
            pstmtUpdate.setString(6, user.getBio());
            pstmtUpdate.setInt(7, userId);

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
            UpdateProfile(user);

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


    public void DeleteProfile() {
        int userId = UserSession.getInstance().getUserId();
        String sqlDelete = "DELETE FROM user_profile WHERE user_id = ?";

        try {
            db.initPrepar(sqlDelete);
            PreparedStatement pstmtDelete = db.getPtsm(sqlDelete);

            pstmtDelete.setInt(1, userId);

            int rowsAffected = pstmtDelete.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Profil utilisateur supprimé avec succès.");
            } else {
                System.out.println("Aucun profil trouvé pour cet utilisateur.");
            }
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
            DeleteProfile();

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

}