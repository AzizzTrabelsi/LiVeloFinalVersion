package services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import models.User;
import net.minidev.json.JSONValue;
import org.mindrot.jbcrypt.BCrypt;
import utils.MyDatabase;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import net.minidev.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Random;
import io.github.cdimascio.dotenv.Dotenv;


public class Authentification implements interfaces.IServiceAuth {
    Connection conn = MyDatabase.getInstance().getConnection();
    private static final HashMap<String, String> resetCodes = new HashMap<>();
    private static String token;


    public static void setToken(String t) {
        token = t;
    }
    public static String getToken() {
        return token;
    }
    @Override
    public String login(String cin, String password) {
        String query = "SELECT * FROM user WHERE cin = ?";  // Recherche par CIN
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cin);  // Utilisation de CIN
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    // Création de l'objet User
                    String typeVehicule = rs.getString("type_vehicule");
                    models.type_vehicule vehicule = null;

                    if (typeVehicule != null) {
                        try {
                            vehicule = models.type_vehicule.valueOf(typeVehicule);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Valeur de type_vehicule invalide, valeur par défaut assignée.");
                        }
                    }

                    User user = new User(
                            rs.getInt("idUser"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            models.role_user.valueOf(rs.getString("role")),
                            rs.getBoolean("verified"),
                            rs.getString("adresse"),
                            vehicule,
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("num_tel"),
                            rs.getString("cin")
                    );

                     Authentification.setToken(generateToken(user));
                    System.out.println("Connexion réussie. Token généré : " + token);
                    // Stocker le token dans la variable statique
                    setToken(token);
                    JSONObject userInfo = decodeToken(token);
                    if (userInfo != null) {
                        System.out.println("Informations de l'utilisateur : " + userInfo.toJSONString());
                    }

                    return token;
                } else {
                    System.out.println("Mot de passe incorrect.");
                }
            } else {
                System.out.println("Utilisateur non trouvé avec le CIN : " + cin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Méthode pour stocker le token
    public static void setToken(String t) {
        token = t;
    }

    // Méthode pour récupérer le token
    public static String getToken() {
        return token;
    }



    private String generateToken(User user) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            JSONObject payload = new JSONObject();
            payload.put("idUser", user.getId());
            payload.put("nom", user.getNom());
            payload.put("prenom", user.getPrenom());
            payload.put("role", user.getRole().toString());
            payload.put("adresse", user.getAdresse());
            payload.put("email", user.getEmail());
            payload.put("num_tel", user.getNum_tel());
            payload.put("cin", user.getCin());
            payload.put("verified", user.isVerified());

            // Vérifiez si type_vehicule est null et fournissez une valeur par défaut
            String typeVehicule = (user.getType_vehicule() != null) ? user.getType_vehicule().toString() : "INCONNU";
            payload.put("type_vehicule", typeVehicule);

            JWSObject jwsObject = new JWSObject(header, new Payload(payload));

            String secretKey = "livelo_256_bit_long_secret_key_here";
            MACSigner signer = new MACSigner(secretKey.getBytes());

            jwsObject.sign(signer);

            return jwsObject.serialize();  // Retourne le token sérialisé
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return null;
    }
    private int getUserIdFromToken(String token) {
        JSONObject decodedToken = decodeToken(token);
        if (decodedToken != null && decodedToken.containsKey("idUser")) {
            return Integer.parseInt(decodedToken.get("idUser").toString());
        }
        return -1; // Retourne -1 si l'ID n'est pas trouvé
    }



    public JSONObject decodeToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            String payload = jwsObject.getPayload().toString();

            return (JSONObject) JSONValue.parse(payload);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }}

    @Override
    public boolean forgotPassword(String email) {
        String code = generateCode();
        resetCodes.put(email, code);
        return sendResetEmail(email, code);
    }

    public String verifyResetCode(String email, String enteredCode) {
        if (resetCodes.containsKey(email) && resetCodes.get(email).equals(enteredCode)) {
            resetCodes.remove(email); // Supprime le code après vérification réussie

            User user = getUserByEmail(email); // Récupère l'utilisateur par email
            if (user != null) {
                String token = generateToken(user); // Génère un token pour l'utilisateur
                System.out.println("Code de réinitialisation vérifié. Token généré : " + token);

                // Décoder le token pour afficher les informations de l'utilisateur
                JSONObject userInfo = decodeToken(token);
                if (userInfo != null) {
                    System.out.println("Informations de l'utilisateur décodées : " + userInfo.toJSONString());
                } else {
                    System.out.println("Erreur lors du décodage du token.");
                }

                return token; // Retourne le token généré
            } else {
                System.out.println("Utilisateur non trouvé avec l'email : " + email);
            }
        }
        return null; // Retourne null si le code est invalide ou l'utilisateur n'existe pas
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("idUser"));
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(models.role_user.valueOf(resultSet.getString("role")));
                user.setVerified(resultSet.getBoolean("verified"));
                user.setAdresse(resultSet.getString("adresse"));
                user.setNum_tel(resultSet.getString("num_tel"));
                user.setCin(resultSet.getString("cin"));

                // Gestion du type_vehicule (enum nullable)
                String typeVehiculeStr = resultSet.getString("type_vehicule");
                if (typeVehiculeStr != null) {
                    try {
                        models.type_vehicule typeVehicule = models.type_vehicule.valueOf(typeVehiculeStr);
                        user.setType_vehicule(typeVehicule);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Valeur de type_vehicule invalide : " + typeVehiculeStr);
                        user.setType_vehicule(null); // Ou définir une valeur par défaut
                    }
                } else {
                    user.setType_vehicule(null); // type_vehicule est null dans la base de données
                }

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private boolean sendResetEmail(String email, String code) {
        Email from = new Email("tasnimbenhassine1@gmail.com");
        String subject = "Password Reset Code";
        Email to = new Email(email);
        Content content = new Content("text/plain", "Your reset code is: " + code);
        Mail mail = new Mail(from, subject, to, content);
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("SECRET_KEY_SENDGRID");
        SendGrid sg = new SendGrid(key);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            return response.getStatusCode() == 202;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean resetPassword(String email, String code, String newPassword) {
        if (resetCodes.containsKey(email) && resetCodes.get(email).equals(code)) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            String query = "UPDATE user SET password = ? WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, hashedPassword);
                stmt.setString(2, email);
                int updated = stmt.executeUpdate();
                if (updated > 0) {
                    resetCodes.remove(email);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
