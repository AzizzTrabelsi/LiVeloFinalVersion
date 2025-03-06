package tests;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainUserInterface extends Application {

    public static MainUserInterface instance;
    public static MainUserInterface getInstance() {
        if (instance == null) {
            instance = new MainUserInterface();
        }
        return instance;
    }
    private FXMLLoader loader = new FXMLLoader();

    public static void main(String[] args) {
        launch(args);
    }
    private Stage primaryStage = new Stage(); public static Stage GetPrimaryStage(){return instance.primaryStage;}

    @Override
    public void start(Stage primaryStage) {

        instance=this;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUp.fxml"));


        try {
            Parent root =loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Livelo app ");
            primaryStage.show();
            this.primaryStage = primaryStage;
            if(primaryStage != null) System.out.println("PS is not null!");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void switchScene(Stage stage, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getInstance().getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}