package operation;


import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

public class Gradient {

    /**
     * The gradient is the difference between the intensity of the same colour in the left and in the right pixel
     *
     * @param myColor         is the Gradient color we want to apply. Choices are RGB.
     * @param myBufferedImage is the BufferedImage to apply the gradient.
     * @return a power-gradiented image resulting of the Gradient application.
     */
    public static BufferedImage createGradient(String myColor, BufferedImage myBufferedImage) {
        BufferedImage gradientBImage = SimpleOperation.cloningBufferedImage(myBufferedImage);
        int maxX = gradientBImage.getWidth();
        int maxY = gradientBImage.getHeight();


        for (int x = 2; x < maxX - 1; x++) {
            for (int y = 1; y < maxY; y++) {

                //pixel à droite
                Color myRightPixelColor = new Color(myBufferedImage.getRGB(x + 1, y));
                //pixel à gauche
                Color myLeftPixelColor = new Color(myBufferedImage.getRGB(x - 1, y));


                int decalage = 0;
                int Left;
                int Right;
                //changement de couleur  //8 pour green, 16 pour rouge, rien pour bleu
                switch (myColor) {
                    case "red":  //rouge
                        Left = myLeftPixelColor.getRed();
                        Right = myRightPixelColor.getRed();
                        decalage = 16;
                        //gradient = (gradient << 16);
                        break;
                    case "green":  //vert
                        Left = myLeftPixelColor.getGreen();
                        Right = myRightPixelColor.getGreen();
                        decalage = 8;
                        //gradient = (gradient << 8);
                        break;
                    default:
                        Left = myLeftPixelColor.getBlue();
                        Right = myRightPixelColor.getBlue();
                        break;
                }
                int gradient = abs(Left - Right);
                if (gradient > 255) {
                    gradient = 255;
                }
                gradient = (gradient << decalage);
                gradientBImage.setRGB(x, y, gradient);

            }
        }
        return gradientBImage;
    }
}
