package models;


import java.util.ArrayList;
import java.util.List;

public class Categorie {




    private int id_categorie;
    private String nom;
    private String description;
    private String url_image;
    private int createdBy;
    private List<Article> articles = new ArrayList<>(); // 🔹 Relation One-to-Many avec Article


    public Categorie(int id_categorie, String nom, String description, String url_image, int createdBy) {
        this.id_categorie = id_categorie;
        this.nom = nom;
        this.description = description;
        this.url_image = url_image;
        this.createdBy = createdBy;
    }
    public Categorie( String nom, String description, int createdBy) {

        this.nom = nom;
        this.description = description;

        this.createdBy = createdBy;
    }
    public Categorie(int id_categorie) {
        this.id_categorie = id_categorie;
    }

    public Categorie(int id_categorie, String nom) {
        this.id_categorie = id_categorie;
        this.nom = nom;
    }
    // Constructeur qui prend un nom et une description
    public Categorie(String nom, String description, String url_image ) {
        this.nom = nom;
        this.description = description;
        this.url_image = url_image;
    }
    // Constructeur qui prend un nom et une description
    public Categorie(String nom, String description ) {
        this.nom = nom;
        this.description = description;
    }

    public Categorie(String nom, String description, String url_image, int createdBy) {
        this.nom = nom;
        this.description = description;
        this.url_image = url_image;
        this.createdBy = createdBy;
    }

    public int getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(int id_categorie) {
        this.id_categorie = id_categorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void addArticle(Article article) {
        this.articles.add(article);
        article.setCategorie(this);
    }


    @Override
    public String toString() {
        return "Categorie{" +
                "id_categorie=" + id_categorie +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", url_image='" + url_image + '\'' +
                ", createdBy=" + createdBy +
                ", articles=" + articles + // 🔹
                '}';
    }
}