package controllers;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class chatbot {
    Dotenv dotenv = Dotenv.load();
    String API_URL = dotenv.get("SECRET_KEY_CHATBOT");

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;
    @FXML
    private void initialize() {
        // Lier le bouton à la méthode envoyerMessage()
        sendButton.setOnAction(event -> envoyerMessage());
    }
    @FXML
    private void envoyerMessage() {
        String message = userInput.getText().trim();
        if (!message.isEmpty()) {
            chatArea.appendText("Vous: " + message + "\n");
            userInput.clear();

            // 📌 Appel à l'IA Gemini
            String reponseIA = appelerGemini(message);
            chatArea.appendText("IA: " + reponseIA + "\n");
        }}

    private String appelerGemini(String message) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // 📌 Construire la requête JSON
            JSONObject json = new JSONObject();
            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "user").put("parts", new JSONArray().put(new JSONObject().put("text", message))));
            json.put("contents", messages);

            // Envoyer la requête
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.toString().getBytes(StandardCharsets.UTF_8));
            }

            // Lire la réponse de l'API
            int responseCode = conn.getResponseCode();
            InputStream responseStream = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
            Scanner scanner = new Scanner(responseStream, StandardCharsets.UTF_8);
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            // Debug : Afficher la réponse complète dans la console
            System.out.println("Réponse Gemini : " + response);

            // Extraire le texte généré par Gemini
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getJSONArray("candidates").getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la communication avec Gemini.";
        }
    }
}

