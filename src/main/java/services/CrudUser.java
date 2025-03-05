package services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import interfaces.IServiceCrud;
import models.User;
import models.role_user;
import models.type_vehicule;
import utils.MyDatabase;
import org.mindrot.jbcrypt.BCrypt;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrudUser implements IServiceCrud<User> {
    Connection conn= MyDatabase.getInstance().getConnection();

    @Override
    public void add(User user) {
        String qry = "INSERT INTO `user` (`nom`, `prenom`, `role`, `verified`, `adresse`, `type_vehicule`, `email`, `password`, `num_tel`, `cin`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getRole().toString());
            statement.setInt(4, user.isVerified() ? 1 : 0);
            statement.setString(5, user.getAdresse());

            if (user.getType_vehicule() != null) {
                statement.setString(6, user.getType_vehicule().toString());
            } else {
                statement.setNull(6, Types.VARCHAR);
            }

            statement.setString(7, user.getEmail());
            statement.setString(8, hashedPassword);
            statement.setString(9, user.getNum_tel());
            statement.setString(10, user.getCin());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        user.setId(generatedId);
                        System.out.println("User ajouté avec ID: " + generatedId);

                        notifyAdmins(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void notifyAdmins(User newUser) {
       List<String> adminEmails = getAdminEmails();

        Email from = new Email("tasnimbenhassine1@gmail.com");
        String subject = "New User Waiting for Access";
        StringBuilder content = new StringBuilder("A new user has registered:\n\n");
        content.append("Nom: ").append(newUser.getNom()).append("\n");
        content.append("Prenom: ").append(newUser.getPrenom()).append("\n");
        content.append("Email: ").append(newUser.getEmail()).append("\n");
        content.append("CIN: ").append(newUser.getCin()).append("\n");


        for (String adminEmail : adminEmails) {
            Email to = new Email(adminEmail);
            Content emailContent = new Content("text/plain", content.toString());
            Mail mail = new Mail(from, subject, to, emailContent);
            Dotenv dotenv = Dotenv.load();
            String key = dotenv.get("SECRET_KEY_SENDGRID");
            SendGrid sg = new SendGrid(key);
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private List<String> getAdminEmails() {
        List<String> adminEmails = new ArrayList<>();
        String qry = "SELECT email FROM user WHERE role = 'admin'";

        try (PreparedStatement statement = conn.prepareStatement(qry);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                adminEmails.add(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return adminEmails;
    }



    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM `user`";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idUser");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String roleString = rs.getString("role");
                String typeVehiculeString = rs.getString("type_vehicule"); // Peut être NULL
                boolean verified = rs.getBoolean("verified");
                String adresse = rs.getString("adresse");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String num_tel = rs.getString("num_tel");
                String cin = rs.getString("cin");

                role_user role = role_user.valueOf(roleString);

                type_vehicule typeVehicule = null;
                if (typeVehiculeString != null && !typeVehiculeString.trim().isEmpty()) {
                    try {
                        typeVehicule = type_vehicule.valueOf(typeVehiculeString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valeur inconnue pour type_vehicule: " + typeVehiculeString);
                        typeVehicule = null;
                    }
                }

                User user = new User(id, nom, prenom, role, verified, adresse,
                        typeVehicule, email, password, num_tel, cin);

                users.add(user);
            }

            System.out.println("Nombre d'utilisateurs récupérés : " + users.size());
            for (User u : users) {
                System.out.println(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


    @Override
    public void update(User user) {
        String qry = "UPDATE `user` SET `nom` = ?, `prenom` = ?, `role` = ?, `verified` = ?, " +
                "`adresse` = ?, `type_vehicule` = ?, `email` = ?, `password` = ?, " +
                "`num_tel` = ?, `cin` = ? WHERE `idUser` = ?";

        try (PreparedStatement statement = conn.prepareStatement(qry)) {
            String hashedPassword;
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            } else {
                System.out.println("Mot de passe vide, mise à jour de l'utilisateur sans changer le mot de passe.");
                hashedPassword = user.getPassword();
            }

            // Set parameters for the prepared statement
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getRole().toString());
            statement.setInt(4, user.isVerified() ? 1 : 0);
            statement.setString(5, user.getAdresse());

            // Handle type_vehicule similarly to the add method
            if (user.getType_vehicule() != null) {
                statement.setString(6, user.getType_vehicule().toString());
            } else {
                statement.setNull(6, Types.VARCHAR);
            }

            statement.setString(7, user.getEmail());
            statement.setString(8, hashedPassword);
            statement.setString(9, user.getNum_tel());
            statement.setString(10, user.getCin());
            statement.setInt(11, user.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No user found with ID " + user.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(int id) {
        String qry = "DELETE FROM `user` WHERE `idUser` = " + id;
        try (PreparedStatement statement = conn.prepareStatement(qry)) {
            // Exécuter la requête
            statement.executeUpdate();
            System.out.println("User deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getById(int id) {
        String query = "SELECT * FROM `user` WHERE `idUser` = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("idUser");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String roleString = rs.getString("role");
                String typeVehiculeString = rs.getString("type_vehicule"); // Peut être NULL
                boolean verified = rs.getBoolean("verified");
                String adresse = rs.getString("adresse");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String num_tel = rs.getString("num_tel");
                String cin = rs.getString("cin");

                // Conversion des enums
                role_user role = role_user.valueOf(roleString);

                type_vehicule typeVehicule = null;
                if (typeVehiculeString != null && !typeVehiculeString.trim().isEmpty()) {
                    try {
                        typeVehicule = type_vehicule.valueOf(typeVehiculeString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valeur inconnue pour type_vehicule: " + typeVehiculeString);
                        typeVehicule = null;
                    }
                }

                User user = new User(userId, nom, prenom, role, verified, adresse,
                        typeVehicule, email, password, num_tel, cin);

                System.out.println("Utilisateur trouvé : " + user);
                return user;
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<User> search(String criteria) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM `user` WHERE `cin` LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchTerm = "%" + criteria + "%";
            stmt.setString(1, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idUser");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String roleString = rs.getString("role");
                boolean verified = rs.getBoolean("verified");
                String adresse = rs.getString("adresse");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String num_tel = rs.getString("num_tel");
                String cin = rs.getString("cin");

                // Convertir roleString en enum
                role_user role = role_user.valueOf(roleString);

                // Gérer le cas où type_vehicule est NULL
                type_vehicule typeVehicule = null;
                String typeVehiculeString = rs.getString("type_vehicule");
                if (typeVehiculeString != null && !typeVehiculeString.trim().isEmpty()) {
                    try {
                        typeVehicule = type_vehicule.valueOf(typeVehiculeString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valeur inconnue pour type_vehicule: " + typeVehiculeString);
                    }
                }

                // Créer l'objet User
                User user = new User(id, nom, prenom, role, verified, adresse,
                        typeVehicule, email, password, num_tel, cin);

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (users.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé pour le critère : " + criteria);
        } else {
            System.out.println("Nombre d'utilisateurs trouvés : " + users.size());
            for (User user : users) {
                System.out.println("Utilisateur trouvé : ");
                System.out.println("Nom: " + user.getNom());
                System.out.println("Prénom: " + user.getPrenom());
                System.out.println("Role: " + user.getRole());
                System.out.println("Vérifié: " + user.isVerified());
                System.out.println("Adresse: " + user.getAdresse());
                System.out.println("Type de véhicule: " + (user.getType_vehicule() != null ? user.getType_vehicule() : "Aucun"));
                System.out.println("Email: " + user.getEmail());
                System.out.println("Numéro de téléphone: " + user.getNum_tel());
                System.out.println("CIN: " + user.getCin());
                System.out.println();
            }
        }

        return users;
    }


    public boolean existsCin(String cin) {
        // Ici, tu effectues une requête pour vérifier si le CIN existe déjà
        String query = "SELECT COUNT(*) FROM `user` WHERE `cin` = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {  // Utilisation de la connexion existante 'conn'
            stmt.setString(1, cin); // On remplace le ? par le CIN

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Si le résultat est supérieur à 0, cela signifie que le CIN existe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Si une exception se produit ou aucun résultat n'est trouvé
    }

    public List<User> getByRole(String role) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM `user` WHERE `role` = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, role);  // Utiliser le rôle comme paramètre dans la requête SQL
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("idUser");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String roleString = rs.getString("role");
                String typeVehiculeString = rs.getString("type_vehicule"); // Peut être NULL
                boolean verified = rs.getBoolean("verified");
                String adresse = rs.getString("adresse");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String num_tel = rs.getString("num_tel");
                String cin = rs.getString("cin");

                // Conversion des enums
                role_user roleEnum = role_user.valueOf(roleString);

                type_vehicule typeVehicule = null;
                if (typeVehiculeString != null && !typeVehiculeString.trim().isEmpty()) {
                    try {
                        typeVehicule = type_vehicule.valueOf(typeVehiculeString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valeur inconnue pour type_vehicule: " + typeVehiculeString);
                        typeVehicule = null;
                    }
                }

                User user = new User(userId, nom, prenom, roleEnum, verified, adresse,
                        typeVehicule, email, password, num_tel, cin);

                users.add(user);  // Ajouter l'utilisateur à la liste
            }

            if (users.isEmpty()) {
                System.out.println("Aucun utilisateur trouvé avec le rôle : " + role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;  // Retourner la liste des utilisateurs
    }


}
