package controllers;

import com.stripe.exception.StripeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Facture;
import models.type_paiement;
import services.CrudFacture;
import services.StripeService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class FactureController {
    @FXML
    private Label montantLabel;
    @FXML
    private CheckBox cashCheckBox;
    @FXML
    private CheckBox onlineCheckBox;

    private int userId;
    private int commandeId;
    CrudFacture crudFacture = new CrudFacture();

    public void setFactureDetails(double montant, int userId, int commandeId) {
        this.montantLabel.setText(montant + " TND");
        this.userId = userId;
        this.commandeId = commandeId;
    }

    @FXML
    private void confirmerPaiement() {
        System.out.println("Paiement confirmé !");
        String type = "";

        if (cashCheckBox.isSelected()) {
            type = "CASH";
            enregistrerFacture(type); // Enregistre directement la facture en cash
        } else if (onlineCheckBox.isSelected()) {
            type = "ONLINE";
            ouvrirPagePaiement(); // Ouvre une page pour le paiement en ligne
            enregistrerFacture(type);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez choisir un mode de paiement", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void enregistrerFacture(String type) {
        Facture facture = new Facture(
                Float.parseFloat(montantLabel.getText().replace(" TND", "")),
                new Date(System.currentTimeMillis()),
                type_paiement.valueOf(type),
                userId, commandeId
        );
        crudFacture.add(facture);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Commande validée avec succès !", ButtonType.OK);
        alert.showAndWait();

        // Rediriger vers la facture
        afficherFacture();
    }

   /* private void ouvrirPagePaiement() {
        try {
            String urlPaiement = "https://buy.stripe.com/test_4gw3cP8TtdtSa80cMM"; // 🔥 LIEN TEST STRIPE
            Desktop.getDesktop().browse(new URI(urlPaiement));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }*/
   private void ouvrirPagePaiement() {
       try {
           StripeService stripeService = new StripeService();
           String urlPaiement = stripeService.createCheckoutSession(commandeId, Float.parseFloat(montantLabel.getText().replace(" TND", "")));

           if (urlPaiement != null && !urlPaiement.isEmpty()) {
               Desktop.getDesktop().browse(new URI(urlPaiement));
           } else {
               System.out.println("Erreur : URL de paiement non générée.");
           }
       } catch (IOException | URISyntaxException | StripeException e) {
           e.printStackTrace();
       }
   }


    private void afficherFacture() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Facture.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
