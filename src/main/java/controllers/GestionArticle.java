package controllers;
import java.awt.event.MouseEvent;
import java.io.File;

import javafx.scene.Parent;
import javafx.stage.Modality;
import services.Authentification;
import java.util.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;

import java.time.LocalDate;

import net.minidev.json.JSONObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Article;
import models.Categorie;
import models.User;

import models.statut_article;
import services.CrudArticle;
import javafx.scene.control.TextField;
import services.CrudUser;
import tests.MainUserInterface;


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
            lblIdCategorie.setMinWidth(40);
            lblIdCategorie.setMaxWidth(40);
            // Afficher le chemin pour debug
            System.out.println("🔍 Chemin de l'image : " + article.getUrlImage());

// ImageView pour afficher l'image de l'article
            // Afficher le chemin pour debug
            System.out.println("🔍 Chemin de l'image : " + article.getUrlImage());

// ImageView pour afficher l'image de l'article
            ImageView imageView = new ImageView();
            imageView.setFitHeight(80);  // Ajuste la hauteur
            imageView.setFitWidth(80);   // Ajuste la largeur

            try {
                // Construire le chemin de l'image dynamiquement sans ajouter "/image/" deux fois
                String imagePath = article.getUrlImage(); // Article a déjà le chemin relatif

                // Vérifier si l'image est au format JPG, PNG, etc.
                String[] extensions = {".jpg", ".png"};
                URL imageUrl = null;

                for (String ext : extensions) {
                    // Tenter de charger l'image avec chaque extension
                    imageUrl = getClass().getResource("/" + imagePath + ext);
                    if (imageUrl != null) {
                        break; // Si l'image est trouvée, on arrête la recherche
                    }
                }

                if (imageUrl == null) {
                    System.out.println("⚠ Image non trouvée pour " + imagePath);
                } else {
                    System.out.println("✅ Image trouvée : " + imageUrl);
                    Image img = new Image(imageUrl.toExternalForm());
                    imageView.setImage(img);
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }





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
            artRow.getChildren().addAll(lblIdCategorie, imageView, lblNom, lblPrix, lblDesc, lblStatu, lblQuant, lblcreatedat);

            // Ajouter un événement au clic sur la ligne pour afficher les détails de l'article
            artRow.setOnMouseClicked(event -> showArticleDetailsPopup(article));

            // Ajouter cette ligne à la vue principale (vListUsers)
            vListUsers.getChildren().add(artRow);
        }
    }


    @FXML
    private void showArticleDetailsPopup(Article article) {
        System.out.println("article sélectionné :  " + article.getNom());

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Item Details");
        alert.setHeaderText("Information on "  + article.getNom());
        alert.setContentText("Item Name: " + article.getNom() + "\n" +
                "Description : " + article.getDescription() + "\n" +
                "Price : " + article.getPrix() + "\n" +
                "Availability : " + article.getStatut().name() + "\n" +
                "Quantity : " + article.getQuantite() + "\n" +
                "Creation Date : " + article.getCreatedAt());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Handle the X button click
        stage.setOnCloseRequest(event -> {
            stage.close();
        });
        ButtonType updateButton = new ButtonType("Update");
        ButtonType deleteButton = new ButtonType("Delete");

        alert.getButtonTypes().setAll(updateButton, deleteButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == updateButton) {
                showUpdatePopup(article);
                System.out.println("Update Item Information");
            } else if (response == deleteButton) {
                // Afficher une pop-up de confirmation pour la suppression
                javafx.scene.control.Alert confirmationAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Confirmation");
                confirmationAlert.setHeaderText("Are you sure you want to delete this item?");
                confirmationAlert.setContentText("This action is irreversible.");

                // Ajouter les boutons de confirmation
                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                confirmationAlert.showAndWait().ifPresent(confirmationResponse -> {
                    if (confirmationResponse == yesButton) {
                        // Effectuer la suppression
                        su.delete(article.getIdArticle());
                        System.out.println("Item deleted.");
                        loadArticle();
                    } else {
                        System.out.println("Deletion canceled.");
                    }
                });
            }
        });
    }

    @FXML
    private void showUpdatePopup(Article article) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Update item");

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

            // Filtrer pour afficher uniquement les images
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            // Spécifier le dossier de départ pour le FileChooser
            File initialDirectory = new File("src/main/resources/image"); // Dossier "image" dans le projet
            if (initialDirectory.exists()) {
                fileChooser.setInitialDirectory(initialDirectory); // Définir le dossier initial
            }

            // Afficher le FileChooser
            File selectedFile = fileChooser.showOpenDialog(MainUserInterface.GetPrimaryStage());

            if (selectedFile != null) {
                // Extraire le nom de l'image sans l'extension
                String fileNameWithoutExtension = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.'));

                // Construire le chemin relatif (dossier "image" + nom du fichier sans extension)
                String relativePath = "image/" + fileNameWithoutExtension;

                // Mettre à jour le champ texte avec le chemin relatif
                imageUrlField.setText(relativePath);

                // Charger l'image dans l'ImageView (optionnel, si vous souhaitez voir l'aperçu)
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            }
        });





        TextField createdByField = new TextField(String.valueOf(article.getCreatedBy()));
        // Rendre le TextField invisible
        createdByField.setVisible(false);
        Label createdByLabel = new Label();
        createdByLabel.setText("This item is created by : : " + afficherNomCompletUtilisateur());

        Spinner<Integer> quantitySpinner = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        quantitySpinner.setValueFactory(valueFactory);

// Personnaliser l'apparence du Spinner
        quantitySpinner.setPromptText("Quantity");

// Vous pouvez également ajouter des boutons à côté du Spinner pour + et -
        quantitySpinner.setEditable(true);

        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>();
        statusChoiceBox.getItems().addAll("Available", "out of stock");
        statusChoiceBox.setValue(article.getStatut() == statut_article.on_stock ? "Available" : "Out of stock");

        // DatePicker with today's date as default and no other dates selectable
        DatePicker createdAtPicker = new DatePicker();
        createdAtPicker.setValue(LocalDate.now()); // Sets today's date automatically
        createdAtPicker.setPromptText("Creation Date");

        // Restrict the DatePicker to today's date only
        createdAtPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(!date.isEqual(LocalDate.now())); // Disable all dates except today
            }
        });


        Button saveButton = new Button("Save");

        saveButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();
                String imageUrl = imageUrlField.getText(); // Obtenez l'URL de l'image sélectionnée
                String status = statusChoiceBox.getValue();
                java.sql.Date createdAt = createdAtPicker.getValue() != null ? java.sql.Date.valueOf(createdAtPicker.getValue()) : null;

                int createdBy = Integer.parseInt(createdByField.getText());
                // Obtenir la quantité à partir du Spinner
                int quantity = quantitySpinner.getValue();

                // Vérification pour empêcher la quantité de 0
                if (quantity == 0) {
                    throw new IllegalArgumentException("The quantity cannot be zero.");
                }
                float price = Float.parseFloat(priceField.getText());

                if (name.isEmpty() || description.isEmpty() || imageUrl.isEmpty() || createdAt == null) {
                    throw new IllegalArgumentException("Please fill in all required fields.");
                }

                statut_article statusEnum = status.equals("Available") ? statut_article.on_stock : statut_article.out_of_stock;

                article.setNom(name);
                article.setDescription(description);
                article.setUrlImage(imageUrl); // Enregistrez le chemin relatif de l'image
                article.setPrix(price);
                article.setStatut(statusEnum);
                article.setCreatedAt(createdAt);
                article.setCreatedBy(createdBy);
                article.setQuantite(quantity);

                su.update(article); // Met à jour l'article dans la base de données

                // Réactualisez l'interface pour afficher l'image
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Image image = new Image(new File(imageUrl).toURI().toString());
                    imageView.setImage(image); // Met à jour l'image dans l'ImageView
                }

                popupStage.close();
                loadArticle();

            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter valid values for numeric fields.");
            } catch (IllegalArgumentException e) {
                showAlert("Required Fields", e.getMessage());
            }
        });


        popupLayout.getChildren().addAll(nameField, priceField, descriptionField, imageUrlField, browseButton, imageView,createdByLabel, createdByField, quantitySpinner, statusChoiceBox, createdAtPicker, saveButton);

        Scene popupScene = new Scene(popupLayout, 400, 500);
        popupStage.setScene(popupScene);
        popupStage.show();
    }



    @FXML
    public void loadArticle() {
        System.out.println("Loading items...");

        // Nettoyer la liste actuelle des utilisateurs
        vListUsers.getChildren().clear();

        HBox headerRow = new HBox(4);
        headerRow.setPrefHeight(32.0);
        headerRow.setPrefWidth(765.0);

        headerRow.setStyle("-fx-background-color: #398c3e; -fx-padding: 10px;");

        Label lblHeaderIdcat = new Label("Category ID");
        lblHeaderIdcat.setMinWidth(80);
        lblHeaderIdcat.setMaxWidth(80);
        lblHeaderIdcat.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderurlimg = new Label("image");
        lblHeaderurlimg.setMinWidth(80);
        lblHeaderurlimg.setMaxWidth(80);
        lblHeaderurlimg.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeadernom = new Label("Name");
        lblHeadernom.setMinWidth(80);
        lblHeadernom.setMaxWidth(80);
        lblHeadernom.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderPrix = new Label("Price");
        lblHeaderPrix.setMinWidth(80);
        lblHeaderPrix.setMaxWidth(80);
        lblHeaderPrix.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderDesc = new Label("Description");
        lblHeaderDesc.setMinWidth(130);
        lblHeaderDesc.setMaxWidth(130);
        lblHeaderDesc.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderquant = new Label("Quantity");
        lblHeaderquant.setMinWidth(105);
        lblHeaderquant.setMaxWidth(105);
        lblHeaderquant.setStyle("-fx-text-fill: black;"); // Changer la couleur du texte en blanc

        Label lblHeaderStat = new Label("availability");
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


            // Afficher le chemin pour debug
            System.out.println("🔍 Chemin de l'image : " + article.getUrlImage());

// ImageView pour afficher l'image de l'article
            ImageView imageView = new ImageView();
            imageView.setFitHeight(80);  // Ajuste la hauteur
            imageView.setFitWidth(80);   // Ajuste la largeur

            try {
                // Construire le chemin de l'image dynamiquement sans ajouter "/image/" deux fois
                String imagePath = article.getUrlImage(); // Article a déjà le chemin relatif

                // Vérifier si l'image est au format JPG, PNG, etc.
                String[] extensions = {".jpg", ".png"};
                URL imageUrl = null;

                for (String ext : extensions) {
                    // Tenter de charger l'image avec chaque extension
                    imageUrl = getClass().getResource("/" + imagePath + ext);
                    if (imageUrl != null) {
                        break; // Si l'image est trouvée, on arrête la recherche
                    }
                }

                if (imageUrl == null) {
                    System.out.println("⚠ Image non trouvée pour " + imagePath);
                } else {
                    System.out.println("✅ Image trouvée : " + imageUrl);
                    Image img = new Image(imageUrl.toExternalForm());
                    imageView.setImage(img);
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }

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

            artRow.getChildren().addAll(lblidcat, imageView, lblNom, lblPrix, lblDesc, lblquant, lblstat, lblcreatedat);

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> {
                javafx.scene.control.Alert confirmationAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Confirmation");
                confirmationAlert.setHeaderText("Are you sure you want to delete this item?");
                confirmationAlert.setContentText("This action is irreversible..");

                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                confirmationAlert.showAndWait().ifPresent(confirmationResponse -> {
                    if (confirmationResponse == yesButton) {
                        // Effectuer la suppression
                        su.delete(article.getIdArticle());
                        System.out.println("deleted item.");
                        loadArticle();
                    } else {
                        System.out.println("deletion failed.");
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

        Label lblHeaderPrenom = new Label("ID category");
        lblHeaderPrenom.setMinWidth(80);
        lblHeaderPrenom.setMaxWidth(80);
        lblHeaderPrenom.setStyle("-fx-text-fill: black;");
        Article art = new Article();
        // Afficher le chemin pour debug
        System.out.println("🔍 Chemin de l'image : " + art.getUrlImage());

// ImageView pour afficher l'image de l'article
        // Afficher le chemin pour debug
        System.out.println("🔍 Chemin de l'image : " + art.getUrlImage());

// ImageView pour afficher l'image de l'article
        ImageView imageView = new ImageView();
        imageView.setFitHeight(80);  // Ajuste la hauteur
        imageView.setFitWidth(80);   // Ajuste la largeur

        try {
            // Construire le chemin de l'image dynamiquement sans ajouter "/image/" deux fois
            String imagePath = art.getUrlImage(); // Article a déjà le chemin relatif

            // Vérifier si l'image est au format JPG, PNG, etc.
            String[] extensions = {".jpg", ".png"};
            URL imageUrl = null;

            for (String ext : extensions) {
                // Tenter de charger l'image avec chaque extension
                imageUrl = getClass().getResource("/" + imagePath + ext);
                if (imageUrl != null) {
                    break; // Si l'image est trouvée, on arrête la recherche
                }
            }

            if (imageUrl == null) {
                System.out.println("⚠ Image non trouvée pour " + imagePath);
            } else {
                System.out.println("✅ Image trouvée : " + imageUrl);
                Image img = new Image(imageUrl.toExternalForm());
                imageView.setImage(img);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }


        Label lblHeaderCin = new Label("name");
        lblHeaderCin.setMinWidth(80);
        lblHeaderCin.setMaxWidth(80);
        lblHeaderCin.setStyle("-fx-text-fill: black;");

        Label lblHeaderAdress = new Label("Price");
        lblHeaderAdress.setMinWidth(80);
        lblHeaderAdress.setMaxWidth(80);
        lblHeaderAdress.setStyle("-fx-text-fill: black;");

        Label lblHeaderEmail = new Label("description");
        lblHeaderEmail.setMinWidth(130);
        lblHeaderEmail.setMaxWidth(130);
        lblHeaderEmail.setStyle("-fx-text-fill: black;");

        Label lblHeaderRole = new Label("quantity");
        lblHeaderRole.setMinWidth(105);
        lblHeaderRole.setMaxWidth(105);
        lblHeaderRole.setStyle("-fx-text-fill: black;");

        Label lblHeaderVerified = new Label("availability");
        lblHeaderVerified.setMinWidth(80);
        lblHeaderVerified.setMaxWidth(80);
        lblHeaderVerified.setStyle("-fx-text-fill: black;");

        Label lblHeaderTransport = new Label("creation date");
        lblHeaderTransport.setMinWidth(80);
        lblHeaderTransport.setMaxWidth(80);
        lblHeaderTransport.setStyle("-fx-text-fill: black;");

        headerRow.getChildren().addAll(lblHeaderPrenom, imageView, lblHeaderCin, lblHeaderAdress, lblHeaderEmail, lblHeaderRole, lblHeaderVerified, lblHeaderTransport);
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
            popupStage.setTitle("Category Items");

            VBox popupLayout = new VBox(10);
            popupLayout.setPadding(new Insets(10));

            Label titleLabel = new Label("Category Items: " + idCategorie);
            popupLayout.getChildren().add(titleLabel);

            ListView<String> articleListView = new ListView<>();
            for (Article article : articles) {
                articleListView.getItems().add(article.getNom());
            }
            popupLayout.getChildren().add(articleListView);

            Button closeButton = new Button("close");
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
    private ImageView tri;

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
    private void navigateToHomee() {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionCategorie.fxml"));
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
    private void navigateToHomeee() {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminCat.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionZoneAdmin.fxml"));
            Scene signUpScene = new Scene(loader.load());

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
    private void navigateback() {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminCat.fxml"));
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
    public void normalEffect(javafx.scene.input.MouseEvent event) {
        ((AnchorPane) event.getSource()).setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

    }

    @FXML
    public void hoverEffect(javafx.scene.input.MouseEvent event) {
        ((AnchorPane) event.getSource()).setStyle("-fx-background-color: lightgrey; -fx-cursor: hand;");

    }



    // Méthode pour récupérer le nom et le prénom de l'utilisateur connecté
    public String afficherNomCompletUtilisateur() {
        String token = Authentification.getToken();

        if (token != null) {
            JSONObject userInfo = new Authentification().decodeToken(token);
            if (userInfo != null) {
                String nom = (String) userInfo.get("nom");  // Récupérer le nom
                String prenom = (String) userInfo.get("prenom");  // Récupérer le prénom
                return nom + " " + prenom;  // Retourner le nom complet
            } else {
                System.out.println("Erreur dans le décodage du token.");
                return "";  // Retourner une chaîne vide en cas d'erreur
            }
        } else {
            System.out.println("Aucun token trouvé. L'utilisateur n'est pas connecté.");
            return "";  // Retourner une chaîne vide si aucun token n'est trouvé
        }
    }

    // Méthode pour récupérer l'ID de l'utilisateur connecté
    public int afficherIdUtilisateur() {
        String token = Authentification.getToken();

        if (token != null) {
            JSONObject userInfo = new Authentification().decodeToken(token);
            if (userInfo != null) {
                // Afficher tout le contenu du token pour diagnostic
                System.out.println("Contenu du token : " + userInfo.toJSONString());

                Object idObject = userInfo.get("idUser");  // Changer ici la clé de "id" à "idUser"
                System.out.println("ID dans le token : " + idObject);  // Affiche l'ID extrait du token

                if (idObject != null) {
                    // Vérification si l'ID est de type Integer
                    if (idObject instanceof Integer) {
                        return (int) idObject; // Retourner l'ID si c'est un Integer
                    } else {
                        try {
                            // Si l'ID est sous forme de String, on le convertit en entier
                            return Integer.parseInt((String) idObject);
                        } catch (NumberFormatException e) {
                            System.out.println("Erreur de conversion de l'ID utilisateur : " + idObject); // Affiche l'ID qui a causé l'erreur
                            return -1; // Retourner -1 en cas d'erreur de conversion
                        }
                    }
                } else {
                    System.out.println("L'ID est null dans le token.");
                    return -1; // Retourne -1 si l'ID est null
                }
            } else {
                System.out.println("Erreur dans le décodage du token.");
                return -1; // Retourne -1 en cas d'erreur de décodage
            }
        } else {
            System.out.println("Aucun token trouvé. L'utilisateur n'est pas connecté.");
            return -1; // Retourne -1 si aucun token n'est trouvé
        }
    }
    @FXML
    private ImageView stats;

    @FXML
    private void navigateTostats() {
        System.out.println("Ouverture du Chatbot..."); // Pour vérifier l'ouverture

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stats.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du chatbot si besoin (ex: pour passer des paramètres)
            Stats controller = loader.getController();
            // Si le chatbot a besoin de paramètres, tu peux les passer ici
            // controller.setSomeParameter(value);

            // Ouvrir la nouvelle fenêtre du chatbot
            Stage stage = new Stage();
            stage.setTitle("statistic");
            stage.setScene(new Scene(root));

            // Rendre la fenêtre modale pour bloquer l'interaction avec la fenêtre principale
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(stats.getScene().getWindow());

            stage.showAndWait(); // Attend la fermeture avant de continuer

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la stat.", Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }






    @FXML
    private void navigateTopie() {
        System.out.println("Ouverture des statistiques..."); // Pour vérifier l'ouverture

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pie.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du PieChart si besoin (ex: pour passer des paramètres)
            pie controller = loader.getController();
            // Si des paramètres doivent être passés, tu peux le faire ici
            // controller.setSomeParameter(value);

            // Ouvrir la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Statistiques");
            stage.setScene(new Scene(root));

            // Rendre la fenêtre modale pour bloquer l'interaction avec la fenêtre principale
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tri.getScene().getWindow()); // Assure-toi que stats est bien défini

            stage.showAndWait(); // Attend la fermeture avant de continuer

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir les statistiques.", Alert.AlertType.ERROR);
        }
    }




    @FXML
    public void handleAddArticleClick(javafx.scene.input.MouseEvent event) {
        openAddArticlePopup();
    }


    @FXML
    private void openAddArticlePopup() {
        // Create a new pop-up window (Stage)
        Stage popupStage = new Stage();
        popupStage.setTitle("Add item");

        // Create a layout (VBox)
        VBox popupLayout = new VBox(10);
        popupLayout.setPadding(new Insets(10));

        // Input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Item name");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

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

            // Filtrer pour afficher uniquement les images
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            // Spécifier le dossier de départ pour le FileChooser
            File initialDirectory = new File("src/main/resources/image"); // Dossier "image" dans le projet
            if (initialDirectory.exists()) {
                fileChooser.setInitialDirectory(initialDirectory); // Définir le dossier initial
            }

            // Afficher le FileChooser
            File selectedFile = fileChooser.showOpenDialog(MainUserInterface.GetPrimaryStage());

            if (selectedFile != null) {
                // Extraire le nom de l'image sans l'extension
                String fileNameWithoutExtension = selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.'));

                // Construire le chemin relatif (dossier "image" + nom du fichier sans extension)
                String relativePath = "image/" + fileNameWithoutExtension;

                // Mettre à jour le champ texte avec le chemin relatif
                imageUrlField.setText(relativePath);

                // Charger l'image dans l'ImageView (optionnel, si vous souhaitez voir l'aperçu)
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            }
        });
        Label createdByLabel = new Label();
        createdByLabel.setText("This item is created by : " + afficherNomCompletUtilisateur());

//        TextField createdByField = new TextField();
//        createdByField.setPromptText("Créé par (Nom utilisateur)");
//
//        // Récupérer le nom de l'utilisateur et l'afficher dans le champ
//        String nomUtilisateur = afficherNomUtilisateur(); // Appelle la méthode pour récupérer le nom
//        createdByField.setText(nomUtilisateur);
        TextField createdByField = new TextField();
        createdByField.setPromptText("Créé par (ID utilisateur)");
        createdByField.setVisible(false);
// Récupérer l'ID de l'utilisateur et l'afficher dans le champ
        int idUtilisateur = afficherIdUtilisateur(); // Appelle la méthode pour récupérer l'ID

// Afficher l'ID sous forme de chaîne dans le TextField
        createdByField.setText(String.valueOf(idUtilisateur)); // TextField attend une chaîne, donc on convertit l'ID en chaîne

// Stocker l'ID utilisateur sous forme d'entier dans les données utilisateur
        createdByField.setUserData(idUtilisateur); // Stocker l'ID utilisateur en tant qu'entier




        Spinner<Integer> quantitySpinner = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        quantitySpinner.setValueFactory(valueFactory);

// Personnaliser l'apparence du Spinner
        quantitySpinner.setPromptText("Quantity");

// Vous pouvez également ajouter des boutons à côté du Spinner pour + et -
        quantitySpinner.setEditable(true);

        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>();
        statusChoiceBox.getItems().addAll("Available", "Out of stock");
        statusChoiceBox.setValue("Available");
//        // Disable or Enable based on status selection
//        statusChoiceBox.setOnAction(event -> {
//            if (statusChoiceBox.getValue().equals("Out of stock")) {
//                quantitySpinner.getValueFactory().setValue(0); // Set quantity to 0 if Out of stock is selected
//                quantitySpinner.setDisable(true); // Disable the quantity spinner
//            } else {
//                quantitySpinner.setDisable(false); // Enable the quantity spinner if Available is selected
//            }
//        });
//
//        // Spinner listener to handle "Available" status
//        quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue == 0) {
//                statusChoiceBox.setValue("Out of stock");
//                statusChoiceBox.setDisable(true); // Disable status choice when quantity is 0
//            } else {
//                statusChoiceBox.setDisable(false); // Enable status choice when quantity is not 0
//            }
//        });

        // DatePicker with today's date as default and no other dates selectable
        DatePicker createdAtPicker = new DatePicker();
        createdAtPicker.setValue(LocalDate.now()); // Sets today's date automatically
        createdAtPicker.setPromptText("Creation date");

        // Restrict the DatePicker to today's date only
        createdAtPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(!date.isEqual(LocalDate.now())); // Disable all dates except today
            }
        });

        // Save button
        Button saveButton = new Button("Save");

        // Handle save button click
        saveButton.setOnAction(event -> {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();
                String imageUrl = imageUrlField.getText();
                String status = statusChoiceBox.getValue();
                java.sql.Date createdAt = createdAtPicker.getValue() != null ? java.sql.Date.valueOf(createdAtPicker.getValue()) : null;

                int createdBy = Integer.parseInt(createdByField.getText());
                // Obtenir la quantité à partir du Spinner
                int quantity = quantitySpinner.getValue();

                // Vérification pour empêcher la quantité de 0

                float price = Float.parseFloat(priceField.getText());
                int categoryId = CategoryID;
                System.out.println(categoryId + " Displayed ");

                // Check for empty fields
                if (name.isEmpty() || description.isEmpty() || imageUrl.isEmpty() || createdAt == null) {
                    throw new IllegalArgumentException("Please fill in all required fields.");
                }

                statut_article statusenum = statut_article.out_of_stock;

                if (Objects.equals(status, "on_stock")) statusenum = statut_article.on_stock;
                // Create new Article object
                Article newArticle = new Article(imageUrl, new Categorie(categoryId), name, price, description, createdBy, quantity, statusenum, createdAt, 0);

                // Add article to database
                CrudArticle.StaticAdd(newArticle);
                // Send SMS to clients
                CrudUser crudUser = new CrudUser(); // Créer une instance de CrudUser
                List<User> clients = crudUser.getByRole("client");  // Récupérer les utilisateurs avec le rôle "client"

// Vérifier si des clients ont été récupérés
                if (clients == null || clients.isEmpty()) {
                    System.err.println("Aucun client trouvé avec le rôle 'client'.");
                } else {
                    System.out.println("Clients récupérés : " + clients.size());
                    for (User client : clients) {
                        String phoneNumber = client.getNum_tel();  // Récupérer le numéro de téléphone
                        String fullName = client.getNom() + " " + client.getPrenom();  // Récupérer nom et prénom du client

                        if (phoneNumber == null || phoneNumber.isEmpty()) {
                            System.err.println("Numéro de téléphone vide pour le client : " + fullName);
                            continue;  // Ignorer ce client et passer au suivant
                        }

                        System.out.println("Envoi du SMS à " + fullName + " (" + phoneNumber + ")");

                        // Appeler la méthode d'envoi de SMS avec le nom et prénom du client
                        SMS.sendSms(phoneNumber, " " + fullName + ",\nUn nouveau produit a été ajouté ! Découvrez-le maintenant.");
                    }
                }



// Fermer la fenêtre pop-up après l'envoi des SMS
                popupStage.close();


                // Refresh articles list (optional)
                loadArticle();

            } catch (NumberFormatException e) {
                showAlert("Erreur", "Please enter valid values for numeric fields.");
            } catch (IllegalArgumentException e) {
                showAlert("Required fields", e.getMessage());
            }
        });

        // Add components to layout

        popupLayout.getChildren().addAll(nameField, priceField, descriptionField, imageUrlField, browseButton,createdByLabel, createdByField, quantitySpinner, statusChoiceBox, createdAtPicker, saveButton);

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
