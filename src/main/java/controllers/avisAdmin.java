package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Avis;
import models.Categorie;
import models.User;
import services.CrudAvis;
import services.CrudUser;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class avisAdmin implements Initializable {
    @FXML
    private HBox hbHedha;
    @FXML
    private TextField anSearch;
    @FXML
    private VBox vListUsers;
    @FXML
    private ImageView imLogo;

    @FXML
    void hoverEffect(MouseEvent event) {

    }

    @FXML
    void normalEffect(MouseEvent event) {

    }


    private CrudUser crudUser = new CrudUser();
    private CrudAvis crudAvis = new CrudAvis();
    private List<Avis> avisList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Avis> avisList = crudAvis.getAll();
        this.avisList = avisList;
        Show();
        anSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // Si le champ de recherche est vide, afficher tous les utilisateurs
                loadCategory();
            } else {
                // Sinon, effectuer une recherche
                searchAvis(newValue);
            }
        });
    }

    @FXML
    void Show() {

        hbHedha.getChildren().clear();
        // vListUsers.getChildren().clear(); // Nettoie la liste avant d'ajouter les nouveaux éléments


        for (Avis avis : avisList) {
            HBox catRow = new HBox(4);
            catRow.setPrefHeight(32.0);
            catRow.setPrefWidth(200.0); // Ajustement de la largeur pour deux champs seulement
            User user = crudUser.getById(avis.getCreatedBy());
            Label lbnom = new Label(user.getNom() + " " + user.getPrenom());
            lbnom.setMinWidth(150);
            lbnom.setMaxWidth(80);

            Label lbdate = new Label(avis.getCreatedAt().toString());
            lbdate.setMinWidth(250);
            lbdate.setMaxWidth(80);

            Label lbldesc = new Label(avis.getDescription());
            lbldesc.setMinWidth(250); // Définit la largeur de l'image
            lbldesc.setMaxWidth(80);// Définit la hauteur de l'image


            catRow.getChildren().addAll(lbnom, lbdate, lbldesc);

            catRow.setOnMouseClicked(event -> showAvisDetailsPopup((avis)));

            vListUsers.getChildren().add(catRow);
        }
    }

    @FXML
    private void showAvisDetailsPopup(Avis avis) {
        System.out.println("Avis sélectionné : " + avis.getIdAvis() + " " + avis.getDescription());
        User user = crudUser.getById(avis.getCreatedBy());

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Détails des avis ");
        alert.setContentText("id avis : " + avis.getIdAvis() + "\n" +
                "created by " + user.getNom()+" "+user.getPrenom() + "\n" +
                "livraison id: " + avis.getLivraison().getIdLivraison() + "\n" +
                "created at : " + avis.getCreatedAt() + "\n" +
                "Description : " + avis.getDescription() + "\n");


        ButtonType deleteButton = new ButtonType("Supprimer");

        alert.getButtonTypes().setAll(deleteButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == deleteButton) {
                // Afficher une pop-up de confirmation pour la suppression
                javafx.scene.control.Alert confirmationAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation de la suppression");
                confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette Avis?");
                confirmationAlert.setContentText("Cette action est irréversible.");

                // Ajouter les boutons de confirmation
                ButtonType yesButton = new ButtonType("Oui");
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                confirmationAlert.showAndWait().ifPresent(confirmationResponse -> {
                    if (confirmationResponse == yesButton) {
                        // Effectuer la suppression
                        crudAvis.delete(avis.getIdAvis());
                        System.out.println("avis supprimée.");
                        loadCategory();
                    } else {
                        System.out.println("Suppression annulée.");
                    }
                });
            }
        });


    }

    private void loadCategory() {
        System.out.println("Chargement des avis...");

        // Nettoyer la liste actuelle des utilisateurs
        vListUsers.getChildren().clear();

        // Création de l'en-tête
        HBox headerRow = new HBox(10);
        headerRow.setPrefHeight(32.0);
        headerRow.setPrefWidth(765.0);
        headerRow.setStyle("-fx-background-color: #398c3e; -fx-padding: 10px;");

        Label lblHeaderNom = new Label("created by");
        lblHeaderNom.setMinWidth(150);
        lblHeaderNom.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label lblHeaderDesc = new Label("created at");
        lblHeaderDesc.setMinWidth(300);
        lblHeaderDesc.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label lblHeaderImg = new Label("description");
        lblHeaderImg.setMinWidth(300);
        lblHeaderImg.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");


        headerRow.getChildren().addAll(lblHeaderNom, lblHeaderDesc, lblHeaderImg);
        vListUsers.getChildren().add(headerRow);

        // Récupération des catégories
        List<Avis> avisList = crudAvis.getAll();

        for (Avis avis : avisList) {
            HBox catRow = new HBox(10);
            catRow.setPrefHeight(32.0);
            catRow.setPrefWidth(765.0);
            catRow.setStyle("-fx-padding: 5px; -fx-border-color: lightgray;");

            User user = crudUser.getById(avis.getCreatedBy());
            Label lbnom = new Label(user.getNom() + " " + user.getPrenom());
            lbnom.setMinWidth(150);

            Label lbdate = new Label(avis.getCreatedAt().toString());
            lbdate.setMinWidth(300);

            Label lbldesc = new Label(avis.getDescription());
            lbldesc.setMinWidth(300);
            catRow.getChildren().addAll(lbnom, lbdate, lbldesc);
            Button deleteButton = new Button("Supprimer");
            deleteButton.setOnAction(event -> {
                javafx.scene.control.Alert confirmationAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation de la suppression");
                confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette avis ?");
                confirmationAlert.setContentText("Cette action est irréversible.");

                ButtonType yesButton = new ButtonType("Oui");
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                confirmationAlert.showAndWait().ifPresent(confirmationResponse -> {
                    if (confirmationResponse == yesButton) {
                        // Effectuer la suppression
                        crudAvis.delete(avis.getIdAvis());
                        System.out.println("Avis supprimé.");
                        loadCategory();
                    } else {
                        System.out.println("Suppression annulée.");
                    }
                });
            });

            catRow.getChildren().add(deleteButton);

            catRow.setOnMouseClicked(event -> showAvisDetailsPopup(avis));

            vListUsers.getChildren().add(catRow);
        }

    }

    private void searchAvis(String createdAtCriteria) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date searchDate = sdf.parse(createdAtCriteria); // Conversion String -> Date


            System.out.println("Recherche des avis pour la date : " + searchDate);

            vListUsers.getChildren().clear(); // Effacer les anciens résultats

            // Création de l'en-tête des colonnes
            HBox headerRow = new HBox(10);
            headerRow.setPrefHeight(32.0);
            headerRow.setPrefWidth(765.0);
            headerRow.setStyle("-fx-background-color: #398c3e; -fx-padding: 10px;");

            Label lblHeaderNom = new Label("created by");
            lblHeaderNom.setMinWidth(150);
            lblHeaderNom.setMaxWidth(150);
            lblHeaderNom.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            /*Label lblHeaderCreatedBy = new Label("Créé par");
            lblHeaderCreatedBy.setMinWidth(80);
            lblHeaderCreatedBy.setMaxWidth(80);
            lblHeaderCreatedBy.setStyle("-fx-text-fill: black;");*/

            Label lbdate = new Label("created at");
            lbdate.setMinWidth(100);
            lbdate.setMaxWidth(100);
            lbdate.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            Label lblHeaderDesc = new Label("Description ");
            lblHeaderDesc.setMinWidth(100);
            lblHeaderDesc.setMaxWidth(100);
            lblHeaderDesc.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            headerRow.getChildren().addAll(lblHeaderNom,  lbdate, lblHeaderDesc);
            vListUsers.getChildren().add(headerRow);

            // Exécuter la recherche
            List<Avis> avisList = crudAvis.searchByDate(searchDate);

            // Affichage des résultats
            for (Avis avis : avisList) {
                HBox avisRow = new HBox(4);
                avisRow.setPrefHeight(32.0);
                avisRow.setPrefWidth(765.0);
                avisRow.setStyle("-fx-padding: 10px;");

                User user = crudUser.getById(avis.getCreatedBy());
                Label lbnom = new Label(user.getNom() + " " + user.getPrenom());
                lbnom.setMinWidth(150);
                lbnom.setMaxWidth(150);

                /*Label lblCreatedBy = new Label(String.valueOf(avis.getCreatedBy()));
                lblCreatedBy.setMinWidth(80);
                lblCreatedBy.setMaxWidth(80);*/

                Label lblCreatedAt = new Label(avis.getCreatedAt().toString());
                lblCreatedAt.setMinWidth(100);
                lblCreatedAt.setMaxWidth(100);

                Label lbldesc = new Label(avis.getDescription());
                lbldesc.setMinWidth(100);
                lbldesc.setMaxWidth(100);

                avisRow.getChildren().addAll(lbnom, lblCreatedAt, lbldesc);
                vListUsers.getChildren().add(avisRow);
                avisRow.setOnMouseClicked(event -> showAvisDetailsPopup(avis));
            }
        } catch (ParseException e) {
            System.out.println("Format de date invalide : " + createdAtCriteria);
        }

    }

/*
    @FXML
    void navigateToHome(MouseEvent event) {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeAdmin.fxml"));
            Scene signUpScene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) imLogo.getScene().getWindow();
            stage.setScene(signUpScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading SignUp.fxml.");
        }
    }
*/


}
