# ResizingApp

Java 11, JavaFx

Rendu : 30 novembre

[sujet](https://docs.google.com/document/d/1WnQn_8BGB1dcxQVLzfz0Gq7P1-xpGMfX4qt73U7mLEM/edit)

[FXML](http://fxexperience.com/wp-content/uploads/2011/08/Introducing-FXML.pdf)

L’objectif de ce projet est l’implémentation d’une application de redimensionnement d’image en Java.
Cette application permettra d'effectuer les opérations suivantes sur une images : recadrage, mise à l’échelle et seam carving.

## Remarques sur le sujet
Faire des packages : par exemple un dédié au traitement d'image

Différence pixel de gauche et pixel de droite : calcul du gradient (on obtiendra des lignes ), max 255

## Difficultés rencontrées

* resizing 
    * à effectuer sur la BufferedImage plutôt que sur l'ImageView

* crop (recardage)
    * dépendant du FXML et de sa définition de l'ImageView

* gradient 
    * a soulevé le problème de la copie de la BufferedImage
    
## Les extensions rajoutées
### Zoom
Possibilité de zoomer sur l'image

Implémenté car il nous a permis de faire bien la différence entre zoom, crop et resizing

    
