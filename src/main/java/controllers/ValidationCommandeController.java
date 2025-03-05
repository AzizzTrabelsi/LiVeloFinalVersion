package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;
import services.CrudCommande;
import services.CrudFacture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ValidationCommandeController {

    @FXML
    private TextField adresseArrField;

    @FXML
    private TextField adresseDepField;

    @FXML
    private VBox articlesContainer;

    @FXML
    private Label labelTotal;

    @FXML
    private ComboBox<String> typeLivraisonCombo;
    @FXML
    private Button OrderBtn;
    private Commande commandeEnCours;

    private CrudCommande crudCommande = new CrudCommande();
    private List<Article> articlesList=new ArrayList<>();
    private CrudFacture crudFacture=new CrudFacture();

    @FXML
    public void initialize() {

        // Initialiser le type de livraison avec une seule option "Standard"
        ObservableList<String> options = FXCollections.observableArrayList("Standard");
        typeLivraisonCombo.setItems(options);
        typeLivraisonCombo.setValue("Standard");

        // Créer une nouvelle commande vide au démarrage
        commandeEnCours = new Commande();
        commandeEnCours.setStatut(statutlCommande.Processing);
        commandeEnCours=crudCommande.getById(MarketClient.getCommandeId());

        System.out.println("Commande recupere du market Client " + commandeEnCours.getId_Commande());

        // Charger les articles (si pré-sélectionnés)
        afficherArticlesSelectionnes();

    }
    private void afficherArticlesSelectionnes() {
        articlesContainer.getChildren().clear();
        System.out.println("id pour recherche article commandes"+commandeEnCours.getId_Commande());
        List<Article> articles = crudCommande.getlisteArticleCommande(commandeEnCours.getId_Commande()); // Récupérer les articles de la commande

        double total = 0.0;
        for (Article article : articles) {
            HBox articleBox = new HBox(50);

            // Image de l'article
            ImageView imageView = new ImageView(new Image(article.getUrlImage()+".jpg"));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            int quantite = crudCommande.getQuantiteArticleCommande(commandeEnCours.getId_Commande(), article.getIdArticle());
            System.out.println("9bal affichage taa quantity---------"+quantite);
            // Nom et prix de l'article
            Label nameLabel = new Label(article.getNom() + " - " + article.getPrix() + "TND"+" - Quantity:  "+quantite);
            // Label pour afficher la quantité sélectionnée
            // Bouton de suppression
            Button removeButton = new Button("X");
            removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            removeButton.setOnAction(event -> {crudCommande.supprimerArticleCommande(MarketClient.getCommandeId(),article);
                                                    afficherArticlesSelectionnes();});
            // Ajouter les composants dans la HBox
            articleBox.getChildren().addAll(imageView, nameLabel, removeButton);
            articlesContainer.getChildren().add(articleBox);

            articlesList=articles;
            total += quantite * article.getPrix();

        }

        // Mettre à jour le total
        labelTotal.setText(String.format("%.2f TND", total)); // Format en deux décimales
    }




    @FXML
    private void validerCommande() {
        if (adresseDepField.getText().isEmpty()||adresseDepField.getText().equals("Adresse inconnue") || adresseArrField.getText().isEmpty()||adresseArrField.getText().equals("Adresse inconnue")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Fields are required", ButtonType.OK);
            alert.show();
            return;
        }
        if (articlesContainer.getChildren().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nothing to order!!!", ButtonType.OK);
            alert.show();
            return;
        }

        // Mise à jour des données de la commande
        commandeEnCours.setAdresse_dep(adresseDepField.getText());
        commandeEnCours.setAdresse_arr(adresseArrField.getText());
        commandeEnCours.setType_livraison((String) typeLivraisonCombo.getValue());
        commandeEnCours.setStatut(statutlCommande.Shipping);

        // Mise à jour dans la base de données
        crudCommande.update(commandeEnCours);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order succesfully done", ButtonType.OK);
        double total = Double.parseDouble(labelTotal.getText().replace(" TND", "").replace(",", "."));
        alert.showAndWait().ifPresent(response -> {
            try {
                // Charger la page de Facture
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Facture.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur de Facture
                FactureController factureController = loader.getController();

                // Passer les détails de la commande
                factureController.setFactureDetails(total,commandeEnCours.getCreated_by(), commandeEnCours.getId_Commande());

                // Changer de scène
                Stage stage = (Stage) labelTotal.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Facture Payement");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void chargerCommande(int idCommande) {
        // Charger la commande depuis la base de données
        System.out.println("id charger commande validation"+idCommande  );
        commandeEnCours = crudCommande.getById(idCommande);

        if (commandeEnCours != null) {
            adresseDepField.setText(commandeEnCours.getAdresse_dep());
            adresseArrField.setText(commandeEnCours.getAdresse_arr());
            typeLivraisonCombo.setValue(commandeEnCours.getType_livraison());

            afficherArticlesSelectionnes();
           // mettreAJourTotal();
        } else {
            System.out.println("Commande introuvable avec ID: " + idCommande);
        }
    }
}
