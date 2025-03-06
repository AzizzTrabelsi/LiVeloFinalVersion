package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;
import services.CrudCommande;
import services.CrudFacture;
import services.CrudLivraison;
import services.CrudUser;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MyHistoeyController implements Initializable {

    @FXML
    private ImageView backButton;

    @FXML
    private VBox ordersContainer;

    private final CrudLivraison crudLivraison = new CrudLivraison();
    private final CrudCommande crudCommande = new CrudCommande();
    private final CrudUser crudUser = new CrudUser();
    private final CrudFacture crudFacture = new CrudFacture();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::loadAvailableOrders);
    }



    @FXML
    private void navigerVersHomeLivreur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeLivreur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadAvailableOrders() {
        ordersContainer.getChildren().clear();  // Nettoyer l'affichage

        List<Livraison> livraisons = crudLivraison.gethistorylivraisonbyid_livreur(SignIn.id_User); // Récupérer livraisons en attente
        List<Commande> commandes = livraisons.stream()
                .map(livraison -> crudCommande.getById(livraison.getCommandeId())) // Récupérer la commande associée
                .toList();
        for (Commande commande : commandes) {
            HBox livraisonBox = new HBox(100); // Une ligne par livraison
            livraisonBox.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f8f8); " + // Dégradé subtil
                            "-fx-padding: 15px; " + // Plus d'espace intérieur
                            "-fx-border-radius: 12px; " + // Coins arrondis
                            "-fx-background-radius: 12px; " + // Appliquer aux bords aussi
                            "-fx-border-color: #e0e0e0; " + // Bordure fine et moderne
                            "-fx-border-width: 1px; " + // Épaisseur de la bordure
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 10, 0, 0, 5);" // Ombre légère
            );
            livraisonBox.setOnMouseEntered(event -> {
                livraisonBox.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #ffffff, #f0f0f0); " + // Légèrement plus foncé
                                "-fx-padding: 15px; " +
                                "-fx-border-radius: 12px; " +
                                "-fx-background-radius: 12px; " +
                                "-fx-border-color: #d0d0d0; " +
                                "-fx-border-width: 1px; " +
                                "-fx-scale-x: 1.05; " + // Grossit de 5%
                                "-fx-scale-y: 1.05; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 15, 0, 0, 7); " + // Ombre plus marquée
                                "-fx-transition: all 0.3s ease-in-out;"
                );
            });
            livraisonBox.setOnMouseExited(event -> {
                livraisonBox.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f8f8); " +
                                "-fx-padding: 15px; " +
                                "-fx-border-radius: 12px; " +
                                "-fx-background-radius: 12px; " +
                                "-fx-border-color: #e0e0e0; " +
                                "-fx-border-width: 1px; " +
                                "-fx-scale-x: 1.0; " + // Retour à la taille normale
                                "-fx-scale-y: 1.0; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 10, 0, 0, 5); " +
                                "-fx-transition: all 0.3s ease-in-out;"
                );
            });
            System.out.println("ena livraison kbal me nhotha fi box"+commande);
            // Infos de la livraison
            Text IDlivraison= new Text("Order ID : " + commande.getId_Commande());
            Text Adress= new Text("First adress  :" + commande.getAdresse_dep()+"\n Second Adress :" + commande.getAdresse_arr());
            User user=crudUser.getById(commande.getCreated_by());
            Text clientName = new Text("Client : " + user.getNom()+ " " + user.getPrenom());
            Facture facture=crudFacture.getByCommandID(commande.getId_Commande());
            Text montantTotal = new Text("Total: " + facture.getMontant() + "TND");
            System.out.println("aaaaaa kbal affichage"+IDlivraison.getText()+"name="+clientName.getText()+"montant="+montantTotal.getText());
            // Bouton pour accepter la livraison


            livraisonBox.getChildren().addAll(IDlivraison, clientName, montantTotal,Adress);
            ordersContainer.getChildren().add(livraisonBox);
        }
    }




}
