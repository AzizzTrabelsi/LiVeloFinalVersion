package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import models.Facture;
import net.minidev.json.JSONObject;
import services.Authentification;
import services.CrudFacture;
import models.type_paiement;
import services.PDFservice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // For generating a unique token

public class ClientBills {

    private final CrudFacture su = new CrudFacture();
    @FXML
    private VBox vboxBills; // Reference to the VBox where bills will be displayed
    private Facture selectedFacture;

    public void setSelectedFacture(Facture facture) {
        this.selectedFacture = facture;
    }
        @FXML
        private VBox billsContainer;

        public void initialize () {
            List<Facture> factures = su.getByIdUser((getUserIDFromToken()));
            for (Facture fact : factures) {
                HBox billItem = new HBox(20);
                billItem.setStyle("-fx-padding: 15px; -fx-background-color: white; -fx-border-radius: 10px; -fx-border-color: #ccc;");

                Label billId = new Label("Bill ID: " + fact.getIdFacture());
                Label amount = new Label("Amount: " + fact.getMontant() + " TND");
                Label date = new Label("Date: " + fact.getDatef());

                Button downloadButton = new Button("Download PDF");
                downloadButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                downloadButton.setOnAction(e -> onDownloadInvoiceClicked(fact));

                billItem.getChildren().addAll(billId, amount, date, downloadButton);
                billsContainer.getChildren().add(billItem);
            }
        }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
        private void onDownloadInvoiceClicked (Facture facture){
            PDFservice pdfService = new PDFservice(facture);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Invoice PDF created successfully.");
        }
        public int getUserIDFromToken () {
            String token = Authentification.getToken();
            if (token != null) {
                JSONObject userInfo = new Authentification().decodeToken(token);
                if (userInfo != null) {
                    System.out.println("Token récupéré: " + token);
                    Object idObject = userInfo.get("idUser");
                    if (idObject instanceof Number) {
                        System.out.println("ID utilisateur connecté: " + ((Number) idObject).intValue());
                        return ((Number) idObject).intValue();
                    } else {
                        System.out.println("Erreur: idUser n'est pas un nombre.");
                    }
                } else {
                    System.out.println("Erreur dans le décodage du token.");
                }
            } else {
                System.out.println("Aucun token trouvé. L'utilisateur n'est pas connecté.");
            }
            return -1;
        }
        private void addBillToDisplay () {
            int userId = getUserIDFromToken();
            if (userId == -1) {
                System.out.println("Impossible de récupérer l'ID utilisateur, liste non chargée.");
                return;
            }
            List<Facture> factures = new ArrayList<>(su.getByIdUser(userId)); // Assuming su is the service class to fetch factures by user
            if (factures.size() == 0) {

                for (Facture fact : factures) {
                    // Create an HBox for each bill (facture)
                    HBox billItem = new HBox(15);
                    billItem.setStyle("-fx-padding: 10px; -fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 5px;");

                    // Create Labels for each field and add them to the HBox
                    billItem.getChildren().add(new Label("ID: " + fact.getIdFacture())); // Bill ID (could also be facture.getId())
                    billItem.getChildren().add(new Label("Amount: " + fact.getMontant())); // Amount
                    billItem.getChildren().add(new Label("Date: " + fact.getDatef())); // Date
                    billItem.getChildren().add(new Label("Payment Type: " + fact.getTypePaiement())); // Payment Type

                    billItem.getChildren().add(new Label("Order ID: " + fact.getCommandeId())); // Order ID

                    // Add the HBox to the VBox for display
                    vboxBills.getChildren().add(billItem);
                }
            } else {
                // Handle case when there is no facture for the given user ID
                System.out.println("No bills found for user with ID: " + userId);
            }
        }
    }
