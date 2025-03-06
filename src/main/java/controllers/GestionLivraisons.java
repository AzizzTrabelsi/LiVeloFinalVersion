package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Livraison;
import models.User;
import models.Zone;
import services.CrudLivraison;
import services.CrudUser;
import services.CrudZone;
import utils.MyDatabase;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.AnchorPane;

public class GestionLivraisons implements Initializable {

    private CrudUser crudUser = new CrudUser();
    private CrudZone crudZone= new CrudZone();
    private CrudLivraison crudLivraison= new CrudLivraison();


    @FXML
    private Button ajouterLivraison;

    @FXML
    private AnchorPane anPendingUsers;



    @FXML
    private TextField anSearch;

    @FXML
    private HBox headerHBox;

    @FXML
    private VBox vListUsers;

    private Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conn = MyDatabase.getInstance().getConnection();
        afficherLivraisons();

    }
    @FXML
    void goAjoutLivraison(ActionEvent event) {
        try {
            Stage stage = (Stage) ajouterLivraison.getScene().getWindow(); // Get reference to the login window's stage
            stage.setTitle("Ajout Livraison");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterLivraison.fxml"));
            Parent p = loader.load();
            Scene scene = new Scene(p);

            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle navigation failure
        }

    }

    @FXML
    void goToModifier(ActionEvent event, Livraison livraison) {
        try {
            Stage stage = (Stage) ajouterLivraison.getScene().getWindow(); // Get reference to the login window's stage
            stage.setTitle("Modifier Livraison");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierLivraison.fxml"));
            Parent p = loader.load();

            // Pass the Livraison object to the ModifierLivraison controller
            ModifierLivraison controller = loader.getController();
            controller.setLivraison(livraison);  // Set the Livraison data

            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle navigation failure
        }
    }

    @FXML
    void deleteLivraison(ActionEvent event, int id) {
        try {
        crudLivraison.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle navigation failure
        }

    }
    private void afficherLivraisons() {
        try {
/*
        List<Livraison> livraisons = getLivraisons();
        int x = 0;
        for (Livraison livraison : livraisons) {

            HBox row = new HBox();
            row.setSpacing(20);
            //headerHBox.setSpacing(20);
            Label idLabel = new Label(String.valueOf(livraison.getIdLivraison()));
            Label commandeLabel = new Label(String.valueOf(livraison.getCommandeId()));
            Label dateLabel = new Label(livraison.getCreatedAt().toString());
            Label factureLabel = new Label(String.valueOf(livraison.getFactureId()));
            Label zoneLabel = new Label(String.valueOf(livraison.getZoneId()));
            Label createdByLabel = new Label(String.valueOf(livraison.getCreatedBy()));

           // headerHBox.getChildren().addAll(idLabel, commandeLabel, dateLabel, factureLabel, zoneLabel, createdByLabel);
            row.getChildren().addAll(idLabel, commandeLabel, dateLabel, factureLabel, zoneLabel, createdByLabel);

            vListUsers.getChildren().add(row);
            //vListUsers.getChildren().add(headerHBox);

        }*/
        vListUsers.getChildren().clear(); // Nettoyer l'affichage avant de le remplir

// Vérifier et ajouter l'en-tête en haut
        if (!vListUsers.getChildren().contains(headerHBox)) {
            vListUsers.getChildren().add(headerHBox);
        }

        List<Livraison> livraisons = getLivraisons();

        for (Livraison livraison : livraisons) {
            HBox row = new HBox();
            row.setSpacing(10);  // Set spacing between elements

            // Style and padding for row
            row.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px; -fx-border-color: #ccc; -fx-border-width: 0 0 1px 0;");

            // Make the row take the full width of its parent
            row.setMaxWidth(Double.MAX_VALUE);  // Allow the row to expand to the max width
            row.setPrefWidth(765); // Set the preferred width

            // Create Labels with specific widths for each


            Label commandeLabel = new Label(String.valueOf(livraison.getCommandeId()));
            commandeLabel.setPrefWidth(131); // Set preferred width for commandeLabel
            commandeLabel.setAlignment(Pos.CENTER); // Center text within the label
            Label dateLabel = new Label(livraison.getCreatedAt().toString());
            dateLabel.setPrefWidth(149); // Set preferred width for dateLabel
            dateLabel.setAlignment(Pos.CENTER); // Center text within the label
            dateLabel.setStyle("-fx-padding: 0 0 0 50px;"); // Padding for left side (top, right, bottom, left)
            Label factureLabel = new Label(String.valueOf(livraison.getFactureId()));
            factureLabel.setPrefWidth(77); // Set preferred width for factureLabel
            factureLabel.setAlignment(Pos.CENTER); // Center text within the label
            factureLabel.setStyle("-fx-padding: 0 30 0 50px;"); // Padding for left side (top, right, bottom, left)
            Zone zone = crudZone.getById(livraison.getZoneId());
            Label zoneLabel = new Label(zone!=null ?zone.getNom():"unkown");
            zoneLabel.setPrefWidth(128); // Set preferred width for zoneLabel
            zoneLabel.setAlignment(Pos.CENTER); // Center text within the label
            zoneLabel.setStyle("-fx-padding: 0 0 0 80px;"); // Padding for left side (top, right, bottom, left)

            User user = crudUser.getById(livraison.getCreatedBy());
            Label createdByLabel = new Label(user.getNom()+" "+user.getPrenom());
            createdByLabel.setPrefWidth(128); // Set preferred width for createdByLabel
            createdByLabel.setAlignment(Pos.CENTER); // Center text within the label
            createdByLabel.setStyle("-fx-padding: 0 0 0 10px;"); // Padding for left side (top, right, bottom, left)


            // Create a Delete Button
            HBox hbox = new HBox();
            hbox.setPadding(new Insets(0, 0, 0, 50));

            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 5px 10px 5px 20px;");  // 20px left padding

            deleteButton.setOnAction(e -> {
                // Afficher une alerte de confirmation avant de supprimer
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Alert");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr de  supprimer cette livraison ?");

                // Attendre la réponse de l'utilisateur
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Si l'utilisateur confirme, supprimer la livraison
                        crudLivraison.delete(livraison.getIdLivraison());
                        refreshLivraisons();


                    }
                });
            });

            deleteButton.setAlignment(Pos.CENTER); // Center text within the label


            Button editButton = new Button("Modifier");
            editButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 5px 10px 5px 20px;");  // 20px left padding
            editButton.setOnAction(e -> {
                // When clicked, delete the livraison by its id
                goToModifier(e, livraison);
            });
            // Add labels to row
            row.getChildren().addAll( commandeLabel, dateLabel, factureLabel, zoneLabel, createdByLabel,hbox,editButton,deleteButton);

            // Add each row under the header
            vListUsers.getChildren().add(row);
        }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle navigation failure
        }
    }
    @FXML
    void refreshLivraisons() {
        try {
            Stage stage = (Stage) ajouterLivraison.getScene().getWindow(); // Get reference to the login window's stage
            stage.setTitle("Gestion Livraisons");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionLivraison.fxml"));
            Parent p = loader.load();
            Scene scene = new Scene(p);

            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle navigation failure
        }

    }

    private List<Livraison> getLivraisons() {
        List<Livraison> livraisons = new ArrayList<>();
        String query = "SELECT * FROM livraison";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Livraison livraison = new Livraison(
                        rs.getInt("idLivraison"),
                        rs.getInt("commandeId"),
                        rs.getInt("created_by"),
                        rs.getDate("created_at"),
                        rs.getInt("factureId"),
                        rs.getInt("zoneId"),
                        crudUser.getById(rs.getInt("id_livreur"))
                );
                livraisons.add(livraison);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livraisons;
    }
}

