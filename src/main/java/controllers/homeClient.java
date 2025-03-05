package controllers;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.File;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;

import javafx.stage.Stage;

import net.minidev.json.JSONObject;
import org.w3c.dom.Text;
import services.Authentification;
import services.CrudCategorie;

import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import controllers.MarketClient;  // Mets le bon package si nécessaire

import javafx.scene.Parent;

import java.util.List;
public class homeClient implements Initializable  {
    private CrudCategorie su = new CrudCategorie();
    @FXML
    private Text logout;
    @FXML
    private ImageView logoutlogo;
    @FXML
    private ImageView imLogo;
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCategories();
        String nomComplet = afficherNomCompletUtilisateur();

        // Vérifier si le nom est vide avant d'afficher
        if (!nomComplet.isEmpty()) {
            nomUtilisateurLabel.setText(nomComplet);
        } else {
            nomUtilisateurLabel.setText("Utilisateur inconnu");
        }
        String videoPath = getClass().getResource("/image/liveloo.mp4").toExternalForm();
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);

        // Connecte le MediaPlayer au MediaView pour afficher la vidéo
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
    }
    @FXML
    private void logout(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene signInScene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(signInScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading login.fxml.");
        }
    }

    @FXML
    public void playVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.play(); // Démarre la lecture de la vidéo
        }
    }
    @FXML
    public void pauseVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.pause(); // Met la vidéo en pause
        }
    }

    // Méthode pour arrêter la vidéo
    @FXML
    public void stopVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Arrête la lecture de la vidéo
        }
    }

    @FXML
    private ComboBox<String> combo;
    @FXML
    private AnchorPane anLogout;

    @FXML
    private ImageView imBills;

    private void loadCategories() {
        List<String> categoryNames = su.getAllCategoryNames();  // Méthode pour récupérer les noms des catégories
        combo.getItems().clear();
        combo.getItems().addAll(categoryNames);  // Ajouter les noms des catégories au ComboBox
    }

    @FXML
    void navigateToBillsClient(MouseEvent event) {
        try {
            // Load the ClientBills.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientBills.fxml"));
            Scene billsClientScene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) imLogo.getScene().getWindow();
            stage.setScene(billsClientScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading ClientBills.fxml.");
        }
    }


    @FXML
    private void handleCategorySelection(ActionEvent event) {
        String selectedCategory = combo.getSelectionModel().getSelectedItem();  // Récupérer la catégorie sélectionnée

        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            int categoryId = su.getCategoryIdByName(selectedCategory);  // Récupérer l'ID de la catégorie

            if (categoryId != -1) {
                openMarketClient(categoryId);  // Ouvrir MarketClient avec l'ID de la catégorie
            } else {
                showAlert("Erreur", "La catégorie sélectionnée est invalide.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une catégorie.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private Label nomUtilisateurLabel;
    @FXML
    private MediaView mediaView;
    private MediaPlayer mediaPlayer;

    // Méthode pour récupérer le nom et le prénom de l'utilisateur connecté
    public String afficherNomCompletUtilisateur() {
        String token = Authentification.getToken();

        if (token != null) {
            JSONObject userInfo = new Authentification().decodeToken(token);
            if (userInfo != null) {
                String nom = (String) userInfo.get("nom");  // Récupérer le nom
                String prenom = (String) userInfo.get("prenom");  // Récupérer le prénom
                return nom + " " + prenom;  // Retourner le nom complet
            } else {
                System.out.println("Erreur dans le décodage du token.");
                return "";
            }
        } else {
            System.out.println("Aucun token trouvé. L'utilisateur n'est pas connecté.");
            return "";
        }
    }

    private void openMarketClient(int categoryId) {
        System.out.println("Ouverture de MarketClient avec ID: " + categoryId); // Ajout ici pour vérifier l'ID

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MarketClient.fxml"));
            Parent root = loader.load();

            // Passer l'ID de la catégorie au contrôleur MarketClient
            MarketClient controller = loader.getController();
            controller.setCategoryId(categoryId);  // Assurez-vous que cette méthode existe dans MarketClient.java

            // Ouvrir la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Marché Client - Articles");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page du marché.", Alert.AlertType.ERROR);
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}