package controllers;
import javafx.scene.Node;
import java.util.Optional;
import javafx.scene.control.Alert.AlertType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Categorie;
import models.User;
import services.CrudCategorie;
import services.CrudUser;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import tests.MainUserInterface;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
public class GestionCategorie implements Initializable  {
    private CrudCategorie su = new CrudCategorie();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Afficher tous les utilisateurs au démarrage
        Show(null);

        // Ajouter un écouteur sur le champ de recherche
        anSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // Si le champ de recherche est vide, afficher tous les utilisateurs
                loadCategory();
            } else {
                // Sinon, effectuer une recherche
                searchCategories(newValue);
            }
        });
    }
    @FXML
    void Show(ActionEvent showEvent) {
        hbHedha.getChildren().clear();
        vListUsers.getChildren().clear(); // Nettoie la liste avant d'ajouter les nouveaux éléments

        List<Categorie> categorieList = su.getAll();

        for (Categorie category : categorieList) {
            HBox catRow = new HBox(4);
            catRow.setPrefHeight(32.0);
            catRow.setPrefWidth(200.0); // Ajustement de la largeur pour deux champs seulement

            Label lbnom = new Label(category.getNom());
            lbnom.setMinWidth(80);
            lbnom.setMaxWidth(80);

            Label lbdescrip = new Label(category.getDescription());
            lbdescrip.setMinWidth(80);
            lbdescrip.setMaxWidth(80);

            //Label lblimg = new Label(category.getUrl_image());
           // lblimg.setMinWidth(80); // Définit la largeur de l'image
           // lblimg.setMaxWidth(80);// Définit la hauteur de l'image



            catRow.getChildren().addAll(lbnom, lbdescrip);

            catRow.setOnMouseClicked(event -> showCategoryDetailsPopup((category)));

            vListUsers.getChildren().add(catRow);
        }
    }
    @FXML
    private void showCategoryDetailsPopup(Categorie categorie) {

        GestionArticle.CategoryID = categorie.getId_categorie();
        System.out.println("categorie ID : " + GestionArticle.CategoryID);
        System.out.println("categorie sélectionné : " + categorie.getNom() + " " + categorie.getDescription());

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Category Details ");
        alert.setContentText( "ID category : " + categorie.getId_categorie() + "\n" +
                "Name : " + categorie.getNom() + "\n" +
                "Description : " + categorie.getDescription());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();


        // Handle the X button click
        stage.setOnCloseRequest(event -> {
            stage.close();
        });

        ButtonType viewArticlesButton = new ButtonType("show items");
        alert.getButtonTypes().setAll(viewArticlesButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == viewArticlesButton) {
                // TODO: Display the articles page given a category ID
                MainUserInterface.switchScene(MainUserInterface.GetPrimaryStage(),"/GestionArticle.fxml");
            }
        });
    }



    @FXML
    private void showUpdatePopup(Categorie categorie) {
        Dialog<Categorie> dialog = new Dialog<>();
        dialog.setTitle("Mettre à jour la catégorie ");
        dialog.setHeaderText("Modifier les informations de la catégorie");

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);



        TextField nomField = new TextField(categorie.getNom());
        TextField descField = new TextField(categorie.getDescription());
        //TextField imgField = new TextField(categorie.getUrl_image());

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
       // grid.add(new Label("url image:"), 0, 2);
        //grid.add(imgField, 1, 2);


        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Update the user object with the new values
                categorie.setNom(nomField.getText());
                categorie.setDescription(descField.getText());
               // categorie.setUrl_image(imgField.getText());


                // Save the updated user to the database
                su.update(categorie);

                // Refresh the user list in the UI
                loadCategory(); // Call loadUsers to refresh the list

                return categorie;
            }
            return null;
        });

        dialog.showAndWait();
    }
    @FXML
    public void loadCategory() {
        System.out.println("Chargement des catégories...");

        // Nettoyer la liste actuelle des utilisateurs
        vListUsers.getChildren().clear();

        // Création de l'en-tête
        HBox headerRow = new HBox(10);
        headerRow.setPrefHeight(32.0);
        headerRow.setPrefWidth(765.0);
        headerRow.setStyle("-fx-background-color: #398c3e; -fx-padding: 10px;");

        Label lblHeaderNom = new Label("Category Name");
        lblHeaderNom.setMinWidth(150);
        lblHeaderNom.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label lblHeaderDesc = new Label("Description");
        lblHeaderDesc.setMinWidth(300);
        lblHeaderDesc.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
       // Label lblHeaderImg = new Label("url image");
       // lblHeaderImg.setMinWidth(300);
        //lblHeaderImg.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");



        headerRow.getChildren().addAll(lblHeaderNom, lblHeaderDesc);
        vListUsers.getChildren().add(headerRow);

        // Récupération des catégories
        List<Categorie> categorieList = su.getAll();

        for (Categorie categorie : categorieList) {
            HBox catRow = new HBox(10);
            catRow.setPrefHeight(32.0);
            catRow.setPrefWidth(765.0);
            catRow.setStyle("-fx-padding: 5px; -fx-border-color: lightgray;");

            Label lblNom = new Label(categorie.getNom());
            lblNom.setMinWidth(150);

            Label lblDesc = new Label(categorie.getDescription());
            lblDesc.setMinWidth(300);
           // Label lblimg = new Label(categorie.getUrl_image());
            //lblimg.setMinWidth(300);
            catRow.getChildren().addAll(lblNom, lblDesc);
           // Button deleteButton = new Button("Supprimer");
//            deleteButton.setOnAction(event -> {
//                javafx.scene.control.Alert confirmationAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
//                confirmationAlert.setTitle("Confirmation de la suppression");
//                confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette categorie ?");
//                confirmationAlert.setContentText("Cette action est irréversible.");
//
//                ButtonType yesButton = new ButtonType("Oui");
//                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
//
//                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);
//
//                confirmationAlert.showAndWait().ifPresent(confirmationResponse -> {
//                    if (confirmationResponse == yesButton) {
//                        // Effectuer la suppression
//                        su.delete(categorie.getId_categorie());
//                        System.out.println("categorie supprimé.");
//                        loadCategory();
//                    } else {
//                        System.out.println("Suppression annulée.");
//                    }
//                });
//            });
//
//            catRow.getChildren().add(deleteButton);

            catRow.setOnMouseClicked(event -> showCategoryDetailsPopup(categorie));

            vListUsers.getChildren().add(catRow);
        }

    }




    @FXML
    private void searchCategories(String criteria) {
        System.out.println("Recherche des categories pour le critère : " + criteria);

        vListUsers.getChildren().clear();

        HBox headerRow = new HBox(4);
        headerRow.setPrefHeight(32.0);
        headerRow.setPrefWidth(765.0);
        headerRow.setStyle("-fx-background-color: #398c3e; -fx-padding: 10px;");

        Label lblHeaderNom = new Label("Category name");
        lblHeaderNom.setMinWidth(80);
        lblHeaderNom.setMaxWidth(80);
        lblHeaderNom.setStyle("-fx-text-fill: black;");

        Label lblHeaderDesc = new Label("Category description");
        lblHeaderDesc.setMinWidth(80);
        lblHeaderDesc.setMaxWidth(80);
        lblHeaderDesc.setStyle("-fx-text-fill: black;");



        headerRow.getChildren().addAll(lblHeaderNom, lblHeaderDesc);
        vListUsers.getChildren().add(headerRow);

        List<Categorie> categorieList = su.search(criteria);

        for (Categorie categorie : categorieList) {
            HBox catRow = new HBox(4);
            catRow.setPrefHeight(32.0);
            catRow.setPrefWidth(765.0);
            catRow.setStyle("-fx-padding: 10px;");

            Label lblNom = new Label(categorie.getNom());
            lblNom.setMinWidth(80);
            lblNom.setMaxWidth(80);

            Label lblDesc = new Label(categorie.getDescription());
            lblDesc.setMinWidth(80);
            lblDesc.setMaxWidth(80);



            catRow.getChildren().addAll(lblNom, lblDesc);

            vListUsers.getChildren().add(catRow);
        }
    }
    @FXML
    private void handleAddCategoryClick(MouseEvent event) {
        // Logique pour ouvrir le popup d'ajout de catégorie
        System.out.println("Icone cliquée pour ajouter une catégorie");

        // Appelez une méthode pour ouvrir la popup d'ajout
        openAddCategoryPopup();
    }
    @FXML
    private void openAddCategoryPopup() {
        // Créer une nouvelle fenêtre (popup)
        Stage popupStage = new Stage();
        popupStage.setTitle("Ajouter une catégorie");

        // Créer un layout pour la popup (VBox)
        VBox popupLayout = new VBox(10);
        popupLayout.setPadding(new Insets(10));

        // Champs de saisie
        TextField nameField = new TextField();
        nameField.setPromptText("Nom de la catégorie");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description de la catégorie");
//        TextField imageUrlField = new TextField();
//        imageUrlField.setPromptText("URL de l'image");

        // Créer un bouton pour enregistrer la catégorie
        Button saveButton = new Button("Enregistrer");

        // Gérer l'événement du bouton Enregistrer
        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            //String imageUrl = imageUrlField.getText();

            // Vérifier si les champs ne sont pas vides
            if (!name.isEmpty() && !description.isEmpty()/* && !imageUrl.isEmpty()*/) {
                // Créer une nouvelle catégorie
                Categorie newCategory = new Categorie(name, description); // Assurez-vous que votre constructeur est correct

                // Ajouter la catégorie à la base de données ou au service
                su.add(newCategory);  // Ajoutez la catégorie à votre service de gestion des catégories

                // Fermer la popup
                popupStage.close();

                // Recharger la liste des catégories (ou toute autre action nécessaire)
                loadCategory();
            } else {
                // Si des champs sont vides, afficher un message d'erreur
                Alert alert = new Alert(AlertType.ERROR, "Veuillez remplir tous les champs.");
                alert.showAndWait();
            }
        });

        // Ajouter les champs et le bouton au layout de la popup
        popupLayout.getChildren().addAll(nameField, descriptionField, saveButton);

        // Créer la scène et ajouter le layout
        Scene popupScene = new Scene(popupLayout, 300, 250);
        popupStage.setScene(popupScene);

        // Afficher la popup
        popupStage.show();
    }


    @FXML
    private AnchorPane anCategories;

    @FXML
    private AnchorPane anCoverageArea;

    @FXML
    private AnchorPane anLogout;
    @FXML
    private ImageView idadd;
    @FXML
    private AnchorPane anOrder;

    @FXML
    private AnchorPane anPendingUsers;
    @FXML
    private Label lblimg;

    @FXML
    private AnchorPane anRiders;

    @FXML
    private TextField anSearch;

    @FXML
    private AnchorPane anUsers;

    @FXML
    private HBox hbHedha;

    @FXML
    private HBox headerhb;

    @FXML
    private ImageView imLogo;

    @FXML
    private Label lbdescrip;

    @FXML
    private Label lbnom;

    @FXML
    private VBox vListUsers;



    @FXML
    void navigateToHome(MouseEvent event) {
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
    public void normalEffect(javafx.scene.input.MouseEvent event) {
        ((AnchorPane) event.getSource()).setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

    }

    @FXML
    public void hoverEffect(javafx.scene.input.MouseEvent event) {
        ((AnchorPane) event.getSource()).setStyle("-fx-background-color: lightgrey; -fx-cursor: hand;");

    }

}
