package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;
import services.*;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class LivraisonClient  implements Initializable {

    private CrudFacture crudFacture = new CrudFacture();
    private CrudUser crudUser = new CrudUser();
    private CrudAvis crudAvis = new CrudAvis();
    private CrudZone crudZone = new CrudZone();
    private CrudCommande crudCommande = new CrudCommande();
    private int userId ;

    @FXML
    private AnchorPane anPendingUsers;

    @FXML
    private HBox headerHBox;

    @FXML
    private VBox vListUsers;

    @FXML
    private ImageView backButton;

    private Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            System.out.println("initialize() exécuté !");
            this.conn = MyDatabase.getInstance().getConnection();
            getUser();
            afficherLivraisons();
        });
    }


    void getUser() {
        if (crudAvis.getUser() != null) {
            this.userId = crudAvis.getUser().getId();
            System.out.println("Utilisateur récupéré, ID = " + this.userId);
        } else {
            System.out.println("Erreur: Aucun utilisateur trouvé !");
        }
    }


    @FXML
    void goAjouterAvis(ActionEvent event, Livraison livraison) {

        try {
            Stage stage = (Stage) headerHBox.getScene().getWindow(); // Get reference to the login window's stage
            stage.setTitle("Ajouter Avis");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutAvis.fxml"));
            Parent p = loader.load();

            // Pass the Livraison object to the ModifierLivraison controller
            AjoutAvis controller = loader.getController();
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
    private void afficherLivraisons() {

        try {

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
                Commande cmd = crudCommande.getById(livraison.getCommandeId());

                Label commandeLabel = new Label(String.valueOf(cmd.getStatut()));
                commandeLabel.setPrefWidth(195); // Set preferred width for commandeLabel
                commandeLabel.setAlignment(Pos.CENTER); // Center text within the label


                //get date de creation
                Label dateLabel = new Label(livraison.getCreatedAt().toString());
                dateLabel.setPrefWidth(149); // Set preferred width for dateLabel
                dateLabel.setAlignment(Pos.CENTER); // Center text within the label
                dateLabel.setStyle("-fx-padding: 0 0 0 50px;"); // Padding for left side (top, right, bottom, left)


                Zone zone = crudZone.getById(livraison.getZoneId());
                Label zoneLabel = new Label(zone != null ? zone.getNom() : "unkown");
                zoneLabel.setPrefWidth(134); // Set preferred width for zoneLabel
                zoneLabel.setAlignment(Pos.CENTER); // Center text within the label
                zoneLabel.setStyle("-fx-padding: 0 0 0 80px;"); // Padding for left side (top, right, bottom, left)


                HBox hbox = new HBox();
                hbox.setPadding(new Insets(0, 0, 0, 50));
                //get montant de facture
                Facture ff = crudFacture.getById(livraison.getFactureId());
                Label factureLabel = new Label(String.valueOf(ff.getMontant()) + "DT");
                factureLabel.setPrefWidth(153); // Set preferred width for factureLabel
                factureLabel.setAlignment(Pos.CENTER); // Center text within the label
                factureLabel.setStyle("-fx-padding: 0 5 0 10px;"); // Padding for left side (top, right, bottom, left)

                Button editButton = new Button("Avis  ");
                editButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 5px 10px 5px 20px;");  // 20px left padding
                editButton.setOnAction(e -> {
                    // When clicked, delete the livraison by its id
                    goAjouterAvis(e, livraison);
                });

                HBox hbox1 = new HBox();
                hbox1.setPadding(new Insets(0, 0, 0, 15));
                // Add labels to row
                row.getChildren().addAll(commandeLabel, dateLabel, zoneLabel, hbox, factureLabel,hbox1, editButton);

                // Add each row under the header
                vListUsers.getChildren().add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle navigation failure
        }


    }


    private List<Livraison> getLivraisons() {
        List<Livraison> livraisons = new ArrayList<>();
        String query = "SELECT * FROM livraison WHERE created_by = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);  // Sécurisation du paramètre

            try (ResultSet rs = ps.executeQuery()) {

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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livraisons;
    }

    @FXML
    private void navigateToHomeClient(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeClient.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}