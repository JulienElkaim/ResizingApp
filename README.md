# ResizingApp  
_**School project : Nov. 2018**_

## Table des matières
1. [Introduction](#introduction)
1. [Difficultés rencontrées](#difficultés-rencontrées)
1. [Fonctionnalités de base](#fonctionnalités-de-base)
    1. [Ouverture du fichier (Open)](#ouverture-du-fichier-open)
    1. [Recadrage (Crop)](#recadrage-crop)
    1. [Mise à l'échelle (Resize)](#Mise-à-l'échelle-(Resize))
    1. [Retrait de bandelettes (Carve seams)](#retrait-de-bandelettes-carve-seams)
    1. [Enregistrement du fichier (Save)](#enregistrement-du-fichier-save)
1. [Extensions](#extensions)
    1. [Persistance des modifications](#persistance-des-modifications)
    1. [Remise à zéro des modifications](#remise-à-zéro-des-modifications)
    1. [Zoom](#zoom)  
    1. [Informations utilisateur selon contexte](#informations-utilisateur-selon-contexte)   
    1. [Choix de la direction du traitement d'image](#choix-de-la-direction-du-traitement-dimage)   
    1. [Colorisation](#colorisation)
1. [Interface utilisateur](#interface-utilisateur)
    1. [Aperçu de l'interface utilisateur](#aperçu-de-l'interface-utilisateur)
    1. [Affichages contextuels](#affichages-contextuels)
    1. [Raccourcis clavier](#raccourcis-clavier)
    1. [Fonction d'aide (Help)](#fonction-daide-help)
1. [Exemple de résultats](#exemples-de-résultats)

## Introduction 
Java 11, JavaFx

Rendu : 30 novembre

[sujet](https://docs.google.com/document/d/1WnQn_8BGB1dcxQVLzfz0Gq7P1-xpGMfX4qt73U7mLEM/edit)

[FXML](http://fxexperience.com/wp-content/uploads/2011/08/Introducing-FXML.pdf)

L’objectif de ce projet est l’implémentation d’une application de redimensionnement d’image en Java.
Cette application permettra d'effectuer les opérations suivantes sur une image : recadrage, mise à l’échelle et seam carving.


## Difficultés rencontrées

* resizing *(mise à l'échelle)*
    * à effectuer sur la BufferedImage plutôt que sur l'ImageView

* crop *(recadrage)*
    * dépendant du FXML et de sa définition de l'ImageView

* gradient 
    * a soulevé le problème de la copie de la BufferedImage
    
* on a rencontré des erreurs de type NullPointerException. Il a fallu résoudre ce problème par des if pour traiter les
les exceptions

* quand nous avons travaillé avec les images, on a remarqué qu'il était nécessaire de les cloner pour en avoir une
bonne copie

* l'interface utilisateur plante durant le processing -> trouver un moyen de faire tourner les tâches en arrière plan
   
* rajout d'une image de taille plus faible que l'image du château (Broadway) pour que les opérations soient plus rapides
     à effectuer et à tester

* nous avons rencontrés des bugs et ajouté des tests afin de les trouver

* problèmes lors du traitement des différents canaux de couleur. On a donc ajouté un enum pour éviter de se mélanger et
clarifier le traitement des différentes couleurs.
 
## Fonctionnalités de base

### Ouverture du fichier (Open)
Cette opération permet d'ouvrir un fichier contenant une image, dans le but de l'éditer.

Choix d'implémentation : 
* Ajout d'un chemin par défaut contenant plusieurs images d'exemple.
* Limitation aux fichiers image de type JPEG.

__Mode d'emploi :__ Au clavier `Ctrl + O` ou à partir du menu *File > Open*.

### Recadrage (Crop)
Cette opération découpe l'image pour ne garder que le pourcentage de l'image d'origine choisi avec le curseur.

Choix d'implémentation :
* Pour le recadrage suivant la largeur, on fait le choix de ne garder que la partie gauche de l'image.
* Pour le recadrage suivant la hauteur (extension), on fait le choix de ne garder que la partie haute de l'image.

### Mise à l'échelle (Resize)
Cette opération permet de changer la largeur ou la hauteur de l'image pour qu'elle tienne dans la taille choisie avec le curseur.


### Retrait de bandelettes (Carve seams)
Nous suivons cet [algorithme](https://en.wikipedia.org/wiki/Seam_carving).

Choix d'implémentation :
* Pour le Seam Carving suivant la largeur, les bandelettes verticales sont détruites une par une pour éviter des erreurs de suppression.
* Pour le Seam Carving suivant la hauteur (extension), les bandelettes horizontales sont détruites une par une pour éviter des erreurs de suppression.

### Enregistrement du fichier (Save)
Cette opération permet d'écrire sur le disque le fichier actuellement ouvert.

Choix d'implémentation : 
* Ajout d'un chemin par défaut contenant plusieurs images d'exemple.
* Limitation aux fichiers image de type JPEG.

__Mode d'emploi :__ Au clavier `Ctrl + S` ou à partir du menu *File > Save*.

## Extensions
### Persistance des modifications
__Fonctionnalité :__ permet d'enregistrer les modifications sur image depuis le dernier enregistrement (checkpoint).

__Mode d'emploi :__ Au clavier `Ctrl + V` ou à partir du menu  *Changes > Create checkpoint*.

Mis en oeuvre car il n'enregistre que les modifications, il ne crée pas pour autant un fichier image au même titre que Save.

### Remise à zéro des modifications
__Fontionalité :__ permet d'annuler les modifications effectuées depuis le dernier checkpoint

__Mode d'emploi :__ Au clavier `Ctrl + Shift + V` pour revenir au dernier enregistrement (checkpoint) ou à partir du menu *Edit > Cancel changes since checkpoint*.

Mis en oeuvre pour permettre à l'utilisateur de revenir en arrière pour éviter d'enchainer les changements innoportuns.

### Zoom
__Fonctionnalité :__ Permet de zoomer sur l'image de manière intuitive par un clic de souris.

__Mode d'emploi :__
1. Sélectionner la fonction Zoom en appuyant sur le bouton Zoom (option par défaut au lancement)
1. Ajuster le curseur pour définir le facteur de zoom
1. Cliquer sur l'image pour définir le centre de zoom

Implémenté pour permettre à l'utilisateur d'obtenir directement une sous image de manière très précise dans l'image. La valeur du curseur est utilisée pour définir le facteur de zoom.

### Informations utilisateur selon contexte
__Fonctionnalité :__
- __Gradients :__ permet d'afficher le gradient RVB de la couleur de votre choix.
- __Energie et Seam :__ permet d'afficher le résultat du calcul d'énergie sur l'image, ainsi que la bandelette d'énergie minimale.

__Mode d'emploi :__ Survoler la surface associée.
* __Gradients :__ un des carrés de couleur pour le gradient de la couleur correspondante.
* __Energie et Seam :__ le Label d'énergie ou le label de l'énergie et du seam d'énergie minimale.

Mis en oeuvre pour proposer à l'utilisateur une nouvelle façon de faire un choix, différente des boutons "classiques",
ils permettent de faire un changement temporaire, juste un affichage temporaire du résulat de l'opération souhaitée. Le résultat est à titre informatif pour permettre à l'utilisateur de faire ses futurs choix d'opération.

### Choix de la direction du traitement d'image
__Fonctionnalité :__ Choisir si l'utilisateur souhaite effectuer les traitements d'image selon la hauteur ou la largeur.

__Mode d'emploi :__ Au clavier `Ctrl + D` ou à partir du menu *Edit > Switch direction*.

Implémenté pour fournir à l'utilisateur un degré de liberté supérieur dans la modification de l'image en cours de modification.

### Colorisation 
__Fonctionnalité :__ Permet de renforcer ou réduire l'intensité d'une des trois couleurs R/V/B. 

__Mode d'emploi :__ Utiliser le curseur associé à la couleur à modifier. La modification se fait dynamiquement.

Cette utilisation des Listeners permet à l'utilisateur de modifier intrinsèquement son image avant de faire des retouches de redimensionnement. 

### Raccourcis clavier
__Fonctionnalité :__ Permet d'activer rapidement les fonctionnalités implémentées.

__Mode d'emploi :__  Appui simultané des touches de clavier.

Liste des raccourcis au paragraphe [Interface utilisteur](#Raccourcis clavier).

Permet d'améliorer l'interface en relayant certaines fonctions dans le menu, sans pour autant sacrifier l'expérience utilisateur.

### Informations contextuelles 
 __Fonctionnalité :__ Permet une visualisation directe des caractéristiques de l'outil. Améliore l'expérience utilisateur.
 
 Liste des informations concernées au paragraphe [Interface utilisteur](#affichages-contextuels).

__Mode d'emploi :__ 
- __Position du pointeur :__ Déplacer le curseur sur l'image affichée. L'affichage se fait dans la barre d'état.  
- __Autres informations :__ lecture de l'affichage dans la barre d'état.

## Interface utilisateur

### Aperçu de l'interface utilisateur

<img src="./example/UI.png" alt = "Interface utilisateur, voir dans dossier img si erreur d'affichage" width="714"/>

- Bandeau supérieur : barre de menu.
- Bandeau inférieur : barre d'état.
- Bandeau latéral gauche : commandes et affichages.

### Affichages contextuels

> - Traitement en cours : cropping | resizing | seam carving
> - Direction : horizontal | vertical
> - Position du pointeur : x pixels à partir de la gauche, y pixels à partir du haut

### Raccourcis clavier

> - **Ctrl+O** 
Ouverture (_Open_) d'un fichier image.
> - **Ctrl + D** 
Modification du sens (_Direction_) des traitements sur image.
> - **Ctrl + V** 
Persistance des modifications réalisées (_Save checkpoint_).
> - **Ctrl + Shift + V** 
Annualation (_Cancel_) des modifications effectués depuis le dernier checkpoint
> - **Ctrl + S** 
Enregistrement (_Save_) des modifications en fichier image.

> - **F1**  
Ouverture du fichier d'aide.

### Fonction d'aide (Help)
Permet d'accéder directement au fichier README.md pour comprendre les fonctionnalités.

__Mode d'emploi :__ Au clavier `F1` ou à partir du menu *Help*.

## Exemples de résultats

Les traitements ont été réalisés selon la direction horizontale.

__Image d'origine__

<img src="./img/broadway.jpg" alt = "Image d'origine, voir dans dossier img si erreur d'affichage" width="714"/>

__Image après recadrage (Crop)__

<img src="./example/AfterCrop.jpg" alt = "Image après recadrage, voir dans dossier img si erreur d'affichage" width="482"/>

__Image après mise à l'échelle (Resize)__

<img src="./example/AfterResize.jpg" alt = "Image après redimensionnement, voir dans dossier img si erreur d'affichage" width="482"/>

__Image après suppression de bandelettes (Carve seams)__

<img src="./example/afterSeamCarving.jpg" alt = "Image après seam carving, voir dans dossier img si erreur d'affichage" width="482"/>
