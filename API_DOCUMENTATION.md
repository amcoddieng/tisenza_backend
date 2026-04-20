# Tissenza Backend API Documentation

## Table des Matières
- [Configuration](#configuration)
- [User Management APIs](#user-management-apis)
- [Boutique Management APIs](#boutique-management-apis)
- [Category Management APIs](#category-management-apis)
- [Product Management APIs](#product-management-apis)
- [Article Management APIs](#article-management-apis)
- [Stock History APIs](#stock-history-apis)

---

## Authentication APIs

### Connexion Utilisateur
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Connexion réussie",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "userId": 1,
    "email": "john.doe@example.com",
    "username": "Doe John",
    "role": "CLIENT",
    "expiresIn": 86400000
  }
}
```

**Erreurs possibles:**
- `400 Bad Request` - Email ou mot de passe invalide
- `401 Unauthorized` - Compte suspendu ou inactif

**Codes d'erreur détaillés:**
- `BAD_PASSWORD` - Mot de passe incorrect pour l'email spécifié
- `EMAIL_NOT_FOUND` - Aucun compte trouvé avec l'email spécifié
- `ACCOUNT_SUSPENDED` - Compte suspendu. Veuillez contacter l'administrateur
- `ACCOUNT_INACTIVE` - Compte inactif. Veuillez activer votre compte
- `ACCOUNT_DISABLED` - Compte désactivé. Veuillez contacter l'administrateur
- `ACCOUNT_LOCKED` - Compte verrouillé. Veuillez contacter l'administrateur
- `AUTH_ERROR` - Erreur technique lors de l'authentification
- `EMAIL_ALREADY_EXISTS` - Email déjà utilisé lors de l'inscription
- `INVALID_CREDENTIALS` - Identifiants invalides (générique)

**Exemples de réponses d'erreur:**

**Email non trouvé:**
```json
{
  "success": false,
  "message": "Aucun compte trouvé avec l'email: user@example.com",
  "data": null,
  "errorCode": "EMAIL_NOT_FOUND"
}
```

**Mot de passe incorrect:**
```json
{
  "success": false,
  "message": "Mot de passe incorrect pour l'email: user@example.com",
  "data": null,
  "errorCode": "BAD_PASSWORD"
}
```

---

### Inscription Utilisateur
```http
POST /api/auth/register
Content-Type: application/json

{
  "nom": "Doe",
  "prenom": "John",
  "email": "john.doe@example.com",
  "password": "password123",
  "telephone": "+221771234567",
  "adresse": "Dakar, Sénégal",
  "ville": "dakar",
  "role": "CLIENT"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Compte créé avec succès",
  "data": {
    "userId": 1,
    "email": "john.doe@example.com",
    "username": "Doe John",
    "role": "CLIENT",
    "message": "Compte créé avec succès"
  }
}
```

**Validation:**
- Nom obligatoire (2-100 caractères)
- Prénom obligatoire (2-100 caractères)
- Email unique et format valide
- Mot de passe obligatoire (min 6 caractères)
- Téléphone optionnel
- Rôle par défaut: CLIENT

---

### Déconnexion
```http
POST /api/auth/logout
Authorization: Bearer <token_jwt>
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Déconnexion réussie",
  "data": null
}
```

---

### Informations Utilisateur Actuel
```http
GET /api/auth/me
Authorization: Bearer <token_jwt>
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Informations utilisateur récupérées",
  "data": {
    "id": 2,
    "nom": "Doe",
    "prenom": "John",
    "adresse": "Dakar, Sénégal",
    "photoProfil": null,
    "ville": "dakar",
    "createdAt": "2026-04-16T22:11:53.802206",
    "updatedAt": "2026-04-16T22:11:53.802206"
  }
}
```

**Headers requis:**
- `Authorization: Bearer <token_jwt>` pour accéder

---

### Diagnostic d'Authentification

#### Vérifier un Email
```http
GET /api/auth/diagnostic/check-email/{email}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Diagnostic complété",
  "data": {
    "found": true,
    "compteId": 1,
    "email": "john.doe@example.com",
    "statut": "ACTIF",
    "role": "CLIENT",
    "isVerified": false,
    "hasPassword": true,
    "passwordLength": 60,
    "passwordHash": "$2a$10$N9qo8uLOickgx2ZMRZoMy...",
    "hasPersonne": true,
    "personneId": 1,
    "personneName": "Doe John"
  }
}
```

#### Lister tous les Comptes
```http
GET /api/auth/diagnostic/list-accounts
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Liste des comptes",
  "data": {
    "totalAccounts": 2,
    "accounts": [
      {
        "id": 1,
        "email": "john.doe@example.com",
        "statut": "ACTIF",
        "role": "CLIENT",
        "isVerified": false,
        "hasPassword": true,
        "hasPersonne": true
      }
    ]
  }
}
```

---

## Configuration

**Base URL:** `http://localhost:8081`
**Swagger UI:** `http://localhost:8081/swagger-ui.html`
**Content-Type:** `application/json`

---

## User Management APIs

### Personne APIs

#### Créer une personne
```http
POST /api/personnes
Content-Type: application/json

{
  "nom": "Doe",
  "prenom": "John",
  "email": "john.doe@example.com",
  "telephone": "+221771234567",
  "dateNaissance": "1990-05-15",
  "adresse": "Dakar, Sénégal"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nom": "Doe",
  "prenom": "John",
  "email": "john.doe@example.com",
  "telephone": "+221771234567",
  "dateNaissance": "1990-05-15",
  "adresse": "Dakar, Sénégal",
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Lister toutes les personnes
```http
GET /api/personnes
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nom": "Doe",
    "prenom": "John",
    "email": "john.doe@example.com",
    "telephone": "+221771234567",
    "dateNaissance": "1990-05-15",
    "adresse": "Dakar, Sénégal",
    "createdAt": "2026-04-13T01:50:00"
  }
]
```

#### Rechercher par nom
```http
GET /api/personnes/search/nom?nom=Doe
```

#### Compter les personnes
```http
GET /api/personnes/count
```

**Response (200 OK):**
```json
1
```

### Compte APIs

#### Créer un compte
```http
POST /api/comptes
Content-Type: application/json

{
  "personneId": 1,  
  "email": "john.doe@example.com",
  "motDePasse": "password123",
  "telephone": "+221771234567",
  "role": "CLIENT"
}
```

**Contraintes d'unicité:**
- `email` doit être unique
- `telephone` doit être unique

**Erreurs possibles:**
- `400 Bad Request` - Email ou téléphone déjà utilisé
- `Runtime Exception` - "Email déjà utilisé: xxx" ou "Téléphone déjà utilisé: xxx"

**Response (201 Created):**
```json
{
  "id": 1,
  "personneId": 1,
  "email": "john.doe@example.com",
  "motDePasse": "$2a$10$...",
  "telephone": "+221771234567",
  "role": "CLIENT",
  "statut": "ACTIF",
  "isVerified": false,
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Activer/Désactiver un compte
```http
PUT /api/comptes/1/activate
PUT /api/comptes/1/deactivate
```

#### Vérifier un compte
```http
PUT /api/comptes/1/verify
```

---

## Boutique Management APIs

### Boutique APIs

#### Créer une boutique
```http
POST /api/boutiques
Content-Type: application/json

{
  "vendeur": {
    "id": 1
  },
  "nom": "Ma Boutique",
  "description": "Boutique de vêtements",
  "addresse": "Dakar, Plateau",
  "logo": "https://example.com/logo.png"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "vendeur": {
    "id": 1
  },
  "nom": "Ma Boutique",
  "description": "Boutique de vêtements",
  "addresse": "Dakar, Plateau",
  "logo": "https://example.com/logo.png",
  "statut": "EN_ATTENTE",
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Modifier une boutique
```http
PUT /api/boutiques/{id}
Authorization: Bearer <token_jwt>
Content-Type: application/json

{
  "vendeur": {
    "id": 123
  },
  "nom": "Nouveau nom de boutique",
  "description": "Nouvelle description",
  "addresse": "Nouvelle adresse",
  "logo": "https://example.com/nouveau-logo.png",
  "statut": "VALIDE",
  "note": 4.5
}
```

#### Valider/Refuser une boutique
```http
PUT /api/boutiques/{id}/validate
PUT /api/boutiques/{id}/refuse
```

#### Mettre à jour la note
```http
PUT /api/boutiques/{id}/note?note=4.5
```

#### Rechercher des boutiques
```http
GET /api/boutiques/search?keyword=vêtements
GET /api/boutiques/statut/VALIDE
GET /api/boutiques/vendeur/1
```

---

## Document Management APIs

### Document APIs

#### Types de documents disponibles
- `CARTE_IDENTITE`
- `PASSPORT` 
- `RCCM`
- `PERMIS_SEJOUR`

#### Créer un document
```http
POST /api/documents
Content-Type: application/json

{
  "personneId": 1,
  "type": "CARTE_IDENTITE",
  "url": "https://example.com/carte-id.pdf"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "personneId": 1,
  "type": "CARTE_IDENTITE",
  "url": "https://example.com/carte-id.pdf",
  "validated": false,
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Types de documents disponibles
- `CARTE_IDENTITE`
- `NINEA`
- `PASSPORT`
- `RCCM`

#### Valider un document
```http
PUT /api/documents/1/validate
```

#### Rechercher des documents
```http
GET /api/documents/personne/1
GET /api/documents/type/CARTE_IDENTITE
GET /api/documents/validated/true
```

---

## Category Management APIs

### Catégorie APIs

#### Créer une catégorie
```http
POST /api/categories
Content-Type: application/json

{
  "nom": "Vêtements",
  "description": "Catégorie de vêtements pour hommes et femmes"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nom": "Vêtements",
  "description": "Catégorie de vêtements pour hommes et femmes",
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Lister les catégories avec sous-catégories
```http
GET /api/categories/with-sous-categories
```

#### Rechercher des catégories
```http
GET /api/categories/search?keyword=vêtements
GET /api/categories/nom/Vêtements
```

#### Modifier une catégorie
```http
PUT /api/categories/{id}
Content-Type: application/json

{
  "nom": "Nouveau nom",
  "description": "Nouvelle description"
}
```

### Sous-Catégorie APIs

#### Créer une sous-catégorie
```http
POST /api/sous-categories
Content-Type: application/json

{
  "categorieId": 1,
  "nom": "T-shirts",
  "description": "T-shirts pour hommes"
}
```

#### Lister les sous-catégories
```http
GET /api/sous-categories
GET /api/sous-categories/categorie/1
```

#### Modifier une sous-catégorie
```http
PUT /api/sous-categories/{id}
Content-Type: application/json

{
  "categorieId": 1,
  "nom": "Polo shirts",
  "description": "Polos pour hommes"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "categorieId": 1,
  "nom": "Chemises",
  "description": "Chemises pour hommes",
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Lister les sous-catégories par catégorie
```http
GET /api/sous-categories/categorie/1
GET /api/sous-categories/categorie/1/with-produits
```

---

## Product Management APIs

### Produit APIs

#### Créer un produit
```http
POST /api/produits
Content-Type: application/json

{
  "boutiqueId": 1,
  "sousCategorieId": 1,
  "nom": "Chemise en coton",
  "description": "Chemise confortable en coton bio",
  "image": "https://example.com/chemise.jpg",
  "statut": "ACTIF"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "boutiqueId": 1,
  "sousCategorieId": 1,
  "nom": "Chemise en coton",
  "description": "Chemise confortable en coton bio",
  "image": "https://example.com/chemise.jpg",
  "statut": "ACTIF",
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Lister les produits avec articles
```http
GET /api/produits/1/with-articles
```

#### Rechercher des produits
```http
GET /api/produits/boutique/1
GET /api/produits/sous-categorie/1
GET /api/produits/statut/ACTIF
GET /api/produits/search?keyword=chemise
```

#### Activer/Désactiver un produit
```http
PUT /api/produits/1/activate
PUT /api/produits/1/deactivate
```

---

## Article Management APIs

### Article APIs

#### Créer un article
```http
POST /api/articles
Content-Type: application/json

{
  "produitId": 1,
  "sku": "CHM-COT-001",
  "prix": 29.99,
  "stockActuel": 50,
  "attributs": "{\"couleur\":\"bleu\",\"taille\":\"M\",\"matiere\":\"coton\"}",
  "image": "https://example.com/chemise-bleu.jpg"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "produitId": 1,
  "sku": "CHM-COT-001",
  "prix": 29.99,
  "stockActuel": 50,
  "attributs": "{\"couleur\":\"bleu\",\"taille\":\"M\",\"matiere\":\"coton\"}",
  "image": "https://example.com/chemise-bleu.jpg"
}
```

#### Gérer le stock
```http
# Mettre à jour le stock
PUT /api/articles/1/stock?newStock=45

# Ajouter du stock
PUT /api/articles/1/stock/add?quantity=10&motif=Réapprovisionnement

# Retirer du stock
PUT /api/articles/1/stock/remove?quantity=5&motif=Vente
```

#### Rechercher des articles
```http
GET /api/articles/produit/1
GET /api/articles/sku/CHM-COT-001
GET /api/articles/out-of-stock
GET /api/articles/price-range?min=20&max=50
GET /api/articles/search?keyword=bleu
```

#### Articles par stock
```http
GET /api/articles/stock/greater/10
GET /api/articles/stock/less/5
```

#### Statistiques sur les articles
```http
GET /api/articles/count/out-of-stock
GET /api/articles/price/min/1
GET /api/articles/price/max/1
GET /api/articles/stock/total/1
```

---

## Stock History APIs

### Historique de Stock APIs

#### Créer un mouvement de stock
```http
POST /api/historique-stock
Content-Type: application/json

{
  "articleId": 1,
  "quantite": 10,
  "type": "ENTREE",
  "motif": "Nouvelle livraison"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "articleId": 1,
  "quantite": 10,
  "type": "ENTREE",
  "motif": "Nouvelle livraison",
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Types de mouvements
- `ENTREE` - Entrée de stock
- `SORTIE` - Sortie de stock

#### Rechercher l'historique
```http
GET /api/historique-stock/article/1
GET /api/historique-stock/type/ENTREE
GET /api/historique-stock/article/1/type/ENTREE
GET /api/historique-stock/search/motif?keyword=livraison
```

#### Historique par plage de dates
```http
GET /api/historique-stock/date-range?startDate=2026-04-01T00:00:00&endDate=2026-04-30T23:59:59
GET /api/historique-stock/article/1/date-range?startDate=2026-04-01T00:00:00&endDate=2026-04-30T23:59:59
```

#### Derniers mouvements
```http
GET /api/historique-stock/latest
GET /api/historique-stock/latest/article/1
```

#### Statistiques sur les mouvements
```http
GET /api/historique-stock/total/entree/1
GET /api/historique-stock/total/sortie/1
GET /api/historique-stock/stats/type
```

---

## Exemples de Workflows Complets

### 1. Créer une boutique complète avec produits

```bash
# 1. Créer une personne
curl -X POST http://localhost:8081/api/personnes \
  -H "Content-Type: application/json" \
  -d '{"nom":"Seller","prenom":"John","email":"seller@example.com","telephone":"+221771234567","dateNaissance":"1985-05-15","adresse":"Dakar"}'

# 2. Créer son compte vendeur
curl -X POST http://localhost:8081/api/comptes \
  -H "Content-Type: application/json" \
  -d '{"personneId":1,"email":"seller@example.com","motDePasse":"password123","telephone":"+221771234567","role":"VENDEUR"}'

# 3. Créer une catégorie
curl -X POST http://localhost:8081/api/categories \
  -H "Content-Type: application/json" \
  -d '{"nom":"Électronique","description":"Appareils électroniques"}'

# 4. Créer une sous-catégorie
curl -X POST http://localhost:8081/api/sous-categories \
  -H "Content-Type: application/json" \
  -d '{"categorieId":1,"nom":"Smartphones","description":"Téléphones mobiles"}'

# 5. Créer la boutique
curl -X POST http://localhost:8081/api/boutiques \
  -H "Content-Type: application/json" \
  -d '{"vendeurId":1,"nom":"Tech Store","description":"Boutique de tech","addresse":"Dakar"}'

# 6. Valider la boutique
curl -X PUT http://localhost:8081/api/boutiques/1/validate

# 7. Créer un produit
curl -X POST http://localhost:8081/api/produits \
  -H "Content-Type: application/json" \
  -d '{"boutiqueId":1,"sousCategorieId":1,"nom":"iPhone 15","description":"Dernier iPhone","statut":"ACTIF"}'

# 8. Créer un article
curl -X POST http://localhost:8081/api/articles \
  -H "Content-Type: application/json" \
  -d '{"produitId":1,"sku":"IP15-128-BLK","prix":999.99,"stockActuel":25,"attributs":"{\"couleur\":\"noir\",\"capacite\":\"128GB\"}"}'
```

### 2. Gérer le stock d'un article

```bash
# Ajouter du stock
curl -X PUT "http://localhost:8081/api/articles/1/stock/add?quantity=10&motif=Réapprovisionnement"

# Retirer du stock (vente)
curl -X PUT "http://localhost:8081/api/articles/1/stock/remove?quantity=2&motif=Vente client"

# Vérifier l'historique
curl -X GET "http://localhost:8081/api/historique-stock/article/1"

# Vérifier le stock actuel
curl -X GET "http://localhost:8081/api/articles/1"
```

---

## Product Management APIs

### Produit APIs

#### Créer un produit
```http
POST /api/produits
Content-Type: application/json

{
  "nom": "iPhone 15 Pro",
  "description": "Dernier modèle d'iPhone avec écran OLED",
  "reference": "IP15-PRO-256",
  "categorieId": 1,
  "boutiqueId": 1,
  "image": "https://example.com/iphone15pro.jpg"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nom": "iPhone 15 Pro",
  "description": "Dernier modèle d'iPhone avec écran OLED",
  "reference": "IP15-PRO-256",
  "categorieId": 1,
  "boutiqueId": 1,
  "image": "https://example.com/iphone15pro.jpg",
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Lister les produits
```http
GET /api/produits
GET /api/produits/{id}/with-articles
```

#### Rechercher des produits
```http
GET /api/produits/search?keyword=iPhone
GET /api/produits/categorie/1
GET /api/produits/boutique/1
```

### Article APIs

#### Créer un article
```http
POST /api/articles
Content-Type: application/json

{
  "produitId": 1,
  "sku": "IP15-128-BLK",
  "prix": 999.99,
  "stockActuel": 25,
  "attributs": "{\"couleur\":\"noir\",\"capacite\":\"128GB\"}"
}
```

#### Modifier un article
```http
PUT /api/articles/{id}
Content-Type: application/json

{
  "produitId": 1,
  "sku": "IP15-128-BLK",
  "prix": 899.99,
  "stockActuel": 20,
  "attributs": "{\"couleur\":\"noir\",\"capacite\":\"128GB\"}"
}
```

#### Gérer le stock
```http
PUT /api/articles/{id}/stock/add?quantity=10&motif=Réapprovisionnement
PUT /api/articles/{id}/stock/remove?quantity=2&motif=Vente
```

#### Historique du stock
```http
GET /api/historique-stock/article/{id}
GET /api/historique-stock/produit/{id}
```

---

## Module Paiement

### Gestion des Moyens de Paiement

#### Créer un moyen de paiement
```http
POST /api/paiement/moyens
Content-Type: application/json

{
  "nom": "Wave",
  "photo": "https://example.com/wave-logo.png"
}
```

**Réponse:**
```json
{
  "success": true,
  "message": "Moyen de paiement créé avec succès",
  "data": {
    "id": 1,
    "nom": "Wave",
    "photo": "https://example.com/wave-logo.png",
    "createdAt": "2026-04-20T12:00:00"
  }
}
```

#### Lister tous les moyens de paiement
```http
GET /api/paiement/moyens
```

#### Récupérer un moyen de paiement par ID
```http
GET /api/paiement/moyens/{id}
```

#### Mettre à jour un moyen de paiement
```http
PUT /api/paiement/moyens/{id}
Content-Type: application/json

{
  "nom": "Orange Money",
  "photo": "https://example.com/orange-logo.png"
}
```

#### Supprimer un moyen de paiement
```http
DELETE /api/paiement/moyens/{id}
```

#### Rechercher des moyens de paiement
```http
GET /api/paiement/moyens/search?keyword=wave
```

### Gestion des Associations Utilisateur-Moyen de Paiement

#### Associer un moyen de paiement à un utilisateur
```http
POST /api/paiement/associations?userId=1&moyenPaiementId=2&actif=true
```

**Réponse:**
```json
{
  "success": true,
  "message": "Association créée avec succès",
  "data": {
    "id": 1,
    "userId": 1,
    "userEmail": "user@example.com",
    "userNom": "John Doe",
    "moyenPaiementId": 2,
    "moyenPaiementNom": "Wave",
    "moyenPaiementPhoto": "https://example.com/wave-logo.png",
    "actif": true,
    "createdAt": "2026-04-20T12:00:00"
  }
}
```

#### Lister les moyens de paiement d'un utilisateur
```http
GET /api/paiement/associations/user/{userId}
```

#### Lister les moyens de paiement actifs d'un utilisateur
```http
GET /api/paiement/associations/user/{userId}/actifs
```

#### Lister les utilisateurs ayant un moyen de paiement spécifique
```http
GET /api/paiement/associations/moyen/{moyenPaiementId}
```

#### Activer/Désactiver un moyen de paiement pour un utilisateur
```http
PUT /api/paiement/associations/{userId}/{moyenPaiementId}/activer
PUT /api/paiement/associations/{userId}/{moyenPaiementId}/desactiver
```

#### Supprimer une association
```http
DELETE /api/paiement/associations/{userId}/{moyenPaiementId}
```

#### Vérifier si un utilisateur a un moyen de paiement actif
```http
GET /api/paiement/associations/{userId}/{moyenPaiementId}/check
```

**Réponse:**
```json
{
  "success": true,
  "message": "Vérification terminée",
  "data": true
}
```

#### Compter les moyens de paiement actifs d'un utilisateur
```http
GET /api/paiement/associations/user/{userId}/count
```

**Réponse:**
```json
{
  "success": true,
  "message": "Nombre de moyens de paiement actifs",
  "data": 3
}
```

### Structure des Données

#### MoyenPaiement
```json
{
  "id": 1,
  "nom": "Wave",
  "photo": "https://example.com/wave-logo.png",
  "createdAt": "2026-04-20T12:00:00"
}
```

#### UserMoyenPaiement
```json
{
  "id": 1,
  "userId": 1,
  "userEmail": "user@example.com",
  "userNom": "John Doe",
  "moyenPaiementId": 2,
  "moyenPaiementNom": "Wave",
  "moyenPaiementPhoto": "https://example.com/wave-logo.png",
  "actif": true,
  "createdAt": "2026-04-20T12:00:00"
}
```

### Contraintes et Validation

#### MoyenPaiement
- `nom`: obligatoire, max 100 caractères, unique
- `photo`: optionnel, max 255 caractères

#### UserMoyenPaiement
- `userId` et `moyenPaiementId`: obligatoires
- `actif`: défaut `true`
- Contrainte unique sur `(userId, moyenPaiementId)`

---

## Codes d'Erreur Communs

- **400 Bad Request** - Données invalides ou manquantes
- **404 Not Found** - Ressource non trouvée
- **409 Conflict** - Conflit de données (ex: SKU déjà existant)
- **500 Internal Server Error** - Erreur serveur

---

## Notes importantes

1. **Authentication**: Actuellement tous les endpoints sont publics (`permitAll()`)
2. **Database**: PostgreSQL configuré pour le développement
3. **Enums**: Utilisent `VARCHAR` pour compatibilité PostgreSQL
4. **JSON**: Les attributs des articles sont stockés en JSON
5. **Timestamps**: Gérés automatiquement avec `@CreationTimestamp`
6. **Contraintes d'unicité**: 
   - `Compte.email` doit être unique
   - `Compte.telephone` doit être unique
   - `Article.sku` doit être unique

---

*Dernière mise à jour: 20 Avril 2026*
