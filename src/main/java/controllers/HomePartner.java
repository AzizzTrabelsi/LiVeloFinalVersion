package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePartner {

    @FXML
    private AnchorPane anArticleMain;

    @FXML
    private AnchorPane anArticles;

    @FXML
    private AnchorPane anLogout;

    @FXML
    private AnchorPane anOrder;

    @FXML
    private AnchorPane anOrdersMain;

    @FXML
    private ImageView imLogo;

    @FXML
    public void normalEffect(javafx.scene.input.MouseEvent event) {
        ((AnchorPane) event.getSource()).setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

    }
    @FXML
    public void hoverEffect(javafx.scene.input.MouseEvent event) {
        ((AnchorPane) event.getSource()).setStyle("-fx-background-color: lightgrey; -fx-cursor: hand;");

    }
    @FXML
    private void navigateToHome() {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homePartner.fxml"));
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
    private void navigateToStatistics() {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatisticsPartner.fxml"));
            Scene signUpScene = new Scene(loader.load());

            Stage stage = (Stage) imLogo.getScene().getWindow();
            stage.setScene(signUpScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Statistics.fxml.");
        }
    }
    @FXML
    private void navigateToGestionCat() {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionCategorie.fxml"));
            Scene signUpScene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) anArticleMain.getScene().getWindow();
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



}
