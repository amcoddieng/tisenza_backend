# Utilisateurs Par Défaut

Lors du démarrage de l'application, les utilisateurs suivants sont automatiquement créés :

## 🔐 Identifiants de Connexion

### Administrateur
- **Email** : admin@tissenza.com
- **Mot de passe** : admin123
- **Rôle** : ADMIN
- **Téléphone** : 22112345678

### Vendeurs
- **Email** : vendeur1@tissenza.com
- **Mot de passe** : vendeur123
- **Rôle** : VENDEUR
- **Téléphone** : 22112345679

- **Email** : vendeur2@tissenza.com
- **Mot de passe** : vendeur123
- **Rôle** : VENDEUR
- **Téléphone** : 22112345680

### Clients
- **Email** : client1@tissenza.com
- **Mot de passe** : client123
- **Rôle** : CLIENT
- **Téléphone** : 22112345681

- **Email** : client2@tissenza.com
- **Mot de passe** : client123
- **Rôle** : CLIENT
- **Téléphone** : 22112345682

## 📋 Informations des Utilisateurs

### Personnes créées
- **Nom** : Admin, Vendeur Un, Vendeur Deux, Client Un, Client Deux
- **Prénom** : Tissenza, Vendeur, Vendeur, Client, Client
- **Adresse** : Adresse par défaut
- **Ville** : Dakar

### Comptes créés
- **Statut** : ACTIF
- **Vérifié** : Oui
- **Téléphones uniques** : Chaque utilisateur a un numéro différent
- **Date de création** : Date du démarrage

## 🚀 Comportement

- **Premier démarrage** : Création automatique des 5 utilisateurs
- **Démarrages suivants** : Détection des utilisateurs existants, pas de recréation
- **Logs** : Affichage du nombre d'utilisateurs créés

## 🔧 Sécurité

- Les mots de passe sont hashés avec BCrypt
- Les utilisateurs sont créés avec des rôles appropriés
- Les comptes sont activés par défaut

## 📝 Notes

- Les utilisateurs sont créés dans la base de données locale PostgreSQL
- Les données persistent entre les redémarrages
- Pour réinitialiser, il faut vider la table `comptes` et `personne`
