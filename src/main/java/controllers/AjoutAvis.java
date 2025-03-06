package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Avis;
import models.Livraison;
import services.CrudAvis;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AjoutAvis implements Initializable {
    private CrudAvis crudAvis = new CrudAvis();
    private int userId ;

    @FXML
    private TextField avisIdField;

    @FXML
    private Button cancel;

    @FXML
    private Text ctrl;

    @FXML
    private Button envoyer;



    @FXML
    void handleAvis(ActionEvent event) {
        if (avisIdField.getText().length()>4){
            Avis avis = new Avis();
            avis.setCreatedBy(userId);
            avis.setDescription(avisIdField.getText());
            avis.setLivraison(livraison);
            avis.setCreatedAt(new Date());
            crudAvis.add(avis);
            goToGestionAvis();
        } else if (avisIdField.getText().length() == 0) {
            ctrl.setText("Vous ne pouvez pas ajouter un avis vide !");
            ctrl.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }else{
            ctrl.setText("Un avis comporte 5 caracteres minimum!");
            ctrl.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    Livraison livraison;
    void setLivraison(Livraison l){
        this.livraison = l;
    }
    @FXML
    void goToGestionAvis() {
        try {
            Stage stage = (Stage) avisIdField.getScene().getWindow(); // Get reference to the login window's stage
            stage.setTitle("Gestion Avis");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LivraisonClientt.fxml"));
            Parent p = loader.load();
            Scene scene = new Scene(p);

            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle navigation failure
        }

    }

    void getUser(){
        this.userId=crudAvis.getUser().getId();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getUser();
    }
}
