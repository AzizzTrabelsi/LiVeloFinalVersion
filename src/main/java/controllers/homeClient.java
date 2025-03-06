package controllers;

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
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCategories();
    }
    @FXML
    private ComboBox<String> combo;
    @FXML
    private AnchorPane anLogout;

    @FXML
    private ImageView history;


    @FXML
    private ImageView imLogo;

    @FXML
    void logout(MouseEvent event) {

    }
    private void loadCategories() {
        List<String> categoryNames = su.getAllCategoryNames();  // Méthode pour récupérer les noms des catégories
        combo.getItems().clear();
        combo.getItems().addAll(categoryNames);  // Ajouter les noms des catégories au ComboBox
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

    @FXML
    private void navigerVersHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LivraisonClientt.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) history.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}