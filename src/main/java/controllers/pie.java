package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import net.minidev.json.JSONObject;
import services.Authentification;
import services.CrudArticle;
import models.Article;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
public class pie implements Initializable {
    @FXML
    private TableView<Article> onstock;
    @FXML
    private TableColumn<Article, String> outstockName;
    @FXML
    private TableColumn<Article, String> onstockName;
    @FXML
    private TableView<Article> outstock;
    @FXML
    private PieChart pie;

    private CrudArticle su = new CrudArticle(); // Assure-toi que cette classe existe

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableViews();
        loadPieChart();
    }
    private void setupTableViews() {
        // Configurer les colonnes du TableView onstock (nom uniquement)
        onstockName.setCellValueFactory(new PropertyValueFactory<>("nom"));

        // Configurer les colonnes du TableView outstock (nom uniquement)
        outstockName.setCellValueFactory(new PropertyValueFactory<>("nom"));

        // Charger les données dans les TableView
        loadTableViewData();
    }private void loadTableViewData() {
        // Récupérer les articles en stock et hors stock
        List<Article> articlesInStock = su.getArticlesByStatus("on_stock");
        List<Article> articlesOutStock = su.getArticlesByStatus("out_of_stock");

        // Convertir en ObservableList et affecter aux TableView
        onstock.setItems(FXCollections.observableArrayList(articlesInStock));
        outstock.setItems(FXCollections.observableArrayList(articlesOutStock));
    }

    private void loadPieChart() {
        // Récupérer l'ID de l'utilisateur connecté
        int userId = afficherIdUtilisateur();
        if (userId == -1) {
            System.out.println("Erreur : utilisateur non connecté ou ID invalide.");
            return;  // Arrêter l'exécution si l'utilisateur n'est pas connecté ou l'ID est invalide
        }

        // Récupérer les articles créés par l'utilisateur connecté
        List<Article> articlesInStock = su.getArticlesByStatusAndUser("on_stock", userId);
        List<Article> articlesOutStock = su.getArticlesByStatusAndUser("out_of_stock", userId);

        // Récupérer les noms des articles
        String inStockNames = articlesInStock.stream()
                .map(Article::getNom)
                .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);

        String outStockNames = articlesOutStock.stream()
                .map(Article::getNom)
                .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);

        // Compter les articles
        int inStockCount = articlesInStock.size();
        int outStockCount = articlesOutStock.size();

        // Ajouter les données au PieChart avec les noms des articles
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("In Stock (" + inStockCount + ")", inStockCount),
                new PieChart.Data("Out of Stock (" + outStockCount + ")", outStockCount)
        );

        pie.setData(pieChartData);
        pie.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Ajouter des Tooltips avec les noms des articles
        for (PieChart.Data data : pie.getData()) {
            Tooltip tooltip = new Tooltip();

            if (data.getName().contains("In Stock")) {
                tooltip.setText("Articles in stock:\n" + inStockNames);
            } else {
                tooltip.setText("Articles out of stock:\n" + outStockNames);
            }

            Tooltip.install(data.getNode(), tooltip);
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
}