# 📦 **GUIDE D'EXPLOITATION DES APIs PRODUITS**

---

## 🎯 **Vue d'Ensemble**

Ce guide présente toutes les APIs disponibles pour la gestion des produits avec leurs fonctionnalités, paramètres, et exemples d'utilisation.

---

## 📋 **TABLE DES MATIÈRES**

1. [**APIs de Base**](#apis-de-base)
2. [**APIs de Recherche**](#apis-de-recherche)
3. [**APIs par Boutique**](#apis-par-boutique)
4. [**APIs par Sous-Catégorie**](#apis-par-sous-catégorie)
5. [**APIs Combinées**](#apis-combinées)
6. [**Structure des DTOs**](#structure-des-dtos)
7. [**Exemples d'Utilisation**](#exemples-dutilisation)

---

## 🔧 **APIS DE BASE**

### **1. Récupérer tous les produits**
```http
GET /api/produits
```

**Réponse :**
```json
[
  {
    "id": 1,
    "nom": "Chemise Blanche",
    "description": "Chemise élégante en coton",
    "prix": 25000,
    "stockActuel": 50,
    "image": "/uploads/produit/chemise-blanche.jpg",
    "statut": "DISPONIBLE",
    "sousCategorieId": 1,
    "boutiqueId": 1,
    "createdAt": "2026-05-11T16:28:00Z"
  }
]
```

### **2. Récupérer un produit par ID**
```http
GET /api/produits/{id}
```

**Paramètres :**
- `id` (Long) : ID du produit

**Réponse :**
```json
{
  "id": 1,
  "nom": "Chemise Blanche",
  "description": "Chemise élégante en coton",
  "prix": 25000,
  "stockActuel": 50,
  "image": "/uploads/produit/chemise-blanche.jpg",
  "statut": "DISPONIBLE",
  "sousCategorieId": 1,
  "boutiqueId": 1,
  "createdAt": "2026-05-11T16:28:00Z"
}
```

### **3. Créer un produit**
```http
POST /api/produits
Authorization: Bearer {token}
Content-Type: application/json
```

**Corps de la requête :**
```json
{
  "nom": "Nouveau Produit",
  "description": "Description du produit",
  "prix": 35000,
  "stockActuel": 100,
  "image": "/uploads/produit/nouveau-produit.jpg",
  "sousCategorieId": 2,
  "boutiqueId": 1
}
```

**Réponse :**
```json
{
  "id": 15,
  "nom": "Nouveau Produit",
  "description": "Description du produit",
  "prix": 35000,
  "stockActuel": 100,
  "image": "/uploads/produit/nouveau-produit.jpg",
  "statut": "DISPONIBLE",
  "sousCategorieId": 2,
  "boutiqueId": 1,
  "createdAt": "2026-05-11T16:28:00Z"
}
```

### **4. Mettre à jour un produit**
```http
PUT /api/produits/{id}
Authorization: Bearer {token}
Content-Type: application/json
```

**Corps de la requête :**
```json
{
  "nom": "Produit Modifié",
  "description": "Nouvelle description",
  "prix": 30000,
  "stockActuel": 75
}
```

### **5. Supprimer un produit**
```http
DELETE /api/produits/{id}
Authorization: Bearer {token}
```

---

## 🔍 **APIS DE RECHERCHE**

### **1. Rechercher par nom**
```http
GET /api/produits/search/nom?nom=chemise
```

**Paramètres :**
- `nom` (String) : Terme de recherche (insensible à la casse)

**Réponse :**
```json
[
  {
    "id": 1,
    "nom": "Chemise Blanche",
    "description": "Chemise élégante en coton",
    "prix": 25000,
    "stockActuel": 50
  }
]
```

### **2. Rechercher par mot-clé**
```http
GET /api/produits/search/keyword?keyword=chemise
```

**Paramètres :**
- `keyword` (String) : Mot-clé de recherche

### **3. Rechercher par plage de prix**
```http
GET /api/produits/search/prix?min=10000&max=50000
```

**Paramètres :**
- `min` (Double) : Prix minimum (optionnel)
- `max` (Double) : Prix maximum (optionnel)

---

## 🏪 **APIS PAR BOUTIQUE**

### **1. Produits par boutique**
```http
GET /api/produits/boutique/{boutiqueId}
```

**Paramètres :**
- `boutiqueId` (Long) : ID de la boutique

### **2. Produits par boutique avec détails**
```http
GET /api/produits/boutique/{boutiqueId}/with-details
```

### **3. Produits par boutique + nom**
```http
GET /api/produits/boutique/{boutiqueId}/search/nom?nom=chemise
```

---

## 📂 **APIS PAR SOUS-CATÉGORIE**

### **1. Produits par sous-catégorie**
```http
GET /api/produits/sous-categorie/{sousCategorieId}
```

**Paramètres :**
- `sousCategorieId` (Long) : ID de la sous-catégorie

### **2. Produits par sous-catégorie avec détails**
```http
GET /api/produits/sous-categorie/{sousCategorieId}/with-details
```

### **3. Produits par sous-catégorie + nom**
```http
GET /api/produits/sous-categorie/{sousCategorieId}/search/nom?nom=chemise
```

---

## 🔗 **APIS COMBINÉES**

### **1. Recherche flexible (paramètres optionnels)**
```http
GET /api/produits/search/combined?boutiqueId=1&sousCategorieId=2&nom=chemise&min=10000&max=50000
```

**Paramètres (tous optionnels) :**
- `boutiqueId` (Long) : ID de la boutique
- `sousCategorieId` (Long) : ID de la sous-catégorie
- `nom` (String) : Nom du produit
- `min` (Double) : Prix minimum
- `max` (Double) : Prix maximum

**Exemples d'utilisation :**
```http
# Produits d'une boutique spécifique
GET /api/produits/search/combined?boutiqueId=1

# Produits d'une sous-catégorie spécifique
GET /api/produits/search/combined?sousCategorieId=2

# Produits par nom
GET /api/produits/search/combined?nom=chemise

# Combinaison boutique + sous-catégorie
GET /api/produits/search/combined?boutiqueId=1&sousCategorieId=2

# Recherche complète
GET /api/produits/search/combined?boutiqueId=1&sousCategorieId=2&nom=chemise&min=10000&max=50000
```

### **2. Recherche stricte (paramètres obligatoires)**
```http
GET /api/produits/search/boutique-souscategorie-nom?boutiqueId=1&sousCategorieId=2&nom=chemise
```

**Paramètres (tous obligatoires) :**
- `boutiqueId` (Long) : ID de la boutique
- `sousCategorieId` (Long) : ID de la sous-catégorie
- `nom` (String) : Nom du produit

---

## 📊 **STRUCTURE DES DTOS**

### **ProduitDTO**
```json
{
  "id": 1,
  "nom": "Chemise Blanche",
  "description": "Chemise élégante en coton",
  "prix": 25000,
  "stockActuel": 50,
  "image": "/uploads/produit/chemise-blanche.jpg",
  "statut": "DISPONIBLE",
  "sousCategorieId": 1,
  "boutiqueId": 1,
  "createdAt": "2026-05-11T16:28:00Z"
}
```

### **ProduitCreateDTO**
```json
{
  "nom": "Nouveau Produit",
  "description": "Description du produit",
  "prix": 35000,
  "stockActuel": 100,
  "image": "/uploads/produit/nouveau-produit.jpg",
  "sousCategorieId": 2,
  "boutiqueId": 1
}
```

### **ProduitUpdateDTO**
```json
{
  "nom": "Produit Modifié",
  "description": "Nouvelle description",
  "prix": 30000,
  "stockActuel": 75,
  "image": "/uploads/produit/modifie.jpg",
  "sousCategorieId": 3,
  "boutiqueId": 2
}
```

---

## 💡 **EXEMPLES D'UTILISATION**

### **1. Frontend React - Recherche de produits**
```javascript
// Recherche flexible
const searchProducts = async (filters) => {
  const params = new URLSearchParams();
  
  if (filters.boutiqueId) params.append('boutiqueId', filters.boutiqueId);
  if (filters.sousCategorieId) params.append('sousCategorieId', filters.sousCategorieId);
  if (filters.nom) params.append('nom', filters.nom);
  if (filters.min) params.append('min', filters.min);
  if (filters.max) params.append('max', filters.max);
  
  const response = await fetch(`/api/produits/search/combined?${params}`, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  
  return response.json();
};

// Utilisation
const products = await searchProducts({
  boutiqueId: 1,
  sousCategorieId: 2,
  nom: 'chemise',
  min: 10000,
  max: 50000
});
```

### **2. Frontend React - Création de produit**
```javascript
const createProduct = async (productData) => {
  const response = await fetch('/api/produits', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      nom: productData.nom,
      description: productData.description,
      prix: productData.prix,
      stockActuel: productData.stockActuel,
      image: productData.image,
      sousCategorieId: productData.sousCategorieId,
      boutiqueId: productData.boutiqueId
    })
  });
  
  return response.json();
};
```

### **3. Frontend React - Mise à jour de produit**
```javascript
const updateProduct = async (id, productData) => {
  const response = await fetch(`/api/produits/${id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(productData)
  });
  
  return response.json();
};
```

---

## 🔐 **AUTHENTIFICATION**

### **Rôles requis :**
- **Lecture** : `isAuthenticated()` - Utilisateurs connectés
- **Création** : `hasRole('VENDOR')` - Vendeurs et Admins
- **Modification** : `hasRole('VENDOR')` - Vendeurs et Admins
- **Suppression** : `hasRole('ADMIN')` - Admins uniquement

### **Format du token :**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 📈 **PERFORMANCE ET PAGINATION**

### **Pagination (si disponible) :**
```http
GET /api/produits?page=0&size=20&sort=nom,asc
```

### **Filtrage avancé :**
```http
GET /api/produits/search/combined?boutiqueId=1&sousCategorieId=2&nom=chemise&min=10000&max=50000&statut=DISPONIBLE
```

---

## 🚀 **BONNES PRATIQUES**

### **1. Gestion des erreurs**
```javascript
try {
  const products = await searchProducts(filters);
  console.log('Produits trouvés:', products);
} catch (error) {
  console.error('Erreur lors de la recherche:', error);
  // Afficher un message à l'utilisateur
}
```

### **2. Validation des données**
```javascript
const validateProductData = (data) => {
  if (!data.nom || data.nom.trim() === '') {
    throw new Error('Le nom du produit est requis');
  }
  if (!data.prix || data.prix <= 0) {
    throw new Error('Le prix doit être supérieur à 0');
  }
  if (!data.stockActuel || data.stockActuel < 0) {
    throw new Error('Le stock doit être positif');
  }
};
```

### **3. Optimisation des requêtes**
```javascript
// Utiliser la recherche combinée pour éviter multiples appels
const optimizedSearch = async (filters) => {
  // ✅ Un seul appel API
  const products = await searchProducts(filters);
  
  // ❌ Éviter plusieurs appels
  // const boutiqueProducts = await getProductsByBoutique(filters.boutiqueId);
  // const categoryProducts = await getProductsByCategory(filters.sousCategorieId);
};
```

---

## 📋 **RÉCAPITULATIF DES ENDPOINTS**

| Méthode | Endpoint | Description | Authentification |
|---------|-----------|-------------|------------------|
| GET | `/api/produits` | Tous les produits | Connecté |
| GET | `/api/produits/{id}` | Produit par ID | Connecté |
| POST | `/api/produits` | Créer produit | VENDOR |
| PUT | `/api/produits/{id}` | Mettre à jour | VENDOR |
| DELETE | `/api/produits/{id}` | Supprimer produit | ADMIN |
| GET | `/api/produits/search/nom` | Recherche par nom | Connecté |
| GET | `/api/produits/search/keyword` | Recherche par mot-clé | Connecté |
| GET | `/api/produits/search/prix` | Recherche par prix | Connecté |
| GET | `/api/produits/boutique/{id}` | Produits par boutique | Connecté |
| GET | `/api/produits/sous-categorie/{id}` | Produits par sous-catégorie | Connecté |
| GET | `/api/produits/search/combined` | Recherche flexible | Connecté |
| GET | `/api/produits/search/boutique-souscategorie-nom` | Recherche stricte | Connecté |

---

## 🏁 **CONCLUSION**

Ce guide couvre l'ensemble des APIs disponibles pour la gestion des produits dans l'application Tissenza. Les APIs sont conçues pour être flexibles, sécurisées et performantes, offrant des fonctionnalités de recherche avancées et une gestion complète du cycle de vie des produits.

**Points clés :**
- ✅ **Recherche flexible** avec paramètres optionnels
- ✅ **Authentification sécurisée** par rôles
- ✅ **DTOs structurés** pour la cohérence
- ✅ **Gestion d'erreurs** appropriée
- ✅ **Performance optimisée** avec pagination

**Pour toute question ou problème technique, consulter la documentation API ou contacter l'équipe de développement.** 🚀
