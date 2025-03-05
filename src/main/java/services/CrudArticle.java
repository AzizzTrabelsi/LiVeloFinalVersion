package services;

import interfaces.IServiceCrud;
import models.Article;
import models.Categorie;
import models.User;
import models.statut_article;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudArticle implements IServiceCrud<Article> {
    Connection conn = MyDatabase.getInstance().getConnection();

    public static CrudArticle instance;

    public static CrudArticle getInstance() {
        if (instance == null) {
            instance = new CrudArticle();
        }
        return instance;
    }

    public static void StaticAdd(Article article) {
        getInstance().add(article);
    }

    public List<Article> getArticlesByCategorie(int categorieId) {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article WHERE id_categorie = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, categorieId);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Resultset: " + resultSet);
            while (resultSet.next()) {
                Article article = new Article();
                article.setIdArticle(resultSet.getInt("id"));
                article.setNom(resultSet.getString("nom"));
                article.setPrix(resultSet.getFloat("prix"));
                article.setDescription(resultSet.getString("description"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }
    public void incrementNbViews(int id_article) {
        String query = "UPDATE article SET nbViews = nbViews + 1 WHERE id_article = ?";

        // Utilisation de try-with-resources pour s'assurer que la connexion et la statement sont fermées après utilisation
        try ( // Connexion à la base de données
             PreparedStatement statement = conn.prepareStatement(query)) {  // Préparer la requête

            // Passer l'ID de l'article dans la requête
            statement.setInt(1, id_article);

            // Exécuter la requête de mise à jour
            int rowsUpdated = statement.executeUpdate();  // Mettre à jour la table

            // Vérifier si la mise à jour a été effectuée
            if (rowsUpdated > 0) {
                System.out.println("Le nombre de vues a été mis à jour pour l'article avec l'ID : " + id_article);
            } else {
                System.err.println("Aucun article trouvé avec l'ID : " + id_article);
            }
        } catch (SQLException e) {
            // Afficher une erreur en cas d'exception
            e.printStackTrace();
        }
    }



    @Override
    public void add(Article article) {
        String qry = "INSERT INTO `article` (`url_image`, `id_categorie`, `nom`, `prix`, `description`, `created_by`, `quantite`, `statut`, `createdAt`, `nbViews`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, article.getUrlImage());


            if (article.getCategorie() != null) {
                statement.setInt(2, article.getCategorie().getId_categorie());
            } else {
                statement.setNull(2, Types.INTEGER);
            }

            statement.setString(3, article.getNom());
            statement.setFloat(4, article.getPrix());
            statement.setString(5, article.getDescription());
            statement.setInt(6, article.getCreatedBy());
            statement.setInt(7, article.getQuantite());
            statement.setString(8, article.getStatut().toString());
            statement.setDate(9, new java.sql.Date(article.getCreatedAt().getTime()));
            statement.setInt(10, article.getNbViews());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        article.setIdArticle(generatedId);
                        System.out.println("Article added successfully with ID: " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Article> getArticlesByCategoryId(int idCategorie) {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM `article` WHERE `id_categorie` = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idCategorie);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) { // Correction ici
                int articleId = rs.getInt("id_article");
                String urlImage = rs.getString("url_image");
                int idCate = rs.getInt("id_categorie");
                String nom = rs.getString("nom");
                float prix = rs.getFloat("prix");
                String description = rs.getString("description");
                int createdBy = rs.getInt("created_by");
                int quantite = rs.getInt("quantite");
                statut_article statut = statut_article.valueOf(rs.getString("statut"));
                Date createdAt = rs.getDate("createdAt");
                int nbViews = rs.getInt("nbViews");

                // Correction : Utilisation de l'ID de la catégorie récupérée
                Categorie categorie = new Categorie(idCate);

                Article article = new Article(articleId, urlImage, categorie, nom, prix, description, createdBy, quantite, statut, createdAt, nbViews);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }



    @Override
    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article";

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Executing query: " + query);

            while (resultSet.next()) {
                int idArticle = resultSet.getInt("id_article");
                String urlImage = resultSet.getString("url_image");
                int idCategorie = resultSet.getInt("id_categorie");
                String nom = resultSet.getString("nom");
                float prix = resultSet.getFloat("prix");
                String description = resultSet.getString("description");
                int createdBy = resultSet.getInt("created_by");
                int quantite = resultSet.getInt("quantite");
                int nbViews = resultSet.getInt("nbViews");
                Date createdAt = resultSet.getDate("createdAt");


                String statutString = resultSet.getString("statut");
                System.out.println("Statut: " + statutString);


                statut_article statut;
                try {
                    statut = statut_article.valueOf(statutString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    statut = statut_article.on_stock;
                }


                Article article = new Article(idArticle, urlImage, null, nom, prix, description, createdBy, quantite, statut, createdAt, nbViews);

                Categorie categorie = getCategorieById(idCategorie);
                article.setCategorie(categorie);


                articles.add(article);


                if (categorie != null) {
                    System.out.println("Article ID: " + idArticle + " | Nom: " + nom + " | Catégorie: " + categorie.getNom());
                } else {
                    System.out.println("Article ID: " + idArticle + " | Nom: " + nom + " | Catégorie non trouvée");
                }
            }

            System.out.println("Nombre d'articles récupérés: " + articles.size());
            for (Article a : articles) {
                System.out.println(a);
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL: " + e.getMessage());
        }


        return articles;
    }

    public List<Article> getArticlesByStatus(String status) {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article WHERE statut = ?"; // Suppression de id_categorie

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, status);

            System.out.println("Executing query: " + query + " avec statut=" + status);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int idArticle = resultSet.getInt("id_article");
                    String urlImage = resultSet.getString("url_image");
                    int idCategorie = resultSet.getInt("id_categorie"); // Toujours récupérer la catégorie
                    String nom = resultSet.getString("nom");
                    float prix = resultSet.getFloat("prix");
                    String description = resultSet.getString("description");
                    int createdBy = resultSet.getInt("created_by");
                    int quantite = resultSet.getInt("quantite");
                    int nbViews = resultSet.getInt("nbViews");
                    Date createdAt = resultSet.getDate("createdAt");

                    // Vérification et conversion du statut
                    String statutString = resultSet.getString("statut");
                    System.out.println("Statut: " + statutString);

                    statut_article statut;
                    try {
                        statut = statut_article.valueOf(statutString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        statut = statut_article.on_stock;
                    }

                    // Récupérer la catégorie associée
                    Categorie categorie = getCategorieById(idCategorie);

                    // Création de l'objet Article
                    Article article = new Article(idArticle, urlImage, categorie, nom, prix, description, createdBy, quantite, statut, createdAt, nbViews);

                    articles.add(article);

                    if (categorie != null) {
                        System.out.println("Article ID: " + idArticle + " | Nom: " + nom + " | Catégorie: " + categorie.getNom());
                    } else {
                        System.out.println("Article ID: " + idArticle + " | Nom: " + nom + " | Catégorie non trouvée");
                    }
                }

                System.out.println("Nombre d'articles récupérés: " + articles.size());
                for (Article a : articles) {
                    System.out.println(a);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL: " + e.getMessage());
        }

        return articles;
    }


    public Categorie getCategorieById(int idCategorie) {
        Categorie categorie = null;
        String query = "SELECT * FROM categorie WHERE id_categorie = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, idCategorie);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String categorieNom = resultSet.getString("nom");
                    categorie = new Categorie(idCategorie, categorieNom);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la catégorie: " + e.getMessage());
        }

        return categorie;
    }




    @Override
    public void update(Article article) {
        // Utilisation de l'objet Categorie pour obtenir l'ID
        String qry = "UPDATE `article` SET `url_image` = ?, `id_categorie` = ?, `nom` = ?, `prix` = ?, `description` = ?, `created_by` = ?, `quantite` = ?, `statut` = ?, `createdAt` = ?, `nbViews` = ? WHERE `id_article` = ?";

        try (PreparedStatement statement = conn.prepareStatement(qry)) {
            statement.setString(1, article.getUrlImage());
            if (article.getCategorie() != null) {
                statement.setInt(2, article.getCategorie().getId_categorie());
            } else {
                statement.setNull(2, Types.INTEGER);
            }

            statement.setString(3, article.getNom());
            statement.setFloat(4, article.getPrix());
            statement.setString(5, article.getDescription());
            statement.setInt(6, article.getCreatedBy());
            statement.setInt(7, article.getQuantite());
            statement.setString(8, article.getStatut().toString());
            statement.setDate(9, new java.sql.Date(article.getCreatedAt().getTime()));
            statement.setInt(10, article.getNbViews());


            statement.setInt(11, article.getIdArticle());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Article updated successfully.");
            } else {
                System.out.println("No article found with ID " + article.getIdArticle());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(int id) {
        String qry = "DELETE FROM `article` WHERE `id_article` = " + id;
        try (PreparedStatement statement = conn.prepareStatement(qry)) {
            statement.executeUpdate();
            System.out.println("Article deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Article getById(int id) {
        String query = "SELECT * FROM `article` WHERE `id_article` = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int articleId = rs.getInt("id_article");
                String urlImage = rs.getString("url_image");
                int idCategorie = rs.getInt("id_categorie");
                String nom = rs.getString("nom");
                float prix = rs.getFloat("prix");
                String description = rs.getString("description");
                int createdBy = rs.getInt("created_by");
                int quantite = rs.getInt("quantite");
                statut_article statut = statut_article.valueOf(rs.getString("statut"));
                Date createdAt = rs.getDate("createdAt");
                int nbViews = rs.getInt("nbViews");
                Categorie categorie = null;
                if (idCategorie > 0) {
                    String categoryQuery = "SELECT * FROM `categorie` WHERE `id_categorie` = ?";
                    try (PreparedStatement categoryStmt = conn.prepareStatement(categoryQuery)) {
                        categoryStmt.setInt(1, idCategorie);
                        ResultSet categoryRs = categoryStmt.executeQuery();
                        if (categoryRs.next()) {
                            String categoryName = categoryRs.getString("nom");
                            categorie = new Categorie(idCategorie, categoryName);
                        }
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la récupération de la catégorie : " + e.getMessage());
                    }
                }

                Article article = new Article(articleId, urlImage, categorie, nom, prix, description, createdBy, quantite, statut, createdAt, nbViews);

                System.out.println("Article trouvé : " + article);
                return article;
            } else {
                System.out.println("Aucun article trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Article> search(String criteria) {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM `article` WHERE `nom` LIKE ? OR `description` LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchTerm = "%" + criteria + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                statut_article statut;
                try {
                    statut = statut_article.valueOf(rs.getString("statut").toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Valeur inconnue pour statut : " + rs.getString("statut"));
                    statut = statut_article.on_stock; // Valeur par défaut
                }

                // Récupérer les informations de la catégorie
                Categorie categorie = null;
                int idCategorie = rs.getInt("id_categorie");
                if (idCategorie > 0) {
                    String categoryQuery = "SELECT * FROM `categorie` WHERE `id_categorie` = ?";
                    try (PreparedStatement categoryStmt = conn.prepareStatement(categoryQuery)) {
                        categoryStmt.setInt(1, idCategorie);
                        ResultSet categoryRs = categoryStmt.executeQuery();
                        if (categoryRs.next()) {
                            String categoryName = categoryRs.getString("nom");
                            categorie = new Categorie(idCategorie, categoryName);
                        }
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la récupération de la catégorie : " + e.getMessage());
                    }
                }

                Article article = new Article(
                        rs.getInt("id_article"),
                        rs.getString("url_image"),
                        categorie,
                        rs.getString("nom"),
                        rs.getFloat("prix"),
                        rs.getString("description"),
                        rs.getInt("created_by"),
                        rs.getInt("quantite"),
                        statut,
                        rs.getDate("createdAt"),
                        rs.getInt("nbViews")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (articles.isEmpty()) {
            System.out.println("Aucun article trouvé pour le critère : " + criteria);
        } else {
            System.out.println("Nombre d'articles trouvés : " + articles.size());
            for (Article article : articles) {
                System.out.println("Article trouvé : ");
                System.out.println("ID: " + article.getIdArticle());
                System.out.println("Nom: " + article.getNom());
                System.out.println("Catégorie: " + (article.getCategorie() != null ? article.getCategorie().getNom() : "Non définie"));
                System.out.println("Prix: " + article.getPrix());
                System.out.println("Description: " + article.getDescription());
                System.out.println("Créé par (ID utilisateur): " + article.getCreatedBy());
                System.out.println("Quantité: " + article.getQuantite());
                System.out.println("Statut: " + article.getStatut());
                System.out.println("Date de création: " + article.getCreatedAt());
                System.out.println("Nombre de vues: " + article.getNbViews());
                System.out.println();
            }
        }

        return articles;
    }
    public List<Article> getAllByPartner(int idPartner){
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM `article` WHERE `created_by` = ?";
        try(PreparedStatement stmt= conn.prepareStatement(query)) {
            stmt.setInt(1, idPartner);
            System.out.println("idpartner get all partner article"+idPartner);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Article article = new Article(rs.getInt("id_article")
                        ,rs.getString("url_image")
                        ,null
                        ,rs.getString("nom")
                        ,rs.getFloat("prix")
                        ,rs.getString("description")
                        ,rs.getInt("created_by")
                        ,rs.getInt("quantite")
                        ,statut_article.valueOf(rs.getString("Statut"))
                        ,rs.getDate("createdAt")
                        ,rs.getInt("nbViews")
                );
                articles.add(article);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return articles;
    }



}