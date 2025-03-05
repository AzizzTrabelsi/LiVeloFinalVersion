package controllers;
import javafx.stage.Modality;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import services.CrudCategorie;
import tests.MainUserInterface;
import net.minidev.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import services.Authentification;
import javafx.scene.Node;
import java.util.Optional;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;

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

public class adminCat implements Initializable  {
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
        alert.setContentText( "Id category : " + categorie.getId_categorie() + "\n" +
                "Name: " + categorie.getNom() + "\n" +

                "Description : " + categorie.getDescription());


        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Handle the X button click
        stage.setOnCloseRequest(event -> {
            stage.close();
        });


        ButtonType updateButton = new ButtonType("Update");
        ButtonType deleteButton = new ButtonType("Delete");
        ButtonType viewArticlesButton = new ButtonType("View Items");
        alert.getButtonTypes().setAll(updateButton, deleteButton, viewArticlesButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == updateButton) {
                showUpdatePopup(categorie);
                System.out.println("Update Category Information");
            } else if (response == deleteButton) {
                // Afficher une pop-up de confirmation pour la suppression
                javafx.scene.control.Alert confirmationAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Confirmation");
                confirmationAlert.setHeaderText("Are you sure you want to delete this category?");
                confirmationAlert.setContentText("This action is irreversible.");

                // Ajouter les boutons de confirmation
                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                confirmationAlert.showAndWait().ifPresent(confirmationResponse -> {
                    if (confirmationResponse == yesButton) {
                        // Effectuer la suppression
                        su.delete(categorie.getId_categorie());
                        System.out.println("Category deleted..");
                        loadCategory();
                    } else {
                        System.out.println("Deletion canceled.");
                    }
                });
            } else if (response == viewArticlesButton) {
                // TODO: Display the articles page given a category ID

               MainUserInterface.switchScene(MainUserInterface.GetPrimaryStage(),"/adminArt.fxml");
            }

        });


    }


    @FXML
    private void showUpdatePopup(Categorie categorie) {
        Dialog<Categorie> dialog = new Dialog<>();
        dialog.setTitle("Update Category ");
        dialog.setHeaderText("Edit Category Information");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);



        TextField nomField = new TextField(categorie.getNom());
        TextField descField = new TextField(categorie.getDescription());
        //TextField imgField = new TextField(categorie.getUrl_image());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        // grid.add(new Label("url image:"), 0, 2);
        //grid.add(imgField, 1, 2);
        Label createdByLabel = new Label();
        createdByLabel.setText("This category is created by : " + afficherNomCompletUtilisateur());
        TextField createdByField = new TextField();
        createdByField.setPromptText("created by (user id)");
        createdByField.setVisible(false);
        int idUtilisateur = afficherIdUtilisateur(); // Appelle la méthode pour récupérer l'ID

// Afficher l'ID sous forme de chaîne dans le TextField
        createdByField.setText(String.valueOf(idUtilisateur)); // TextField attend une chaîne, donc on convertit l'ID en chaîne

// Stocker l'ID utilisateur sous forme d'entier dans les données utilisateur
        createdByField.setUserData(idUtilisateur);

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
        popupStage.setTitle("Add category");

        // Créer un layout pour la popup (VBox)
        VBox popupLayout = new VBox(10);
        popupLayout.setPadding(new Insets(10));

        // Champs de saisie
        TextField nameField = new TextField();
        nameField.setPromptText("Category Name");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description ...");
//        TextField imageUrlField = new TextField();
//        imageUrlField.setPromptText("URL de l'image");
        Label createdByLabel = new Label();
        createdByLabel.setText("This category is created by : " + afficherNomCompletUtilisateur());
        TextField createdByField = new TextField();
        createdByField.setPromptText("Créé par (ID utilisateur)");
        createdByField.setVisible(false);
        int idUtilisateur = afficherIdUtilisateur(); // Appelle la méthode pour récupérer l'ID

// Afficher l'ID sous forme de chaîne dans le TextField
        createdByField.setText(String.valueOf(idUtilisateur)); // TextField attend une chaîne, donc on convertit l'ID en chaîne

// Stocker l'ID utilisateur sous forme d'entier dans les données utilisateur
        createdByField.setUserData(idUtilisateur);
        // Créer un bouton pour enregistrer la catégorie
        Button saveButton = new Button("Save");

        // Gérer l'événement du bouton Enregistrer
        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            int createdBy = Integer.parseInt(createdByField.getText());
            //String imageUrl = imageUrlField.getText();

            // Vérifier si les champs ne sont pas vides
            if (!name.isEmpty() && !description.isEmpty()/* && !imageUrl.isEmpty()*/) {
                // Créer une nouvelle catégorie
                Categorie newCategory = new Categorie(name, description,createdBy); // Assurez-vous que votre constructeur est correct

                // Ajouter la catégorie à la base de données ou au service
                su.add(newCategory);  // Ajoutez la catégorie à votre service de gestion des catégories

                // Fermer la popup
                popupStage.close();

                // Recharger la liste des catégories (ou toute autre action nécessaire)
                loadCategory();
            } else {
                // Si des champs sont vides, afficher un message d'erreur

                Alert alert = new Alert(AlertType.ERROR, "Please fill in all fields.");
                alert.showAndWait();
            }
        });

        // Ajouter les champs et le bouton au layout de la popup
        popupLayout.getChildren().addAll(nameField, descriptionField, createdByLabel,createdByField,saveButton);

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
    private ImageView robot;
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
    private void navigateToChatbot() {
        System.out.println("Ouverture du Chatbot..."); // Pour vérifier l'ouverture

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/popupchatbot.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du chatbot si besoin (ex: pour passer des paramètres)
            chatbot controller = loader.getController();
            // Si le chatbot a besoin de paramètres, tu peux les passer ici
            // controller.setSomeParameter(value);

            // Ouvrir la nouvelle fenêtre du chatbot
            Stage stage = new Stage();
            stage.setTitle("Chatbot");
            stage.setScene(new Scene(root));

            // Rendre la fenêtre modale pour bloquer l'interaction avec la fenêtre principale
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(robot.getScene().getWindow());

            stage.showAndWait(); // Attend la fermeture avant de continuer

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le Chatbot.", Alert.AlertType.ERROR);
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
    private void NavigateToGestionCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminCat.fxml"));
            Scene GestionCategorieScene = new Scene(loader.load());

            Stage stage = (Stage) anCategories.getScene().getWindow();
            stage.setScene(GestionCategorieScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading gestionategorie.fxml.");
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
    private void navigateToAvis() {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/avisAdmin.fxml"));
            Scene signUpScene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) anRiders.getScene().getWindow();
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
