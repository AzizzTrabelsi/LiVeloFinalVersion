package controllers;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import models.Article;
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




    private void loadArticleQuantities() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Article/Quantities");

        String query = "SELECT nom, quantite FROM article";
        Connection conn = MyDatabase.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Executing query: " + query);

            while (rs.next()) {
                String articleName = rs.getString("nom");
                int quantity = rs.getInt("quantite");
                series.getData().add(new XYChart.Data<>(articleName, quantity));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        barChart.getData().add(series);
        barChart.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());


    }

}
