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

                Color myRightPixelColor = new Color(myBufferedImage.getRGB(x + 1, y));
                Color myLeftPixelColor = new Color(myBufferedImage.getRGB(x - 1, y));


                int colorChange = 0;
                int Left;
                int Right;
                //Color change requires on operation on gradient: 8 for green, 16 for red, nothing for blue
                switch (myColor) {
                    case "red":
                        Left = myLeftPixelColor.getRed();
                        Right = myRightPixelColor.getRed();
                        colorChange = 16;
                        break;
                    case "green":
                        Left = myLeftPixelColor.getGreen();
                        Right = myRightPixelColor.getGreen();
                        colorChange = 8;
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
                gradient = (gradient << colorChange);
                gradientBImage.setRGB(x, y, gradient);

            }
        }
        return gradientBImage;
    }

    /** Modify color composition of a bufferedImage.
     *
     * @param myColor is the color to increase/decrease.
     * @param myBufferedImage is the bufferedImage to color.
     * @param ratio is the ratio to apply at the color concerned.
     * @return a bufferedImage with rgb values modified.
     */
    public static BufferedImage imageColoring(String myColor, BufferedImage myBufferedImage, double ratio) {
        int decalage;
        int colorToChange;
        int secondColor;
        int thirdColor;
        int maxX = myBufferedImage.getWidth();
        int maxY = myBufferedImage.getHeight();

        for (int x = 0; x < maxX; x++){
            for(int y = 0; y < maxY; y++) {

                Color myPixelColor = new Color(myBufferedImage.getRGB(x, y));

                switch (myColor) {
                    case "R":
                        decalage = 16;
                        colorToChange = myPixelColor.getRed();
                        secondColor = myPixelColor.getGreen()<<8;
                        thirdColor = myPixelColor.getBlue();
                        break;
                    case "G":
                        decalage = 8;
                        colorToChange = myPixelColor.getGreen();
                        secondColor = myPixelColor.getRed()<<16;
                        thirdColor = myPixelColor.getBlue();
                        break;
                    default:
                        decalage = 0;
                        colorToChange = myPixelColor.getBlue();
                        secondColor = myPixelColor.getGreen()<<8;
                        thirdColor = myPixelColor.getRed()<<16;
                        break;
                }
                colorToChange =( Math.min((int)(colorToChange*ratio),255) )<< decalage;
                myBufferedImage.setRGB( x , y ,( colorToChange + secondColor + thirdColor ) );
            }

        }
        return myBufferedImage;
    }
}
