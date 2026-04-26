-- ========================================
-- INSERTION DE 10 CATÉGORIES ET SOUS-CATÉGORIES
-- ========================================

-- Insertion des catégories principales
INSERT INTO categories (nom, description, created_at) VALUES
('Vêtements Hommes', 'Collection complète de vêtements pour hommes', NOW()),
('Vêtements Femmes', 'Mode féminine tendance et élégante', NOW()),
('Vêtements Enfants', 'Habillages confortables pour enfants', NOW()),
('Chaussures', 'Toutes sortes de chaussures pour toute la famille', NOW()),
('Accessoires', 'Accessoires de mode et utilitaires', NOW()),
('Électronique', 'Appareils électroniques et gadgets', NOW()),
('Maison & Décoration', 'Articles pour la maison et décoration', NOW()),
('Sports & Loisirs', 'Équipements sportifs et articles de loisirs', NOW()),
('Beauté & Santé', 'Produits de beauté et bien-être', NOW()),
('Bagagerie', 'Sacs, valises et accessoires de voyage', NOW());

-- Insertion des sous-catégories pour chaque catégorie

-- Sous-catégories pour Vêtements Hommes (ID: 1)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Chemises', 'Chemises formelles et décontractées pour hommes', 1, NOW()),
('T-shirts', 'T-shirts et débardeurs pour hommes', 1, NOW()),
('Pantalons', 'Pantalons, jeans et pantalons chinos', 1, NOW()),
('Vestes', 'Vestes et manteaux pour hommes', 1, NOW()),
('Costumes', 'Costumes complets pour occasions spéciales', 1, NOW());

-- Sous-catégories pour Vêtements Femmes (ID: 2)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Robes', 'Robes pour toutes occasions', 2, NOW()),
('Jupes', 'Jupes de différentes longueurs et styles', 2, NOW()),
('Tops', 'Tops, décolletés et chemisiers féminins', 2, NOW()),
('Pantalons Femmes', 'Pantalons et leggings pour femmes', 2, NOW()),
('Lingerie', 'Sous-vêtements et lingerie féminine', 2, NOW());

-- Sous-catégories pour Vêtements Enfants (ID: 3)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Vêtements Bébés', 'Habillages pour bébés (0-2 ans)', 3, NOW()),
('Vêtements Filles', 'Vêtements pour filles (3-12 ans)', 3, NOW()),
('Vêtements Garçons', 'Vêtements pour garçons (3-12 ans)', 3, NOW()),
('Ados', 'Mode pour adolescents (13-18 ans)', 3, NOW()),
('Pyjamas', 'Pyjamas et vêtements de nuit pour enfants', 3, NOW());

-- Sous-catégories pour Chaussures (ID: 4)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Chaussures Hommes', 'Chaussures formelles et sportives pour hommes', 4, NOW()),
('Chaussures Femmes', 'Escarpins, baskets et bottes pour femmes', 4, NOW()),
('Chaussures Enfants', 'Chaussures confortables pour enfants', 4, NOW()),
('Chaussures Sport', 'Chaussures de sport et running', 4, NOW()),
('Sandales & Tongs', 'Sandales et tongs pour toute la famille', 4, NOW());

-- Sous-catégories pour Accessoires (ID: 5)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Montres', 'Montres pour hommes et femmes', 5, NOW()),
('Ceintures', 'Ceintures en cuir et synthétiques', 5, NOW()),
('Lunettes', 'Lunettes de soleil et vue', 5, NOW()),
('Bijoux', 'Bijoux fantaisie et précieux', 5, NOW()),
('Sacs & Portefeuilles', 'Sacs à main et portefeuilles', 5, NOW());

-- Sous-catégories pour Électronique (ID: 6)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Téléphones', 'Smartphones et accessoires téléphoniques', 6, NOW()),
('Ordinateurs', 'Ordinateurs portables et de bureau', 6, NOW()),
('Télévisions', 'TV et équipements home cinéma', 6, NOW()),
('Audio', 'Casques, enceintes et équipements audio', 6, NOW()),
('Gadgets', 'Gadgets électroniques innovants', 6, NOW());

-- Sous-catégories pour Maison & Décoration (ID: 7)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Meubles', 'Meubles pour salon, chambre et cuisine', 7, NOW()),
('Décoration', 'Articles décoratifs pour intérieur', 7, NOW()),
('Cuisine', 'Ustensiles et électroménager', 7, NOW()),
('Literie', 'Draps, couettes et oreillers', 7, NOW()),
('Jardin', 'Mobilier et articles de jardin', 7, NOW());

-- Sous-catégories pour Sports & Loisirs (ID: 8)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Fitness', 'Équipements de fitness et musculation', 8, NOW()),
('Sports d''équipe', 'Équipements pour football, basketball, etc.', 8, NOW()),
('Sports extrêmes', 'Matériel pour sports extrêmes', 8, NOW()),
('Jeux & Jouets', 'Jeux de société et jouets', 8, NOW()),
('Musique', 'Instruments de musique et accessoires', 8, NOW());

-- Sous-catégories pour Beauté & Santé (ID: 9)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Soins du visage', 'Crèmes, lotions et soins visage', 9, NOW()),
('Maquillage', 'Produits de maquillage professionnels', 9, NOW()),
('Parfums', 'Parfums pour hommes et femmes', 9, NOW()),
('Soins corporels', 'Gels douche, huiles et soins corps', 9, NOW()),
('Compléments alimentaires', 'Vitamines et compléments santé', 9, NOW());

-- Sous-catégories pour Bagagerie (ID: 10)
INSERT INTO sous_categories (nom, description, categorie_id, created_at) VALUES
('Valises', 'Valises rigides et souples', 10, NOW()),
('Sacs à dos', 'Sacs à dos pour école et voyage', 10, NOW()),
('Sacs de voyage', 'Sacs de voyage et cabines', 10, NOW()),
('Accessoires voyage', 'Pochettes, étuis et organisateurs', 10, NOW()),
('Maroquinerie', 'Portefeuilles et petits articles en cuir', 10, NOW());

-- ========================================
-- VÉRIFICATION DES INSERTIONS
-- ========================================

-- Vérifier les catégories insérées
SELECT * FROM categories ORDER BY id;

-- Vérifier les sous-catégories insérées
SELECT sc.*, c.nom as categorie_nom 
FROM sous_categories sc 
JOIN categories c ON sc.categorie_id = c.id 
ORDER BY c.id, sc.id;

-- Compter le nombre d'enregistrements
SELECT 
    (SELECT COUNT(*) FROM categories) as nb_categories,
    (SELECT COUNT(*) FROM sous_categories) as nb_sous_categories;
