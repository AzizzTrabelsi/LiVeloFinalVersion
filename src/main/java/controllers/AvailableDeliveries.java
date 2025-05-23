package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import models.*;
import services.*;


import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import java.util.Date;

import java.util.List;
import java.util.ResourceBundle;

public class AvailableDeliveries implements Initializable {







    @FXML
    private ImageView backButton;

    @FXML
    private VBox ordersContainer;


    private int userId;
    private final User user = new User();
    private final CrudCommande crudCommande = new CrudCommande();
    private final CrudFacture crudFacture = new CrudFacture();
    private final CrudUser crudUser = new CrudUser();
    private final CrudLivraison crudLivraison = new CrudLivraison();
    private final CrudZone crudZone = new CrudZone();
    private Stage stage;
    private Scene scene;
    private Parent root;
    private CrudAvis crudAvis= new CrudAvis();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getUser();
        Platform.runLater(this::loadAvailableOrders);
    }

    void getUser(){
        this.userId=crudAvis.getUser().getId();
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

        List<Commande> commandes = crudCommande.getCommandesByStatut("Processing"); // Récupérer commandes en attente
        for (Commande commande : commandes) {
            HBox commandeBox = new HBox(100); // Une ligne par commande
            commandeBox.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f8f8); " + // Dégradé subtil
                            "-fx-padding: 15px; " + // Plus d'espace intérieur
                            "-fx-border-radius: 12px; " + // Coins arrondis
                            "-fx-background-radius: 12px; " + // Appliquer aux bords aussi
                            "-fx-border-color: #e0e0e0; " + // Bordure fine et moderne
                            "-fx-border-width: 1px; " + // Épaisseur de la bordure
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.15), 10, 0, 0, 5);" // Ombre légère
            );
            commandeBox.setOnMouseEntered(event -> {
                commandeBox.setStyle(
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
            commandeBox.setOnMouseExited(event -> {
                commandeBox.setStyle(
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
            System.out.println("ena commande kbal me nhotha fi box"+commande);
            // Infos de la commande
            Text IDcommande= new Text("Order ID : " + commande.getId_Commande());
            Text Adress= new Text("First adress  :" + commande.getAdresse_dep()+"\n Second Adress :" + commande.getAdresse_arr());
            User user=crudUser.getById(commande.getCreated_by());
            Text clientName = new Text("Client : " + user.getNom()+ " " + user.getPrenom());
            Facture facture=crudFacture.getByCommandID(commande.getId_Commande());
            Text montantTotal = new Text("Total: " + facture.getMontant() + "TND");
            System.out.println("aaaaaa kbal affichage"+IDcommande.getText()+"name="+clientName.getText()+"montant="+montantTotal.getText());
            // Bouton pour accepter la commande
            Button acceptButton = new Button("Accepter");
            acceptButton.setStyle("-fx-background-color: #398c3e; -fx-text-fill: white; -fx-font-weight: bold;");
            acceptButton.setOnAction(event -> acceptOrder(commande));

            commandeBox.getChildren().addAll(IDcommande, clientName, montantTotal,Adress, acceptButton);
            ordersContainer.getChildren().add(commandeBox);
        }
    }

    private void acceptOrder(Commande commande) {
        commande.setStatut(statutlCommande.Shipping); // Mettre à jour le statut
        crudCommande.update(commande); // Sauvegarder en base

        // Afficher une confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Commande acceptée !", ButtonType.OK);
        alert.showAndWait();

        loadAvailableOrders(); // Rafraîchir la liste

        user.setId(userId );
        Facture f  = crudFacture.getByCommandID(commande.getId_Commande());

        Livraison livraison =new Livraison(commande.getId_Commande(), commande.getCreated_by(), new Date(),f.getIdFacture(), /*zone id*/ 3
                ,  user);
        //navigate to my livraison
        crudLivraison.add(livraison);
        navigateToMyDelivry();
    }
    @FXML
    private void navigateToMyDelivry(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MyDeliveries.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}