package controllers;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;
import models.User;
import services.CrudUser;
import java.util.List;


public class SMS {
    static Dotenv dotenv = Dotenv.load();
    static final String ACCOUNT_SID = dotenv.get("ACCOUNT_SID");
    static final String AUTH_TOKEN = dotenv.get("AUTH_TOKEN");
    static final String TWILIO_PHONE_NUMBER = dotenv.get("TWILIO_PHONE_NUMBER");


   
    static {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            System.out.println("Twilio initialisé avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur d'initialisation de Twilio : " + e.getMessage());
        }
    }
    public static void sendSms(String clientPhoneNumber, String articleName) {
        CrudUser crudUser = new CrudUser(); // Créer une instance de CrudUser
        List<User> clients = crudUser.getByRole("client");
        try {
            // Vérifier si le numéro de téléphone est valide
            if (clientPhoneNumber == null || clientPhoneNumber.isEmpty()) {
                System.err.println("Numéro de téléphone invalide : " + clientPhoneNumber);
                return;
            }

            // Si le numéro ne commence pas par "+216", ajouter le code pays pour la Tunisie
            if (!clientPhoneNumber.startsWith("+")) {
                clientPhoneNumber = "+216" + clientPhoneNumber;  // Ajouter le code pays +216 (pour la Tunisie)
            }

            // Vérifier que le numéro commence bien par +216 suivi de 8 chiffres
            if (!clientPhoneNumber.matches("\\+216\\d{8}")) {
                System.err.println("Numéro de téléphone invalide après correction : " + clientPhoneNumber);
                return;
            }

            // Envoi du SMS
            Message message = Message.creator(
                    new PhoneNumber(clientPhoneNumber),  // Numéro du client
                    new PhoneNumber(TWILIO_PHONE_NUMBER), // Numéro Twilio
                    "Bonjour ! " + articleName
            ).create();

            // Afficher la SID du message pour confirmer l'envoi
            System.out.println("SMS envoyé avec succès, SID: " + message.getSid());

        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
            e.printStackTrace();  // Afficher la trace complète de l'exception pour le débogage
        }
    }

}

