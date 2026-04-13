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

#### Valider une boutique
```http
PUT /api/boutiques/1/validate
```

#### Rechercher des boutiques
```http
GET /api/boutiques/search?keyword=vêtements
GET /api/boutiques/statut/VALIDE
GET /api/boutiques/vendeur/1
```

#### Mettre à jour la note
```http
PUT /api/boutiques/1/note?note=4.5
```

### Document APIs

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
  "description": "Catégorie de vêtements pour hommes et femmes",
  "image": "https://example.com/vetements.jpg"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nom": "Vêtements",
  "description": "Catégorie de vêtements pour hommes et femmes",
  "image": "https://example.com/vetements.jpg",
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

### Sous-Catégorie APIs

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

---

*Dernière mise à jour: 13 Avril 2026*
