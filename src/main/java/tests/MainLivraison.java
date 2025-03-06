package tests;


import models.Livraison;
import models.User;
import services.CrudLivraison;
import java.util.Date;

public class MainLivraison {
    public static void main(String[] args) {
        User user = new User();
        user.setId(52);
        // Création d'une nouvelle instance de Livraison
        Livraison nouvelleLivraison = new Livraison(
                1,                          // ID de la livraison
                5,                        // ID de la commande (commandeId)
                53,                        // ID de l'utilisateur (createdBy)
                new Date(),                 // Date de la livraison (createdAt)
                3,                        // ID de la facture (factureId)
                404,                         // ID de la zone (zoneId)
                user
        );

        // Instanciation du service de gestion des livraisons
        CrudLivraison crudLivraison = new CrudLivraison();

        // Ajouter la nouvelle livraison
        crudLivraison.add(nouvelleLivraison);

        // Récupérer toutes les livraisons
        crudLivraison.getAll();

        // Mise à jour d'une livraison (exemple de modification)
        //nouvelleLivraison.setCreatedAt(2025-02-19); // Changer l'ID de la commande par exemple
        crudLivraison.update(nouvelleLivraison);

        // Récupérer toutes les livraisons après mise à jour
        crudLivraison.getAll();

        // Recherche d'une livraison par son ID
        crudLivraison.getById(1);

        // Recherche d'une livraison selon un critère (par exemple, l'ID de la commande)
        crudLivraison.search("505");
    }
}
