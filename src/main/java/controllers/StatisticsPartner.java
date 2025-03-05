package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.Article;
import net.minidev.json.JSONObject;
import services.Authentification;
import services.CrudArticle;
import services.CrudCommande;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsPartner {

    @FXML
    private Button btnBack;

    @FXML
    private VBox articleListContainer;

    @FXML
    private PieChart pieChart;  // ✅ Changed from BarChart to PieChart

    @FXML
    private ScrollPane scrollPane;

    private final CrudCommande su = new CrudCommande();
    private final CrudArticle cu = new CrudArticle();

    @FXML
    public void initialize() {
        setupBackButton();
        populateArticleList();
        loadChartData();
    }

    // Back button functionality
    private void setupBackButton() {
        btnBack.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/homePartner.fxml"));
                Stage stage = (Stage) btnBack.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public int getUserIDFromToken() {
        String token = Authentification.getToken();
        if (token != null) {
            JSONObject userInfo = new Authentification().decodeToken(token);
            if (userInfo != null) {
                System.out.println("Token récupéré: " + token);
                Object idObject = userInfo.get("idUser");
                if (idObject instanceof Number) {
                    System.out.println("ID utilisateur connecté: " + ((Number) idObject).intValue());
                    return ((Number) idObject).intValue();
                } else {
                    System.out.println("Erreur: idUser n'est pas un nombre.");
                }
            } else {
                System.out.println("Erreur dans le décodage du token.");
            }
        } else {
            System.out.println("Aucun token trouvé. L'utilisateur n'est pas connecté.");
        }
        return -1;
    }

    private void populateArticleList() {
        int userId = getUserIDFromToken();
        if (userId == -1) {
            System.out.println("Impossible de récupérer l'ID utilisateur, liste non chargée.");
            return;
        }

        List<Article> articles = cu.getAllByPartner(userId);
        if (articles == null || articles.isEmpty()) {
            System.out.println("Aucun article trouvé pour cet utilisateur.");
            return;
        }

        articleListContainer.getChildren().clear();
        for (Article a : articles) {
            HBox articleItem = new HBox(10);
            articleItem.setStyle("-fx-padding: 10px; -fx-background-color: #ffffff; -fx-border-radius: 10px; -fx-border-color: #dcdcdc;");

            // Image
            ImageView articleImage = new ImageView(new Image(a.getUrlImage() + ".jpg"));
            articleImage.setFitHeight(40);
            articleImage.setFitWidth(40);

            // Article Name
            Label articleName = new Label(a.getNom());
            articleName.setStyle("-fx-font-size: 16px; -fx-text-fill: #2C3E50;");

            // Add elements to HBox
            articleItem.getChildren().addAll(articleImage, articleName);
            articleListContainer.getChildren().add(articleItem);
        }
    }



    private void loadChartData() {
        int userId = getUserIDFromToken();
        if (userId == -1) {
            System.out.println("Impossible de récupérer l'ID utilisateur, statistiques non chargées.");
            return;
        }

        Map<String, Integer> ordersData = su.getOrderStatistics(userId);
        if (ordersData.isEmpty()) {
            System.out.println("Aucune commande trouvée pour cet utilisateur.");
            return;
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : ordersData.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChartData.add(data);
        }

        pieChart.setData(pieChartData);

        // Apply gradient colors dynamically
        applyDynamicColors(pieChart);
    }

    private void applyDynamicColors(PieChart pieChart) {
        // Liste de couleurs inspirées de #398c3e
        List<String> colors = Arrays.asList(
                "#398c3e", // Vert original
                "#2e7d32", // Vert foncé
                "#4caf50", // Vert plus clair
                "#6fbf73", // Vert menthe
                "#1b5e20"  // Vert sapin
        );

        int colorIndex = 0;

        for (PieChart.Data data : pieChart.getData()) {
            String color = colors.get(colorIndex % colors.size());

            // Appliquer la couleur au graphique
            data.getNode().setStyle("-fx-pie-color: " + color + ";");

            colorIndex++;
        }

        // Modifier la légende pour matcher les couleurs
        for (Node node : pieChart.lookupAll(".chart-legend-item")) {
            if (node instanceof Label) {
                Label legendLabel = (Label) node;
                String articleName = legendLabel.getText(); // Nom de l'article

                // Trouver la couleur associée à cet article
                for (PieChart.Data data : pieChart.getData()) {
                    if (data.getName().equals(articleName)) {
                        String color = colors.get(pieChart.getData().indexOf(data) % colors.size());
                        legendLabel.setGraphic(new Circle(5, Color.web(color))); // Mettre un cercle coloré
                    }
                }
            }
        }
    }


    private void applyLegendColors(PieChart pieChart, Map<String, String> colorMap) {
        Platform.runLater(() -> {
            try {
                Thread.sleep(500); // Wait to ensure UI is rendered
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Node node : pieChart.lookupAll(".chart-legend-item")) {
                if (node instanceof HBox legendItem) {
                    if (!legendItem.getChildren().isEmpty()) {
                        Label label = (Label) legendItem.getChildren().get(1);
                        String articleName = label.getText();

                        Node colorBox = legendItem.getChildren().get(0);
                        if (colorBox instanceof Region && colorMap.containsKey(articleName)) {
                            String newColor = colorMap.get(articleName);
                            ((Region) colorBox).setStyle("-fx-background-color: " + newColor + "; -fx-border-color: black;");
                        }
                    }
                }
            }
        });
    }

}
