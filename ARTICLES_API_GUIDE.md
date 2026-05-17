# 📦 **GUIDE D'EXPLOITATION DES APIs ARTICLES**

---

## 🎯 **Vue d'Ensemble**

Ce guide présente toutes les APIs disponibles pour la gestion des articles avec leurs fonctionnalités, paramètres, et exemples d'utilisation.

---

## 📋 **TABLE DES MATIÈRES**

1. [**APIs de Base**](#apis-de-base)
2. [**APIs de Recherche**](#apis-de-recherche)
3. [**APIs par Produit**](#apis-par-produit)
4. [**APIs par Boutique**](#apis-par-boutique)
5. [**APIs Combinées**](#apis-combinées)
6. [**Structure des DTOs**](#structure-des-dtos)
7. [**Gestion des Attributs JSON**](#gestion-des-attributs-json)
8. [**Exemples d'Utilisation**](#exemples-dutilisation)

---

## 🔧 **APIS DE BASE**

### **1. Récupérer tous les articles**
```http
GET /api/articles
```

**Réponse :**
```json
[
  {
    "id": 1,
    "sku": "ART-001",
    "prix": 25000,
    "stockActuel": 50,
    "image": "/uploads/articles/chemise-blanche.jpg",
    "produitId": 1,
    "attributs": {
      "taille": "L",
      "couleur": "Blanc",
      "matiere": "Coton",
      "made_in_senegal": true
    },
    "createdAt": "2026-05-11T16:59:00Z"
  }
]
```

### **2. Récupérer un article par ID**
```http
GET /api/articles/{id}
```

**Paramètres :**
- `id` (Long) : ID de l'article

**Réponse :**
```json
{
  "id": 1,
  "sku": "ART-001",
  "prix": 25000,
  "stockActuel": 50,
  "image": "/uploads/articles/chemise-blanche.jpg",
  "produitId": 1,
  "attributs": {
    "taille": "L",
    "couleur": "Blanc",
    "matiere": "Coton",
    "made_in_senegal": true
  },
  "createdAt": "2026-05-11T16:59:00Z"
}
```

### **3. Créer un article**
```http
POST /api/articles
Authorization: Bearer {token}
Content-Type: application/json
```

**Corps de la requête :**
```json
{
  "sku": "ART-002",
  "prix": 30000,
  "stockActuel": 100,
  "image": "/uploads/articles/nouvel-article.jpg",
  "produit_id": 1,
  "attributs": {
    "taille": "XL",
    "couleur": "Noir",
    "matiere": "Coton",
    "made_in_senegal": true,
    "special": "Edition limitée"
  }
}
```

**Réponse :**
```json
{
  "id": 2,
  "sku": "ART-002",
  "prix": 30000,
  "stockActuel": 100,
  "image": "/uploads/articles/nouvel-article.jpg",
  "produitId": 1,
  "attributs": {
    "taille": "XL",
    "couleur": "Noir",
    "matiere": "Coton",
    "made_in_senegal": true,
    "special": "Edition limitée"
  },
  "createdAt": "2026-05-11T16:59:00Z"
}
```

### **4. Mettre à jour un article**
```http
PUT /api/articles/{id}
Authorization: Bearer {token}
Content-Type: application/json
```

**Corps de la requête :**
```json
{
  "sku": "ART-001-MODIFIED",
  "prix": 28000,
  "stock_actuel": 75,
  "attributs": {
    "taille": "L",
    "couleur": "Blanc",
    "matiere": "Coton bio",
    "made_in_senegal": true,
    "promotion": "20%"
  }
}
```

### **5. Supprimer un article**
```http
DELETE /api/articles/{id}
Authorization: Bearer {token}
```

---

## 🔍 **APIS DE RECHERCHE**

### **1. Rechercher par SKU**
```http
GET /api/articles/search/sku?sku=ART-001
```

**Paramètres :**
- `sku` (String) : SKU de l'article

**Réponse :**
```json
{
  "id": 1,
  "sku": "ART-001",
  "prix": 25000,
  "stockActuel": 50,
  "attributs": {
    "taille": "L",
    "couleur": "Blanc"
  }
}
```

### **2. Rechercher par mot-clé**
```http
GET /api/articles/search/keyword?keyword=chemise
```

**Paramètres :**
- `keyword` (String) : Mot-clé de recherche

### **3. Rechercher par plage de prix**
```http
GET /api/articles/search/prix?min=10000&max=50000
```

**Paramètres :**
- `min` (Double) : Prix minimum (optionnel)
- `max` (Double) : Prix maximum (optionnel)

### **4. Rechercher par attributs**
```http
GET /api/articles/search/attributs?taille=L&couleur=Blanc
```

**Paramètres :**
- `taille` (String) : Taille de l'article
- `couleur` (String) : Couleur de l'article
- `matiere` (String) : Matière de l'article

---

## 📦 **APIS PAR PRODUIT**

### **1. Articles par produit**
```http
GET /api/articles/produit/{produitId}
```

**Paramètres :**
- `produitId` (Long) : ID du produit

**Réponse :**
```json
[
  {
    "id": 1,
    "sku": "ART-001",
    "prix": 25000,
    "stockActuel": 50,
    "produitId": 1,
    "attributs": {
      "taille": "L",
      "couleur": "Blanc"
    }
  },
  {
    "id": 2,
    "sku": "ART-002",
    "prix": 30000,
    "stockActuel": 100,
    "produitId": 1,
    "attributs": {
      "taille": "XL",
      "couleur": "Noir"
    }
  }
]
```

### **2. Articles par produit avec détails**
```http
GET /api/articles/produit/{produitId}/with-details
```

### **3. Articles par produit + taille**
```http
GET /api/articles/produit/{produitId}/taille/{taille}
```

---

## 🏪 **APIS PAR BOUTIQUE**

### **1. Articles par boutique**
```http
GET /api/articles/boutique/{boutiqueId}
```

**Paramètres :**
- `boutiqueId` (Long) : ID de la boutique

### **2. Articles par boutique avec détails**
```http
GET /api/articles/boutique/{boutiqueId}/with-details
```

### **3. Articles par boutique + prix**
```http
GET /api/articles/boutique/{boutiqueId}/prix?min=10000&max=50000
```

---

## 🔗 **APIS COMBINÉES**

### **1. Recherche flexible (paramètres optionnels)**
```http
GET /api/articles/search/combined?boutiqueId=1&produitId=2&sku=ART&min=10000&max=50000&taille=L&couleur=Blanc
```

**Paramètres (tous optionnels) :**
- `boutiqueId` (Long) : ID de la boutique
- `produitId` (Long) : ID du produit
- `sku` (String) : SKU de l'article
- `min` (Double) : Prix minimum
- `max` (Double) : Prix maximum
- `taille` (String) : Taille de l'article
- `couleur` (String) : Couleur de l'article
- `matiere` (String) : Matière de l'article

**Exemples d'utilisation :**
```http
# Articles d'une boutique spécifique
GET /api/articles/search/combined?boutiqueId=1

# Articles d'un produit spécifique
GET /api/articles/search/combined?produitId=2

# Articles par taille
GET /api/articles/search/combined?taille=L

# Articles par couleur
GET /api/articles/search/combined?couleur=Blanc

# Combinaison boutique + produit + taille
GET /api/articles/search/combined?boutiqueId=1&produitId=2&taille=L

# Recherche complète
GET /api/articles/search/combined?boutiqueId=1&produitId=2&sku=ART&min=10000&max=50000&taille=L&couleur=Blanc&matiere=Coton
```

### **2. Recherche stricte (paramètres obligatoires)**
```http
GET /api/articles/search/boutique-produit-taille?boutiqueId=1&produitId=2&taille=L
```

**Paramètres (tous obligatoires) :**
- `boutiqueId` (Long) : ID de la boutique
- `produitId` (Long) : ID du produit
- `taille` (String) : Taille de l'article

---

## 📊 **STRUCTURE DES DTOS**

### **ArticleDTO**
```json
{
  "id": 1,
  "sku": "ART-001",
  "prix": 25000,
  "stockActuel": 50,
  "image": "/uploads/articles/chemise-blanche.jpg",
  "produitId": 1,
  "attributs": {
    "taille": "L",
    "couleur": "Blanc",
    "matiere": "Coton",
    "made_in_senegal": true
  },
  "createdAt": "2026-05-11T16:59:00Z"
}
```

### **ArticleCreateDTO**
```json
{
  "sku": "ART-002",
  "prix": 30000,
  "stockActuel": 100,
  "image": "/uploads/articles/nouvel-article.jpg",
  "produit_id": 1,
  "attributs": {
    "taille": "XL",
    "couleur": "Noir",
    "matiere": "Coton",
    "made_in_senegal": true,
    "special": "Edition limitée"
  }
}
```

### **ArticleUpdateDTO**
```json
{
  "sku": "ART-001-MODIFIED",
  "prix": 28000,
  "stock_actuel": 75,
  "image": "/uploads/articles/modifie.jpg",
  "attributs": {
    "taille": "L",
    "couleur": "Blanc",
    "matiere": "Coton bio",
    "made_in_senegal": true,
    "promotion": "20%"
  }
}
```

---

## 🎯 **GESTION DES ATTRIBUTS JSON**

### **Structure des attributs**
Les attributs sont stockés en format JSON et peuvent contenir n'importe quelles propriétés personnalisées :

```json
{
  "attributs": {
    "taille": "L",
    "couleur": "Blanc",
    "matiere": "Coton",
    "made_in_senegal": true,
    "poids": "200g",
    "origine": "Dakar",
    "promotion": "20%",
    "special": "Edition limitée",
    "composition": "100% coton",
    "entretien": "Lavage à 30°C",
    "certification": "Bio"
  }
}
```

### **Types de données supportés**
- **String** : taille, couleur, matière, etc.
- **Number** : poids, prix_promotion
- **Boolean** : made_in_senegal, disponible
- **Array** : couleurs_disponibles, tailles_disponibles
- **Object** : dimensions, caractéristiques

### **Exemples par catégorie**

#### **Vêtements**
```json
{
  "attributs": {
    "taille": "L",
    "couleur": "Blanc",
    "matiere": "Coton",
    "made_in_senegal": true,
    "composition": "95% coton, 5% élasthanne",
    "entretien": "Lavage à 30°C",
    "origine": "Dakar"
  }
}
```

#### **Électronique**
```json
{
  "attributs": {
    "couleur": "Noir",
    "capacite": "256GB",
    "marque": "Samsung",
    "garantie": "2 ans",
    "poids": "180g",
    "dimensions": {
      "longueur": "15cm",
      "largeur": "7cm",
      "epaisseur": "0.8cm"
    }
  }
}
```

#### **Accessoires**
```json
{
  "attributs": {
    "style": "Classique",
    "matiere": "Cuir véritable",
    "couleur": "Marron",
    "waterproof": true,
    "dimensions": {
      "longueur": "20cm",
      "largeur": "3cm"
    }
  }
}
```

---

## 💡 **EXEMPLES D'UTILISATION**

### **1. Frontend React - Création d'article**
```javascript
const createArticle = async (articleData) => {
  const response = await fetch('/api/articles', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      sku: articleData.sku,
      prix: articleData.prix,
      stockActuel: articleData.stockActuel,
      image: articleData.image,
      produit_id: articleData.produitId,
      attributs: {
        taille: articleData.taille,
        couleur: articleData.couleur,
        matiere: articleData.matiere,
        made_in_senegal: articleData.madeInSenegal
      }
    })
  });
  
  return response.json();
};

// Utilisation
const newArticle = await createArticle({
  sku: 'ART-003',
  prix: 35000,
  stockActuel: 75,
  image: '/uploads/articles/chemise-rouge.jpg',
  produitId: 1,
  taille: 'M',
  couleur: 'Rouge',
  matiere: 'Coton',
  madeInSenegal: true
});
```

### **2. Frontend React - Recherche combinée**
```javascript
const searchArticles = async (filters) => {
  const params = new URLSearchParams();
  
  if (filters.boutiqueId) params.append('boutiqueId', filters.boutiqueId);
  if (filters.produitId) params.append('produitId', filters.produitId);
  if (filters.sku) params.append('sku', filters.sku);
  if (filters.min) params.append('min', filters.min);
  if (filters.max) params.append('max', filters.max);
  if (filters.taille) params.append('taille', filters.taille);
  if (filters.couleur) params.append('couleur', filters.couleur);
  if (filters.matiere) params.append('matiere', filters.matiere);
  
  const response = await fetch(`/api/articles/search/combined?${params}`, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  
  return response.json();
};

// Utilisation
const articles = await searchArticles({
  boutiqueId: 1,
  produitId: 2,
  taille: 'L',
  couleur: 'Blanc',
  min: 10000,
  max: 50000
});
```

### **3. Frontend React - Mise à jour d'article**
```javascript
const updateArticle = async (id, articleData) => {
  const response = await fetch(`/api/articles/${id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      sku: articleData.sku,
      prix: articleData.prix,
      stock_actuel: articleData.stockActuel,
      attributs: {
        taille: articleData.taille,
        couleur: articleData.couleur,
        matiere: articleData.matiere,
        promotion: articleData.promotion
      }
    })
  });
  
  return response.json();
};
```

### **4. Frontend React - Gestion des attributs**
```javascript
const buildAttributes = (category, formData) => {
  const baseAttributes = {
    made_in_senegal: formData.madeInSenegal || false
  };

  switch (category) {
    case 'Vêtements':
      return {
        ...baseAttributes,
        taille: formData.taille,
        couleur: formData.couleur,
        matiere: formData.matiere,
        composition: formData.composition,
        entretien: formData.entretien
      };
      
    case 'Électronique':
      return {
        ...baseAttributes,
        couleur: formData.couleur,
        capacite: formData.capacite,
        marque: formData.marque,
        garantie: formData.garantie,
        poids: formData.poids
      };
      
    case 'Accessoires':
      return {
        ...baseAttributes,
        style: formData.style,
        matiere: formData.matiere,
        couleur: formData.couleur,
        waterproof: formData.waterproof
      };
      
    default:
      return baseAttributes;
  }
};

// Utilisation
const attributes = buildAttributes('Vêtements', {
  taille: 'L',
  couleur: 'Blanc',
  matiere: 'Coton',
  composition: '95% coton, 5% élasthanne',
  entretien: 'Lavage à 30°C',
  madeInSenegal: true
});
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
GET /api/articles?page=0&size=20&sort=createdAt,desc
```

### **Filtrage avancé :**
```http
GET /api/articles/search/combined?boutiqueId=1&produitId=2&taille=L&couleur=Blanc&min=10000&max=50000
```

---

## 🚀 **BONNES PRATIQUES**

### **1. Validation des attributs**
```javascript
const validateAttributes = (category, attributes) => {
  const errors = [];
  
  switch (category) {
    case 'Vêtements':
      if (!attributes.taille) errors.push('La taille est requise');
      if (!attributes.couleur) errors.push('La couleur est requise');
      if (!attributes.matiere) errors.push('La matière est requise');
      break;
      
    case 'Électronique':
      if (!attributes.couleur) errors.push('La couleur est requise');
      if (!attributes.capacite) errors.push('La capacité est requise');
      break;
  }
  
  return errors;
};
```

### **2. Gestion des erreurs**
```javascript
try {
  const article = await createArticle(articleData);
  console.log('Article créé:', article);
} catch (error) {
  console.error('Erreur lors de la création:', error);
  // Afficher un message à l'utilisateur
}
```

### **3. Optimisation des requêtes**
```javascript
// Utiliser la recherche combinée pour éviter multiples appels
const optimizedSearch = async (filters) => {
  // ✅ Un seul appel API
  const articles = await searchArticles(filters);
  
  // ❌ Éviter plusieurs appels
  // const boutiqueArticles = await getArticlesByBoutique(filters.boutiqueId);
  // const productArticles = await getArticlesByProduct(filters.produitId);
};
```

---

## 📋 **RÉCAPITULATIF DES ENDPOINTS**

| Méthode | Endpoint | Description | Authentification |
|---------|-----------|-------------|------------------|
| GET | `/api/articles` | Tous les articles | Connecté |
| GET | `/api/articles/{id}` | Article par ID | Connecté |
| POST | `/api/articles` | Créer article | VENDOR |
| PUT | `/api/articles/{id}` | Mettre à jour | VENDOR |
| DELETE | `/api/articles/{id}` | Supprimer article | ADMIN |
| GET | `/api/articles/search/sku` | Recherche par SKU | Connecté |
| GET | `/api/articles/search/keyword` | Recherche par mot-clé | Connecté |
| GET | `/api/articles/search/prix` | Recherche par prix | Connecté |
| GET | `/api/articles/search/attributs` | Recherche par attributs | Connecté |
| GET | `/api/articles/produit/{id}` | Articles par produit | Connecté |
| GET | `/api/articles/boutique/{id}` | Articles par boutique | Connecté |
| GET | `/api/articles/search/combined` | Recherche flexible | Connecté |
| GET | `/api/articles/search/boutique-produit-taille` | Recherche stricte | Connecté |

---

## 🏁 **CONCLUSION**

Ce guide couvre l'ensemble des APIs disponibles pour la gestion des articles dans l'application Tissenza. Les APIs sont conçues pour être flexibles, sécurisées et performantes, offrant des fonctionnalités de recherche avancées et une gestion complète des attributs JSON personnalisés.

**Points clés :**
- ✅ **Recherche flexible** avec paramètres optionnels
- ✅ **Attributs JSON** personnalisables par catégorie
- ✅ **Authentification sécurisée** par rôles
- ✅ **DTOs structurés** pour la cohérence
- ✅ **Gestion d'erreurs** appropriée
- ✅ **Performance optimisée** avec pagination

**Pour toute question ou problème technique, consulter la documentation API ou contacter l'équipe de développement.** 🚀
