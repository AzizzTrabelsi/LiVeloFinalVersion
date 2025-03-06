package models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Livraison {
    private int idLivraison;
    private int commandeId;
    private int createdBy;
    private Date createdAt;
    private int factureId;
    private int zoneId;
    private User user;
    private List<Avis> avisList = new ArrayList<>(); // 🔹 Relation One-to-Many avec Avis

    public Livraison(int idLivraison, int commandeId, int createdBy, Date createdAt, int factureId, int zoneId, User user) {
        this.idLivraison = idLivraison;
        this.commandeId = commandeId;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.factureId = factureId;
        this.zoneId = zoneId;
        this.user = user;
    }
    public Livraison(int idLivraison, Date dateLivraison, int adresse, User user) {
        this.idLivraison = idLivraison;
        this.createdAt = dateLivraison;
        this.zoneId= adresse;
        this.user = user;
    }


    public Livraison(int commandeId, int createdBy, Date createdAt, int factureId, int zoneId, User user) {
        this.commandeId = commandeId;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.factureId = factureId;
        this.zoneId = zoneId;
        this.user = user;
    }

    public int getIdLivraison() {
        return idLivraison;
    }

    public void setIdLivraison(int idLivraison) {
        this.idLivraison = idLivraison;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getFactureId() {
        return factureId;
    }

    public void setFactureId(int factureId) {
        this.factureId = factureId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public List<Avis> getAvisList() {
        return avisList;
    }

    public void setAvisList(List<Avis> avisList) {
        this.avisList = avisList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Livraison{" +
                "idLivraison=" + idLivraison +
                ", commandeId=" + commandeId +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", factureId=" + factureId +
                ", zoneId=" + zoneId +
                ", avisList=" + avisList + // 🔹
                ", livreur=" + user+
                '}';
    }
}