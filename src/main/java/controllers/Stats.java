package controllers;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import models.Article;
import net.minidev.json.JSONObject;
import services.Authentification;
import services.CrudArticle;
import utils.MyDatabase;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import models.Article;
import services.CrudArticle;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class Stats {
    @FXML
    private PieChart pie;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private CategoryAxis xAxis;
    private final Connection conn = MyDatabase.getInstance().getConnection();
    @FXML
    private NumberAxis yAxis;
    private CrudArticle su;
    @FXML

    public void initialize() {
        // Charge les statuts dans le ComboBox

        loadArticleQuantities(); // Charge tous les articles par défaut

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

    private void loadArticleQuantities() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Article/Quantities");

        // Récupérer l'ID de l'utilisateur connecté
        int userId = afficherIdUtilisateur();
        if (userId == -1) {
            System.out.println("Erreur : utilisateur non connecté ou ID invalide.");
            return;  // Arrêter l'exécution si l'utilisateur n'est pas connecté ou l'ID est invalide
        }

        // Requête SQL avec filtrage par 'created_by' (ID utilisateur)
        String query = "SELECT nom, quantite FROM article WHERE created_by = ?";
        Connection conn = MyDatabase.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);  // Remplacer le ? par l'ID de l'utilisateur

            System.out.println("Executing query: " + query);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String articleName = rs.getString("nom");
                    int quantity = rs.getInt("quantite");
                    series.getData().add(new XYChart.Data<>(articleName, quantity));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ajouter les données au graphique
        barChart.getData().add(series);
        barChart.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }


}