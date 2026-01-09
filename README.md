# Esieamon - Jeu de Combat Pokémon-like

## Developpé par Romain MECHAIN & Léni CHABILAN 

Un jeu de combat au tour par tour mettant en scène des monstres d'une même famille, inspiré des mécaniques pokémon. Projet universitaire ESIEA (Programmation Orientée Objet - Java).

---

##  Vue d'ensemble

Esieamon est un jeu de combat stratégique à deux joueurs où chaque joueur contrôle une équipe de monstres. Les joueurs s'affrontent à tour par tour en sélectionnant des attaques, en changeant de monstre, ou en utilisant des objets pour influencer le combat.

### Caractéristiques principales

- **Système de combat** : Combat au tour par tour avec résolution simultanée ou séquentielle selon la vitesse
- **8 types de monstres** : Feu, Eau, Électrique, Terre, Herbe, Insecte, Nature, Normal
- **Système d'attaques** : 7 types d'attaques avec efficacité selon les types
- **Statuts** : Brûlé, Paralysé, Empoisonné, Enfoui
- **Terrains dynamiques** : Terrain normal et terrain inondé
- **Objets consommables** : Potions (HP, ATK, DEF, SPD) et médicaments (guérisons)
- **Deux modes** : Interface Terminal ou Interface Graphique 

---

## Prérequis

### Système

Pour faire fonctionner Esieamon, vous aurez besoin de :
```bash
sudo apt install openjdk-17-jdk
sudo apt install ffmpeg # Pour la lecture audio (Linux)
```

### Vérification de l'installation Java

```bash
java --version
javac --version
```

---

## Installation et lancement

### Option 1 : Avec Make (Recommandé) 

Le projet inclut un `Makefile` pour simplifier la compilation et l'exécution.

#### Compiler et lancer en une commande :
```bash
make run
```

#### Ou compiler seulement :
```bash
make compile
```

#### Autres commandes utiles :
```bash
make clean      # Nettoyer les fichiers compilés
make rebuild    # Recompiler complètement
```

---

### Option 2 : Avec les commandes Java (Manuel)

Si vous préférez éviter Make, vous pouvez compiler et lancer manuellement.

#### 1. Compiler le projet

**Linux/macOS :**
```bash
javac --release 17 -d classes -sourcepath src -cp "lib/javafx-base-21.0.1-linux.jar:lib/javafx-graphics-21.0.1-linux.jar:lib/javafx-controls-21.0.1-linux.jar:lib/javafx-fxml-21.0.1-linux.jar" $(find src -name "*.java")
```

#### 2. Lancer le jeu

**Linux/macOS :**
```bash
java -cp classes:"lib/javafx-base-21.0.1-linux.jar:lib/javafx-graphics-21.0.1-linux.jar:lib/javafx-controls-21.0.1-linux.jar:lib/javafx-fxml-21.0.1-linux.jar" com.esiea.pootp.EsieamonExecutable
```


---

### 4. Sélectionner un mode

Au démarrage, le jeu vous propose :
- **Mode 1 (Terminal)** : Interface textuelle en console
- **Mode 2 (Graphique)** : Interface JavaFX

---

##  Fonctionnalités implémentées

###  Système de combat

#### Mécanique générale
- Joueurs alternent les actions par tour
- Resolution d'attaque : ordre basé sur la vitesse du monstre
- Calcul des dégâts : Formule équilibrée prenant en compte type, puissance et efficacité
- Points de Vie (PV) : Gestion complète avec KO possible

#### Résolution de tour
1. **Sélection des actions** (attaque, changement de monstre, objet)
2. **Application des changements de monstre**
3. **Utilisation des objets**
4. **Application des statuts**
5. **Application des effets de terrain**
6. **Application des effets passifs**
7. **Résolution des attaques** (ordre basé sur la vitesse)
8. **Vérification des KO**

###  Système de monstres (8 types)

| Type | Spécialité | Effet particulier |
|------|-----------|------------------|
| **Feu** | Attaque puissante | Peut brûler l'adversaire (dégâts/tour) |
| **Eau** | Inonde le terrain | Crée terrain inondé → chutes dégâts pour non-Eau |
| **Électrique** | Paralyse | Réduit capacité à attaquer (25% chance/tour) |
| **Terre** | Défense élevée | Peut s'enfouir (double défense 1-3 tours) |
| **Herbe** (Nature) | Régénération | +5% HP/tour sur terrain inondé, auto-guérison |
| **Insecte** (Nature) | Empoisonnement progressif | Empoisonne tous les 3 attaques spéciales |
| **Nature** | Type polymorphe | Bonuseffets terrain inondé (+5% HP/tour) |
| **Normal** | Basique | Pas de spécialité |

###  Système d'attaques

#### Caractéristiques
- **7 types** : Normal, Feu, Eau, Électrique, Terre, Herbe, Insecte
- **Points de Puissance (PP)** : Limitations par attaque
- **Taux d'échec** : Possibilité de miss
- **Efficacité de type** : x2 (super efficace), x1 (normal), x0.5 (peu efficace)
- **Attaque "Lutte"** : Attaque de fallback sans objet

#### Table d'efficacité
```
FEU         → super efficace contre : Nature
EAU         → super efficace contre : Feu, 
ÉLECTRIQUE  → super efficace contre : Eau
TERRE       → super efficace contre : Électrique
NATURE       → super efficace contre : Terre
```

###  Système de statuts

| Statut | Effet | Durée | Guérison |
|--------|-------|-------|----------|
| **Normal** | Aucun | - | - |
| **Brûlé** | -10% ATK/tour en dégâts | Illimité | Médicament ou terrain inondé |
| **Paralysé** | 25% chance incapacité/tour | Augmente (guérison progressive) | Médicament |
| **Empoisonné** | -10% DEF/tour en dégâts | Illimité | Médicament ou terrain inondé |
| **Enfoui** | +100% DEF temporaire | 1-3 tours | Automatique |

###  Système de terrains

| Terrain | Effet | Durée |
|---------|-------|--------|
| **Normal** | Aucun | Illimité |
| **Inondé** | Non-Eau : 25-50% chance glissade/tour (-25% ATK) | 1-3 tours |

#### Effets additionnels
- Monstres Herbe : +5% HP/tour sur terrain inondé
- Monstres Eau : Immunisé aux chutes
- Brûlé/Empoisonné : Guéri sur terrain inondé

###  Système d'objets

#### Potions (par efficacité)
```
NORMAL  : 20 HP / 15 ATK,DEF,SPD
SUPER   : 50 HP / 25 ATK,DEF,SPD  
HYPER   : 100 HP / 40 ATK,DEF,SPD
```

#### Médicaments
- **Antibrûlure** : Guérit le statut Brûlé
- **Antiparalysie** : Guérit le statut Paralysé
- **Antipoison** : Guérit le statut Empoisonné
- **Éponge de terrain** : Restaure terrain Normal depuis inondé

###  Modes de jeu

#### Mode Terminal
- Interface textuelle complète
- Affichage coloré avec codes ANSI
- Sélection au clavier (numéros)
- Affichage en temps réel du combat

#### Mode Graphique
- Interface graphique intuitive
- Boutons et menus visuels
- Affichage des statistiques en temps réel
- Gestion des animations (optionnel)

---

##  Bonus apportés

### 1. **Système de terrain dynamique** 
- Implémentation complète du terrain inondé
- Effets spécifiques sur différents types de monstres
- Régénération HP monstre Herbe sur terrain inondé
- Durée de terrain aléatoire (1-3 tours)

### 2. **Interface Mode Terminal colorisée** 
- Codes couleur ANSI (bleu, orange, rouge, etc.)
- Affichage formaté et lisible
- Menu textuels interactifs

### 3. **Lecteur audio MusicPlayer** 
- Lecture de musique de fond en loop
- Support multi-lecteurs (ffplay, paplay, mpv, aplay)
- Gestion des threads pour non-blocage
- Arrêt propre du processus audio


### 5. **Inventaire et système d'objets (Possibilitées de faire son propre inventaire) ** 
- Potions à 3 niveaux d'efficacité
- Médicaments spécialisés
- Gestion du poids (implémentée mais pas limitée)

### 6. ** Composition d'équipe **
- Choix libre des monstres pour chaque joueur
- choisir le nombre de monstres (par défaut 3)
- Stratégies variées selon les types et attaques

### 7. **Interface (Mode Graphique)** 
- Interface graphique complète
- Gestion d'événements souris/clavier
- Affichage des monstres et statistiques
- Sélection visuelle des actions


## 📄 Licence

Projet universitaire - Tous droits réservés ESIEA

---
