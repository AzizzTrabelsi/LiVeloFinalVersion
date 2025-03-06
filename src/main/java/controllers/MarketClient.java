package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Article;
import models.Commande;
import models.statutlCommande;
import services.CrudArticle;
import services.CrudCategorie;
import services.CrudCommande;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MarketClient implements Initializable {
        private final CrudArticle su = new CrudArticle();
        private CrudCategorie crudCategorie = new CrudCategorie();
        private List<Article> articleList;
        @FXML
        private TextField anSearch;
        @FXML
        private HBox articleLayout;
        @FXML
        private FlowPane grid;

        @FXML
        private ImageView imLogo;

        @FXML
        void navigateToHome(MouseEvent event) {

        }
        public static int commandeId = -1;
        public static void setCommandeId(int id) {
                commandeId = id;
        }

        public static int getCommandeId() {
                return commandeId;
        }
        private void ajouterNouvelleCommande() {
                CrudCommande crudCommande = new CrudCommande();
                Commande nouvelleCommande = new Commande();
                nouvelleCommande.setAdresse_dep("Adresse inconnue");
                nouvelleCommande.setAdresse_arr("Adresse inconnue");
                nouvelleCommande.setType_livraison("Standard");
                nouvelleCommande.setHoraire(new Timestamp(System.currentTimeMillis()));
                nouvelleCommande.setStatut(statutlCommande.Processing);
                nouvelleCommande.setCreated_by(53);

                crudCommande.add(nouvelleCommande);
                System.out.println("Commande ID enregistrée dans MarketClient: " + getCommandeId());
        }



        private int categoryId = -1; // Définit une valeur par défaut pour éviter 0
        private boolean isCategorySet = false;

        // ✅ Méthode pour définir l'ID de la catégorie AVANT de charger les articles
        public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
                this.isCategorySet = true;
                System.out.println("✅ ID de catégorie reçu dans MarketClient : " + categoryId);

                // Si l'interface est déjà chargée, affiche les articles immédiatement
                if (grid != null) {
                        loadArticlesByCategory();
                }
        }
        // ✅ Sépare la logique de chargement des articles
        private void loadArticlesByCategory() {
                System.out.println("🔄 Chargement des articles pour catégorie ID : " + categoryId);

                if (categoryId <= 0) {
                        System.out.println("⚠️ ID de catégorie invalide !");
                        return;
                }

                List<Article> articles = su.getArticlesByCategoryId(categoryId);
                System.out.println("🛒 Articles trouvés : " + articles.size());

                if (articles.isEmpty()) {
                        System.out.println("⚠️ Aucun article trouvé !");
                        return;
                }

                int column = 0;
                int row = 0;

                try {
                        for (Article a : articles) {
                                System.out.println("📦 Article trouvé : " + a.getNom());

                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Article.fxml")); // Assure-toi que c'est le bon fichier
                                VBox articleBox = fxmlLoader.load();

                                article articleController = fxmlLoader.getController();
                                if (articleController == null) {
                                        System.out.println("❌ articleController est NULL !");
                                        continue;
                                }

                                articleController.setData(a);
                                articleBox.setOnMouseClicked(event -> afficherPopupArticle(a));

                                grid.getChildren().add(articleBox);
                                FlowPane.setMargin(articleBox, new Insets(10, 10, 10, 10));

                                column++;
                                if (column == 6) {
                                        column = 0;
                                        row++;
                                }
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        @Override
        public void initialize(URL url, ResourceBundle rb) {
                int column = 0;
                int row = 0;
                System.out.println("🔄 MarketClient initialisé, attente de categoryId...");
                ajouterNouvelleCommande();
                // Si l'ID a déjà été défini, charge les articles directement
                if (isCategorySet) {
                        loadArticlesByCategory();
                }

                /*int column = 0;
                int row = 0;

                try {
                        for (Article a : su.getAll()) {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/article.fxml"));
                                VBox articleBox = fxmlLoader.load();

                                // Vérification du contrôleur
                                article articleController = fxmlLoader.getController();
                                if (articleController == null) {
                                        System.out.println("❌ articleController est NULL !");
                                        continue; // Ne pas ajouter un article mal chargé
                                }

                                // Assigner les données
                                articleController.setData(a);

                                // Ajouter à la grille
                                // ✅ Ajouter à FlowPane (de gauche à droite)
                                grid.getChildren().add(articleBox);

                                // ✅ Ajouter un petit espace entre les articles
                                FlowPane.setMargin(articleBox, new Insets(10, 10, 10, 10));

                                // Gérer le changement de ligne
                                if (column == 6) {
                                        column = 0;
                                        row++;
                                }

                                System.out.println("✅ Ajout au GridPane : " + a.getNom() + " -> Ligne " + row + ", Colonne " + column);
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }*/
        /*public void initialize(URL url, ResourceBundle rb) {
                System.out.println("aaa"+su.getAll());
                int column=0;
                int row=0;
                try{
                        for(Article a:su.getAll()){
                                FXMLLoader fxmlLoader=new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("/article.fxml"));
                                VBox articleBox = fxmlLoader.load();
                                article articleController=fxmlLoader.getController();
                                articleController.setData(a);
                                if(column==6){
                                        column=0;
                                        ++row;
                                }
                                grid.add(articleBox,column++,row);
                                GridPane.setMargin(articleBox,new Insets(10));
                                System.out.println("Ajout au GridPane : " + a.getNom() + " -> Ligne " + row + ", Colonne " + column);


                        }

                }catch (IOException e){
                e.printStackTrace();}*/
        }
        private void afficherPopupArticle(Article article) {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/popuparticle.fxml"));
                        AnchorPane Root = loader.load();

                        // Récupérer le contrôleur et envoyer l'article sélectionné
                        PopupArticleController popupController = loader.getController();
                        popupController.setData(article);

                        // Créer une nouvelle fenêtre
                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setTitle("Details about Article");
                        popupStage.setScene(new Scene(Root));
                        popupStage.showAndWait();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        @FXML
        private void ouvrirValidationCommande() {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ValidationCommande.fxml"));
                        Parent root = loader.load();

                        // Passer l'ID de la commande en cours (à adapter selon ton système)
                        ValidationCommandeController controller = loader.getController();
                        controller.chargerCommande(commandeId);

                        Stage stage = new Stage();
                        stage.setTitle("Validation de la commande");
                        stage.setScene(new Scene(root));
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}

