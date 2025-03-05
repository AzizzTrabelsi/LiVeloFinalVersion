package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Article;
import services.CrudCommande;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class PopupArticleController implements Initializable {

    @FXML
    private Label descriptionArticle;
    @FXML
    private Label nbvue;
    @FXML
    private ImageView imageArticle;

    @FXML
    private Label nomArticle;

    @FXML
    private Label prixArticle;

    @FXML
    private Spinner<Integer> quantiteSpinner;
    @FXML
    private Button btnAjouterPanier;


    private Article article;
    private CrudCommande su = new CrudCommande(); // Service pour la base de données
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quantiteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
    }
    public void setData(Article article) {
        this.article = article;
        nomArticle.setText(article.getNom());
        prixArticle.setText("Prix : " + article.getPrix() + " TND");
        descriptionArticle.setText(article.getDescription());
        // Afficher le nombre de vues
        nbvue.setText("Vues : " + article.getNbViews());
        String imagePath=article.getUrlImage()+".jpg";
        imageArticle.setImage(new Image(imagePath)); // Assurez-vous que l'image URL est correcte
    }

    @FXML
    private void ajouterAuPanier() {
        int quantite = (int) quantiteSpinner.getValue();
        su.ajouterarticleCommande(MarketClient.getCommandeId(),article, quantite);
        // Afficher le message temporairement

        fermerPopup();
    }

    private void fermerPopup() {
        Stage stage = (Stage) btnAjouterPanier.getScene().getWindow();
        stage.close();
    }
}
