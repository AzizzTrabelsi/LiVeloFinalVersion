package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
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
        // Récupérer les articles en stock et hors stock
        List<Article> articlesInStock = su.getArticlesByStatus("on_stock");
        List<Article> articlesOutStock = su.getArticlesByStatus("out_of_stock");

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
}
