# Real Estate Manager

## Introduction

Real Estate Manager est une application mobile Android conçue pour faciliter la gestion et la visualisation des propriétés immobilières. Grâce à une interface intuitive, les utilisateurs peuvent consulter les détails des propriétés, ajouter ou modifier des annonces, et visualiser la localisation des biens directement sur une carte.

## Fonctionnalités

- **Visualisation des propriétés :**
  - Affichage des détails de la propriété, y compris le prix, la surface, le nombre de pièces, etc.
  - Visualisation des photos en utilisant RecyclerView.
  - Affichage de la localisation sur une carte statique via l'API Google Maps Static.

- **Ajout et modification de propriétés :**
  - Formulaire de propriété avec des champs pour tous les détails nécessaires.
  - Ajout de photos à partir de la galerie.
  - Utilisation de NestedScrollView pour un défilement fluide.

- **Recherche de propriétés :**
  - Filtrage des propriétés par type, surface, prix, etc.
  - Utilisation de Jetpack Compose pour une interface utilisateur moderne et réactive.

- **Paramètres de l'application :**
  - Changement de la devise (Euro ou Dollar).
  - Gestion des préférences utilisateur avec Jetpack Compose.

- **Synchronisation des données :**
  - Intégration avec Firebase pour la gestion des données en temps réel.
  - Utilisation de Room pour la gestion des données locales hors ligne.

## Technologies Utilisées

- **Langage :** Kotlin
- **Frameworks et Bibliothèques :**
  - Jetpack Compose
  - RecyclerView
  - ViewModel
  - LiveData
  - Room
  - Firebase Firestore
  - Google Maps Static API
- **Architecture :** MVVM (Model-View-ViewModel) avec Repository Pattern
- **Dépendance Injection :** Hilt


