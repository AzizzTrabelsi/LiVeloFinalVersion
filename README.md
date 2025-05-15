# Livelo 🚲🌱

**Livelo** est une application desktop de **livraison écologique** développée en **Java**.  
Elle prend en charge une **gestion multi-rôles** (*Admin, Partenaire, Client, Livreur*), affiche des **statistiques en temps réel**, intègre un **chatbot intelligent**, et vise à optimiser les **livraisons urbaines durables**.

> Ce projet a été réalisé dans le cadre du cours **PIDEV 3A** à *Esprit School of Engineering*.  
Il explore la **gestion de tâches en temps réel** avec une attention particulière portée à **l’ergonomie** de l’interface et aux **fonctionnalités collaboratives**.
## 🌍 Objectif

L’objectif de **Livelo** est de :

- Réduire l'empreinte carbone liée aux **livraisons urbaines**.
- Optimiser la **gestion des commandes** et des **livreurs**.
- Proposer une **alternative écologique** aux services traditionnels.
🛠️ Fonctionnalités principales
Gestion des clients, livreurs et commandes.

Suivi des livraisons en temps réel.

Interface d'administration intuitive.

Notifications de statut de commande.

Sécurité et gestion des rôles utilisateurs.
## 📋 Table des matières

- [🚀 Installation](#-installation)
- [💻 Utilisation](#-utilisation)
- [🤝 Contribution](#-contribution)
- [🧰 Technologies utilisées](#-technologies-utilisées)
- [📄 Licence](#-licence)
## 🚀 Installation

### Cloner le projet :

```bash
git clone https://github.com/AzizzTrabelsi/LiVeloFinalVersion
cd LiveloDesktop
```
### Ouvrir le projet dans IntelliJ IDEA :

1. Lancez **IntelliJ IDEA**.  
2. Cliquez sur **"Open"**, puis sélectionnez le dossier `LiveloDesktop`.

### Configurer le JDK :

- Allez dans `File > Project Structure > Project`.
- Sélectionnez le **JDK 21** (ou la version que vous utilisez).

### Configurer la base de données (MySQL) :

1. Assurez-vous que **MySQL** est installé et en cours d'exécution.
2. Créez une base de données nommée `livelo` :

```sql
CREATE DATABASE livelo;
```
### Vérifiez que les identifiants suivants correspondent à votre configuration Java :

```java
private final String URL = "jdbc:mysql://127.0.0.1:3306/livelo";
private final String USER = "root";
private final String PASSWORD = "";
```
> 🔒 **Remarque** : Si vous avez défini un mot de passe pour l'utilisateur `root`, renseignez-le dans le champ `PASSWORD`.

---

### 🧩 Configurer Scene Builder :

1. Téléchargez et installez **JavaFX Scene Builder**.
2. Dans IntelliJ :
   - Faites un **clic droit** sur un fichier `.fxml` > **Open in Scene Builder**.
3. Vérifiez le chemin de Scene Builder dans :  
   `File > Settings > Languages & Frameworks > JavaFX`.

---

### ▶️ Lancer l'application :

Dans **IntelliJ**, exécutez la **classe principale** contenant la méthode suivante :

```java
public static void main(String[] args)
```

## 💻 Utilisation

Livelo propose une expérience différente selon le rôle de l’utilisateur :

### 👩‍💼 Administrateur

- Gère tous les utilisateurs (clients, livreurs, partenaires).
- Crée et modifie les catégories d’articles.
- Supervise les statistiques globales.
- Dispose d’un tableau de bord complet.

### 🤝 Partenaire

- Ajoute des articles dans les catégories créées.
- Consulte les statistiques liées à ses articles.
- Interagit avec les clients via le chatbot intelligent.

### 🛒 Client

- Parcourt les articles via la boutique.
- Passe une commande en ligne (paiement en ligne ou en espèces).
- Suit l’état de ses commandes en temps réel.

### 🚴 Livreur

- Accède à la liste des commandes à livrer.
- Accepte une commande.
- Met à jour le statut de livraison.
- Consulte son historique de livraisons.

---

## 🤝 Contribution

Un grand merci à tous ceux qui ont contribué à ce projet ! 🙌

### 👨‍💻 Contributeurs :

- **Tasnim Benhassine** – Développeuse du module utilisateur  
- **Mohamed Aziz Trabelsi** – Développeur du module commande  
- **Nouha Saidane** – Développeuse du module partenaire  
- **Zied Filali** – Développeur du module livraison  
- **Anas Ghorch** – Développeur du module zone

### 🛠️ Comment contribuer ?

1. **Forkez** le projet.
2. **Créez votre branche** :

```bash
git checkout -b nouvelle-fonctionnalité
```
### ✅ Commitez vos modifications :

```bash
git commit -am "Ajout d’une fonctionnalité"
```
### 📤 Poussez votre branche :

```bash
git push origin nouvelle-fonctionnalité
```
### 🔃 Créez une Pull Request via GitHub.
## 🧰 Technologies utilisées

- **Java 21** – Langage principal
- **JavaFX** – Interface graphique
- **Scene Builder** – Conception des fichiers `.fxml`
- **IntelliJ IDEA** – IDE principal
- **Maven** – Gestion des dépendances
- **MySQL** – Base de données relationnelle
- **JDBC** – Connexion à la base de données
- **FXML** – Définition de l’interface graphique

---

## 📄 Licence

Ce projet est sous licence **MIT**.  
Vous êtes libre de l’utiliser, modifier et redistribuer le code tant que vous conservez les mentions de droits d’auteur d’origine.

