# 📍 Client Mobile Dawan - Cartographie & Géolocalisation

## 📝 Présentation du Projet
Application mobile native développée dans le cadre de l'évaluation **ME Expert IT (Applications Intelligentes et Big Data)**[cite: 1].
Ce client interroge un service web (API REST) pour afficher dynamiquement la liste des centres de formation Dawan[cite: 1]. Il intègre une gestion avancée de la persistance des données permettant une utilisation fluide même en l'absence de connectivité réseau[cite: 1].

## 🚀 Fonctionnalités Clés
- **Communication Réseau (Networking)** : Consommation de l'API REST via la bibliothèque Retrofit pour la récupération des infrastructures[cite: 1].
- **Persistance des Données (Offline-First)** : Stockage local SQLite (via l'abstraction Room) garantissant l'accès ininterrompu aux informations[cite: 1].
- **Moteur Cartographique** : Intégration d'OpenStreetMap (Osmdroid) avec placement algorithmique des épingles de géolocalisation[cite: 1].
- **Interface Utilisateur (UI)** : Affichage modulaire via des composants *Fragments* pour consulter l'adresse et les coordonnées GPS précises au clic[cite: 1].

## 🛠️ Pile Technologique (Stack)
- **Langage** : Java 17
- **Client HTTP / API** : Retrofit 2 + Convertisseur GSON[cite: 1].
- **Base de données locale** : Room (SGBD SQLite)[cite: 1].
- **Cartographie** : Osmdroid
- **Assurance Qualité** : Tests instrumentés via le cadriciel Espresso[cite: 1].

## 🏗️ Architecture et Bonnes Pratiques
Le développement s'articule autour des principes **SOLID** et de la séparation des préoccupations (architecture en couches)[cite: 1] :
1. **Couche `data/`** : Orchestration des sources de vérité (Service Web distant et Base de données locale).
2. **Couche `model/`** : Définition des objets métiers (Entités).
3. **Couche `ui/`** : Logique de présentation (Activités et Fragments).

## 👤 Développeur
**Baptiste LOUERAT**
*Master AIBD - Spécialisation Applications Intelligentes et Big Data*