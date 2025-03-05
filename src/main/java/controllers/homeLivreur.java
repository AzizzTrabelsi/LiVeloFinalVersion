package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class homeLivreur {

    @FXML
    private AnchorPane anAvdeliveries;

    @FXML
    private AnchorPane anAvdeliveriesMain;

    @FXML
    private AnchorPane anFeedbacks;

    @FXML
    private AnchorPane anFeedbacksMain;

    @FXML
    private AnchorPane anHistory;

    @FXML
    private AnchorPane anHistoryMain;

    @FXML
    private AnchorPane anLogout;
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
    private void navigateToAvailableDeliveries() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AvailableDeliveries.fxml"));
            Scene signUpScene = new Scene(loader.load());

            Stage stage = (Stage) imLogo.getScene().getWindow();
            stage.setScene(signUpScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading SignUp.fxml.");
        }
    }
    @FXML
    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeLivreur.fxml"));
            Scene signUpScene = new Scene(loader.load());

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

}
