package controllers;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.time.ZoneId;
import java.time.LocalDate;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Article;
import models.Categorie;


import models.statut_article;
import services.CrudArticle;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import tests.MainUserInterface;

import java.time.LocalDate;




import java.io.IOException;
import java.net.URL;

public class GestionArticle implements Initializable {

    private CrudArticle su = new CrudArticle();
    public static int CategoryID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Afficher tous les utilisateurs au démarrage
        ShowByCategorie(null);

        anSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadArticle();
            } else {
                searchArticle(newValue);
            }
        });
    }

    @FXML
    void Show(ActionEvent showEvent) {
        hbHedha.getChildren().clear();

        // Récupérer tous les articles
        List<Article> articleList = su.getAll();
        //List<Article> articleList = su.getArticlesByCategoryId(CategoryID);

        // Parcours de la liste des articles
        for (Article article : articleList) {
            HBox artRow = new HBox(4);
            artRow.setPrefHeight(32.0);
            artRow.setPrefWidth(765.0);

            // Label pour l'ID de la catégorie (associée à l'article)
            Label lblIdCategorie = new Label(String.valueOf(article.getCategorie().getId_categorie()));
            lblIdCategorie.setMinWidth(80);
            lblIdCategorie.setMaxWidth(80);

            // Label pour l'URL de l'image de l'article
            Label lblurlimg = new Label(article.getUrlImage());
            lblurlimg.setMinWidth(80);
            lblurlimg.setMaxWidth(80);

            // Label pour le nom de l'article
            Label lblNom = new Label(article.getNom());
            lblNom.setMinWidth(80);
            lblNom.setMaxWidth(80);

            // Label pour le prix de l'article
            Label lblPrix = new Label(String.valueOf(article.getPrix()));  // Correction
            lblPrix.setMinWidth(80);
            lblPrix.setMaxWidth(80);

            // Label pour la description de l'article
            Label lblDesc = new Label(article.getDescription());
            lblDesc.setMinWidth(130);
            lblDesc.setMaxWidth(130);

            // Label pour le statut de l'article
            Label lblStatu = new Label(article.getStatut().name());
            lblStatu.setMinWidth(105);
            lblStatu.setMaxWidth(105);

            // Label pour la quantité de l'article
            Label lblQuant = new Label(String.valueOf(article.getQuantite()));  // Correction
            lblQuant.setMinWidth(80);
            lblQuant.setMaxWidth(80);

            // Label pour la date de création de l'article
            Label lblcreatedat = new Label(article.getCreatedAt().toString());
            lblcreatedat.setMinWidth(80);
            lblcreatedat.setMaxWidth(80);

            // Ajouter tous les labels dans le HBox (ligne)
            artRow.getChildren().addAll(lblIdCategorie, lblurlimg, lblNom, lblPrix, lblDesc, lblStatu, lblQuant, lblcreatedat);

            // Ajouter un événement au clic sur la ligne pour afficher les détails de l'article
            artRow.setOnMouseClicked(event -> showArticleDetailsPopup(article));

            // Ajouter cette ligne à la vue principale (vListUsers)
            vListUsers.getChildren().add(artRow);
        }
    }

    @FXML
    void ShowByCategorie(ActionEvent showEvent) {

        hbHedha.getChildren().clear();

        List<Article> articleList = su.getAll();
        List<Article> Temp = new ArrayList<>();
        //List<Article> articleList = su.getArticlesByCategoryId(CategoryID);
        for (Article article : articleList) {
            if(article.getCategorie().getId_categorie() == CategoryID) {
                //articleList.remove(article);
                Temp.add(article);
            }
        }

        articleList = Temp;

        // Parcours de la liste des articles
        for (Article article : articleList) {
            HBox artRow = new HBox(4);
            artRow.setPrefHeight(32.0);
            artRow.setPrefWidth(765.0);

            // Label pour l'ID de la catégorie (associée à l'article)
            Label lblIdCategorie = new Label(String.valueOf(article.getCategorie().getId_categorie()));
            lblIdCategorie.setMinWidth(80);
            lblIdCategorie.setMaxWidth(80);

            // Label pour l'URL de l'image de l'article
            Label lblurlimg = new Label(article.getUrlImage());
            lblurlimg.setMinWidth(80);
            lblurlimg.setMaxWidth(80);

            // Label pour le nom de l'article
            Label lblNom = new Label(article.getNom());
            lblNom.setMinWidth(80);
            lblNom.setMaxWidth(80);

            // Label pour le prix de l'article
            Label lblPrix = new Label(String.valueOf(article.getPrix()));  // Correction
            lblPrix.setMinWidth(80);
            lblPrix.setMaxWidth(80);

            // Label pour la description de l'article
            Label lblDesc = new Label(article.getDescription());
            lblDesc.setMinWidth(130);
            lblDesc.setMaxWidth(130);

            // Label pour le statut de l'article
            Label lblStatu = new Label(article.getStatut().name());
            lblStatu.setMinWidth(105);
            lblStatu.setMaxWidth(105);

            // Label pour la quantité de l'article
            Label lblQuant = new Label(String.valueOf(article.getQuantite()));  // Correction
            lblQuant.setMinWidth(80);
            lblQuant.setMaxWidth(80);

            // Label pour la date de création de l'article
            Label lblcreatedat = new Label(article.getCreatedAt().toString());
            lblcreatedat.setMinWidth(80);
            lblcreatedat.setMaxWidth(80);

            // Ajouter tous les labels dans le HBox (ligne)
            artRow.getChildren().addAll(lblIdCategorie, lblurlimg, lblNom, lblPrix, lblDesc, lblStatu, lblQuant, lblcreatedat);

            // Ajouter un événement au clic sur la ligne pour afficher les détails de l'article
            artRow.setOnMouseClicked(event -> showArticleDetailsPopup(article));

            // Ajouter cette ligne à la vue principale (vListUsers)
            vListUsers.getChildren().add(artRow);
        }
    }


    @FXML
    private void showArticleDetailsPopup(Article article) {
        System.out.println("article sélectionné : " + article.getIdArticle() + " " + article.getNom());

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de l'article");
        alert.setHeaderText("Informations sur " + article.getIdArticle() + " " + article.getNom());
        alert.setContentText("nom de larticle : " + article.getNom() + "\n" +
                "description : " + article.getDescription() + "\n" +
                "prix : " + article.getPrix() + "\n" +
                "statut : " + article.getStatut().name() + "\n" +
                "quantité : " + article.getQuantite() + "\n" +
                "date de creation : " + article.getCreatedAt());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Handle the X button click
        stage.setOnCloseRequest(event -> {
            stage.close();
        });
        ButtonType updateButton = new ButtonType("Mettre à jour");
        ButtonType deleteButton = new ButtonType("Supprimer");

        alert.getButtonTypes().setAll(updateButton, deleteButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == updateButton) {
                showUpdatePopup(article);
                System.out.println("Mettre à jour les informations de l'article.");
            } else if (response == deleteButton) {
                // Afficher une pop-up de confirmation pour la suppression
                javafx.scene.control.Alert confirmationAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation de la suppression");
                confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet article ?");
                confirmationAlert.setContentText("Cette action est irréversible.");

                // Ajouter les boutons de confirmation
                ButtonType yesButton = new ButtonType("Oui");
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                confirmationAlert.showAndWait().ifPresent(confirmationResponse -> {
                    if (confirmationResponse == yesButton) {
                        // Effectuer la suppression
                        su.delete(article.getIdArticle());
                        System.out.println("article supprimé.");
                        loadArticle();
                    } else {
                        System.out.println("Suppression annulée.");
                    }
                });
            }
        });
    }

    @FXML
    private void showUpdatePopup(Article article) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Mettre à jour l'article");

        VBox popupLayout = new VBox(10);
        popupLayout.setPadding(new Insets(10));

        TextField nameField = new TextField(article.getNom());
        TextField priceField = new TextField(String.valueOf(article.getPrix()));
        TextArea descriptionField = new TextArea(article.getDescription());

        TextField imageUrlField = new TextField(article.getUrlImage());
        imageUrlField.setEditable(false);

        Button browseButton = new Button("Parcourir...");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        if (article.getUrlImage() != null && !article.getUrlImage().isEmpty()) {
            Image image = new Image(new File(article.getUrlImage()).toURI().toString());
            imageView.setImage(image);
        }

        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(MainUserInterface.GetPrimaryStage());

            if (selectedFile != null) {
                imageUrlField.setText(selectedFile.getAbsolutePath());
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            }
        });

        TextField createdByField = new TextField(String.valueOf(article.getCreatedBy()));
        TextField quantityField = new TextField(String.valueOf(article.getQuantite()));

        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>();
        statusChoiceBox.getItems().addAll("Disponible", "En rupture de stock");
        statusChoiceBox.setValue(article.getStatut() == statut_article.on_stock ? "Disponible" : "En rupture de stock");

        DatePicker createdAtPicker = new DatePicker();
        if (article.getCreatedAt() != null) {
            // Conversion de java.sql.Date à java.util.Date
            Date utilDate = new Date(article.getCreatedAt().getTime());
            // Conversion en LocalDate
            LocalDate localDate = utilDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            createdAtPicker.setValue(localDate);
        }


        Button saveButton = new Button("Enregistrer");

        saveButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();
                String imageUrl = imageUrlField.getText();
                String status = statusChoiceBox.getValue();
                java.sql.Date createdAt = createdAtPicker.getValue() != null ? java.sql.Date.valueOf(createdAtPicker.getValue()) : null;

                int createdBy = Integer.parseInt(createdByField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                float price = Float.parseFloat(priceField.getText());

                if (name.isEmpty() || description.isEmpty() || imageUrl.isEmpty() || createdAt == null) {
                    throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires.");
                }

                statut_article statusEnum = status.equals("Disponible") ? statut_article.on_stock : statut_article.out_of_stock;

                article.setNom(name);
                article.setDescription(description);
                article.setUrlImage(imageUrl);
                article.setPrix(price);
                article.setStatut(statusEnum);
                article.setCreatedAt(createdAt);
                article.setCreatedBy(createdBy);
                article.setQuantite(quantity);

                su.update(article);
                popupStage.close();
                loadArticle();

            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des valeurs valides pour les champs numériques.");
            } catch (IllegalArgumentException e) {
                showAlert("Champs obligatoires", e.getMessage());
            }
        });

        popupLayout.getChildren().addAll(nameField, priceField, descriptionField, imageUrlField, browseButton, imageView, createdByField, quantityField, statusChoiceBox, createdAtPicker, saveButton);

        Scene popupScene = new Scene(popupLayout, 400, 500);
        popupStage.setScene(popupScene);
        popupStage.show();
    }



    @FXML
    public void loadArticle() {
        System.out.println("Chargement des articles...");

        // Nettoyer la liste actuelle des utilisateurs
        vListUsers.getChildren().clear();

        HBox headerRow = new HBox(4);
        headerRow.setPrefHeight(32.0);
        headerRow.setPrefWidth(765.0);

        headerRow.setStyle("-fx-background-color: #398c3e; -fx-padding: 10px;");

        Label lblHeaderIdcat = new Label("id category");
        lblHeaderIdcat.setMinWidth(80);
        lblHeaderIdcat.setMaxWidth(80);
        lblHeaderIdcat.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderurlimg = new Label("Url image");
        lblHeaderurlimg.setMinWidth(80);
        lblHeaderurlimg.setMaxWidth(80);
        lblHeaderurlimg.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeadernom = new Label("nom");
        lblHeadernom.setMinWidth(80);
        lblHeadernom.setMaxWidth(80);
        lblHeadernom.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderPrix = new Label("prix");
        lblHeaderPrix.setMinWidth(80);
        lblHeaderPrix.setMaxWidth(80);
        lblHeaderPrix.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderDesc = new Label("description");
        lblHeaderDesc.setMinWidth(130);
        lblHeaderDesc.setMaxWidth(130);
        lblHeaderDesc.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderquant = new Label("quantite");
        lblHeaderquant.setMinWidth(105);
        lblHeaderquant.setMaxWidth(105);
        lblHeaderquant.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderStat = new Label("statut");
        lblHeaderStat.setMinWidth(80);
        lblHeaderStat.setMaxWidth(80);
        lblHeaderStat.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeadercreated = new Label("created at");
        lblHeadercreated.setMinWidth(80);
        lblHeadercreated.setMaxWidth(80);
        lblHeadercreated.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        headerRow.getChildren().addAll(lblHeaderIdcat, lblHeaderurlimg, lblHeadernom, lblHeaderPrix, lblHeaderDesc, lblHeaderquant, lblHeaderStat, lblHeadercreated);

        vListUsers.getChildren().add(headerRow);

        //List<Article> articleList = su.getAll();
        List<Article> articleList = su.getAll();
        List<Article> Temp = new ArrayList<>();
        //List<Article> articleList = su.getArticlesByCategoryId(CategoryID);
        for (Article article : articleList) {
            if(article.getCategorie().getId_categorie() == CategoryID) {
                //articleList.remove(article);
                Temp.add(article);
            }
        }

        articleList = Temp;


        for (Article article : articleList) {
            HBox artRow = new HBox(4);
            artRow.setPrefHeight(32.0);
            artRow.setPrefWidth(765.0);

            Label lblidcat = new Label(String.valueOf(article.getCategorie().getId_categorie()));
            lblidcat.setMinWidth(80);
            lblidcat.setMaxWidth(80);


            Label lblUrlimg = new Label(article.getUrlImage());
            lblUrlimg.setMinWidth(80);
            lblUrlimg.setMaxWidth(80);

            Label lblNom = new Label(article.getNom());
            lblNom.setMinWidth(80);
            lblNom.setMaxWidth(80);

            Label lblPrix = new Label(String.valueOf(article.getPrix()));
            lblPrix.setMinWidth(80);
            lblPrix.setMaxWidth(80);


            Label lblDesc = new Label(article.getDescription());
            lblDesc.setMinWidth(130);
            lblDesc.setMaxWidth(130);
            Label lblquant = new Label(String.valueOf(article.getQuantite()));
            lblquant.setMinWidth(130);
            lblquant.setMaxWidth(130);

            Label lblstat = new Label(article.getStatut().name());
            lblstat.setMinWidth(105);
            lblstat.setMaxWidth(105);



            Label lblcreatedat = new Label(article.getCreatedAt().toString());
            lblcreatedat.setMinWidth(80);
            lblcreatedat.setMaxWidth(80);

            artRow.getChildren().addAll(lblidcat, lblUrlimg, lblNom, lblPrix, lblDesc, lblquant, lblstat, lblcreatedat);

            Button deleteButton = new Button("Supprimer");
            deleteButton.setOnAction(event -> {
                javafx.scene.control.Alert confirmationAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation de la suppression");
                confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet article ?");
                confirmationAlert.setContentText("Cette action est irréversible.");

                ButtonType yesButton = new ButtonType("Oui");
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                confirmationAlert.showAndWait().ifPresent(confirmationResponse -> {
                    if (confirmationResponse == yesButton) {
                        // Effectuer la suppression
                        su.delete(article.getIdArticle());
                        System.out.println("artilce supprimé.");
                        loadArticle();
                    } else {
                        System.out.println("Suppression annulée.");
                    }
                });
            });

            artRow.getChildren().add(deleteButton);

            artRow.setOnMouseClicked(event -> showArticleDetailsPopup(article));

            vListUsers.getChildren().add(artRow);
        }
    }

    @FXML
    private void searchArticle(String criteria) {
        System.out.println("Recherche des utilisateurs pour le critère : " + criteria);

        vListUsers.getChildren().clear();

        HBox headerRow = new HBox(4);
        headerRow.setPrefHeight(32.0);
        headerRow.setPrefWidth(765.0);
        headerRow.setStyle("-fx-background-color: #398c3e; -fx-padding: 10px;");

        Label lblHeaderPrenom = new Label("First name");
        lblHeaderPrenom.setMinWidth(80);
        lblHeaderPrenom.setMaxWidth(80);
        lblHeaderPrenom.setStyle("-fx-text-fill: black;");

        Label lblHeaderNom = new Label("Last name");
        lblHeaderNom.setMinWidth(80);
        lblHeaderNom.setMaxWidth(80);
        lblHeaderNom.setStyle("-fx-text-fill: black;");

        Label lblHeaderCin = new Label("CIN");
        lblHeaderCin.setMinWidth(80);
        lblHeaderCin.setMaxWidth(80);
        lblHeaderCin.setStyle("-fx-text-fill: black;");

        Label lblHeaderAdress = new Label("Address");
        lblHeaderAdress.setMinWidth(80);
        lblHeaderAdress.setMaxWidth(80);
        lblHeaderAdress.setStyle("-fx-text-fill: black;");

        Label lblHeaderEmail = new Label("Email");
        lblHeaderEmail.setMinWidth(130);
        lblHeaderEmail.setMaxWidth(130);
        lblHeaderEmail.setStyle("-fx-text-fill: black;");

        Label lblHeaderRole = new Label("Role");
        lblHeaderRole.setMinWidth(105);
        lblHeaderRole.setMaxWidth(105);
        lblHeaderRole.setStyle("-fx-text-fill: black;");

        Label lblHeaderVerified = new Label("Verified");
        lblHeaderVerified.setMinWidth(80);
        lblHeaderVerified.setMaxWidth(80);
        lblHeaderVerified.setStyle("-fx-text-fill: black;");

        Label lblHeaderTransport = new Label("Transport");
        lblHeaderTransport.setMinWidth(80);
        lblHeaderTransport.setMaxWidth(80);
        lblHeaderTransport.setStyle("-fx-text-fill: black;");

        headerRow.getChildren().addAll(lblHeaderPrenom, lblHeaderNom, lblHeaderCin, lblHeaderAdress, lblHeaderEmail, lblHeaderRole, lblHeaderVerified, lblHeaderTransport);
        vListUsers.getChildren().add(headerRow);

        List<Article> articleList = su.search(criteria);

        for (Article article : articleList) {
            HBox artRow = new HBox(4);
            artRow.setPrefHeight(32.0);
            artRow.setPrefWidth(765.0);
            artRow.setStyle("-fx-padding: 10px;");

            Label lblidcat = new Label(String.valueOf(article.getCategorie().getId_categorie()));
            lblidcat.setMinWidth(80);
            lblidcat.setMaxWidth(80);;

            Label lblurlimg = new Label(article.getUrlImage());
            lblurlimg.setMinWidth(80);
            lblurlimg.setMaxWidth(80);

            Label lblnom = new Label(article.getNom());
            lblnom.setMinWidth(80);
            lblnom.setMaxWidth(80);

            Label lblPrix = new Label(String.valueOf(article.getPrix()));
            lblPrix.setMinWidth(80);
            lblPrix.setMaxWidth(80);


            Label lbldesc = new Label(article.getDescription());
            lbldesc.setMinWidth(130);
            lbldesc.setMaxWidth(130);
            Label lblquant = new Label(String.valueOf(article.getQuantite()));
            lblquant.setMinWidth(130);
            lblquant.setMaxWidth(130);


            Label lblstat = new Label(article.getStatut().toString());
            lblstat.setMinWidth(105);
            lblstat.setMaxWidth(105);


            Label lblcreatedat = new Label(article.getCreatedAt().toString());
            lblcreatedat.setMinWidth(80);
            lblcreatedat.setMaxWidth(80);

            artRow.getChildren().addAll(lblidcat, lblurlimg, lblnom, lblPrix, lbldesc, lblquant ,lblstat, lblcreatedat);

            vListUsers.getChildren().add(artRow);
        }
    }
    public void showArticlesForCategory(int idCategorie) {
        try {
            List<Article> articles = su.getArticlesByCategoryId(idCategorie); // Assurez-vous que cette méthode existe

            Stage popupStage = new Stage();
            popupStage.setTitle("Articles de la catégorie");

            VBox popupLayout = new VBox(10);
            popupLayout.setPadding(new Insets(10));

            Label titleLabel = new Label("Articles de la catégorie : " + idCategorie);
            popupLayout.getChildren().add(titleLabel);

            ListView<String> articleListView = new ListView<>();
            for (Article article : articles) {
                articleListView.getItems().add(article.getNom());
            }
            popupLayout.getChildren().add(articleListView);

            Button closeButton = new Button("Fermer");
            closeButton.setOnAction(e -> popupStage.close());
            popupLayout.getChildren().add(closeButton);

            Scene popupScene = new Scene(popupLayout, 300, 250);
            popupStage.setScene(popupScene);
            popupStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Une erreur est survenue.");
            errorAlert.showAndWait();
        }
    }



    @FXML
    private HBox headerhb;

    @FXML
    private HBox hbActions;

    @FXML
    private AnchorPane anCategories;

    @FXML
    private AnchorPane anCoverageArea;

    @FXML
    private AnchorPane anLogout;

    @FXML
    private AnchorPane anOrder;

    @FXML
    private AnchorPane anPendingUsers;

    @FXML
    private AnchorPane anRiders;

    @FXML
    private TextField anSearch;

    @FXML
    private AnchorPane anUsers;

    @FXML
    private ImageView imLogo;

    @FXML
    private VBox vListUsers;

    @FXML
    private HBox hbHedha;

    @FXML
    private void navigateToHome() {
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
    @FXML
    private void NavigateToGestionUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionUtilisateurs.fxml"));
            Scene GestionUtilisateursScene = new Scene(loader.load());

            Stage stage = (Stage) anLogout.getScene().getWindow();
            stage.setScene(GestionUtilisateursScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading SignUp.fxml.");
        }
    }
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
    @FXML
    private void NavigateToPendingUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionUsersVerification.fxml"));
            Scene GestionUtilisateursScene = new Scene(loader.load());

            Stage stage = (Stage) anPendingUsers.getScene().getWindow();
            stage.setScene(GestionUtilisateursScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading SignUp.fxml.");
        }
    }
    @FXML
    private void navigateToZones() {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionZoneAdmin.fxml"));
            Scene signUpScene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) anCoverageArea.getScene().getWindow();
            stage.setScene(signUpScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading SignUp.fxml.");
        }
    }
    @FXML
    private void navigateToCommandes() {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/commandeAdmin.fxml"));
            Scene signUpScene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) anOrder.getScene().getWindow();
            stage.setScene(signUpScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading SignUp.fxml.");
        }
    }

    @FXML
    void NavigateToGestionCategorie(MouseEvent event) {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionCategorie.fxml"));
            Scene signUpScene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) anCategories.getScene().getWindow();
            stage.setScene(signUpScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading SignUp.fxml.");
        }
    }
    @FXML
    public void normalEffect(javafx.scene.input.MouseEvent event) {
        ((AnchorPane) event.getSource()).setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

    }

    @FXML
    public void hoverEffect(javafx.scene.input.MouseEvent event) {
        ((AnchorPane) event.getSource()).setStyle("-fx-background-color: lightgrey; -fx-cursor: hand;");

    }

    @FXML
    public void handleAddArticleClick(javafx.scene.input.MouseEvent event) {
        openAddArticlePopup();
    }


    @FXML
    private void openAddArticlePopup() {
        // Create a new pop-up window (Stage)
        Stage popupStage = new Stage();
        popupStage.setTitle("Ajouter un article");

        // Create a layout (VBox)
        VBox popupLayout = new VBox(10);
        popupLayout.setPadding(new Insets(10));

        // Input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Nom de l'article");

        TextField priceField = new TextField();
        priceField.setPromptText("Prix");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description");

        TextField imageUrlField = new TextField();
        imageUrlField.setPromptText("Chemin de l'image");
        imageUrlField.setEditable(false); // Prevent manual text input

        Button browseButton = new Button("Parcourir...");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(MainUserInterface.GetPrimaryStage());

            if (selectedFile != null) {
                imageUrlField.setText(selectedFile.getAbsolutePath());
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            }
        });


        TextField createdByField = new TextField();
        createdByField.setPromptText("Créé par (ID utilisateur)");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantité");

        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>();
        statusChoiceBox.getItems().addAll("Disponible", "En rupture de stock");
        statusChoiceBox.setValue("Disponible");


        // DatePicker with today's date as default and no other dates selectable
        DatePicker createdAtPicker = new DatePicker();
        createdAtPicker.setValue(LocalDate.now()); // Sets today's date automatically
        createdAtPicker.setPromptText("Date de création");

        // Restrict the DatePicker to today's date only
        createdAtPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(!date.isEqual(LocalDate.now())); // Disable all dates except today
            }
        });

        // Save button
        Button saveButton = new Button("Enregistrer");

        // Handle save button click
        saveButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();
                String imageUrl = imageUrlField.getText();
                String status = statusChoiceBox.getValue();
                java.sql.Date createdAt = createdAtPicker.getValue() != null ? java.sql.Date.valueOf(createdAtPicker.getValue()) : null;

                int createdBy = Integer.parseInt(createdByField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                float price = Float.parseFloat(priceField.getText());
                int categoryId = CategoryID;
                System.out.println(categoryId + " Displayed ");

                // Check for empty fields
                if (name.isEmpty() || description.isEmpty() || imageUrl.isEmpty() || createdAt == null) {
                    throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires.");
                }

                statut_article statusenum = statut_article.out_of_stock;

                if (Objects.equals(status, "on_stock")) statusenum = statut_article.on_stock;
                // Create new Article object
                Article newArticle = new Article(imageUrl, new Categorie(categoryId), name, price, description, createdBy, quantity, statusenum, createdAt, 0);

                // Add article to database
                CrudArticle.StaticAdd(newArticle);

                // Close pop-up
                popupStage.close();

                // Refresh articles list (optional)
                loadArticle();

            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des valeurs valides pour les champs numériques.");
            } catch (IllegalArgumentException e) {
                showAlert("Champs obligatoires", e.getMessage());
            }
        });

        // Add components to layout

        popupLayout.getChildren().addAll(nameField, priceField, descriptionField, imageUrlField, browseButton, createdByField, quantityField, statusChoiceBox, createdAtPicker, saveButton);

        // Create scene and set stage
        Scene popupScene = new Scene(popupLayout, 400, 500);
        popupStage.setScene(popupScene);
        popupStage.show();

    }

    // Helper method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene SignInScene = new Scene(loader.load());

            Stage stage = (Stage) anLogout.getScene().getWindow();
            stage.setScene(SignInScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading SignUp.fxml.");
        }
    }
}
