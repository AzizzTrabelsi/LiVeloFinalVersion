
package services;


import com.stripe.model.PaymentSourceTypeAdapterFactory;
import interfaces.IServiceCrud;
import models.*;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudLivraison implements IServiceCrud<Livraison> {
    private CrudUser crudUser = new CrudUser();
    private CrudAvis crudAvis = new CrudAvis();
    Connection conn = MyDatabase.getInstance().getConnection();
    User user = new User();
    public CrudLivraison (){
        user.setId(52);
        this.user=crudAvis.getUser();
    }



    @Override
    public void add(Livraison liv) {
        String qry = "INSERT INTO livraison (commandeId, created_by, created_at, factureId, zoneId, id_livreur) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, liv.getCommandeId());
            statement.setInt(2, liv.getCreatedBy());
            statement.setDate(3, new java.sql.Date(liv.getCreatedAt().getTime()));
            statement.setInt(4, liv.getFactureId());
            statement.setInt(5, liv.getZoneId());
            statement.setInt(6, user.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        liv.setIdLivraison(generatedId);
                        System.out.println("Livraison ajoutée avec succès avec l'ID : " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int idLivraison) {
        // Supprimer les avis associés à cette livraison
        String deleteAvisQuery = "DELETE FROM avis WHERE livraisonId = ?";
        try (PreparedStatement avisStmt = conn.prepareStatement(deleteAvisQuery)) {
            avisStmt.setInt(1, idLivraison);
            avisStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Supprimer la livraison
        String deleteLivraisonQuery = "DELETE FROM livraison WHERE idLivraison = ?";
        try (PreparedStatement livraisonStmt = conn.prepareStatement(deleteLivraisonQuery)) {
            livraisonStmt.setInt(1, idLivraison);
            int rowsAffected = livraisonStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livraison et avis associés supprimés avec succès.");
            } else {
                System.out.println("Aucune livraison trouvée avec l'ID " + idLivraison);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public List<Livraison> getAll() {
        List<Livraison> livraisons = new ArrayList<>();
        String query = "SELECT * FROM livraison";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idLivraison = rs.getInt("idLivraison");
                int commandeId = rs.getInt("commandeId");
                int createdBy = rs.getInt("created_by");
                Timestamp createdAt = rs.getTimestamp("created_at");
                int factureId = rs.getInt("factureId");
                int zoneId = rs.getInt("zoneId");

                Livraison livraison = new Livraison(idLivraison, commandeId, createdBy, createdAt, factureId, zoneId, user);

                // Récupérer les avis associés à cette livraison
                String avisQuery = "SELECT * FROM avis WHERE livraisonId = ?";
                try (PreparedStatement avisStmt = conn.prepareStatement(avisQuery)) {
                    avisStmt.setInt(1, idLivraison);
                    try (ResultSet avisRs = avisStmt.executeQuery()) {
                        List<Avis> avisList = new ArrayList<>();
                        while (avisRs.next()) {
                            Avis avis = new Avis(
                                    avisRs.getInt("idAvis"),
                                    avisRs.getInt("created_by"),
                                    livraison, //
                                    avisRs.getTimestamp("created_at"),
                                    avisRs.getString("description")
                            );
                            avisList.add(avis);
                        }
                        livraison.setAvisList(avisList);
                    }
                }

                livraisons.add(livraison);
            }

            System.out.println("Nombre de livraisons récupérées : " + livraisons.size());
            for (Livraison l : livraisons) {
                System.out.println(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livraisons;
    }


    @Override
    public void update(Livraison livraison) {
        String qry = "UPDATE livraison SET commandeId = ?, created_by = ?, created_at = ?, factureId = ?, zoneId = ? WHERE idLivraison = ?";

        try (PreparedStatement statement = conn.prepareStatement(qry)) {
            statement.setInt(1, livraison.getCommandeId());
            statement.setInt(2, livraison.getCreatedBy());
            statement.setDate(3, new java.sql.Date(livraison.getCreatedAt().getTime()));
            statement.setInt(4, livraison.getFactureId());
            statement.setInt(5, livraison.getZoneId());
            statement.setInt(6, livraison.getIdLivraison());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livraison mise à jour avec succès.");
            } else {
                System.out.println("Aucune livraison trouvée avec l'ID " + livraison.getIdLivraison());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public Livraison getById(int idLivraison) {
        String query = "SELECT * FROM livraison WHERE idLivraison = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idLivraison);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Création de l'objet Livraison
                Livraison livraison = new Livraison(
                        rs.getInt("idLivraison"),
                        rs.getInt("commandeId"),
                        rs.getInt("created_by"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("factureId"),
                        rs.getInt("zoneId"),
                        crudUser.getById(rs.getInt("id_livreur"))
                );

                // Récupérer les avis associés à cette livraison
                String avisQuery = "SELECT * FROM avis WHERE livraisonId = ?";
                try (PreparedStatement avisStmt = conn.prepareStatement(avisQuery)) {
                    avisStmt.setInt(1, idLivraison);
                    try (ResultSet avisRs = avisStmt.executeQuery()) {
                        List<Avis> avisList = new ArrayList<>();
                        while (avisRs.next()) {
                            // Ici, on crée un objet Avis et on assigne l'objet Livraison à cet avis
                            Avis avis = new Avis(
                                    avisRs.getInt("idAvis"),
                                    avisRs.getInt("created_by"),
                                    livraison,  // Passer l'objet livraison
                                    avisRs.getTimestamp("created_at"),
                                    avisRs.getString("description")
                            );
                            avisList.add(avis);
                        }
                        // Associer la liste d'avis à la livraison
                        livraison.setAvisList(avisList);
                    }
                }

                System.out.println("Livraison trouvée : " + livraison);
                return livraison;
            } else {
                System.out.println("Aucune livraison trouvée avec l'ID : " + idLivraison);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    @Override
    public List<Livraison> search(String criteria) {
        List<Livraison> livraisons = new ArrayList<>();
        String query = "SELECT * FROM livraison WHERE commandeId LIKE ? OR created_by LIKE ? OR factureId LIKE ? OR zoneId LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchTerm = "%" + criteria + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);
            stmt.setString(4, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Livraison livraison = new Livraison(
                        rs.getInt("idLivraison"),
                        rs.getInt("commandeId"),
                        rs.getInt("created_by"),
                        rs.getTimestamp("created_at"),
                        rs.getInt("factureId"),
                        rs.getInt("zoneId"),
                        crudUser.getById(rs.getInt("id_livreur"))
                );
                livraisons.add(livraison);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (livraisons.isEmpty()) {
            System.out.println("Aucune livraison trouvée pour le critère : " + criteria);
        } else {
            System.out.println("Nombre de livraisons trouvées : " + livraisons.size());
            for (Livraison livraison : livraisons) {
                System.out.println("Livraison trouvée : ");
                System.out.println("ID: " + livraison.getIdLivraison());
                System.out.println("Commande ID: " + livraison.getCommandeId());
                System.out.println("Créée par (ID utilisateur): " + livraison.getCreatedBy());
                System.out.println("Facture ID: " + livraison.getFactureId());
                System.out.println("Zone ID: " + livraison.getZoneId());
                System.out.println("Date de création: " + livraison.getCreatedAt());
                System.out.println();
            }
        }

        return livraisons;
    }
    public List<Livraison> gethistorylivraisonbyid_livreur(int id){
        System.out.println("l'id du usze est ppppppppppppppppppppppppppppppppppppppppppp"+id);
        List<Livraison> livraisons = new ArrayList<>();
        String query = "SELECT * FROM livraison " +
                "INNER JOIN commande ON commande.id_commande = livraison.commandeId " +
                "WHERE livraison.id_livreur = " + id +
                " AND commande.statut = '" + statutlCommande.Delivered.name() + "'";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idLivraison = rs.getInt("idLivraison");
                int commandeId = rs.getInt("commandeId");
                int createdBy = rs.getInt("created_by");
                Timestamp createdAt = rs.getTimestamp("created_at");
                int factureId = rs.getInt("factureId");
                int zoneId = rs.getInt("zoneId");

                Livraison livraison = new Livraison(idLivraison, commandeId, createdBy, createdAt, factureId, zoneId, user);

                // Récupérer les avis associés à cette livraison
                String avisQuery = "SELECT * FROM avis WHERE livraisonId = ?";
                try (PreparedStatement avisStmt = conn.prepareStatement(avisQuery)) {
                    avisStmt.setInt(1, idLivraison);
                    try (ResultSet avisRs = avisStmt.executeQuery()) {
                        List<Avis> avisList = new ArrayList<>();
                        while (avisRs.next()) {
                            Avis avis = new Avis(
                                    avisRs.getInt("idAvis"),
                                    avisRs.getInt("created_by"),
                                    livraison, //
                                    avisRs.getTimestamp("created_at"),
                                    avisRs.getString("description")
                            );
                            avisList.add(avis);
                        }
                        livraison.setAvisList(avisList);
                    }
                }

                livraisons.add(livraison);
            }

            System.out.println("Nombre de livraisons récupérées : " + livraisons.size());
            for (Livraison l : livraisons) {
                System.out.println(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livraisons;

    }

}
