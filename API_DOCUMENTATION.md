# Tissenza Backend API Documentation

## Table des Matières
- [Configuration](#configuration)
- [Gestion des Utilisateurs](#gestion-des-utilisateurs)
- [Gestion des Boutiques](#gestion-des-boutiques)
- [Gestion des Catégories](#gestion-des-catégories)
- [Gestion des Produits](#gestion-des-produits)
- [Gestion des Articles](#gestion-des-articles)
- [Gestion des Lots](#gestion-des-lots)
- [Gestion des Stocks](#gestion-des-stocks)
- [Gestion des Commandes](#gestion-des-commandes)
- [Workflows Complets](#workflows-complets)
- [Codes d'Erreur](#codes-derreur)
- [Notes Techniques](#notes-techniques)

---

## Configuration

**Base URL:** `http://localhost:8080`
**Swagger UI:** `http://localhost:8080/swagger-ui.html`
**Content-Type:** `application/json`

---

## Gestion des Utilisateurs

### Personnes

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

#### Lister et rechercher des personnes
```http
GET /api/personnes
GET /api/personnes/search/nom?nom=Doe
GET /api/personnes/count
```

### Comptes Utilisateurs

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

#### Gérer un compte
```http
PUT /api/comptes/1/activate
PUT /api/comptes/1/deactivate
PUT /api/comptes/1/verify
```

### Documents

#### Créer et valider un document
```http
POST /api/documents
Content-Type: application/json

{
  "personneId": 1,
  "type": "CARTE_IDENTITE",
  "url": "https://example.com/carte-id.pdf"
}
```

**Types de documents:**
- `CARTE_IDENTITE`
- `NINEA`
- `PASSPORT`
- `RCCM`

#### Gérer les documents
```http
PUT /api/documents/1/validate
GET /api/documents/personne/1
GET /api/documents/type/CARTE_IDENTITE
GET /api/documents/validated/true
```

---

## Gestion des Boutiques

### Boutiques

#### Créer une boutique
```http
POST /api/boutiques
Content-Type: application/json

{
  "vendeurId": 1,
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
  "vendeurId": 1,
  "nom": "Ma Boutique",
  "description": "Boutique de vêtements",
  "addresse": "Dakar, Plateau",
  "logo": "https://example.com/logo.png",
  "statut": "EN_ATTENTE",
  "createdAt": "2026-04-13T01:50:00"
}
```

#### Gérer une boutique
```http
PUT /api/boutiques/1/validate
PUT /api/boutiques/1/note?note=4.5
```

#### Rechercher des boutiques
```http
GET /api/boutiques/search?keyword=vêtements
GET /api/boutiques/statut/VALIDE
GET /api/boutiques/vendeur/1
```

---

## Gestion des Catégories

### Catégories

#### Créer une catégorie
```http
POST /api/categories
Content-Type: application/json

{
  "nom": "Vêtements",
  "description": "Catégorie de vêtements pour hommes et femmes",
  "image": "https://example.com/vetements.jpg"
}
```

#### Lister les catégories
```http
GET /api/categories/with-sous-categories
GET /api/categories/search?keyword=vêtements
GET /api/categories/nom/Vêtements
```

### Sous-Catégories

#### Créer une sous-catégorie
```http
POST /api/sous-categories
Content-Type: application/json

{
  "categorieId": 1,
  "nom": "Chemises",
  "description": "Chemises pour hommes"
}
```

#### Lister les sous-catégories
```http
GET /api/sous-categories/categorie/1
GET /api/sous-categories/categorie/1/with-produits
```

---

## Gestion des Produits

### Produits

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

#### Gérer un produit
```http
PUT /api/produits/1/activate
PUT /api/produits/1/deactivate
```

#### Rechercher des produits
```http
GET /api/produits/1/with-articles
GET /api/produits/boutique/1
GET /api/produits/sous-categorie/1
GET /api/produits/statut/ACTIF
GET /api/produits/search?keyword=chemise
```

---

## Gestion des Articles

### Articles

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

#### Gérer le stock d'un article
```http
PUT /api/articles/1/stock?newStock=45
PUT /api/articles/1/stock/add?quantity=10&motif=Réapprovisionnement
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

#### Articles par niveau de stock
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

## Gestion des Lots

### Lots

#### Créer un lot
```http
POST /api/lots
Content-Type: application/json

{
  "articleId": 1,
  "numeroLot": "LOT-2024-001",
  "quantiteInitiale": 100,
  "dateFabrication": "2024-01-15",
  "dateExpiration": "2025-01-15",
  "prixAchat": 15.50,
  "fournisseur": "Fournisseur A",
  "emplacement": "Entrepôt A"
}
```

#### Gérer un lot
```http
PUT /api/lots/1/stock/add?quantity=10&motif=Réapprovisionnement
PUT /api/lots/1/stock/remove?quantity=5&motif=Vente
DELETE /api/lots/1
```

#### Rechercher des lots
```http
GET /api/lots/article/1
GET /api/lots/numero/LOT-2024-001
GET /api/lots/statut/ACTIF
GET /api/lots/fournisseur/Fournisseur%20A
```

#### Lots par péremption
```http
GET /api/lots/perimes
GET /api/lots/proches-peremption
GET /api/lots/peremption-before/2024-12-31
```

#### Statistiques sur les lots
```http
GET /api/lots/statistics
GET /api/lots/count/statut/ACTIF
```

---

## Gestion des Stocks

### Historique des Stocks

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

**Types de mouvements:**
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

#### Derniers mouvements et statistiques
```http
GET /api/historique-stock/latest
GET /api/historique-stock/latest/article/1
GET /api/historique-stock/total/entree/1
GET /api/historique-stock/total/sortie/1
GET /api/historique-stock/stats/type
```

---

## Gestion des Commandes

### Commandes

#### Créer une commande
```http
POST /api/commandes
Content-Type: application/json

{
  "clientId": 1,
  "lignesCommande": [
    {
      "articleId": 1,
      "quantite": 2,
      "prixUnitaire": 29.99,
      "remise": 5.00,
      "prixAchatUnitaire": 15.00
    },
    {
      "articleId": 2,
      "quantite": 1,
      "prixUnitaire": 49.99
    }
  ],
  "adresseLivraison": "123 Rue de la Paix, Dakar, Sénégal",
  "notes": "Livraison après 18h",
  "dateLivraisonSouhaitee": "2026-04-20T18:00:00"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "clientId": 1,
  "clientEmail": "client@example.com",
  "clientNom": "Doe John",
  "numeroCommande": "CMD-20260415163030-ABC12345",
  "montantTotal": 104.97,
  "statut": "EN_ATTENTE",
  "adresseLivraison": "123 Rue de la Paix, Dakar, Sénégal",
  "notes": "Livraison après 18h",
  "dateLivraisonSouhaitee": "2026-04-20T18:00:00",
  "dateLivraisonReelle": null,
  "createdAt": "2026-04-15T16:30:30",
  "updatedAt": null,
  "lignesCommande": [
    {
      "id": 1,
      "articleId": 1,
      "articleNom": "Chemise en coton",
      "articleSku": "CHM-COT-001",
      "quantite": 2,
      "prixUnitaire": 29.99,
      "remise": 5.00,
      "montantTotal": 54.98,
      "prixAchatUnitaire": 15.00,
      "marge": 24.98,
      "tauxMarge": 83.27
    }
  ]
}
```

#### Rechercher des commandes
```http
GET /api/commandes/1
GET /api/commandes/numero/CMD-20260415163030-ABC12345
GET /api/commandes/client/1
GET /api/commandes/statut/EN_ATTENTE
GET /api/commandes/en-retard
```

#### Gérer le cycle de vie d'une commande
```http
# Valider une commande
PUT /api/commandes/1/valider
Content-Type: application/json
{
  "modifieParId": 2,
  "motif": "Commande validée - Stock disponible"
}

# Mettre en préparation
PUT /api/commandes/1/preparation
Content-Type: application/json
{
  "modifieParId": 2,
  "motif": "Début de la préparation"
}

# Marquer comme prête
PUT /api/commandes/1/prete
Content-Type: application/json
{
  "modifieParId": 2,
  "motif": "Commande prête pour livraison"
}

# Mettre en livraison
PUT /api/commandes/1/livraison
Content-Type: application/json
{
  "modifieParId": 3,
  "motif": "Remise au livreur"
}

# Livrer la commande
PUT /api/commandes/1/livrer
Content-Type: application/json
{
  "modifieParId": 3,
  "motif": "Livraison effectuée avec succès"
}

# Annuler une commande
PUT /api/commandes/1/annuler
Content-Type: application/json
{
  "modifieParId": 2,
  "motif": "Annulation demandée par le client"
}
```

#### Statuts de commande
- `EN_ATTENTE` - En attente de validation
- `VALIDEE` - Validée par le vendeur
- `EN_PREPARATION` - En cours de préparation
- `PRETE` - Prête pour livraison
- `EN_LIVRAISON` - En cours de livraison
- `LIVREE` - Livrée avec succès
- `ANNULEE` - Annulée

#### Statistiques sur les commandes
```http
GET /api/commandes/statistiques
```

**Response:**
```json
{
  "totalCommandes": 150,
  "commandesEnAttente": 12,
  "commandesValidees": 8,
  "commandesEnPreparation": 5,
  "commandesLivrees": 120,
  "commandesAnnulees": 5,
  "chiffreAffaires": 15420.75
}
```

### Historique des Commandes

#### Structure de l'historique
Chaque changement de statut est automatiquement enregistré avec :
- Ancien et nouveau statut
- Utilisateur ayant effectué le changement
- Motif du changement
- Date et heure

#### Consultation de l'historique
L'historique peut être consulté via les endpoints dédiés ou directement dans les détails de la commande.

---

## Workflows Complets

### 1. Créer une boutique complète

```bash
# 1. Créer une personne
curl -X POST http://localhost:8080/api/personnes \
  -H "Content-Type: application/json" \
  -d '{"nom":"Seller","prenom":"John","email":"seller@example.com","telephone":"+221771234567","dateNaissance":"1985-05-15","adresse":"Dakar"}'

# 2. Créer son compte vendeur
curl -X POST http://localhost:8080/api/comptes \
  -H "Content-Type: application/json" \
  -d '{"personneId":1,"email":"seller@example.com","motDePasse":"password123","telephone":"+221771234567","role":"VENDEUR"}'

# 3. Créer une catégorie
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"nom":"Électronique","description":"Appareils électroniques"}'

# 4. Créer la boutique
curl -X POST http://localhost:8080/api/boutiques \
  -H "Content-Type: application/json" \
  -d '{"vendeurId":1,"nom":"Tech Store","description":"Boutique de tech","addresse":"Dakar"}'

# 5. Valider la boutique
curl -X PUT http://localhost:8080/api/boutiques/1/validate
```

### 2. Gérer le stock d'un article

```bash
# Ajouter du stock
curl -X PUT "http://localhost:8080/api/articles/1/stock/add?quantity=10&motif=Réapprovisionnement"

# Retirer du stock (vente)
curl -X PUT "http://localhost:8080/api/articles/1/stock/remove?quantity=2&motif=Vente client"

# Vérifier l'historique
curl -X GET "http://localhost:8080/api/historique-stock/article/1"
```

### 3. Gérer le cycle de vie complet d'une commande

```bash
# 1. Créer une commande
curl -X POST http://localhost:8080/api/commandes \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": 1,
    "lignesCommande": [
      {
        "articleId": 1,
        "quantite": 2,
        "prixUnitaire": 29.99,
        "prixAchatUnitaire": 15.00
      }
    ],
    "adresseLivraison": "123 Rue de la Paix, Dakar",
    "notes": "Livraison après 18h",
    "dateLivraisonSouhaitee": "2026-04-20T18:00:00"
  }'

# 2. Valider la commande (vendeur)
curl -X PUT http://localhost:8080/api/commandes/1/valider \
  -H "Content-Type: application/json" \
  -d '{"modifieParId": 2, "motif": "Stock disponible, commande validée"}'

# 3. Mettre en préparation
curl -X PUT http://localhost:8080/api/commandes/1/preparation \
  -H "Content-Type: application/json" \
  -d '{"modifieParId": 2, "motif": "Début préparation commande"}'

# 4. Marquer comme prête pour livraison
curl -X PUT http://localhost:8080/api/commandes/1/prete \
  -H "Content-Type: application/json" \
  -d '{"modifieParId": 2, "motif": "Commande prête"}'

# 5. Mettre en livraison
curl -X PUT http://localhost:8080/api/commandes/1/livraison \
  -H "Content-Type: application/json" \
  -d '{"modifieParId": 3, "motif": "Remise au livreur"}'

# 6. Livrer la commande
curl -X PUT http://localhost:8080/api/commandes/1/livrer \
  -H "Content-Type: application/json" \
  -d '{"modifieParId": 3, "motif": "Livraison réussie"}'

# 7. Consulter les statistiques
curl -X GET "http://localhost:8080/api/commandes/statistiques"
```

---

## Codes d'Erreur

| Code | Description | Exemples |
|------|-------------|----------|
| **400** | Bad Request | Données invalides, champs manquants |
| **404** | Not Found | Ressource non trouvée (ID inexistant) |
| **409** | Conflict | Conflit de données (email, SKU déjà utilisés) |
| **500** | Internal Server Error | Erreur serveur, base de données |

**Messages d'erreur courants:**
- `"Article non trouvé avec l'ID: X"`
- `"Email déjà utilisé: xxx"`
- `"Numéro de lot déjà utilisé: xxx"`

---

## Notes Techniques

### Configuration
- **Base de données**: PostgreSQL
- **Port**: 8080
- **Content-Type**: `application/json`

### Sécurité
- **Authentication**: Endpoints publics (`permitAll()`)
- **Passwords**: Hashés avec BCrypt
- **JWT**: Configuré mais non activé

### Base de données
- **Enums**: Utilisent `VARCHAR` pour PostgreSQL
- **JSON**: Attributs des articles stockés en JSON
- **Timestamps**: Gérés automatiquement

### Contraintes d'unicité
- `Compte.email` unique
- `Compte.telephone` unique  
- `Article.sku` unique
- `Lot.numeroLot` unique

---

*Documentation mise à jour le 15 Avril 2026*
