package services;


import models.statut_article;
import interfaces.IServiceCrud;
import models.Categorie;
import models.User;
import models.Article;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CrudCategorie implements IServiceCrud<Categorie> {
    Connection conn = MyDatabase.getInstance().getConnection();

    @Override
    public void add(Categorie cat) {
        String qry = "INSERT INTO categorie (nom, description, url_image, created_by) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, cat.getNom());
            statement.setString(2, cat.getDescription());
            statement.setString(3, cat.getUrl_image());
            statement.setInt(4, cat.getCreatedBy());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        cat.setId_categorie(generatedId);
                        System.out.println("Catégorie ajoutée avec succès avec l'ID : " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<String> getAllCategoryNames() {
        List<String> categoryNames = new ArrayList<>();
        String query = "SELECT nom FROM categorie";  // Récupère uniquement les noms des catégories

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nom = rs.getString("nom");  // Récupère le nom de la catégorie
                categoryNames.add(nom);  // Ajoute le nom dans la liste
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryNames;
    }

    public int getCategoryIdByName(String categoryName) {
        String query = "SELECT id_categorie FROM categorie WHERE nom = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id_categorie");
            } else {
                System.out.println("Category not found: " + categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }




    @Override
    public List<Categorie> getAll() {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_categorie");
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                String urlImage = rs.getString("url_image");
                int createdBy = rs.getInt("created_by");

                Categorie categorie = new Categorie(id, nom, description, urlImage, createdBy);

                // Récupérer les articles associés à cette catégorie
                String articleQuery = "SELECT * FROM article WHERE id_categorie = ?";
                try (PreparedStatement articleStmt = conn.prepareStatement(articleQuery)) {
                    articleStmt.setInt(1, id);
                    try (ResultSet articleRs = articleStmt.executeQuery()) {
                        List<Article> articles = new ArrayList<>();
                        while (articleRs.next()) {
                            Article article = new Article(
                                    articleRs.getInt("id_article"),
                                    articleRs.getString("url_image"),
                                    categorie, // 🔹 Référence à la catégorie
                                    articleRs.getString("nom"),
                                    articleRs.getFloat("prix"),
                                    articleRs.getString("description"),
                                    articleRs.getInt("created_by"),
                                    articleRs.getInt("quantite"),
                                    statut_article.valueOf(articleRs.getString("statut")), // Convertir String en enum
                                    articleRs.getDate("createdAt"),
                                    articleRs.getInt("nbViews")
                            );
                            articles.add(article);
                        }
                        categorie.setArticles(articles);
                    }
                }

                categories.add(categorie);
            }

            System.out.println("Nombre de catégories récupérées : " + categories.size());
            for (Categorie c : categories) {
                System.out.println(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    public void update(Categorie cat) {
        String qry = "UPDATE categorie SET nom = ?, description = ?, url_image = ?, created_by = ? WHERE id_categorie = ?";

        try (PreparedStatement statement = conn.prepareStatement(qry)) {
            statement.setString(1, cat.getNom());
            statement.setString(2, cat.getDescription());
            statement.setString(3, cat.getUrl_image());
            statement.setInt(4, cat.getCreatedBy());
            statement.setInt(5, cat.getId_categorie());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Catégorie mise à jour avec succès.");
            } else {
                System.out.println("Aucune catégorie trouvée avec l'ID " + cat.getId_categorie());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id_categorie) {
        // Supprimer les articles associés à cette catégorie
        String deleteArticlesQuery = "DELETE FROM article WHERE id_categorie = ?";
        try (PreparedStatement articleStmt = conn.prepareStatement(deleteArticlesQuery)) {
            articleStmt.setInt(1, id_categorie);
            articleStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Supprimer la catégorie
        String deleteCategorieQuery = "DELETE FROM categorie WHERE id_categorie = ?";
        try (PreparedStatement categorieStmt = conn.prepareStatement(deleteCategorieQuery)) {
            categorieStmt.setInt(1, id_categorie);
            int rowsAffected = categorieStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Catégorie et articles associés supprimés avec succès.");
            } else {
                System.out.println("Aucune catégorie trouvée avec l'ID " + id_categorie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Categorie getById(int id) {
        String query = "SELECT * FROM categorie WHERE id_categorie = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Categorie categorie = new Categorie(
                        rs.getInt("id_categorie"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("url_image"),
                        rs.getInt("created_by")
                );


                String articleQuery = "SELECT * FROM article WHERE id_categorie = ?";
                try (PreparedStatement articleStmt = conn.prepareStatement(articleQuery)) {
                    articleStmt.setInt(1, id);
                    try (ResultSet articleRs = articleStmt.executeQuery()) {
                        List<Article> articles = new ArrayList<>();
                        while (articleRs.next()) {
                            Article article = new Article(
                                    articleRs.getInt("id_article"),
                                    articleRs.getString("url_image"),
                                    categorie, // 🔹 Référence à la catégorie
                                    articleRs.getString("nom"),
                                    articleRs.getFloat("prix"),
                                    articleRs.getString("description"),
                                    articleRs.getInt("created_by"),
                                    articleRs.getInt("quantite"),
                                    statut_article.valueOf(articleRs.getString("statut")),
                                    articleRs.getDate("createdAt"),
                                    articleRs.getInt("nbViews")
                            );
                            articles.add(article);
                        }
                        categorie.setArticles(articles);
                    }
                }

                System.out.println("Catégorie trouvée : " + categorie);
                return categorie;
            } else {
                System.out.println("Aucune catégorie trouvée avec l'ID : " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Categorie> search(String criteria) {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie WHERE nom LIKE ? OR description LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchTerm = "%" + criteria + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Categorie categorie = new Categorie(
                        rs.getInt("id_categorie"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("url_image"),
                        rs.getInt("created_by")
                );
                categories.add(categorie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (categories.isEmpty()) {
            System.out.println("Aucune catégorie trouvée pour le critère : " + criteria);
        } else {
            System.out.println("Nombre de catégories trouvées : " + categories.size());
            for (Categorie categorie : categories) {
                System.out.println("Catégorie trouvée : ");
                System.out.println("ID: " + categorie.getId_categorie());
                System.out.println("Nom: " + categorie.getNom());
                System.out.println("Description: " + categorie.getDescription());
                System.out.println("URL Image: " + categorie.getUrl_image());
                System.out.println("Créée par (ID utilisateur): " + categorie.getCreatedBy());
                System.out.println();
            }
        }

        return categories;
    }
}