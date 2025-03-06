package controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.minidev.json.JSONObject;
import services.Authentification;

import java.io.IOException;

public class SignIn {

    @FXML
    private Button BtnSignUp;

    @FXML
    private Button LoginButton;

    @FXML
    private TextField tfCIN;

    @FXML
    private ImageView imView;

    @FXML
    private Text txtForgotPassword;

    @FXML
    private PasswordField tfPassword;

    //  user id static
    public static int  id_User;

    private boolean isPasswordVisible = false;

    @FXML
    private void handleLogin() {
        String cin = tfCIN.getText();
        String password = tfPassword.getText();

        Authentification authService = new Authentification();

        String token = authService.login(cin, password);

        if (token != null) {
            System.out.println("Connexion réussie. Token : " + token);

            JSONObject userInfo = authService.decodeToken(token);
            if (userInfo != null) {
                String role = userInfo.get("role").toString();  // Get role as string
                // Get user Id when login
                id_User = (int) userInfo.get("idUser");
                boolean isVerified = Boolean.parseBoolean(userInfo.get("verified").toString()); // Get verified status as boolean

                if (!isVerified) {
                    navigateToHomeNotVerified();
                } else {
                    if (role.equals("admin")) {
                        navigateToHomeAdmin();
                    } else if (role.equals("partner")){
                        navigateToHomePartner();
                    }else if (role.equals("delivery_person")){
                        navigateToHomeLivreur();
                    }else if (role.equals("client")){
                        navigateToHomeClient();
                    }
                }
            } else {
                System.out.println("Erreur lors de la lecture du token.");
            }

        } else {
            System.out.println("Échec de la connexion.");
        }
    }

    @FXML
    private void navigateToHomeNotVerified() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeNotVerified.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfCIN.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        // Bind the LoginButton to the handleLogin method
        LoginButton.setOnAction(event -> handleLogin());

        // Bind the SignUp button to the navigateToSignUp method
        BtnSignUp.setOnAction(event -> navigateToSignUp());

        // Add event handler to the eye icon
        imView.setOnMouseClicked(event -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Switch back to PasswordField
            tfPassword.setVisible(true);
            tfPassword.setText(tfPassword.getText());
            isPasswordVisible = false;
        } else {
            // Switch to TextField to show the password
            tfPassword.setVisible(false);
            TextField textField = new TextField(tfPassword.getText());
            textField.setLayoutX(tfPassword.getLayoutX());
            textField.setLayoutY(tfPassword.getLayoutY());
            textField.setPrefHeight(tfPassword.getPrefHeight());
            textField.setPrefWidth(tfPassword.getPrefWidth());
            textField.setStyle(tfPassword.getStyle());
            textField.setPromptText(tfPassword.getPromptText());
            ((javafx.scene.layout.Pane) tfPassword.getParent()).getChildren().add(textField);
            isPasswordVisible = true;
        }
    }

    @FXML
    private void navigateToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));
            Scene signUpScene = new Scene(loader.load());
            Stage stage = (Stage) BtnSignUp.getScene().getWindow();
            stage.setScene(signUpScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading SignUp.fxml.");
        }
    }

    @FXML
    private void navigateToHomeAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeAdmin.fxml"));
            Scene homeAdminScene = new Scene(loader.load());
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(homeAdminScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading homeAdmin.fxml.");
        }
    }

    @FXML
    private void navigateToHomePartner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homePartner.fxml"));
            Scene homeAdminScene = new Scene(loader.load());
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(homeAdminScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading homeAdmin.fxml.");
        }
    }

    @FXML
    private void navigateToHomeLivreur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeLivreur.fxml"));
            Scene homeAdminScene = new Scene(loader.load());
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(homeAdminScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading homeAdmin.fxml.");
        }
    }
    @FXML
    private void navigateToHomeClient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeClient.fxml"));
            Scene homeAdminScene = new Scene(loader.load());
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(homeAdminScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading homeAdmin.fxml.");
        }
    }

    @FXML
    private void ouvrirResetPasswordPoupup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupResetPassword.fxml"));
            Parent root = loader.load();
            PopupResetPassword controller = loader.getController();
            controller.setSignInStage((Stage) txtForgotPassword.getScene().getWindow());
            Stage stage = new Stage();
            stage.setTitle("Reset password");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}