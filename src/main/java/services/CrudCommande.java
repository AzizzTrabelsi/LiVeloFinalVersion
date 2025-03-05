package services;

import controllers.MarketClient;
import models.*;
import utils.MyDatabase;

import java.sql.*;
import java.util.*;

public class CrudCommande implements interfaces.IServiceCrud<Commande> {
    Connection conn= MyDatabase.getInstance().getConnection();
    private CrudArticle crudArticle=new CrudArticle();
    @Override
    public void add(Commande commande) {
        String qry = "INSERT INTO `commande`(`adresse_dep`, `adresse_arr`, `type_livraison`, `horaire`, `statut`, `created_by`) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false); // Démarre la transaction

            // Préparer la requête avec récupération de l'ID généré
            PreparedStatement pstm = conn.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, commande.getAdresse_dep());
            pstm.setString(2, commande.getAdresse_arr());
            pstm.setString(3, commande.getType_livraison());
            pstm.setTimestamp(4, commande.getHoraire());
            pstm.setString(5, commande.getStatut().toString());
            pstm.setInt(6, commande.getCreated_by());

            // Exécuter l'insertion
            pstm.executeUpdate();

            // Récupérer l'ID généré
            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                int commandeId = generatedKeys.getInt(1);
                commande.setId_Commande(commandeId);
                System.out.println("Commande créée avec ID: " + commandeId);

                // Stocker l'ID de la commande dans `MarketClient`
                MarketClient.setCommandeId(commandeId);
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback échoué: " + rollbackEx.getMessage());
            }
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Échec du reset auto-commit: " + e.getMessage());
            }
        }
    }

    /*public void add(Commande commande) {
        String qry = "INSERT INTO `commande`(`adresse_dep`, `adresse_arr`, `type_livraison`, `horaire`, `statut`, `created_by`) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false); // Démarre la transaction

            // Préparer la déclaration pour l'insertion de la commande avec récupération de la clé générée
            PreparedStatement pstm = conn.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, commande.getAdresse_dep());
            pstm.setString(2, commande.getAdresse_arr());
            pstm.setString(3, commande.getType_livraison());
            pstm.setTimestamp(4, commande.getHoraire());
            pstm.setString(5, commande.getStatut().toString());
            pstm.setInt(6, commande.getCreated_by());

            // Exécuter l'insertion de la commande
            pstm.executeUpdate();

            // Récupérer la clé générée (ID de la commande)
            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                int commandeId = generatedKeys.getInt(1); // Obtenir l'ID généré
                System.out.println("Generated Commande ID: " + commandeId);

                // Mettre à jour l'ID de la commande dans l'objet Commande
                commande.setId_Commande(commandeId);

                // Vérifier si la liste des articles est vide
                if (commande.getArticles() != null && !commande.getArticles().isEmpty()) {
                    // Insérer dans la table de jointure pour chaque article associé
                    String joinQry = "INSERT INTO `articlecommande`(`idArticle`, `idCommande`) VALUES (?, ?)";
                    PreparedStatement joinPstm = conn.prepareStatement(joinQry);

                    // Boucle sur les articles et insertion dans la table de jointure
                    for (Article article : commande.getArticles()) {
                        System.out.println("Inserting Article ID: " + article.getIdArticle());
                        joinPstm.setInt(1, article.getIdArticle()); // Utiliser l'ID de l'article
                        joinPstm.setInt(2, commandeId); // Utiliser l'ID de la commande
                        joinPstm.executeUpdate();
                    }
                } else {
                    System.out.println("No articles to insert for Commande ID: " + commandeId);
                }
            } else {
                System.out.println("No generated key obtained.");
            }

            conn.commit(); // Valider la transaction
        } catch (SQLException e) {
            try {
                conn.rollback(); // Annuler en cas d'erreur
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true); // Réinitialiser le mode auto-commit
            } catch (SQLException e) {
                System.out.println("Failed to reset auto-commit: " + e.getMessage());
            }
        }
    }
        /*
        //create Qry SQL
        //execute Qry
        String qry ="INSERT INTO `commande`(`adresse_dep`, `adresse_arr`, `type_livraison`,`horaire`,`statut`,`created_by`) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = conn.prepareStatement(qry);
            pstm.setString(1,commande.getAdresse_dep());
            pstm.setString(2, commande.getAdresse_arr());
            pstm.setString(3,commande.getType_livraison());
            pstm.setTimestamp(4,commande.getHoraire());
            pstm.setString(5, commande.getStatut().toString());
            pstm.setInt(6,commande.getCreated_by());
            pstm.executeUpdate();
            // Retrieve the generated key (ID of the inserted commande)
            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                int commandeId = generatedKeys.getInt(1);
                System.out.println("Generated Commande ID: " + commandeId);

                // Now insert into the join table for each associated article
                try {
                    String joinQry = "INSERT INTO `articlecommande`(`idArticle`, `idCommande`) VALUES (?,?)";
                    PreparedStatement joinPstm = conn.prepareStatement(joinQry);

                    for (Article article : commande.getArticles()) {
                        joinPstm.setInt(1, article.getIdArticle());
                        joinPstm.setInt(2, commandeId);
                        joinPstm.executeUpdate();
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/
    @Override
    public List<Commande> getAll() {
        //create Qry sql
        //execution
        //Mapping data


        List<Commande> commandes = new ArrayList<>();
        String qry ="SELECT * FROM `commande`";

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Commande c = new Commande();
                c.setId_Commande(rs.getInt("id_commande"));
                c.setAdresse_dep(rs.getString(2));
                c.setAdresse_arr(rs.getString("adresse_arr"));
                c.setType_livraison(rs.getString(4));
                c.setHoraire(rs.getTimestamp(5));
                c.setStatut(statutlCommande.valueOf(rs.getString(6)));
                c.setCreated_by(rs.getInt(7));

                commandes.add(c);
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return commandes;
    }

    @Override
    public void update(Commande commande) {
        // Créer la requête SQL avec des paramètres
        String qry = "UPDATE `commande` SET `adresse_dep` = ?, `adresse_arr` = ?, `type_livraison` = ?, `horaire` = ?, " +
                "`statut` = ?, `created_by` = ? WHERE `id_commande` ="+commande.getId_Commande();

        try (PreparedStatement statement = conn.prepareStatement(qry)) {
            // Remplacer les points d'interrogation par les valeurs
            statement.setString(1, commande.getAdresse_dep());
            statement.setString(2, commande.getAdresse_arr());
            statement.setString(3, commande.getType_livraison());
            statement.setTimestamp(4, commande.getHoraire());
            statement.setString(5, commande.getStatut().toString());
            statement.setInt(6, commande.getCreated_by());
            // Exécuter la requête
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Command updated successfully.");
            } else {
                System.out.println("No command found with ID " + commande.getId_Commande());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String qry = "DELETE FROM `commande` WHERE `id_commande` = " + id;
        try (PreparedStatement statement = conn.prepareStatement(qry)) {
            // Exécuter la requête
            statement.executeUpdate();
            System.out.println("Command deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Commande getById(int id) {
        String query = "SELECT * FROM `commande` WHERE `id_commande` = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("id_commande"),
                        rs.getString("adresse_dep"),
                        rs.getString("adresse_arr"),
                        rs.getString("type_livraison"),
                        rs.getTimestamp("horaire"),
                        statutlCommande.valueOf(rs.getString("statut")),
                        rs.getInt("created_by")
                );
                System.out.println("Commande trouvé : " + commande);
                return commande;
            } else {
                System.out.println("Aucune commande trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Commande> getCommandesByStatut(String statut) {
        List<Commande> commandes = new ArrayList<>();
        String qry = "SELECT * FROM `commande` WHERE statut = ?";
        try (PreparedStatement stmt = conn.prepareStatement(qry)) {
            stmt.setString(1, statutlCommande.valueOf(statut).toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("id_commande"),
                        rs.getString("adresse_dep"),
                        rs.getString("adresse_arr"),
                        rs.getString("type_livraison"),
                        rs.getTimestamp("horaire"),
                        statutlCommande.valueOf(rs.getString("statut")),
                        rs.getInt("created_by")
                );
                if(!commande.getAdresse_arr().equals("Adresse inconnue"))
                {commandes.add(commande);}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (commandes.isEmpty()) {
            System.out.println("Aucun commande trouvé pour le critère : " + statut);
        } else {
            System.out.println("Nombre de commandes trouvés : " + commandes.size());
            for (Commande commande : commandes) {
                System.out.println("Commande trouvé : ");
                System.out.println(commande.toString());
            }
        }

        return commandes;

    }

    @Override
    public List<Commande> search(String criteria) {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM `commande` WHERE `type_livraison` LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchTerm = "%" + criteria + "%";
            stmt.setString(1, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("id_commande"),
                        rs.getString("adresse_dep"),
                        rs.getString("adresse_arr"),
                        rs.getString("type_livraison"),
                        rs.getTimestamp("horaire"),
                        statutlCommande.valueOf(rs.getString("statut")),
                        rs.getInt("created_by")
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (commandes.isEmpty()) {
            System.out.println("Aucun commande trouvé pour le critère : " + criteria);
        } else {
            System.out.println("Nombre de commandes trouvés : " + commandes.size());
            for (Commande commande : commandes) {
                System.out.println("Commande trouvé : ");
                System.out.println(commande.toString());
            }
        }

        return commandes;
    }

    public void ajouterarticleCommande(int id_commande,Article article,int quantity) {
        String Qry = "INSERT INTO `articlecommande`(`idArticle`, `idCommande`,`quantity`) VALUES (?, ?,?)";
        PreparedStatement Pstm = null;
        try {
            Pstm = conn.prepareStatement(Qry);
            Pstm.setInt(1, article.getIdArticle()); // Utiliser l'ID de l'article
            Pstm.setInt(2, id_commande); // Utiliser l'ID de la commande
            Pstm.setInt(3, quantity); //
            Pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public List<Article> getlisteArticleCommande(int id_commande) {
        List<Article> articles = new ArrayList<>();
        Set<Integer> idArticlesSet = new HashSet<>(); // Utilisation d'un Set pour éviter les doublons

        String Qry = "SELECT idArticle FROM articlecommande WHERE idCommande = ?";
        try (PreparedStatement pstm = conn.prepareStatement(Qry)) {
            pstm.setInt(1, id_commande);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                idArticlesSet.add(rs.getInt("idArticle")); // Ajoute les ID uniques
            }

            for (int idArticle : idArticlesSet) {
                Article article = crudArticle.getById(idArticle);
                if (article != null) {
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des articles de la commande", e);
        }

        return articles;
    }
    public int getQuantiteArticleCommande(int idCommande, int idArticle) {
        int quantite = 0;
        String query = "SELECT quantity FROM articlecommande WHERE idCommande = ? AND idArticle = ?";

        try (
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idCommande);
            stmt.setInt(2, idArticle);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    quantite = rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quantite;
    }
    public void supprimerArticleCommande(int idcommande,Article article)
    {
        String qry = "DELETE FROM `articlecommande` WHERE `idCommande`=? AND `idArticle`=?";
        try (PreparedStatement statement = conn.prepareStatement(qry)) {
            statement.setInt(1, idcommande);
            statement.setInt(2, article.getIdArticle());
            // Exécuter la requête
            statement.executeUpdate();
            System.out.println("Article deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Map<String, Integer> getOrderStatistics(int userId) {
        Map<String, Integer> orderStats = new HashMap<>();

        String query = "SELECT a.nom, COUNT(ac.idArticle) AS order_count " +
                "FROM article a " +
                "LEFT JOIN articlecommande ac ON a.id_article = ac.idArticle " +
                "WHERE a.created_by = ? " +  // Filtrer par le partenaire connecté
                "GROUP BY a.nom";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orderStats.put(rs.getString("nom"), rs.getInt("order_count"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderStats;
    }

}