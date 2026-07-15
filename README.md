# Where is Dawan ?

Application Android développée en **Java 17** dans le cadre du module **ME Expert IT** (Dawan). Elle permet de localiser les centres de formation Dawan sur une carte interactive, avec prise en charge du mode hors connexion.

## À propos du projet

L'application consomme l'API REST publique de Dawan pour récupérer la liste des centres de formation (nom, adresse, coordonnées GPS), les stocke localement en base SQLite via **Room**, puis les affiche sous forme d'épingles sur une carte **OpenStreetMap** (Osmdroid).

En cas d'absence de réseau, les données déjà synchronisées restent accessibles : l'application bascule automatiquement en mode hors ligne.

## Fonctionnalités

- **Carte interactive** : affichage des centres Dawan sur une carte OpenStreetMap
- **API REST** : récupération des données via Retrofit depuis `https://dawan.org/public/location/`
- **Persistance locale** : stockage SQLite avec Room pour un accès offline
- **Détails d'un centre** : clic sur une épingle pour afficher l'adresse et les coordonnées GPS
- **Thème** : interface en noir et rouge

## Stack technique

| Composant      | Technologie                          |
|----------------|--------------------------------------|
| Langage        | Java 17                              |
| Réseau         | Retrofit 2 + Gson                    |
| Base de données| Room (SQLite)                        |
| Carte          | Osmdroid (OpenStreetMap)             |
| UI             | AndroidX (AppCompat, Material, Fragments) |
| Tests          | JUnit, Espresso                      |
| Build          | Gradle 9.1 + Android Gradle Plugin 9 |

## Prérequis

- **Android Studio** (version récente, compatible AGP 9)
- **JDK 17**
- Un **émulateur Android** ou un **appareil physique** (API 25 minimum, cible API 36)
- Connexion Internet (pour la première synchronisation des données)

## Démarrage rapide

### 1. Cloner le dépôt

```bash
git clone <url-du-depot>
cd "Android - CC"
```

### 2. Ouvrir le projet

1. Lancez **Android Studio**
2. Choisissez **Open** et sélectionnez le dossier racine du projet
3. Attendez la fin de la synchronisation Gradle (téléchargement des dépendances)

### 3. Lancer l'application

1. Sélectionnez un émulateur ou connectez un appareil Android (USB + débogage activé)
2. Cliquez sur **Run** (▶) ou utilisez le raccourci `Shift + F10`
3. L'application affiche la carte centrée sur la France ; les épingles apparaissent après la synchronisation avec l'API

### 4. Compiler en ligne de commande (optionnel)

```bash
# Windows
gradlew.bat assembleDebug

# macOS / Linux
./gradlew assembleDebug
```

L'APK de debug sera généré dans `app/build/outputs/apk/debug/`.

## Structure du projet

```
Android - CC/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/fr/dawan/myapplication/
│       │   │   ├── data/          # Couche données (API, BDD, DAO)
│       │   │   ├── model/         # Entités métier (Location)
│       │   │   └── ui/            # Interface (MainActivity, DetailFragment)
│       │   └── res/               # Layouts, thèmes, chaînes
│       ├── androidTest/           # Tests instrumentés (Espresso)
│       └── test/                  # Tests unitaires
├── gradle/                        # Wrapper Gradle et versions
├── build.gradle.kts               # Configuration racine
└── settings.gradle.kts            # Modules du projet
```

### Architecture par couches

Le projet suit une organisation simple inspirée des principes SOLID :

1. **`model/`** — Entité `Location` (id, nom, adresse, latitude, longitude)
2. **`data/`** — Accès aux données :
   - `DawanApi` : interface Retrofit pour l'endpoint REST
   - `RetrofitClient` : client HTTP singleton
   - `AppDatabase` / `LocationDao` : persistance Room
3. **`ui/`** — Présentation :
   - `MainActivity` : carte, synchronisation, marqueurs
   - `DetailFragment` : détails d'un centre sélectionné

## Flux de données

```
API REST (dawan.org)
       │
       ▼
   Retrofit ──► Room (SQLite) ──► Carte Osmdroid
       │              │
       └─ Échec réseau ┘
          (données en cache)
```

1. Au démarrage, `MainActivity` appelle l'API via Retrofit
2. En cas de succès, les centres sont insérés en base locale
3. En cas d'échec, un toast « Mode hors-ligne activé » s'affiche et les données en cache sont utilisées
4. Les épingles sont placées sur la carte à partir des coordonnées GPS
5. Un clic sur une épingle ouvre le fragment de détails (adresse + coordonnées)

## API utilisée

| Élément    | Valeur                              |
|------------|-------------------------------------|
| Base URL   | `https://dawan.org/`                |
| Endpoint   | `GET /public/location/`             |
| Réponse    | Liste JSON de centres de formation  |

Exemple de structure d'un objet `Location` :

```json
{
  "id": 1,
  "name": "Centre Dawan Paris",
  "address": "123 rue Example, 75000 Paris",
  "latitude": 48.8566,
  "longitude": 2.3522
}
```

## Tests

### Tests instrumentés (Espresso)

Vérifient que la carte est visible au lancement :

```bash
gradlew.bat connectedAndroidTest
```

Fichier : `app/src/androidTest/java/fr/dawan/myapplication/MainActivityTest.java`

### Tests unitaires

```bash
gradlew.bat test
```

## Configuration

| Paramètre        | Valeur                    |
|------------------|---------------------------|
| `applicationId`  | `fr.dawan.myapplication`  |
| `minSdk`         | 25 (Android 7.1)          |
| `targetSdk`      | 36                        |
| `compileSdk`     | 36                        |

### Permissions (AndroidManifest)

- `INTERNET` — appels API
- `ACCESS_NETWORK_STATE` — détection de la connectivité
- `WRITE_EXTERNAL_STORAGE` — cache des tuiles Osmdroid

> **Note :** L'application utilise **Osmdroid** (OpenStreetMap) et non Google Maps. La clé API Google présente dans le manifeste n'est pas utilisée par le code actuel.

## Dépannage

| Problème                          | Solution                                              |
|-----------------------------------|-------------------------------------------------------|
| Gradle sync échoue                | Vérifiez JDK 17 et la connexion Internet              |
| Carte vide sans réseau            | Lancez l'app une première fois avec Internet          |
| Émulateur lent                    | Activez l'accélération matérielle (HAXM / Hyper-V)    |
| Erreur `allowMainThreadQueries`   | Volontaire pour cet exercice ; à éviter en production |

## Auteur

Projet réalisé dans le cadre de la formation **Expert IT** — Spécialisation Applications Intelligentes et Big Data (Dawan).
