<div align="center">

# Real Estate Manager

**Application Android de gestion de biens immobiliers**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-API_21%2B-3DDC84?logo=android)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-1.5-4285F4?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-Firestore_%2B_Storage-FFCA28?logo=firebase)](https://firebase.google.com)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM_%2B_Repository-blue)](https://developer.android.com/topic/architecture)
[![OpenClassrooms](https://img.shields.io/badge/OpenClassrooms-Projet_9-FF6600)](https://openclassrooms.com/fr/paths/507-developpeur-android)

</div>

---

## Contexte

Projet 9 de la [formation Développeur Android OpenClassrooms](https://openclassrooms.com/fr/paths/507-developpeur-android). Objectif : concevoir une application professionnelle de gestion immobilière combinant stockage local offline (Room), synchronisation cloud (Firebase Firestore) et géolocalisation (Google Maps).

---

## Fonctionnalités

- **Liste et détail des biens** — affichage des propriétés avec photos, prix, surface, nombre de pièces, statut de vente
- **Ajout et modification** — formulaire complet avec ajout de photos depuis la galerie
- **Carte interactive** — localisation des biens via Google Maps Static API
- **Recherche et filtres** — filtrage par type, surface, prix, nombre de pièces
- **Mode offline** — accès aux données sans connexion grâce à Room (SQLite)
- **Synchronisation cloud** — Firebase Firestore pour la persistance multi-device
- **Conversion de devise** — bascule Euro / Dollar dans les paramètres
- **Tests unitaires** — couverture sur la conversion de devise, le formatage de dates et la disponibilité réseau

---

## Stack technique

| Technologie | Usage |
|-------------|-------|
| **Kotlin 1.9** | Langage principal |
| **Jetpack Compose + Material 3** | UI déclarative (écrans paramètres, filtres) |
| **MVVM + Repository Pattern** | Architecture |
| **Hilt** | Injection de dépendances |
| **Room 2.6** | Base de données locale (offline) |
| **Firebase Firestore + Storage** | Synchronisation cloud + stockage photos |
| **Google Maps SDK** | Carte interactive et géolocalisation |
| **Google Places API** | Recherche d'adresses |
| **Coroutines + LiveData** | Programmation asynchrone et réactivité UI |
| **Glide** | Chargement et cache des images |
| **Navigation Component** | Navigation entre fragments |
| **View Binding** | Liaison des vues XML |

---

## Architecture

```
app/src/main/java/com/openclassrooms/realestatemanager/
├── data/
│   ├── AppDatabase.kt          ← Base Room
│   ├── PropertyDao.kt          ← Requêtes propriétés
│   ├── PhotoDao.kt             ← Requêtes photos
│   ├── models/                 ← Entités de données
│   └── repository/
│       ├── PropertyRepository.kt
│       ├── PictureRepository.kt
│       └── AgentRepository.kt
│
├── view/
│   ├── activity/               ← Activités principales
│   ├── fragment/               ← Fragments (liste, détail, carte, paramètres)
│   ├── adapter/                ← RecyclerView adapters
│   └── useCase/                ← Cas d'usage métier
│
└── viewmodel/
    ├── ListPropertyViewModel.kt
    ├── PropertyDetailViewModel.kt
    ├── PropertyFormViewModel.kt
    ├── MapPropertyViewModel.kt
    ├── SearchViewModel.kt
    └── AgentViewModel.kt
```

---

## Lancer le projet

### Prérequis

- Android Studio Hedgehog (2023.1.1) ou plus récent
- JDK 17
- Un compte [Google Cloud Console](https://console.cloud.google.com) avec les APIs suivantes activées :
  - **Maps SDK for Android**
  - **Places API**
- Un projet Firebase avec Firestore et Storage activés

### 1. Configurer la clé Google Maps

Dans le fichier `local.properties` à la racine du projet (ce fichier n'est pas commité) :

```properties
RR_KEY=VOTRE_CLE_API_GOOGLE_MAPS
```

La clé est injectée automatiquement dans le `BuildConfig` et le `AndroidManifest.xml` via Gradle.

### 2. Configurer Firebase

Télécharge le fichier `google-services.json` depuis ta console Firebase et place-le dans `app/`.

### 3. Lancer

```bash
git clone https://github.com/REEHBANKS/RealEstateManager.git
cd RealEstateManager

# Ouvrir dans Android Studio, puis :
./gradlew assembleDebug

# Ou lancer directement sur un device/émulateur depuis Android Studio
```

### Configuration minimale

| Paramètre | Valeur |
|-----------|--------|
| `minSdkVersion` | 21 (Android 5.0) |
| `targetSdkVersion` | 34 (Android 14) |
| `compileSdk` | 34 |

---

## Tests

```bash
# Tests unitaires
./gradlew test

# Tests instrumentés (device requis)
./gradlew connectedAndroidTest
```

Couverture actuelle : conversion de devise, formatage de dates, détection de la connectivité réseau.

---

## Formation

Projet réalisé dans le cadre de la **formation Développeur Android OpenClassrooms** (Bac+3/4).

- Parcours : [Développeur Android](https://openclassrooms.com/fr/paths/507-developpeur-android)
- Projet : P9 — Real Estate Manager
- Compétences évaluées : architecture MVVM, Room, Firebase, Maps API, Hilt, Jetpack Compose
